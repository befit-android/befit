package com.example.befit.ui.fragments.registration

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.befit.R
import com.example.befit.databinding.FragmentRegistrationStepFourBinding
import com.example.befit.databinding.FragmentRegistrationStepOneBinding

class RegistrationStepFourFragment : Fragment() {

    private var _binding: FragmentRegistrationStepFourBinding? = null
    private val binding: FragmentRegistrationStepFourBinding get() = _binding!!
    private val args by navArgs<RegistrationStepFourFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationStepFourBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        binding.nextBtn.setOnClickListener {
            next()
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    private fun next() {
        val height = binding.heightEt.text.toString()
        val weight = binding.weightEt.text.toString()

        if (!(TextUtils.isEmpty(height) || TextUtils.isEmpty(weight))) {
            val regInfo = args.regInfo + ", $height, $weight"
            val action =
                RegistrationStepFourFragmentDirections.actionRegistrationStepFourFragmentToRegistrationStepFiveFragment(
                    regInfo
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Заполните поля", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}