# ExoPlayer Demo (AndroidX Media3)

Kotlin sample that plays adaptive **HLS**, **DASH**, and progressive video with [AndroidX Media3 ExoPlayer](https://developer.android.com/media/media3/exoplayer).

This project uses **Media3 1.11.1** (`media3-exoplayer`, `media3-exoplayer-hls`, `media3-exoplayer-dash`, `media3-ui`, and `media3-session`), verified on [Google Maven](https://dl.google.com/dl/android/maven2/androidx/media3/media3-exoplayer/1.11.1/media3-exoplayer-1.11.1.pom) and the [Media3 release notes](https://developer.android.com/jetpack/androidx/releases/media3) (stable as of 10 September 2026).

## What the demo does

- Plays a short playlist of official Media3 demo samples via `ExoPlayer.setMediaItems`.
- Uses `androidx.media3.ui.PlayerView` with rewind / fast-forward (15s seek increments), next / previous, buffering indicator, subtitle button, **repeat** (`one` / `all`), and **shuffle**.
- Restores the current item, playback position, play-when-ready, speed, mute, repeat, and shuffle across lifecycle events.
- Lets you pick **video quality**, **audio**, and **text** tracks with `Player.trackSelectionParameters` and `TrackSelectionOverride` (the same Tracks APIs Media3 documents for manual selection).
- Shows a retry overlay on `Player.Listener.onPlayerError` using the Media3 `PlaybackException` error code.
- Publishes an in-activity `MediaSession` so headset, Bluetooth, and system media keys can control the same `ExoPlayer` instance while the activity is alive.
- Picture-in-picture on API 26+: Home / PiP button uses `PictureInPictureParams`. Playback continues in PiP; overlay controls hide.
- Overlay controls for playback speed (`0.5x`–`2.0x`) and mute / unmute.

The sample URIs are taken from the [Media3 main demo media list](https://github.com/androidx/media/blob/release/demos/main/src/main/assets/media.exolist.json):

| Sample | URI |
| --- | --- |
| Apple BIPBOP HLS (fMP4) | `https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8` |
| Tears of Steel DASH HD (clear H264) | `https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd` |
| Screens 480p (FMP4, H264) | `https://storage.googleapis.com/exoplayer-test-media-1/gen-3/screens/dash-vod-single-segment/video-avc-baseline-480.mp4` |

DRM-protected entries from that list are intentionally omitted here (see the separate ExoPlayer DRM sample).

## MediaSession vs notification

`MediaSession.Builder(context, player)` is enough for media keys while the player is owned by `MainActivity`. A `MediaSessionService` / `PlayerNotificationManager` foreground notification is **not** used: this is still a foreground video demo (with PiP), not a background playback service.

## Requirements

- JDK 17 (Android Gradle Plugin 8.11)
- Android Studio Ladybug / Narwhal or newer (or command-line SDK)
- Android SDK with `compileSdk` / `targetSdk` 35
- Device or emulator on **API 24+** with network access

## Build and run

From this directory:

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
```

Install on a connected device:

```bash
./gradlew :app:installDebug
```

Or open the project in Android Studio, wait for Gradle sync, and run the `app` configuration.

The app needs the `INTERNET` permission (already declared) so ExoPlayer can fetch playlists and segments.

## Media3 modules

| Artifact | Role |
| --- | --- |
| `androidx.media3:media3-exoplayer:1.11.1` | Player, `TrackSelectionParameters` |
| `androidx.media3:media3-exoplayer-hls:1.11.1` | HLS `MediaSource` (from `MediaItem` URI) |
| `androidx.media3:media3-exoplayer-dash:1.11.1` | DASH `MediaSource` (from `MediaItem` URI) |
| `androidx.media3:media3-ui:1.11.1` | `PlayerView` / `PlayerControlView` |
| `androidx.media3:media3-session:1.11.1` | `MediaSession` |

Docs: [Getting started with ExoPlayer](https://developer.android.com/media/media3/exoplayer/hello-world), [Track selection](https://developer.android.com/media/media3/exoplayer/track-selection), [MediaSession](https://developer.android.com/media/media3/session/control-playback).

## License

MIT License. See the license text below.

```
MIT License

Copyright (c) 2023 Halil OZEL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the
following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
