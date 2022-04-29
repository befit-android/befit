package com.example.befit.ui.fragments.registration

import android.app.DatePickerDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.befit.R
import com.example.befit.databinding.FragmentRegistrationBinding
import com.example.befit.repository.ApiRepository
import com.example.befit.viewmodel.auth.AuthViewModel
import com.example.befit.viewmodel.auth.AuthViewModelFactory
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private var dateToServer: String = ""

    private val formatDate: DateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy")
    private val formatUser: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val formatServer: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
            setDate()
        }
        binding.dateOfBirthTv.setOnClickListener {
            setDate()
        }

        binding.toLoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        binding.registrationBtn.setOnClickListener {
            register()
        }

        binding.genderTv.setOnClickListener {
            hideKeyboard()
        }

        return binding.root
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.DialogTheme,
            DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                val date = LocalDate.parse("$mDay.${mMonth + 1}.$mYear", formatDate)
                    .format(formatUser).toString()
                dateToServer = LocalDate.parse("$mDay.${mMonth + 1}.$mYear", formatDate)
                    .format(formatServer).toString()
                binding.dateOfBirthTv.setText(date)
            }, year, month, day
        )
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(R.color.purple_700))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.teal_700))
    }

    private fun register() {
        val name = binding.nameEt.text.toString()
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()
        // male - 1, female - 2
        val gender =
            if (binding.genderTv.text.toString() == resources.getStringArray(R.array.gender)[1])
                '2'
            else
                '1'

        val date = dateToServer

        if (inputCheck(name, email, password, date)) {
            viewModel.register(requireContext(), email, name, gender, date, password)
            viewModel.getResponseUserInfo().observe(viewLifecycleOwner, Observer {
                lifecycleScope.launchWhenResumed {
                    if (it == "OK") {
                        delay(100)
                        findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
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

    private fun inputCheck(
        name: String,
        email: String,
        password: String,
        date: String
    ): Boolean {
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(date) || binding.genderTv.text.isEmpty())
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        if (inputMethodManager!!.isActive)
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}