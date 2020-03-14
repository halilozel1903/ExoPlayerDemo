package com.halil.ozel.exoplayerdemo

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {


    // exoPlayer nesnesi tanımlanıyor.
    private lateinit var simpleExoPlayer: SimpleExoPlayer

    // exoPlayer'da kullanmak icin DataSource nesnesi tanımı
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    // exoPlayer'da kullanılacak olan url
    val URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // yeni bir instance baslatılması
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this)

        // DataSource içerisini doldurma
        mediaDataSourceFactory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayerDemo"))

        // media source nesnesine kullanılacak video türüne göre tanımlama ve url koyma islemi
        val mediaSource =
            ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(URL))

        // player'ı hazır hale getirme
        simpleExoPlayer.prepare(mediaSource, false, false)

        // play oynatılmaya hazır olduğunda video oynatma islemi
        simpleExoPlayer.playWhenReady = true

        // loyout dosyasındaki id degeri eslestirme
        playerView.player = simpleExoPlayer

        // player ekranına focuslanma ozelligi
        playerView.requestFocus()
        
    }
}
