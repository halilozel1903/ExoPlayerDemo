package com.halil.ozel.exoplayerdemo

import android.app.AlertDialog
import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride

class TrackSelectionHelper(
    private val context: Context,
    private val playerProvider: () -> Player?,
) {

    fun showVideoTracks() {
        showTracks(
            trackType = C.TRACK_TYPE_VIDEO,
            titleRes = R.string.video_track_title,
            includeAuto = true,
            includeDisable = false,
        )
    }

    fun showAudioTracks() {
        showTracks(
            trackType = C.TRACK_TYPE_AUDIO,
            titleRes = R.string.audio_track_title,
            includeAuto = true,
            includeDisable = true,
        )
    }

    fun showTextTracks() {
        showTracks(
            trackType = C.TRACK_TYPE_TEXT,
            titleRes = R.string.text_track_title,
            includeAuto = false,
            includeDisable = true,
        )
    }

    private fun showTracks(
        trackType: @C.TrackType Int,
        titleRes: Int,
        includeAuto: Boolean,
        includeDisable: Boolean,
    ) {
        val player = playerProvider() ?: return
        val labels = mutableListOf<String>()
        val actions = mutableListOf<() -> Unit>()

        if (includeAuto) {
            labels += context.getString(R.string.track_auto)
            actions += {
                player.trackSelectionParameters = player.trackSelectionParameters
                    .buildUpon()
                    .clearOverridesOfType(trackType)
                    .setTrackTypeDisabled(trackType, false)
                    .build()
            }
        }
        if (includeDisable) {
            labels += context.getString(R.string.track_disable)
            actions += {
                player.trackSelectionParameters = player.trackSelectionParameters
                    .buildUpon()
                    .clearOverridesOfType(trackType)
                    .setTrackTypeDisabled(trackType, true)
                    .build()
            }
        }

        player.currentTracks.groups
            .filter { it.type == trackType }
            .forEach { group ->
                for (index in 0 until group.length) {
                    if (!group.isTrackSupported(index)) {
                        continue
                    }
                    val format = group.getTrackFormat(index)
                    labels += formatLabel(trackType, format, group.isTrackSelected(index))
                    val mediaTrackGroup = group.mediaTrackGroup
                    actions += {
                        player.trackSelectionParameters = player.trackSelectionParameters
                            .buildUpon()
                            .setTrackTypeDisabled(trackType, false)
                            .setOverrideForType(TrackSelectionOverride(mediaTrackGroup, index))
                            .build()
                    }
                }
            }

        if (labels.isEmpty()) {
            AlertDialog.Builder(context)
                .setMessage(R.string.tracks_unavailable)
                .setPositiveButton(android.R.string.ok, null)
                .show()
            return
        }

        AlertDialog.Builder(context)
            .setTitle(titleRes)
            .setItems(labels.toTypedArray()) { _, which -> actions[which].invoke() }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun formatLabel(
        trackType: @C.TrackType Int,
        format: Format,
        selected: Boolean,
    ): String {
        val body = when (trackType) {
            C.TRACK_TYPE_VIDEO -> {
                val height = if (format.height != Format.NO_VALUE) {
                    "${format.height}p"
                } else {
                    context.getString(R.string.track_unknown)
                }
                val bitrate = if (format.bitrate != Format.NO_VALUE) {
                    " • ${format.bitrate / 1000} kbps"
                } else {
                    ""
                }
                height + bitrate
            }
            C.TRACK_TYPE_AUDIO -> {
                val language = format.label ?: format.language
                    ?: context.getString(R.string.track_unknown)
                val channels = if (format.channelCount != Format.NO_VALUE) {
                    " • ${format.channelCount}ch"
                } else {
                    ""
                }
                language + channels
            }
            else -> format.label ?: format.language ?: context.getString(R.string.track_unknown)
        }
        return if (selected) context.getString(R.string.track_selected, body) else body
    }
}
