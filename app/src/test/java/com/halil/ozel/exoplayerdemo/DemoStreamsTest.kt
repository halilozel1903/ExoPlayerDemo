package com.halil.ozel.exoplayerdemo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DemoStreamsTest {

    @Test
    fun playlistUsesOfficialHttpsSamples() {
        assertEquals(3, DemoStreams.playlist.size)
        assertEquals(DemoStreams.playlist.size, DemoStreams.playlist.map { it.id }.toSet().size)
        DemoStreams.playlist.forEach { stream ->
            assertTrue(stream.uri.startsWith("https://"))
        }
    }

    @Test
    fun playlistHostsMatchMedia3DemoList() {
        val uris = DemoStreams.playlist.map { it.uri }
        assertTrue(uris.any { it.contains("devstreaming-cdn.apple.com") && it.endsWith("master.m3u8") })
        assertTrue(uris.any { it.contains("storage.googleapis.com/wvmedia/clear/") && it.endsWith(".mpd") })
        assertTrue(uris.any { it.contains("storage.googleapis.com/exoplayer-test-media-1/") })
    }
}
