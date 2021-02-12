package com.eaglesakura.armyknife.android

import android.Manifest
import android.annotation.TargetApi
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

/**
 * Utilities for Runtime permissions.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/armyknife-jetpack
 * @link https://github.com/eaglesakura/android-application-runtime
 */
@Deprecated("https://github.com/eaglesakura/android-application-runtime")
object RuntimePermissions {

    /**
     * This functions returns true when Android M(Android 6.0).
     */
    @Deprecated("Don't use this. replace to Compat classes in Android Jetpack")
    val supported: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    val PERMISSIONS_SELF_LOCATION = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val PERMISSIONS_BLUETOOTH = listOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN
    )

    val PERMISSIONS_GOOGLE_MAP = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    val PERMISSIONS_EXTERNAL_STORAGE = listOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    /**
     * This method returns Intent for Application settings.
     * User can change this application setting.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    fun newAppSettingIntent(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        return intent
    }

    /**
     * This method returns Intent for Overlay setting.
     * User can change overlay settings.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun newAppOverlaySettingIntent(context: Context): Intent {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:" + context.packageName)
        return intent
    }

    /**
     * This method returns Intent for "usage stats access permission".
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    fun newUsageStatusSettingIntent(@Suppress("UNUSED_PARAMETER") context: Context): Intent {
        return Intent("android.settings.USAGE_ACCESS_SETTINGS")
    }

    /**
     * If all permissions are granted, this method returns true.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    fun hasAllRuntimePermissions(context: Context, permissions: Iterable<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    /**
     * If this app has permission for draw on system-layer, this method returns true.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    @Deprecated("Replace to RuntimePermissions.canDrawOverlays()", ReplaceWith("canDrawOverlays"))
    fun hasDrawOverlayPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            true
        } else {
            Settings.canDrawOverlays(context)
        }
    }

    /**
     * If this app has permission for draw on system-layer, this method returns true.
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    fun canDrawOverlays(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            true
        } else Settings.canDrawOverlays(context)
    }

    /**
     * If this app has permission for "usage stats access", this method returns true.
     *
     * Required, in AndroidManifest.xml
     * <manifest>
     *      <uses-permission android:name="android.permission.GET_TASKS"/>
     *      <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" tools:ignore="ProtectedPermissions"/>
     * </manifest>
     *
     * @author @eaglesakura
     * @link https://github.com/eaglesakura/armyknife-jetpack
     */
    fun hasAccessUsageStatusPermission(context: Context): Boolean {
        @Suppress("LiftReturnOrAssignment")
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        if (Build.VERSION.SDK_INT <= 19) {
            return true
        } else {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val uid = android.os.Process.myUid()
            val mode =
                appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, uid, context.packageName)
            return mode == AppOpsManager.MODE_ALLOWED
        }
    }
}
