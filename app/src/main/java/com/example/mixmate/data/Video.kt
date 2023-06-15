package com.example.mixmate.data

import com.example.mixmate.R

data class Video(
  val title: String,
  val image: Int
)

object VideoData {
  val videos = listOf(
    Video("Fashionable Workwear: Stylish Office Outfit Ideas", R.drawable.video_1),
    Video("The Power of Accessories: Elevating Your Outfit Game", R.drawable.video_2),
    Video("Effortlessly Chic: Casual Outfit Ideas", R.drawable.video_3),
    Video("From Day to Night: Transforming Your Outfit with Simple Hacks", R.drawable.video_4),
  )
}

