package com.halil.ozel.exoplayerdemo

import android.app.Activity
import android.app.AlertDialog
import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.halil.ozel.exoplayerdemo.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private var player: ExoPlayer? = null
    private var playbackPosition = 0L
    private var mediaItemIndex = 0
    private var playWhenReady = true
    private var playbackSpeed = 1f
    private var isMuted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSpeedControls()
        setupMuteControl()
        setupPipControl()
        binding.streamButton.setOnClickListener { showStreamPicker() }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        if (player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isInPipMode() && Build.VERSION.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (!isInPipMode()) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPipMode()
    }

    @Deprecated("Deprecated in Java")
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode)
        onPipModeChanged(isInPictureInPictureMode)
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        onPipModeChanged(isInPictureInPictureMode)
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer() {
        if (player != null) {
            return
        }

        val exoPlayer = ExoPlayer.Builder(this)
            .setSeekBackIncrementMs(SEEK_INCREMENT_MS)
            .setSeekForwardIncrementMs(SEEK_INCREMENT_MS)
            .build()

        exoPlayer.playWhenReady = playWhenReady
        exoPlayer.setPlaybackSpeed(playbackSpeed)
        exoPlayer.volume = if (isMuted) 0f else 1f
        exoPlayer.setMediaItems(DemoStreams.mediaItems(), mediaItemIndex, playbackPosition)
        exoPlayer.prepare()

        binding.playerView.player = exoPlayer
        player = exoPlayer
    }

    private fun setupSpeedControls() {
        binding.increaseSpeedButton.setOnClickListener { changePlaybackSpeed(0.25f) }
        binding.decreaseSpeedButton.setOnClickListener { changePlaybackSpeed(-0.25f) }
        updateSpeedText()
    }

    private fun setupMuteControl() {
        binding.muteToggleButton.setOnClickListener {
            isMuted = !isMuted
            player?.volume = if (isMuted) 0f else 1f
            updateMuteButton()
        }
        updateMuteButton()
    }

    private fun setupPipControl() {
        binding.pipButton.setOnClickListener { enterPipMode() }
    }

    private fun showStreamPicker() {
        val player = player ?: return
        AlertDialog.Builder(this)
            .setTitle(R.string.choose_stream)
            .setItems(DemoStreams.titles()) { _, which ->
                player.seekTo(which, 0L)
                player.prepare()
                player.play()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun changePlaybackSpeed(delta: Float) {
        playbackSpeed = (playbackSpeed + delta).coerceIn(MIN_SPEED, MAX_SPEED)
        player?.setPlaybackSpeed(playbackSpeed)
        updateSpeedText()
    }

    private fun updateSpeedText() {
        binding.textSpeed.text = getString(R.string.speed_format, playbackSpeed)
    }

    private fun updateMuteButton() {
        binding.muteToggleButton.setText(if (isMuted) R.string.unmute else R.string.mute)
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val params = PictureInPictureParams.Builder()
            .setAspectRatio(Rational(16, 9))
            .build()
        enterPictureInPictureMode(params)
    }

    @OptIn(UnstableApi::class)
    private fun onPipModeChanged(isInPictureInPictureMode: Boolean) {
        binding.playerView.useController = !isInPictureInPictureMode
        binding.playbackControls.visibility =
            if (isInPictureInPictureMode) View.GONE else View.VISIBLE
    }

    private fun isInPipMode(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isInPictureInPictureMode
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            mediaItemIndex = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            playbackSpeed = exoPlayer.playbackParameters.speed
            binding.playerView.player = null
            exoPlayer.release()
        }
        player = null
    }

    companion object {
        private const val SEEK_INCREMENT_MS = 15_000L
        private const val MIN_SPEED = 0.5f
        private const val MAX_SPEED = 2f
    }
}
