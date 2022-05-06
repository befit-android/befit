package com.example.befit.ui.fragments.registration

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.befit.R
import com.example.befit.databinding.FragmentRegistrationStepThreeBinding
import com.example.befit.util.Utility
import com.example.befit.util.Utility.date
import com.example.befit.util.Utility.setDate

class RegistrationStepThreeFragment : Fragment() {

    private var _binding: FragmentRegistrationStepThreeBinding? = null
    private val binding: FragmentRegistrationStepThreeBinding get() = _binding!!
    private val args by navArgs<RegistrationStepThreeFragmentArgs>()
    private var date: Array<String> = emptyArray()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationStepThreeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        setDate()

        binding.nextBtn.setOnClickListener {
            next()
        }

        binding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    private fun setDate() {
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
    }

    private fun next() {
        if (!TextUtils.isEmpty(binding.dateOfBirthTv.text.toString())) {
            val date = date[1]
            val regInfo = args.regInfo + ", $date"
            val action =
                RegistrationStepThreeFragmentDirections.actionRegistrationStepThreeFragmentToRegistrationStepFourFragment(
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