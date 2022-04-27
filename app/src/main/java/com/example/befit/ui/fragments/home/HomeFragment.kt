package com.example.befit.ui.fragments.home

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.befit.R
import com.example.befit.databinding.FragmentHomeBinding
import com.example.befit.repository.ApiRepository
import com.example.befit.ui.activity.MainActivity
import com.example.befit.viewmodel.auth.AuthViewModel
import com.example.befit.viewmodel.auth.AuthViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.Period
import kotlin.random.Random

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val repository = ApiRepository()
        val viewModelFactory = AuthViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[AuthViewModel::class.java]
        checkAuthorizationUser()

        binding.logoutBtn.setOnClickListener {
            logout()
        }

        return binding.root
    }

    private fun checkAuthorizationUser() {
        if (viewModel.getUser()) {
            viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
                if (response.isSuccessful) {
                    binding.homeTv.text = response.body()?.first_name
                } else {
                    val errorText =
                        response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                    Log.e("Error Response", errorText.toString())
                    findNavController().navigate(R.id.action_homeFragment_to_welcomeFragment)
                }
            })
        } else findNavController().navigate(R.id.action_homeFragment_to_welcomeFragment)
    }

    private fun logout() {
        viewModel.logout()
        viewModel.myResponseString.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                findNavController().navigate(R.id.action_homeFragment_to_welcomeFragment)
            } else {
                val errorText = response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}