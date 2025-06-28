package com.halil.ozel.exoplayerdemo

import android.app.Activity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.halil.ozel.exoplayerdemo.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var playbackSpeed = 1f
    private var isMuted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        preparePlayer()
        setupSpeedControls()
        setupMuteControl()
    }

    private fun setView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

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

    private fun updateSpeedText() {
        binding.textSpeed.text = String.format("%.2fx", playbackSpeed)
    }

    private fun preparePlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer?.playWhenReady = true
        exoPlayer?.setPlaybackSpeed(playbackSpeed)
        binding.playerView.player = exoPlayer
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

    companion object {
        private const val URL = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
    }
}
