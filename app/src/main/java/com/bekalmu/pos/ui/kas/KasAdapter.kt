package com.bekalmu.pos.ui.kas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bekalmu.pos.R
import com.bekalmu.pos.data.model.Kas
import com.bekalmu.pos.databinding.ItemKasBinding
import com.bekalmu.pos.utils.CurrencyUtils
import com.bekalmu.pos.utils.DateUtils

class KasAdapter(
    private val onDelete: (Kas) -> Unit
) : ListAdapter<Kas, KasAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemKasBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(kas: Kas) {
            binding.tvKategori.text = kas.kategori
            binding.tvCatatan.text = kas.catatan.ifEmpty { "-" }
            binding.tvTanggal.text = DateUtils.formatDateTime(kas.tanggal)
            binding.tvJumlah.text = CurrencyUtils.format(kas.jumlah)

            val isMasuk = kas.jenis == "masuk"
            val color = if (isMasuk) R.color.green_primary else R.color.red_danger
            binding.tvJumlah.setTextColor(binding.root.context.getColor(color))
            binding.tvJenis.text = if (isMasuk) "Masuk" else "Keluar"
            binding.tvJenis.setTextColor(binding.root.context.getColor(color))

            val icon = if (isMasuk) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
            binding.ivIcon.setImageResource(icon)
            binding.ivIcon.setColorFilter(binding.root.context.getColor(color))

            binding.root.setOnLongClickListener {
                onDelete(kas)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<Kas>() {
        override fun areItemsTheSame(oldItem: Kas, newItem: Kas) = oldItem.kasId == newItem.kasId
        override fun areContentsTheSame(oldItem: Kas, newItem: Kas) = oldItem == newItem
    }
}
