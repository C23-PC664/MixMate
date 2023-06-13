package com.example.mixmate.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mixmate.R
import com.example.mixmate.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
  private var _binding: FragmentRegisterBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentRegisterBinding.inflate(inflater, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.btnSignIn.setOnClickListener {
      startLoginFragment()
    }

  }

  private fun startLoginFragment() {
    val loginFragment = LoginFragment()
    val transaction = requireActivity().supportFragmentManager.beginTransaction()
    transaction.replace(R.id.fragmentContainer, loginFragment)
    transaction.addToBackStack(null)
    transaction.commit()
  }
}