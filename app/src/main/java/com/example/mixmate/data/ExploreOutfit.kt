package com.example.mixmate.data

data class ExploreOutfit(
  val race: String,
  val kategori: String,
  val rekomendasi: String,
  val gambar: String,
  val style: String,
  val price: String,
  val gender: String
) {
  val fullImageUrl: String
    get() = "https://storage.googleapis.com/mixmate/$gambar"
}
