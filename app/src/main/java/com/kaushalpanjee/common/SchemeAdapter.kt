package com.kaushalpanjee.common

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.LayoutInflater.from
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.kaushalpanjee.R
import com.kaushalpanjee.databinding.ItemSchemeBinding
import com.kaushalpanjee.model.Scheme

class SchemeAdapter(private val schemes: List<Scheme>) :
    RecyclerView.Adapter<SchemeAdapter.SchemeViewHolder>() {

    // Keep track of active players to release them later
    private val activePlayers = mutableListOf<ExoPlayer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchemeViewHolder {
        return SchemeViewHolder(ItemSchemeBinding.inflate(from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SchemeViewHolder, position: Int) {
        holder.bind(schemes[position])
    }

    override fun getItemCount(): Int = schemes.size

    override fun onViewRecycled(holder: SchemeViewHolder) {
        super.onViewRecycled(holder)
        holder.releasePlayer()
    }

    fun releaseAllPlayers() {
        activePlayers.forEach { it.release() }
        activePlayers.clear()
    }

    inner class SchemeViewHolder(private val binding: ItemSchemeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var player: ExoPlayer? = null

        fun bind(scheme: Scheme) {
            binding.data = scheme
            binding.rvSchemePoint.adapter = SchemeDetailAdapter(scheme.details)

            binding.tvSchemeName.setOnClickListener {
                if (binding.layoutSchemeContent.isVisible) {
                    // Collapse section
                    binding.layoutSchemeContent.visibility = GONE
                    binding.imgArrow.rotation = 0f
                    player?.pause()
                } else {
                    // Expand section
                    binding.layoutSchemeContent.visibility = VISIBLE
                    binding.imgArrow.rotation = 90f

                    if (player == null) {
                        player = initializePlayer(binding.playerView, scheme.url)
                        activePlayers.add(player!!)
                    }
                    player?.play()
                }
            }
        }

        private fun initializePlayer(playerView: PlayerView, url: String): ExoPlayer {
            val exoPlayer = ExoPlayer.Builder(binding.root.context).build()
            playerView.player = exoPlayer

            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = false

            return exoPlayer
        }

        fun releasePlayer() {
            player?.release()
            player = null
        }

        private fun enterFullScreen(activity: Activity) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            val params = binding.playerView.layoutParams
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.playerView.layoutParams = params
        }

        private fun exitFullScreen(activity: Activity) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

            val params = binding.playerView.layoutParams
            params.height = binding.root.context.resources.getDimensionPixelSize(R.dimen.dp_200)
            binding.playerView.layoutParams = params
        }
    }
}