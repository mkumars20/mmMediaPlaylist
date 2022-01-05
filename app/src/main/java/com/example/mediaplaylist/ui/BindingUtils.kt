package com.example.mediaplaylist.playlistdetail

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mediaplaylist.R
import com.example.mediaplaylist.database.MediaPlaylistItem


@BindingAdapter("mediaUrlString")
fun TextView.setMediaUrlString(item: MediaPlaylistItem?) {
    item?.let {
//      text = item.mediaUrl
        text = item.m_mediaName
    }
}

@BindingAdapter("image","placeholder")
fun setImage(image: ImageView, url: String?, placeHolder: Drawable) {

    if (!url.isNullOrEmpty()){

        Glide.with(image.context).load(url).centerCrop()
            .placeholder(R.drawable.exo_notification_pause)
            .into(image)
    }
    else{
        image.setImageDrawable(placeHolder)
    }


}
