package com.example.mixmate.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixmate.R
import com.example.mixmate.api.ApiManager
import com.example.mixmate.api.ApiService
import com.example.mixmate.data.CategoryData
import com.example.mixmate.data.ExploreOutfit
import com.example.mixmate.data.Outfit
import com.example.mixmate.data.ResponseData
import com.example.mixmate.data.VideoData
import com.example.mixmate.databinding.FragmentExploreBinding
import com.example.mixmate.databinding.FragmentLoginBinding
import com.example.mixmate.ui.adapters.CategoriesAdapter
import com.example.mixmate.ui.adapters.ForYouAdapter
import com.example.mixmate.ui.adapters.ResultAdapter
import com.example.mixmate.ui.adapters.VideoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreFragment : Fragment() {

  private var _binding: FragmentExploreBinding? = null
  private val binding get() = _binding!!
  private lateinit var apiService: ApiService
  private var call: Call<List<ExploreOutfit>>? = null // Variabel untuk menyimpan objek panggilan API

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentExploreBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupStyleCategoriesRecyclerView()
    setupVideosRecyclerView()
    fetchForYou()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    // Batalkan panggilan API jika masih berjalan saat fragment dihancurkan
    call?.cancel()
    _binding = null
  }

  private fun setupVideosRecyclerView() {
    binding.videosRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    binding.videosRv.adapter = VideoAdapter(VideoData.videos)
  }

  private fun setupStyleCategoriesRecyclerView() {
    binding.categoryRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    binding.categoryRv.adapter = CategoriesAdapter(CategoryData.categoryList)
  }

  private fun setupForYouRecyclerView( outfits: List<ExploreOutfit>) {
    binding.forYouRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    binding.forYouRv.adapter = ForYouAdapter(outfits)
  }

  private fun fetchForYou() {
    apiService = ApiManager.createExplore()
    call = apiService.getOutfits()
    call?.enqueue(object : Callback<List<ExploreOutfit>> {
      override fun onResponse(
        call: Call<List<ExploreOutfit>>,
        response: Response<List<ExploreOutfit>>
      ) {
        if (response.isSuccessful) {
          val outfits = response.body()
          outfits?.let {
            setupForYouRecyclerView(outfits)
          }
        } else {
          Log.e("FetchForYou", "Response Error: ${response.code()}")
        }
      }

      override fun onFailure(call: Call<List<ExploreOutfit>>, t: Throwable) {
        // Tangani kegagalan koneksi atau permintaan
      }
    })
  }
}

