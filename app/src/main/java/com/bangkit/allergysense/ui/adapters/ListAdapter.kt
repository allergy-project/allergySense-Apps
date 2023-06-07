package com.bangkit.allergysense.ui.adapters

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.allergysense.databinding.ListHistoryBinding
import com.bangkit.allergysense.ui.activities.DetailAllergyActivity
import com.bangkit.allergysense.utils.responses.DataItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListAdapter: PagingDataAdapter<DataItem, ListAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(private val binding: ListHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: DataItem) {
            val created = item.createdAt ?: 0
            val date = Date(created)
            val format = SimpleDateFormat("dd MMMM yyyy - HH:mm", Locale.getDefault())
            val formatted = format.format(date)
            binding.allergy.text = item.allergy
            binding.date.text = formatted

            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailAllergyActivity::class.java)
                intent.putExtra(DetailAllergyActivity.EXTRA_ID, item.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListAdapter.ListViewHolder, position: Int) {
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