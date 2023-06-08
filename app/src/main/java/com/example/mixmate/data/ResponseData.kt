package com.example.mixmate.data

data class ResponseData(
  val gender: String,
  val outfits: List<Outfit>,
  val race: String,
  val style: String
)