package com.example.befit.ui.fragments.registration

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.befit.R
import com.example.befit.databinding.FragmentRegistrationStepTwoBinding

class RegistrationStepTwoFragment : Fragment() {

    private var _binding: FragmentRegistrationStepTwoBinding? = null
    private val binding: FragmentRegistrationStepTwoBinding get() = _binding!!
    private val args by navArgs<RegistrationStepTwoFragmentArgs>()
    private var gender: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationStepTwoBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        setGender()

        binding.nextBtn.setOnClickListener {
            next()
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    private fun setGender() {
        // male - 1, female - 2

        binding.woman.setOnClickListener {
            binding.woman.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_orange_25)
            binding.man.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_25)
            gender = 2
        }

        binding.man.setOnClickListener {
            binding.man.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_orange_25)
            binding.woman.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_25)
            gender = 1
        }
    }

    private fun next() {
        if (gender != 0) {
            val regInfo = args.regInfo + ", $gender"
            val action =
                RegistrationStepTwoFragmentDirections.actionRegistrationStepTwoFragmentToRegistrationStepThreeFragment(
                    regInfo
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Выберите пол", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}