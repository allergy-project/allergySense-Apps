package com.bangkit.allergysense.ui.fragments.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.allergysense.databinding.FragmentProfileBinding
import com.bangkit.allergysense.utils.viewmodels.LoginViewModel
import com.bangkit.allergysense.utils.viewmodels.ProfileViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "set")
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelProfile: ProfileViewModel
    private lateinit var modelUser: LoginViewModel
    private lateinit var dataStore: DataStore<Preferences>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataStore = requireContext().dataStore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modelProfile = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[ProfileViewModel::class.java]
        modelUser = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[LoginViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}