package com.niji.claudio.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.niji.claudio.common.data.feature.media.usecase.DeleteLocalMediaUseCase
import com.niji.claudio.common.tool.AndroidUiUtils
import com.niji.claudio.common.tool.LogUtils
import com.niji.claudio.common.tool.PlayerUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlayerActivity : ComponentActivity() {

    private var llMedias: LinearLayout? = null
    private val mediaPlayingList = mutableListOf<String>()
    private val exoPlayerList = mutableListOf<ExoPlayer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_medias)
        llMedias = findViewById(R.id.activity_medias_ll)
        getVideoViewFromPath(getFilePathFromIntent(intent))
        PlayerUtils.killPlayerLiveData.observeForever {
            if (it) {
                finish()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getVideoViewFromPath(getFilePathFromIntent(intent))
    }

    private fun getFilePathFromIntent(intent: Intent?): String? {
        val filePath = if (intent?.hasExtra(KEY_FILE_PATH) == true) {
            intent.getStringExtra(KEY_FILE_PATH)
        } else {
            null
        }
        return filePath
    }

    private fun getMediaTag(): String {
        var tag = ""
        var intTag = 0
        while (tag.isEmpty()) {
            if (!mediaPlayingList.contains(intTag.toString())) {
                tag = intTag.toString()
            } else {
                intTag++
            }
        }
        return tag
    }

    private fun isMediaSong(filePath: String?): Boolean {
        return filePath?.endsWith(".mp3") == true || filePath?.endsWith(".m4a") == true
    }

    private fun getVideoViewFromPath(filePath: String?) {
        val media = PlayerView(this)
        media.layoutParams = FrameLayout.LayoutParams(
            AndroidUiUtils.getScreenWidth(this),
            AndroidUiUtils.getScreenWidth(this)
        )
        media.useController = false
        val exoPlayer = ExoPlayer.Builder(this).build()
        media.player = exoPlayer
        exoPlayerList.add(exoPlayer)
        val tag = getMediaTag()
        media.tag = tag
        mediaPlayingList.add(tag)
        if (!isMediaSong(filePath)) {
            llMedias?.addView(media)
        }
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    onMediaFinish(tag)
                    exoPlayer.removeListener(this)
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                LogUtils.e(TAG, "error $error", error)
                onMediaFinish(tag)
                CoroutineScope(Dispatchers.IO).launch {
                    filePath?.let { DeleteLocalMediaUseCase(it).execute() }
                }
                // ClaudioApplication.restartApp()
            }
        })
        val mediaItem = MediaItem.fromUri(Uri.parse(filePath))
        exoPlayer.addMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    private fun onMediaFinish(tag: String) {
        val videoView = llMedias?.findViewWithTag<View>(tag)
        llMedias?.removeView(videoView)
        mediaPlayingList.remove(tag)
        if (mediaPlayingList.isEmpty()) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayerList.forEach {
            it.stop()
            it.removeMediaItems(0, it.mediaItemCount)
        }
    }

    companion object {
        const val TAG = "PlayerActivity"
        const val KEY_FILE_PATH = "key_file_path"

        fun getIntent(context: Context?, filePath: String?): Intent {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(KEY_FILE_PATH, filePath)
            return intent
        }
    }
}
