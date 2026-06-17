package com.bekalmu.pos.ui.transaksi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bekalmu.pos.R
import com.bekalmu.pos.data.model.TransaksiWithDetails
import com.bekalmu.pos.databinding.ItemDetailTransaksiBinding
import com.bekalmu.pos.utils.CurrencyUtils
import com.bekalmu.pos.utils.DateUtils

class DetailTransaksiAdapter : ListAdapter<TransaksiWithDetails, DetailTransaksiAdapter.ViewHolder>(DiffCallback()) {

    private var fullList = listOf<TransaksiWithDetails>()

    fun submitFullList(list: List<TransaksiWithDetails>) {
        fullList = list
        submitList(list)
    }

    fun filter(query: String) {
        if (query.isEmpty()) {
            submitList(fullList)
            return
        }
        val filtered = fullList.filter { tx ->
            tx.transaksi.nomorOrder.contains(query, ignoreCase = true) ||
            tx.details.any { detail ->
                detail.namaProduk?.contains(query, ignoreCase = true) == true
            }
        }
        submitList(filtered)
    }

    inner class ViewHolder(private val binding: ItemDetailTransaksiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransaksiWithDetails) {
            val tx = item.transaksi
            binding.tvNomorOrder.text = "#${tx.nomorOrder}"
            binding.tvWaktu.text = DateUtils.formatTime(tx.tanggal)
            binding.tvTotal.text = CurrencyUtils.format(tx.totalHarga)
            binding.tvMetodeBadge.text = tx.metodePembayaran.uppercase()
            val isQris = tx.metodePembayaran.equals("qris", ignoreCase = true)
            binding.tvMetodeBadge.text = if (isQris) "QRIS" else "Tunai"
            binding.tvMetodeBadge.setBackgroundResource(
                if (isQris) R.drawable.bg_chip_outline else R.drawable.bg_chip_green
            )
            // Tampilkan nama produk pertama sebagai preview
            val produkNames = item.details.joinToString(", ") { "${it.namaProduk} · Qty: ${it.jumlah}" }
            binding.tvProdukPreview.text = produkNames
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<TransaksiWithDetails>() {
        override fun areItemsTheSame(oldItem: TransaksiWithDetails, newItem: TransaksiWithDetails) =
            oldItem.transaksi.transaksiId == newItem.transaksi.transaksiId
        override fun areContentsTheSame(oldItem: TransaksiWithDetails, newItem: TransaksiWithDetails) =
            oldItem == newItem
    }
}
