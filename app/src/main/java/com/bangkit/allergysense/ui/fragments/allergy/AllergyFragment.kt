package com.bangkit.allergysense.ui.fragments.allergy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.allergysense.databinding.FragmentAllergyBinding
import com.bangkit.allergysense.ui.activities.UploadAllergyActivity
import com.bangkit.allergysense.ui.adapters.ListAdapter
import com.bangkit.allergysense.utils.viewmodels.AllergyViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.AuthViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.HistoriesViewModel
import com.bangkit.allergysense.utils.viewmodels.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "set")
class AllergyFragment : Fragment() {
    private var _binding: FragmentAllergyBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelUser: LoginViewModel
    private lateinit var modelHistories: HistoriesViewModel
    private lateinit var dataStore: DataStore<Preferences>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataStore = requireContext().dataStore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllergyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading(false)

        binding.fieldAdd.setOnClickListener {
            val intent = Intent(context, UploadAllergyActivity::class.java)
            startActivity(intent)
        }

        modelUser = ViewModelProvider(this, AuthViewModelFactory.getInstance(dataStore))[LoginViewModel::class.java]
        modelHistories = ViewModelProvider(this, AllergyViewModelFactory.getIntance())[HistoriesViewModel::class.java]

        val adapter = ListAdapter()
        binding.rvHistory.adapter = adapter
        binding.rvHistory.layoutManager = LinearLayoutManager(context)

        when (binding.rvHistory.adapter) {
            null -> binding.historyNote.visibility = View.VISIBLE
            else -> {
                binding.historyNote.visibility = View.GONE
                modelUser.user().observe(viewLifecycleOwner) {
                    loading(true)
                    modelHistories.getHistories(it.token).observe(viewLifecycleOwner) { data ->
                        loading(false)
                        adapter.submitData(lifecycle, data)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loading(false)
        val adapter = ListAdapter()
        binding.rvHistory.adapter = adapter

        modelUser.user().observe(viewLifecycleOwner) {
            loading(true)
            modelHistories.getHistories(it.token).observe(viewLifecycleOwner) { data ->
                loading(false)
                adapter.submitData(lifecycle, data)
            }
        }
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