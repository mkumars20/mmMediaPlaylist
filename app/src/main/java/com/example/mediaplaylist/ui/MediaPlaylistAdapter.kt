package com.example.mediaplaylist.playlistdetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import  com.example.mediaplaylist.R
import  com.example.mediaplaylist.databinding.ListItemMediaBinding
import com.example.mediaplaylist.database.MediaPlaylistItem
import com.example.mediaplaylist.database.mediaType

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class MediaPlaylistAdapter(val clickListener: MediaPlaylistListener):
    ListAdapter<DataItem, MediaPlaylistAdapter.ViewHolder>(MediaPlaylistDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is RecyclerView.ViewHolder -> {
                val m_mediaPlaylistItem = getItem(position) as DataItem.mMediaPlaylisttItem
                holder.bind(m_mediaPlaylistItem.mediaplaylistitem, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> ViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    fun addHeaderAndSubmitList(list: List<MediaPlaylistItem>?) {
        adapterScope.launch {
            val mPlstItem = MediaPlaylistItem(0, "www.hello.com",
                "VIDEO", "Video1","Unkonwn","Unkonwn")
            val items = when (list) {
                null -> listOf(DataItem.mMediaPlaylisttItem(mPlstItem))
                else -> list.map { DataItem.mMediaPlaylisttItem(it) }
            }

            //val items = DataItem.mMediaPlaylisttItem(it)
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_ITEM
            is DataItem.mMediaPlaylisttItem -> ITEM_VIEW_TYPE_ITEM
        }
    }



    class ViewHolder private constructor(val binding: ListItemMediaBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: MediaPlaylistItem, clickListener: MediaPlaylistListener) {
            binding.mediaplaylistitem  = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMediaBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
} // End of Adapter




class MediaPlaylistDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class MediaPlaylistListener(val clickListener: (mediaIndex: Long) -> Unit) {
    fun onClick(mItem: MediaPlaylistItem) = clickListener(mItem.mediaIndex)
}

sealed class DataItem {
    abstract val id: Long
    data class mMediaPlaylisttItem(val mediaplaylistitem: MediaPlaylistItem): DataItem()  {
        override val id = mediaplaylistitem.mediaIndex
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }
}