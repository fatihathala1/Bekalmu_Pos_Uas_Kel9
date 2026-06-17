package com.bekalmu.pos.ui.pengaturan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bekalmu.pos.data.model.User
import com.bekalmu.pos.databinding.ItemStafBinding

class StafAdapter(
    private val currentUserId: Int,
    private val onDelete: (User) -> Unit
) : ListAdapter<User, StafAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemStafBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvNama.text = user.nama
            binding.tvUsername.text = "@${user.username}"
            binding.tvRole.text = user.role.replaceFirstChar { it.uppercase() }
            binding.tvStatus.text = if (user.isActive) "Aktif" else "Nonaktif"
            binding.btnDelete.isEnabled = user.userId != currentUserId
            binding.btnDelete.setOnClickListener { onDelete(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStafBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.userId == newItem.userId
        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }
}
