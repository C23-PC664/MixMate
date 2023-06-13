package com.example.mixmate.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mixmate.R
import com.example.mixmate.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
  private var _binding: FragmentLoginBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentLoginBinding.inflate(inflater, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnSignUp.setOnClickListener {
      startRegisterFragment()
    }

  }

  private fun startRegisterFragment() {
    val registerFragment = RegisterFragment()
    val transaction = requireActivity().supportFragmentManager.beginTransaction()
    transaction.replace(R.id.fragmentContainer, registerFragment)
    transaction.addToBackStack(null)
    transaction.commit()
  }


  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}