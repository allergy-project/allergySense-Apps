package com.bangkit.allergysense.ui.fragments.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.allergysense.databinding.FragmentProfileBinding
import com.bangkit.allergysense.ui.activities.LoginActivity
import com.bangkit.allergysense.utils.repositories.Response
import com.bangkit.allergysense.utils.responses.Profile
import com.bangkit.allergysense.utils.viewmodels.AllergyViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.AuthViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.LoginViewModel
import com.bangkit.allergysense.utils.viewmodels.LogoutViewModel
import com.bangkit.allergysense.utils.viewmodels.ProfileViewModel
import com.bumptech.glide.Glide

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "set")
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelProfile: ProfileViewModel
    private lateinit var modelUser: LoginViewModel
    private lateinit var modelLogout: LogoutViewModel
    private lateinit var dataStore: DataStore<Preferences>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataStore = requireContext().dataStore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading(false)

        modelProfile = ViewModelProvider(this, AllergyViewModelFactory.getIntance())[ProfileViewModel::class.java]
        modelUser = ViewModelProvider(this, AuthViewModelFactory.getInstance(dataStore))[LoginViewModel::class.java]
        modelLogout = ViewModelProvider(this, AuthViewModelFactory.getInstance(dataStore))[LogoutViewModel::class.java]

        binding.tvLogout.setOnClickListener {
            modelLogout.logout()
            Toast.makeText(context, "Goodbye, I will miss you", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        modelUser.user().observe(viewLifecycleOwner) {
            modelProfile.getProfile(it.token).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Response.Loading -> loading(true)
                        is Response.Success -> {
                            loading(false)
                            val profile = result.data
                            getProfile(profile)
                            Toast.makeText(context, "Successfully loaded profile", Toast.LENGTH_SHORT).show()
                        }
                        is Response.Error -> {
                            loading(false)
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun getProfile(profile: Profile) {
        Glide.with(this)
            .load(profile.imageUrl).circleCrop()
            .into(binding.ivProfile)
        binding.name.text = profile.username
        binding.email.text = profile.email
    }

    private fun loading(isLoad: Boolean) {
        if (isLoad) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}