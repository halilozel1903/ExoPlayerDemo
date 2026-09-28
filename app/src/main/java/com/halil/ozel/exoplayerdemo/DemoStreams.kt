package com.halil.ozel.exoplayerdemo

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

/**
 * Official sample streams from the Media3 main demo media list:
 * https://github.com/androidx/media/blob/release/demos/main/src/main/assets/media.exolist.json
 */
data class DemoStream(
    val id: String,
    val title: String,
    val uri: String,
)

object DemoStreams {

    val playlist: List<DemoStream> = listOf(
        DemoStream(
            id = "hls-bipbop-fmp4",
            title = "Apple BIPBOP HLS (fMP4)",
            uri = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8",
        ),
        DemoStream(
            id = "dash-tears-h264-hd",
            title = "Tears of Steel DASH HD (H264)",
            uri = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd",
        ),
        DemoStream(
            id = "progressive-screens-480",
            title = "Screens 480p (FMP4, H264)",
            uri = "https://storage.googleapis.com/exoplayer-test-media-1/gen-3/screens/dash-vod-single-segment/video-avc-baseline-480.mp4",
        ),
    )

    fun mediaItems(): List<MediaItem> = playlist.map { stream ->
        MediaItem.Builder()
            .setMediaId(stream.id)
            .setUri(stream.uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(stream.title)
                    .setArtist("Media3 demo samples")
                    .build()
            )
            .build()
    }

    fun titles(): Array<String> = playlist.map { it.title }.toTypedArray()
}
