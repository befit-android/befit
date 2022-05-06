package com.example.befit.ui.fragments.registration

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.befit.R
import com.example.befit.databinding.FragmentRegistrationStepSixBinding
import com.example.befit.repository.ApiRepository
import com.example.befit.viewmodel.auth.AuthViewModel
import com.example.befit.viewmodel.auth.AuthViewModelFactory
import kotlinx.coroutines.delay

class RegistrationStepSixFragment : Fragment() {

    private var _binding: FragmentRegistrationStepSixBinding? = null
    private val binding: FragmentRegistrationStepSixBinding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
    private val args by navArgs<RegistrationStepSixFragmentArgs>()
    private var physicalActivity: Char = '4'

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationStepSixBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val repository = ApiRepository()
        val viewModelFactory = AuthViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[AuthViewModel::class.java]

        setPhysicalActivity()

        binding.registrationBtn.setOnClickListener {
            register()
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    private fun setPhysicalActivity() {
        binding.lowBtn.setOnClickListener {
            physicalActivity = '1'
            binding.lowBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_medium_green_15)
            binding.lowBtn.setTextColor(resources.getColor(R.color.white))

            binding.moderateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.moderateBtn.setTextColor(resources.getColor(R.color.light_gray))

            binding.highBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.highBtn.setTextColor(resources.getColor(R.color.light_gray))
        }

        binding.moderateBtn.setOnClickListener {
            physicalActivity = '2'
            binding.moderateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_medium_green_15)
            binding.moderateBtn.setTextColor(resources.getColor(R.color.white))

            binding.lowBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.lowBtn.setTextColor(resources.getColor(R.color.light_gray))

            binding.highBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.highBtn.setTextColor(resources.getColor(R.color.light_gray))
        }

        binding.highBtn.setOnClickListener {
            physicalActivity = '3'
            binding.highBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_medium_green_15)
            binding.highBtn.setTextColor(resources.getColor(R.color.white))

            binding.moderateBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.moderateBtn.setTextColor(resources.getColor(R.color.light_gray))

            binding.lowBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.lowBtn.setTextColor(resources.getColor(R.color.light_gray))
        }
    }

    private fun register() {
        if (physicalActivity != '4') {
            val regInfo = args.regInfo.split(", ")
            // 0 - email, 1 - password, 2 - name, 3 - sex, 4 - birth date, 5 - height, 6 - weight,
            // 7 - goal
            viewModel.register(requireContext(), regInfo[0], regInfo[2], regInfo[3][0], regInfo[4],
                regInfo[5].toInt(), regInfo[6].toInt(), regInfo[7][0], physicalActivity, regInfo[1])
            viewModel.getResponseUserInfo().observe(viewLifecycleOwner, Observer {
                lifecycleScope.launchWhenResumed {
                    if (it == "OK") {
                        delay(100)
                        findNavController().navigate(R.id.action_registrationStepSixFragment_to_diaryFragment)
                    }
                }
            })
            viewModel.getErrorUserInfo().observe(viewLifecycleOwner, Observer {
                if (it != "") Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
        } else {
            Toast.makeText(requireContext(), "Выберите уровень", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}