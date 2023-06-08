package com.bangkit.allergysense.ui.fragments.home

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.allergysense.databinding.FragmentHomeBinding
import com.bangkit.allergysense.ui.adapters.ListAdapter
import com.bangkit.allergysense.utils.repositories.Response
import com.bangkit.allergysense.utils.responses.DataItem
import com.bangkit.allergysense.utils.responses.Quote
import com.bangkit.allergysense.utils.viewmodels.AllergyViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.AuthViewModelFactory
import com.bangkit.allergysense.utils.viewmodels.HistoriesViewModel
import com.bangkit.allergysense.utils.viewmodels.LoginViewModel
import com.bangkit.allergysense.utils.viewmodels.QuotesViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "set")
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelUser: LoginViewModel
    private lateinit var modelQuote: QuotesViewModel
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading(false)

        modelUser = ViewModelProvider(this, AuthViewModelFactory.getInstance(dataStore))[LoginViewModel::class.java]
        modelQuote = ViewModelProvider(this, AllergyViewModelFactory.getIntance())[QuotesViewModel::class.java]
        modelHistories = ViewModelProvider(this, AllergyViewModelFactory.getIntance())[HistoriesViewModel::class.java]

        modelUser.user().observe(viewLifecycleOwner) {
            modelQuote.quotes(it.token).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Response.Loading -> loading(true)
                        is Response.Success -> {
                            loading(false)
                            val quote = result.data
                            getQuote(quote)
                        }
                        is Response.Error -> {
                            loading(false)
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        when (binding.rvHistory.adapter) {
            null -> binding.historyNote.visibility = View.VISIBLE
            else -> {
                modelUser.user().observe(viewLifecycleOwner) {
                    modelHistories.getHistories(it.token).observe(viewLifecycleOwner) { data ->
                        if (data != null) {
                            when (data) {
                                is Response.Loading -> loading(true)
                                is Response.Success -> {
                                    loading(false)
                                    binding.historyNote.visibility = View.GONE
                                    recyclerList(data.data)
                                }
                                is Response.Error -> {
                                    loading(false)
                                    Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loading(false)

        modelUser.user().observe(viewLifecycleOwner) {
            modelHistories.getHistories(it.token).observe(viewLifecycleOwner) { data ->
                if (data != null) {
                    when (data) {
                        is Response.Loading -> loading(true)
                        is Response.Success -> {
                            loading(false)
                            binding.historyNote.visibility = View.GONE
                            recyclerList(data.data)
                        }
                        is Response.Error -> {
                            loading(false)
                            Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            modelQuote.quotes(it.token).observe(viewLifecycleOwner) { result->
                if (result != null) {
                    when (result) {
                        is Response.Loading -> loading(true)
                        is Response.Success -> {
                            loading(false)
                            val quote = result.data
                            getQuote(quote)
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

    private fun getQuote(quote: Quote) {
        binding.quote.text = quote.quote
        binding.quoter.text = quote.author
    }

    private fun recyclerList(list: List<DataItem>) {
        binding.rvHistory.layoutManager = LinearLayoutManager(context)
        val listAdapter = ListAdapter(list)
        binding.rvHistory.adapter = listAdapter
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