package com.example.befit.ui.fragments.registration

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.befit.R
import com.example.befit.databinding.FragmentRegistrationStepFiveBinding
import com.example.befit.databinding.FragmentRegistrationStepOneBinding

class RegistrationStepFiveFragment : Fragment() {

    private var _binding: FragmentRegistrationStepFiveBinding? = null
    private val binding: FragmentRegistrationStepFiveBinding get() = _binding!!
    private val args by navArgs<RegistrationStepFiveFragmentArgs>()
    private var goal: Char = '4'

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationStepFiveBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        setGoal()

        binding.nextBtn.setOnClickListener {
            next()
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    private fun setGoal() {
        binding.loseBtn.setOnClickListener {
            goal = '1'
            binding.loseBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_medium_green_15)
            binding.loseBtn.setTextColor(resources.getColor(R.color.white))

            binding.maintainBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.maintainBtn.setTextColor(resources.getColor(R.color.light_gray))

            binding.gainBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.gainBtn.setTextColor(resources.getColor(R.color.light_gray))
        }

        binding.maintainBtn.setOnClickListener {
            goal = '2'
            binding.maintainBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_medium_green_15)
            binding.maintainBtn.setTextColor(resources.getColor(R.color.white))

            binding.loseBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.loseBtn.setTextColor(resources.getColor(R.color.light_gray))

            binding.gainBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.gainBtn.setTextColor(resources.getColor(R.color.light_gray))
        }

        binding.gainBtn.setOnClickListener {
            goal = '3'
            binding.gainBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_medium_green_15)
            binding.gainBtn.setTextColor(resources.getColor(R.color.white))

            binding.maintainBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.maintainBtn.setTextColor(resources.getColor(R.color.light_gray))

            binding.loseBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_white_stroke_15)
            binding.loseBtn.setTextColor(resources.getColor(R.color.light_gray))
        }
    }

    private fun next() {
        if (goal != '4') {
            val regInfo = args.regInfo + ", $goal"
            val action =
                RegistrationStepFiveFragmentDirections.actionRegistrationStepFiveFragmentToRegistrationStepSixFragment(
                    regInfo
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Выберите цель", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}