package com.example.befit.ui.fragments.diary

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.befit.R
import com.example.befit.databinding.FragmentDiaryBinding
import com.example.befit.repository.ApiRepository
import com.example.befit.viewmodel.auth.AuthViewModel
import com.example.befit.viewmodel.auth.AuthViewModelFactory

class DiaryFragment : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val binding: FragmentDiaryBinding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
//        (activity as MainActivity).btm_nav.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.home)
        setHasOptionsMenu(true)
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)

        val repository = ApiRepository()
        val viewModelFactory = AuthViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[AuthViewModel::class.java]


        binding.logoutBtn.setOnClickListener {
            logout()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        checkAuthorizationUser()
    }

    private fun checkAuthorizationUser() {
        viewModel.getUser(requireContext())
        viewModel.getResponseUser().observe(viewLifecycleOwner, Observer { response ->
            binding.homeTv.text = response.first_name
        })
        viewModel.getErrorUser().observe(viewLifecycleOwner) {
            if (it != "") {
                if (it == "Unauthorized")
                    findNavController().navigate(R.id.action_diaryFragment_to_welcomeFragment)
                else
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logout() {
        viewModel.logout(requireContext())
        viewModel.getResponseLogout().observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_diaryFragment_to_welcomeFragment)
            binding.homeTv.text = ""
        })
        viewModel.getErrorLogout().observe(viewLifecycleOwner, Observer {
            if (it != "") Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}