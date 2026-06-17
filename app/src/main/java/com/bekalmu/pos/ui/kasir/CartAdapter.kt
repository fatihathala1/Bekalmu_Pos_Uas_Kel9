package com.bekalmu.pos.ui.kasir

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bekalmu.pos.data.model.CartItem
import com.bekalmu.pos.data.model.Produk
import com.bekalmu.pos.databinding.ItemCartBinding
import com.bekalmu.pos.utils.CurrencyUtils

class CartAdapter(
    private val onAdd: (Produk) -> Unit,
    private val onRemove: (Produk) -> Unit
) : ListAdapter<CartItem, CartAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.tvNama.text = item.produk.nama
            binding.tvHarga.text = CurrencyUtils.format(item.produk.hargaJual)
            binding.tvJumlah.text = item.jumlah.toString()
            binding.tvSubtotal.text = CurrencyUtils.format(item.subtotal)

            binding.btnTambah.setOnClickListener { onAdd(item.produk) }
            binding.btnKurang.setOnClickListener { onRemove(item.produk) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem) =
            oldItem.produk.produkId == newItem.produk.produkId
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem) =
            oldItem == newItem && oldItem.jumlah == newItem.jumlah
    }
}
