package com.halil.ozel.exoplayerdemo

/**
 * Cycles [androidx.media3.ui.AspectRatioFrameLayout] resize modes.
 * Values match Media3 1.11 `RESIZE_MODE_FIT` (0), `RESIZE_MODE_ZOOM` (4), `RESIZE_MODE_FILL` (3).
 */
object ResizeModeCycle {

    const val FIT = 0
    const val ZOOM = 4
    const val FILL = 3

    private val modes: IntArray = intArrayOf(FIT, ZOOM, FILL)

    fun next(current: Int): Int {
        val index = modes.indexOf(current).let { if (it < 0) 0 else it }
        return modes[(index + 1) % modes.size]
    }

    fun labelRes(mode: Int): Int {
        return when (mode) {
            ZOOM -> R.string.resize_zoom
            FILL -> R.string.resize_fill
            else -> R.string.resize_fit
        }
    }
}
