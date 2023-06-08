package com.example.mixmate.data

import java.io.Serializable

data class ResponseData(
  val gender: String,
  val outfits: List<Outfit>,
  val race: String,
  val style: String
): Serializable