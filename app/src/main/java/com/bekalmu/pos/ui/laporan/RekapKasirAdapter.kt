package com.bekalmu.pos.ui.laporan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bekalmu.pos.databinding.ItemRekapKasirBinding
import com.bekalmu.pos.utils.CurrencyUtils

data class RekapKasir(
    val namaKasir: String,
    val jumlahTransaksi: Int,
    val totalPendapatan: Long
)

class RekapKasirAdapter : ListAdapter<RekapKasir, RekapKasirAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemRekapKasirBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RekapKasir) {
            binding.tvNamaKasir.text = item.namaKasir
            binding.tvJumlahTransaksi.text = "${item.jumlahTransaksi} Transaksi"
            binding.tvTotalPendapatan.text = CurrencyUtils.format(item.totalPendapatan)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRekapKasirBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<RekapKasir>() {
        override fun areItemsTheSame(oldItem: RekapKasir, newItem: RekapKasir) = oldItem.namaKasir == newItem.namaKasir
        override fun areContentsTheSame(oldItem: RekapKasir, newItem: RekapKasir) = oldItem == newItem
    }
}
