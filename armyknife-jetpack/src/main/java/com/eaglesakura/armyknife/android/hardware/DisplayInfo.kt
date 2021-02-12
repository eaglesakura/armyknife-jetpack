package com.eaglesakura.armyknife.android.hardware

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import kotlinx.parcelize.Parcelize

/**
 * This object calculate display information this device.
 *
 * e.g.)
 * val info = DisplayInfo.newInstance(context)
 * Log.d("DisplayInfo", "${info.widthPixel} x ${info.heightPixel}")
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/armyknife-jetpack
 */
@Suppress("MemberVisibilityCanBePrivate")
@Parcelize
@Deprecated("https://github.com/eaglesakura/displayinfo")
data class DisplayInfo(

    /**
     * Display horizontal pixels
     */
    val widthPixel: Int,

    /**
     * Display vertical pixels
     */
    val heightPixel: Int,

    val deviceType: DeviceType,

    /**
     * Display horizontal dp size.
     */
    val widthDp: Float,

    /**
     * Display vertical dp size.
     */
    val heightDp: Float,

    val widthInch: Float,

    val heightInch: Float,

    /**
     * Display-size with round for user.
     * Rounding off the 2nd decimal place.
     *
     * example.
     * 4.65 inch display, returns "4.7".
     * Inch(major=4, minor=7)
     */
    val diagonalRoundInch: Inch,

    /**
     * Dot per inch.
     */
    val dpi: Dpi,

    /**
     * values-sw${smallestWidthDp}dp
     */
    val smallestWidthDp: Int,

    val diagonalInch: Float
) : Parcelable {

    @Suppress("EnumEntryName")
    enum class Dpi {
        ldpi,
        mdpi,
        tvdpi,
        hdpi,
        xhdpi,
        xxhdpi,
        xxxhdpi;
    }

    enum class DeviceType {
        Watch,
        Phone,
        Phablet,
        Tablet,
        Other
    }

    /**
     * Display inch
     */
    @Parcelize
    data class Inch(val major: Int, val minor: Int) : Parcelable {

        @Suppress("unused")
        fun toFloat(): Float {
            return major.toFloat() + (minor.toFloat() / 10.0f)
        }

        override fun toString(): String {
            return major.toString() + "." + minor.toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context): DisplayInfo {

            val display =
                (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)

            val widthPixel: Int
            val heightPixel: Int

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                val point = Point(0, 0)
                display.getRealSize(point)
                widthPixel = point.x
                heightPixel = point.y
            } else {
                val clazz = Display::class.java
                val getRawWidth = clazz.getMethod("getRawWidth")
                val getRawHeight = clazz.getMethod("getRawHeight")
                widthPixel = getRawWidth.invoke(display) as Int
                heightPixel = getRawHeight.invoke(display) as Int
            }

            val widthDp = widthPixel.toFloat() / displayMetrics.density
            val heightDp = heightPixel.toFloat() / displayMetrics.density

            val widthInch = widthPixel.toFloat() / displayMetrics.xdpi
            val heightInch = heightPixel.toFloat() / displayMetrics.ydpi
            val dpi = toDpi(displayMetrics.xdpi, displayMetrics.ydpi)

            val diagonalInch =
                Math.sqrt((widthInch * widthInch + heightInch * heightInch).toDouble()).toFloat()
            val diagonal = (diagonalInch * 100.0f).toInt().let { (it + 5) / 10 }
            val diagonalRoundInch = Inch(diagonal / 10, diagonal % 10)

            // 丸められたインチ数で、デバイスを種類を指定する
            val deviceType = when {
                Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT_WATCH -> DeviceType.Watch
                diagonalRoundInch.major <= 5 -> DeviceType.Phone
                diagonalRoundInch.major <= 6 -> DeviceType.Phablet
                diagonalRoundInch.major <= 12 -> DeviceType.Tablet
                else -> DeviceType.Other
            }

            val smallestWidthDp = Math.min(widthDp, heightDp).toInt() / 10 * 10

            return DisplayInfo(
                widthPixel = widthPixel,
                heightPixel = heightPixel,
                widthDp = widthDp,
                heightDp = heightDp,
                widthInch = widthInch,
                heightInch = heightInch,
                deviceType = deviceType,
                smallestWidthDp = smallestWidthDp,
                dpi = dpi,
                diagonalInch = diagonalInch,
                diagonalRoundInch = diagonalRoundInch
            )
        }

        private fun toDpi(xdpi: Float, ydpi: Float): DisplayInfo.Dpi {
            val dpi = Math.min(xdpi, ydpi)

            if (dpi > 480) {
                return DisplayInfo.Dpi.xxxhdpi
            }

            if (dpi > 320) {
                return DisplayInfo.Dpi.xxhdpi
            }

            if (dpi > 240) {
                return DisplayInfo.Dpi.xhdpi
            }

            if (dpi > 210) {
                return DisplayInfo.Dpi.tvdpi
            }

            if (dpi > 160) {
                return DisplayInfo.Dpi.hdpi
            }

            return if (dpi > 120) {
                DisplayInfo.Dpi.mdpi
            } else DisplayInfo.Dpi.ldpi
        }
    }
}
