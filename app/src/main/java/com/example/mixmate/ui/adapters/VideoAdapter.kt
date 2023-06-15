package com.example.mixmate.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mixmate.R
import com.example.mixmate.data.Category
import com.example.mixmate.data.Video


class VideoAdapter(private val itemList: List<Video>) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = itemList[position]
    holder.bind(item)
  }

  override fun getItemCount(): Int {
    return itemList.size
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: Video) {
      // Bind data to views in the item layout
      val title: TextView = itemView.findViewById(R.id.tv_title)
      val itemImg: ImageView = itemView.findViewById(R.id.item_img)

      title.text = item.title
      itemImg.setImageResource(item.image)
    }



  }
}