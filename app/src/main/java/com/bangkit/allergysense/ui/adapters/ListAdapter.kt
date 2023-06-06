package com.bangkit.allergysense.ui.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.allergysense.databinding.ListHistoryBinding
import com.bangkit.allergysense.utils.responses.DataItem
import java.time.Instant
import java.util.Date

class ListAdapter: PagingDataAdapter<DataItem, ListAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(private val binding: ListHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: DataItem) {
            val formated = Instant.parse(item.createdAt.toString())
            val date = Date.from(formated)
            binding.allergy.text = item.allergy
            binding.date.text = date.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val histories = getItem(position)
        if (histories != null) {
            holder.bind(histories)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}