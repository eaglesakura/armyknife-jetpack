package com.eaglesakura.armyknife.android.extensions

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.ApplicationRuntime
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleBlockingTest
import com.eaglesakura.armyknife.android.junit4.extensions.makeActivity
import com.eaglesakura.example.ExampleActivity
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityExtensionsKtTest {

    @Test
    fun contextInto() = compatibleBlockingTest {
        val activity = makeActivity(ExampleActivity::class)
        val context = withContext(Dispatchers.Main) {
            MutableLiveData<Context>().also { activity.contextInto(it) }
        }

        assertEquals(activity, context.value)
        delay(1000)
        activity.finish()
        delay(1000)
        assertTrue(activity.isFinishing)

        if (ApplicationRuntime.runIn(ApplicationRuntime.RUNTIME_INSTRUMENTATION)) {
            withTimeout(TimeUnit.SECONDS.toMillis(60)) {
                while (activity.lifecycle.currentState != Lifecycle.State.DESTROYED) {
                    yield()
                }
                while (context.value != null) {
                    yield()
                }
            }

            assertEquals(Lifecycle.State.DESTROYED, activity.lifecycle.currentState)
            assertNull(context.value)
        }
    }
}
