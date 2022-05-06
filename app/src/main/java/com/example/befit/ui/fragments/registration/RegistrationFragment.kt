package com.example.befit.ui.fragments.registration

import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.befit.R
import com.example.befit.databinding.FragmentRegistrationBinding
import com.example.befit.repository.ApiRepository
import com.example.befit.ui.activity.MainActivity
import com.example.befit.util.Utility
import com.example.befit.util.Utility.hideKeyboard
import com.example.befit.viewmodel.auth.AuthViewModel
import com.example.befit.viewmodel.auth.AuthViewModelFactory
import com.example.befit.util.Utility.setDate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_welcome.*
import kotlinx.coroutines.delay

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        binding.toLoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        binding.nextBtn.setOnClickListener {
            next()
        }

        return binding.root
    }

    private fun next() {
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()
        val confirmPassword = binding.confirmEt.text.toString()

        if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword))) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (password == confirmPassword) {
                    val regInfo = "$email, $password"
                    val action = RegistrationFragmentDirections.actionRegistrationFragmentToRegistrationStepOneFragment(regInfo)
                    findNavController().navigate(action)
                    (activity as MainActivity).welcomeVideo.stopPlayback()
                    (activity as MainActivity).welcomeVideo.visibility = View.GONE
                } else
                    Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(requireContext(), "Неверный email", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}