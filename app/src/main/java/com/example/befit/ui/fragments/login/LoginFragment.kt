package com.example.befit.ui.fragments.login

import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.befit.R
import com.example.befit.databinding.FragmentLoginBinding
import com.example.befit.repository.ApiRepository
import com.example.befit.viewmodel.auth.AuthViewModel
import com.example.befit.viewmodel.auth.AuthViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val repository = ApiRepository()
        val viewModelFactory = AuthViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[AuthViewModel::class.java]

        binding.toRegistrationBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.loginBtn.setOnClickListener {
            login()
        }

        return binding.root
    }

    private fun login() {
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()

        if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {
            viewModel.login(email, password)
            viewModel.myResponseUserInfo.observe(requireActivity()) { response ->
                if (response.isSuccessful) {
                    lifecycleScope.launchWhenResumed {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                } else {
                    val errorText =
                        response.errorBody()?.string()?.substringAfter("[\"")?.dropLast(3)
                    if (errorText != "")
                        Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}