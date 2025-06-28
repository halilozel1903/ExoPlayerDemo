package com.halil.ozel.exoplayerdemo

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.graphics.BitmapFactory
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.DefaultTrackNameProvider
import androidx.media3.ui.TrackSelectionDialogBuilder
import com.halil.ozel.exoplayerdemo.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var isLocked = false
    private var isLandscape = false
    private var isBrightMax = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        preparePlayer()
        initControls()
    }

    private fun setView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initControls() {
        binding.brightnessButton.setOnClickListener { toggleBrightness() }
        binding.lockButton.setOnClickListener { toggleLock() }
        binding.orientationButton.setOnClickListener { toggleOrientation() }
        binding.speedButton.setOnClickListener { showSpeedMenu(it) }
        binding.languageButton.setOnClickListener { showLanguageDialog() }
        binding.pipButton.setOnClickListener { enterPipMode() }

      private fun setupSpeedControls() {
        binding.increaseSpeedButton.setOnClickListener { changePlaybackSpeed(0.25f) }
        binding.decreaseSpeedButton.setOnClickListener { changePlaybackSpeed(-0.25f) }
        updateSpeedText()
    }

    private fun setupMuteControl() {
        binding.muteToggleButton.setOnClickListener {
            isMuted = !isMuted
            exoPlayer?.volume = if (isMuted) 0f else 1f
            updateMuteButton()
        }
        updateMuteButton()
    }

    private fun updateMuteButton() {
        val textRes = if (isMuted) R.string.unmute else R.string.mute
        binding.muteToggleButton.setText(textRes)
    }

    private fun changePlaybackSpeed(delta: Float) {
        playbackSpeed = (playbackSpeed + delta).coerceIn(0.5f, 2f)
        exoPlayer?.setPlaybackSpeed(playbackSpeed)
        updateSpeedText()
    }

    private fun preparePlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer?.playWhenReady = true
        exoPlayer?.setPlaybackSpeed(playbackSpeed)
        binding.playerView.player = exoPlayer
        binding.playerView.defaultArtwork = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_launcher_background
        )
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaItem = MediaItem.fromUri(URL)
        val mediaSource =
            HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
        exoPlayer?.apply {
            setMediaSource(mediaSource)
            seekTo(playbackPosition)
            playWhenReady = playWhenReady
            prepare()
        }
    }

    private fun toggleBrightness() {
        val attributes = window.attributes
        attributes.screenBrightness = if (isBrightMax) {
            WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        } else {
            1f
        }
        window.attributes = attributes
        isBrightMax = !isBrightMax
    }

    private fun toggleLock() {
        requestedOrientation = if (isLocked) {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
        isLocked = !isLocked
    }

    private fun toggleOrientation() {
        if (isLocked) return
        requestedOrientation = if (isLandscape) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        isLandscape = !isLandscape
    }

    private fun showSpeedMenu(anchor: View) {
        val menu = PopupMenu(this, anchor)
        val speeds = listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f)
        speeds.forEach { speed ->
            menu.menu.add("${speed}x")
        }
        menu.setOnMenuItemClickListener { item ->
            val label = item.title.toString().removeSuffix("x")
            val speed = label.toFloatOrNull() ?: 1f
            exoPlayer?.setPlaybackParameters(PlaybackParameters(speed))
            true
        }
        menu.show()
    }

    private fun showLanguageDialog() {
        exoPlayer?.let { player ->
            TrackSelectionDialogBuilder(
                this,
                getString(R.string.language),
                player
            )
                .setTrackNameProvider(DefaultTrackNameProvider(resources))
                .build()
                .show()
        }
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPictureInPictureMode()
        }
    }

    private fun releasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPipMode()
    }

    companion object {
        private const val URL = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
    }
}
