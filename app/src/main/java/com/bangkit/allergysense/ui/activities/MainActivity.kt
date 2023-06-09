package com.bangkit.allergysense.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.allergysense.R
import com.bangkit.allergysense.databinding.ActivityMainBinding
import com.bangkit.allergysense.ui.fragments.allergy.AllergyFragment
import com.bangkit.allergysense.ui.fragments.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root as ViewGroup
        setContentView(rootView)
        view()

        if (intent.getStringExtra("check")?.equals("navAllergy") == true) {
            moveToFragment(AllergyFragment())
        } else if (intent.getStringExtra("detailChecl")?.equals("navAllergy") == true) {
            moveToFragment(AllergyFragment())
        }else if(intent.getStringExtra("checkTerms")?.equals("navAllergy") == true) {
            moveToFragment(ProfileFragment())
        }

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentActivityMain) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navHome, R.id.navAllergy, R.id.navProfile, R.id.navSetting,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun moveToFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navHostFragmentActivityMain, fragment)
        fragmentTransaction.commit()
    }

    private fun view() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) window.insetsController?.hide(
            WindowInsets.Type.statusBars()) else window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}