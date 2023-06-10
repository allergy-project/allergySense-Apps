package com.bangkit.allergysense.ui.fragments.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.allergysense.databinding.FragmentSettingBinding
import com.bangkit.allergysense.ui.activities.AboutActivity

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading(false)

        binding.tvAbout.setOnClickListener {
             val intent = Intent(context, AboutActivity::class.java)
             startActivity(intent)
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