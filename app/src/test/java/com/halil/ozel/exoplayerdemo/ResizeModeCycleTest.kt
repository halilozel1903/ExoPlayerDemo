package com.halil.ozel.exoplayerdemo

import org.junit.Assert.assertEquals
import org.junit.Test

class ResizeModeCycleTest {

    @Test
    fun nextWalksFitZoomFill() {
        assertEquals(ResizeModeCycle.ZOOM, ResizeModeCycle.next(ResizeModeCycle.FIT))
        assertEquals(ResizeModeCycle.FILL, ResizeModeCycle.next(ResizeModeCycle.ZOOM))
        assertEquals(ResizeModeCycle.FIT, ResizeModeCycle.next(ResizeModeCycle.FILL))
    }

    @Test
    fun nextFallsBackToFirstKnownMode() {
        assertEquals(ResizeModeCycle.ZOOM, ResizeModeCycle.next(/* unknown */ 99))
    }
}
