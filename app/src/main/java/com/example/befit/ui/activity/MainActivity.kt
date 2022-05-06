package com.example.befit.ui.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.befit.R
import com.example.befit.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        installSplashScreen()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNav: BottomNavigationView = binding.btmNav
        bottomNav.itemIconTintList = null
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNav, navHostFragment.navController)

        binding.chatBtn.setOnClickListener {
            binding.btmNav.selectedItemId = R.id.chatFragment
        }

        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.welcome_video)
        binding.welcomeVideo.setOnPreparedListener { mp -> mp.isLooping = true }
        binding.welcomeVideo.setVideoURI(uri)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}