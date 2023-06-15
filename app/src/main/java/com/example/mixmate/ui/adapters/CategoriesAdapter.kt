package com.example.mixmate.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mixmate.R
import com.example.mixmate.data.Category
import com.example.mixmate.data.ExploreOutfit
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale


class CategoriesAdapter(private val itemList: List<Category>) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.style_categories_item, parent, false)
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

    fun bind(item: Category) {
      // Bind data to views in the item layout
      val name: TextView = itemView.findViewById(R.id.tv_name)
      val itemImg: ImageView = itemView.findViewById(R.id.item_img)

      name.text = item.name
      itemImg.setImageResource(item.image)
    }



  }
}