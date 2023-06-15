package com.example.mixmate.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mixmate.R
import com.example.mixmate.data.ExploreOutfit
import com.example.mixmate.data.Outfit
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale



class ForYouAdapter(private val itemList: List<ExploreOutfit>) : RecyclerView.Adapter<ForYouAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_for_you, parent, false)
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

    fun bind(item: ExploreOutfit) {
      // Bind data to views in the item layout
      val itemName: TextView = itemView.findViewById(R.id.tv_item_name)
      val itemPrice: TextView = itemView.findViewById(R.id.tv_item_price)
      val itemImg: ImageView = itemView.findViewById(R.id.item_img)

      itemName.text = item.rekomendasi
      itemPrice.text = formatRupiah(item.price.toInt())
      itemImg.load(item.fullImageUrl)
    }

    fun formatRupiah(amount: Int): String {
      val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
      format.maximumFractionDigits = 0
      val symbol = Currency.getInstance("IDR").symbol
      val formattedAmount = format.format(amount).substring(2)
      return "Rp $formattedAmount"
    }


  }
}