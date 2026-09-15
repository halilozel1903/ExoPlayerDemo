# ExoPlayer Demo (AndroidX Media3)

Kotlin sample that plays adaptive **HLS** video with [AndroidX Media3 ExoPlayer](https://developer.android.com/media/media3/exoplayer).

This project uses **Media3 1.11.0** (`androidx.media3:media3-exoplayer`, `media3-exoplayer-hls`, and `media3-ui`), verified on [Google Maven](https://dl.google.com/dl/android/maven2/androidx/media3/media3-exoplayer/1.11.0/media3-exoplayer-1.11.0.pom) and the [Media3 release notes](https://developer.android.com/jetpack/androidx/releases/media3) (stable as of 5 August 2026).

## What the demo does

- Plays an official Media3 demo HLS stream (Apple BIPBOP fMP4, including subtitle renditions) via `ExoPlayer.setMediaItem`.
- Uses `androidx.media3.ui.PlayerView` with rewind / fast-forward (15s seek increments on the player), buffering indicator, and the subtitle button.
- Restores playback position, play-when-ready, speed, and mute across lifecycle events.
- Picture-in-picture on API 26+: Home / PiP button uses `PictureInPictureParams`. Playback continues in PiP; overlay controls hide.
- Overlay controls for playback speed (`0.5x`–`2.0x`) and mute / unmute.

The HLS URI is taken from the [Media3 main demo media list](https://github.com/androidx/media/blob/release/demos/main/src/main/assets/media.exolist.json):

`https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8`

## Requirements

- JDK 17 (Android Gradle Plugin 8.11)
- Android Studio Ladybug / Narwhal or newer (or command-line SDK)
- Android SDK with `compileSdk` / `targetSdk` 35
- Device or emulator on **API 24+** with network access

## Build and run

From this directory:

```bash
./gradlew :app:assembleDebug
```

Install on a connected device:

```bash
./gradlew :app:installDebug
```

Or open the project in Android Studio, wait for Gradle sync, and run the `app` configuration.

The app needs the `INTERNET` permission (already declared) so ExoPlayer can fetch the HLS playlist and segments.

## Media3 modules

| Artifact | Role |
| --- | --- |
| `androidx.media3:media3-exoplayer:1.11.0` | Player |
| `androidx.media3:media3-exoplayer-hls:1.11.0` | HLS `MediaSource` (picked up automatically from `MediaItem`) |
| `androidx.media3:media3-ui:1.11.0` | `PlayerView` / `PlayerControlView` |

Docs: [Getting started with ExoPlayer](https://developer.android.com/media/media3/exoplayer/hello-world).

## License

MIT License. See the license text below.

```
MIT License

Copyright (c) 2023 Halil OZEL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

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
