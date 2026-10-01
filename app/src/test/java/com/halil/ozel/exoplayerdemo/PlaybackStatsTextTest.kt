package com.halil.ozel.exoplayerdemo

import org.junit.Assert.assertEquals
import org.junit.Test

class PlaybackStatsTextTest {

    @Test
    fun formatUsesSecondsAndKbps() {
        val text = PlaybackStatsText.format(
            playTimeMs = 12_400L,
            meanVideoBitrateBps = 2_500_000,
            droppedFrames = 3L,
            meanBandwidthBps = 8_000_000,
        )
        assertEquals("play 12s • video 2500 kbps • drop 3 • bw 8000 kbps", text)
    }

    @Test
    fun formatHidesUnsetBitrates() {
        val text = PlaybackStatsText.format(
            playTimeMs = 0L,
            meanVideoBitrateBps = -1,
            droppedFrames = 0L,
            meanBandwidthBps = 0,
        )
        assertEquals("play 0s • video — • drop 0 • bw —", text)
    }
}
