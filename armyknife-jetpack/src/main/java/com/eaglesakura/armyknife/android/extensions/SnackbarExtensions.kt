package com.eaglesakura.armyknife.android.extensions

import android.widget.TextView
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar

/**
 * set background color in Snackbar.
 *
 * e.g.)
 * val snackbar: Snackbar
 * val color: Int = Colors.RED
 * snackbar.setBackgroundColor(color)
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/armyknife-jetpack
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Snackbar.setBackgroundColor(@ColorInt color: Int) {
    view.setBackgroundColor(color)
}

/**
 * set text color in Snackbar.
 *
 * e.g.)
 * val snackbar: Snackbar
 * val color: Int = Colors.RED
 * snackbar.setTextColor(color)
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/armyknife-jetpack
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Snackbar.setTextColor(@ColorInt color: Int) {
    view.findViewByFilter<TextView> { it is TextView }?.setTextColor(color)
}
