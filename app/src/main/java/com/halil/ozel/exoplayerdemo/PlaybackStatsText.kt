package com.halil.ozel.exoplayerdemo

/**
 * Formats [androidx.media3.exoplayer.analytics.PlaybackStats] numbers for the on-screen overlay.
 * Bitrate arguments are bits per second; values `<= 0` mean Media3 has not reported a mean yet.
 */
object PlaybackStatsText {

    fun format(
        playTimeMs: Long,
        meanVideoBitrateBps: Int,
        droppedFrames: Long,
        meanBandwidthBps: Int,
    ): String {
        val playSeconds = (playTimeMs / 1000L).coerceAtLeast(0L)
        return "play ${playSeconds}s • video ${kbps(meanVideoBitrateBps)} • " +
            "drop $droppedFrames • bw ${kbps(meanBandwidthBps)}"
    }

    private fun kbps(bitsPerSecond: Int): String {
        return if (bitsPerSecond > 0) "${bitsPerSecond / 1000} kbps" else "—"
    }
}
