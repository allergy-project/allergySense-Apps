package com.bangkit.allergysense.ui.adapters

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.allergysense.databinding.ListHistoryBinding
import com.bangkit.allergysense.ui.activities.DetailAllergyActivity
import com.bangkit.allergysense.utils.responses.DataItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListAdapter(private val listHistories: List<DataItem>): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
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
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listHistories[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listHistories.size
}