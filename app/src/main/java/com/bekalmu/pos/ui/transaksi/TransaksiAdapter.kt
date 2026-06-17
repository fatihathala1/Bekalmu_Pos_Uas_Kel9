package com.bekalmu.pos.ui.transaksi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bekalmu.pos.data.model.TransaksiWithDetails
import com.bekalmu.pos.databinding.ItemTransaksiHeaderBinding
import com.bekalmu.pos.utils.CurrencyUtils
import com.bekalmu.pos.utils.DateUtils

class TransaksiAdapter(
    private val onHeaderClick: (dateMs: Long) -> Unit
) : ListAdapter<TransaksiAdapter.ListItem, RecyclerView.ViewHolder>(DiffCallback()) {

    sealed class ListItem {
        data class Header(val dateMs: Long, val count: Int, val totalPendapatan: Long) : ListItem()
        data class TransaksiItem(val transaksiWithDetails: TransaksiWithDetails) : ListItem()
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is ListItem.Header -> TYPE_HEADER
        is ListItem.TransaksiItem -> TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(
                ItemTransaksiHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> HeaderViewHolder(
                ItemTransaksiHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ListItem.TransaksiItem -> {} // tidak dipakai lagi
        }
    }

    inner class HeaderViewHolder(private val binding: ItemTransaksiHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: ListItem.Header) {
            binding.tvTanggal.text = DateUtils.formatDate(header.dateMs)
            binding.tvJumlahTransaksi.text = "${header.count} Transaksi"
            binding.tvTotalPendapatan.text = CurrencyUtils.format(header.totalPendapatan)
            binding.root.setOnClickListener { onHeaderClick(header.dateMs) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return when {
                oldItem is ListItem.Header && newItem is ListItem.Header -> oldItem.dateMs == newItem.dateMs
                oldItem is ListItem.TransaksiItem && newItem is ListItem.TransaksiItem ->
                    oldItem.transaksiWithDetails.transaksi.transaksiId == newItem.transaksiWithDetails.transaksi.transaksiId
                else -> false
            }
        }
        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem) = oldItem == newItem
    }
}
