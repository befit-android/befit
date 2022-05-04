package com.example.befit.ui.fragments.registration

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
import androidx.navigation.fragment.findNavController
import com.example.befit.R
import com.example.befit.databinding.FragmentRegistrationBinding
import com.example.befit.repository.ApiRepository
import com.example.befit.util.Utility
import com.example.befit.util.Utility.hideKeyboard
import com.example.befit.viewmodel.auth.AuthViewModel
import com.example.befit.viewmodel.auth.AuthViewModelFactory
import com.example.befit.util.Utility.setDate
import kotlinx.coroutines.delay

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private var date: Array<String> = emptyArray()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val repository = ApiRepository()
        val viewModelFactory = AuthViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[AuthViewModel::class.java]

        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, gender)
        binding.genderTv.setAdapter(arrayAdapter)

        binding.dateOfBirthLayout.setEndIconOnClickListener {
            context?.setDate()!!
            Utility.date.observe(viewLifecycleOwner, Observer {
                date = it
                binding.dateOfBirthTv.setText(date[0])
            })
        }
        binding.dateOfBirthTv.setOnClickListener {
            context?.setDate()!!
            Utility.date.observe(viewLifecycleOwner, Observer {
                date = it
                binding.dateOfBirthTv.setText(date[0])
            })
        }

        binding.toLoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        binding.registrationBtn.setOnClickListener {
            register()
        }

        binding.genderTv.setOnClickListener {
            context?.hideKeyboard(binding)
        }

        return binding.root
    }

    private fun register() {
        val name = binding.nameEt.text.toString()
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()

        // male - 1, female - 2
        val gender =
            if (binding.genderTv.text.toString() == resources.getStringArray(R.array.gender)[1]) '2'
            else '1'

        val date = date[1]

        if (!(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                    || TextUtils.isEmpty(date) || binding.genderTv.text.isEmpty())
        ) {
            viewModel.register(requireContext(), email, name, gender, date, password)
            viewModel.getResponseUserInfo().observe(viewLifecycleOwner, Observer {
                lifecycleScope.launchWhenResumed {
                    if (it == "OK") {
                        delay(100)
                        findNavController().navigate(R.id.action_registrationFragment_to_diaryFragment)
                    }
                }
            })
            viewModel.getErrorUserInfo().observe(viewLifecycleOwner, Observer {
                if (it != "") Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
        } else {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}