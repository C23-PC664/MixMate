package com.example.mixmate.data

import com.example.mixmate.R

data class Category(
  val name: String,
  val image: Int
)


object CategoryData {
  val categoryList = listOf(
    Category("Casual", R.drawable.casual),
    Category("Streetwear", R.drawable.streewear),
    Category("Formal", R.drawable.formal),
    Category("Vintage", R.drawable.vintage),
    Category("Bohemian", R.drawable.bohemian),
    Category("Punk", R.drawable.punk),
  )
}