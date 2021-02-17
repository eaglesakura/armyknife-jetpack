package com.eaglesakura.armyknife.android.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eaglesakura.armyknife.android.junit4.extensions.compatibleBlockingTest
import com.eaglesakura.armyknife.android.junit4.extensions.instrumentationBlockingTest
import com.eaglesakura.armyknife.android.junit4.extensions.makeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.plus
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LiveDataExtensionsKtTest {

    @Test
    fun setValueAsync() = compatibleBlockingTest(Dispatchers.Main) {
        val liveData = MutableLiveData<String>()
        liveData.setValueAsync(Dispatchers.IO) {
            delay(1000)
            "OK"
        }

        assertEquals("OK", liveData.await())
    }

    @Test(expected = TimeoutCancellationException::class)
    fun setValueAsync_cancel() = compatibleBlockingTest(Dispatchers.Main) {
        val liveData = MutableLiveData<String>()

        val scope = Job()
        liveData.setValueAsync((GlobalScope + scope), Dispatchers.IO) {
            delay(1000)
            "OK"
        }
        scope.cancel()

        withTimeout(2000) {
            liveData.await()
        }
    }

    @Test
    fun setValueIfChanged() = compatibleBlockingTest(Dispatchers.Main) {
        val liveData = MutableLiveData<String>()
        var notifyCount = 0

        liveData.observeForever {
            notifyCount++
        }
        yield()
        liveData.setValueIfChanged("Update")
        yield()
        assertEquals(1, notifyCount) // notify
        liveData.setValueIfChanged("Update")
        yield()
        assertEquals(1, notifyCount) // not notify
        liveData.setValueIfChanged("End")
        yield()
        assertEquals(2, notifyCount) // notify
    }

    @Test
    fun setValueIfChanged_lambda() = compatibleBlockingTest(Dispatchers.Main) {
        val liveData = MutableLiveData<String>()
        var notifyCount = 0

        liveData.observeForever {
            notifyCount++
        }
        yield()
        liveData.setValueIfChanged("Update") { oldValue, newValue -> oldValue == newValue }
        yield()
        assertEquals(1, notifyCount) // notify
        liveData.setValueIfChanged("Update") { oldValue, newValue -> oldValue == newValue }
        yield()
        assertEquals(1, notifyCount) // not notify
        liveData.setValueIfChanged("End") { oldValue, newValue -> oldValue == newValue }
        yield()
        assertEquals(2, notifyCount) // notify
    }

    @Test
    fun copyTo() = compatibleBlockingTest(Dispatchers.Main) {
        val activity = makeActivity()
        val src = MutableLiveData<String>()
        val dst = MutableLiveData<String>()

        src.value = "ABC"
        src.copyTo(activity, dst)
        yield()
        assertEquals("ABC", dst.value)

        src.value = "DEF"
        yield()
        assertEquals("DEF", dst.value)

        src.value = null
        yield()
        assertEquals(null, dst.value)
    }

    @Test
    fun setValueWhenCreated() = instrumentationBlockingTest(Dispatchers.Main) {
        val activity = makeActivity()
        val liveData = MutableLiveData<String>()
        liveData.setValueWhenCreated(activity, "OK")
        yield()
        assertEquals("OK", liveData.value)

        activity.finish()
        yield()

        while (activity.lifecycle.currentState != Lifecycle.State.DESTROYED) {
            yield()
        }
        yield()

        assertNull(liveData.value)
    }

    @Test
    fun setValueWhenResumed() = instrumentationBlockingTest(Dispatchers.Main) {
        val activity = makeActivity()
        val liveData = MutableLiveData<String>()
        liveData.setValueWhenResumed(activity, "OK")
        yield()
        assertEquals("OK", liveData.value)

        activity.finish()
        yield()

        while (activity.lifecycle.currentState != Lifecycle.State.DESTROYED) {
            yield()
        }
        yield()

        assertNull(liveData.value)
    }

    @Test
    fun filterNotNull() = compatibleBlockingTest(Dispatchers.Main) {
        val src = MutableLiveData<String?>()
        val dst = src.filterNotNull()

        src.forceActiveForever()
        dst.forceActiveForever()

        assertNull(src.value)
        assertNull(dst.value)
        yield()
        src.value = "foo"
        yield()
        assertEquals(src.value, dst.value)
        src.value = null
        yield()
        assertEquals("foo", dst.value)
    }

    @Test
    fun filterNotNull_initial() = compatibleBlockingTest(Dispatchers.Main) {
        val src = MutableLiveData<String?>()
        val dst = src.filterNotNull("")

        src.forceActiveForever()
        dst.forceActiveForever()

        assertNull(src.value)
        assertEquals("", dst.value)

        yield()
        src.value = "foo"
        yield()
        assertEquals(src.value, dst.value)
        src.value = null
        yield()
        assertEquals("foo", dst.value)
    }
}
