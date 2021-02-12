@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.eaglesakura.armyknife.android

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.os.Build
import android.view.Surface
import android.view.WindowManager
import androidx.annotation.IntDef

/**
 * Utility for Application Runtime information.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/armyknife-jetpack
 */
@Deprecated("https://github.com/eaglesakura/android-application-runtime")
object ApplicationRuntime {

    /**
     * identifier of this process.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    @Deprecated("remove this")
    val pid: Int
        get() = android.os.Process.myPid()

    /**
     * Required Notification channel.
     * it is not exist "NotificationChannelCompat" or such else.
     * Should check SDK_INT, and register notification channel when it.
     */
    @Deprecated("remove this")
    val requiredNotificationChannel: Boolean = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)

    /**
     * Kill self process.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    @Deprecated("remove this")
    fun killSelf() {
        android.os.Process.killProcess(pid)
        while (true) {
            // spin lock.
        }
    }

    /**
     * Robolectric runtime is true.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    @Deprecated("remove this")
    val ROBOLECTRIC: Boolean = try {
        Class.forName("org.robolectric.Robolectric")
        true
    } catch (err: ClassNotFoundException) {
        false
    }

    /**
     * Run on JUnit test case.
     */
    @Deprecated("remove this")
    const val RUNTIME_JUNIT = 0x01 shl 0

    /**
     * Run on JUnit with Robolectric.
     *
     * @link http://robolectric.org/
     */
    @Deprecated("remove this")
    const val RUNTIME_ROBOLECTRIC = 0x01 shl 1

    /**
     * Run on JUnit with Instrumentation Test in Android device.
     */
    const val RUNTIME_INSTRUMENTATION = 0x01 shl 2

    /**
     * Run on Android device.
     */
    @Deprecated("remove this")
    const val RUNTIME_ANDROID_DEVICE = 0x01 shl 3

    /**
     * Run on ART VM in Android device.
     */
    @Deprecated("remove this")
    const val RUNTIME_ART = 0x01 shl 4

    /**
     * Run on Dalvik VM in Android device.
     */
    @Deprecated("remove this")
    const val RUNTIME_DALVIK = 0x01 shl 5

    /**
     * Run on Java VM on PC.
     */
    @Deprecated("remove this")
    const val RUNTIME_JAVA_VM = 0x01 shl 6

    /**
     * Runtime flags in current Virtual Machine(ART in Android or JVM in PC, or such else)
     *
     * @see RUNTIME_JUNIT
     * @see RUNTIME_ROBOLECTRIC
     * @see RUNTIME_INSTRUMENTATION
     * @see RUNTIME_ANDROID_DEVICE
     * @see RUNTIME_ART
     * @see RUNTIME_DALVIK
     * @see RUNTIME_JAVA_VM
     */
    @Deprecated("remove this")
    val runtimeFlags: Int = getRuntimeFlagsImpl()

    @Deprecated("remove this")
    @Retention(AnnotationRetention.BINARY)
    @IntDef(
        RUNTIME_JUNIT,
        RUNTIME_ROBOLECTRIC,
        RUNTIME_INSTRUMENTATION,
        RUNTIME_ANDROID_DEVICE,
        RUNTIME_ART,
        RUNTIME_DALVIK,
        RUNTIME_JAVA_VM
    )
    annotation class RuntimeFlag

    /**
     * Check runtime has all flags.
     *
     * e.g.)
     * if(ApplicationRuntime.runIn(RUNTIME_ART, RUNTIME_INSTRUMENTATION)) {
     *      // ART with Instrumentation test.
     * }
     */
    @Deprecated("remove this")
    @SuppressLint("WrongConstant")
    fun runIn(
        @RuntimeFlag flag: Int,
        @RuntimeFlag vararg flags: Int
    ): Boolean {
        var allFlags = flag
        flags.forEach { flg ->
            allFlags = allFlags or flg
        }
        return (runtimeFlags and allFlags) == allFlags
    }

    /**
     * returns rotation of the device in degree.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    @Deprecated("remove this")
    fun getDeviceRotateDegree(context: Context): Int {
        val surfaceRotation =
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        return when (surfaceRotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> throw IllegalStateException("rotate[$surfaceRotation] is not supported")
        }
    }

    /**
     * If this process is foreground now then returns true.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    @Deprecated("remove this")
    fun isForegroundApplicationSelf(context: Context): Boolean {
        return context.packageName == getTopApplicationPackage(context)
    }

    /**
     * Returns launcher objects.
     */
    @Deprecated("will be delete this.")
    fun listLauncherApplications(context: Context): List<ResolveInfo> {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        return pm.queryIntentActivities(intent, 0)
    }

    /**
     * Returns installed applications.
     */
    @Deprecated("will be delete this.")
    fun listInstalledApplications(context: Context): List<ApplicationInfo> {
        val pm = context.packageManager
        return pm.getInstalledApplications(0)
    }

    /**
     * Returns an running application on top activity of this device.
     *
     * Required, in AndroidManifest.xml
     * <manifest>
     *      <uses-permission android:name="android.permission.GET_TASKS"/>
     *      <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" tools:ignore="ProtectedPermissions"/>
     * </manifest>
     *
     * Required, in Runtime Permission
     * GrantPermissionRule.grant(Manifest.permission.PACKAGE_USAGE_STATS)!!
     *
     * @see RuntimePermissions.hasAccessUsageStatusPermission
     */
    @Deprecated("remove this")
    @SuppressLint("WrongConstant")
    fun getTopApplicationPackage(context: Context): String {
        if (Build.VERSION.SDK_INT >= 22) {
            val usm = context.getSystemService("usagestats") as UsageStatsManager
            val time = System.currentTimeMillis()
            val events = usm.queryEvents(time - 1000 * 60 * 60, time)
            if (events != null && events.hasNextEvent()) {
                val app = android.app.usage.UsageEvents.Event()
                var lastAppTime: Long = 0
                var packageName: String? = null
                while (events.hasNextEvent()) {
                    events.getNextEvent(app)
                    if (app.timeStamp > lastAppTime && app.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        packageName = app.packageName
                        lastAppTime = app.timeStamp
                    }
                }

                if (!packageName.isNullOrEmpty()) {
                    return packageName!!
                }
            }
        } else {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val processes = activityManager.runningAppProcesses
            for (info in processes) {
                if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return if (info.importanceReasonComponent != null) {
                        info.importanceReasonComponent.packageName
                    } else {
                        info.pkgList[0]
                    }
                }
            }
        }
        return context.packageName
    }

    /**
     * This method returns true when a "clazz" service is running on this device.
     */
    @Deprecated("remove this")
    fun <T : Service> isServiceRunning(context: Context, clazz: Class<T>): Boolean {
        try {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val services = activityManager.getRunningServices(Integer.MAX_VALUE)
            for (info in services) {
                if (clazz.name == info.service.className) {
                    // 一致するクラスが見つかった
                    return true
                }
            }
            return false
        } catch (e: Exception) {
        }

        return false
    }

    private fun getRuntimeFlagsImpl(): Int {
        // check Virtual Machine.
        val virtualMachineFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ApplicationRuntime.RUNTIME_ART
        } else {
            ApplicationRuntime.RUNTIME_DALVIK
        }

        try {
            Class.forName("org.junit.Assert")
        } catch (e: ClassNotFoundException) {
            return virtualMachineFlags or RUNTIME_ANDROID_DEVICE
        }

        val runtimeName = System.getProperty("java.runtime.name")?.toLowerCase() ?: ""
        return when {
            runtimeName.isEmpty() -> {
                // Android Runtime.
                virtualMachineFlags or RUNTIME_ANDROID_DEVICE
            }
            runtimeName.contains("android") -> {
                // in Instrumentation test.
                virtualMachineFlags or RUNTIME_ANDROID_DEVICE or RUNTIME_JUNIT or RUNTIME_INSTRUMENTATION
            }
            else -> {
                // Only Java VM
                RUNTIME_JUNIT or RUNTIME_ROBOLECTRIC or RUNTIME_JAVA_VM
            }
        }
    }
}
