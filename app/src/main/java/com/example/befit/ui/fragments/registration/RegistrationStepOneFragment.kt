package com.example.befit.ui.fragments.registration

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.befit.R
import com.example.befit.databinding.FragmentRegistrationStepOneBinding

class RegistrationStepOneFragment : Fragment() {

    private var _binding: FragmentRegistrationStepOneBinding? = null
    private val binding: FragmentRegistrationStepOneBinding get() = _binding!!
    private val args by navArgs<RegistrationStepOneFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationStepOneBinding.inflate(inflater, container, false)
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
        val name = binding.nameEt.text.toString()

        if (!TextUtils.isEmpty(name)) {
            val regInfo = args.regInfo + ", $name"
            val action =
                RegistrationStepOneFragmentDirections.actionRegistrationStepOneFragmentToRegistrationStepTwoFragment(
                    regInfo
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Заполните поле", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}