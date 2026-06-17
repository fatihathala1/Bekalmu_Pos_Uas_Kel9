package com.bekalmu.pos.ui.kasir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bekalmu.pos.data.model.Produk
import com.bekalmu.pos.databinding.ItemProdukKasirBinding

class ProdukKasirAdapter(
    private val onAdd: (Produk) -> Unit,
    private val onRemove: (Produk) -> Unit
) : ListAdapter<Produk, ProdukKasirAdapter.ViewHolder>(DiffCallback()) {

    // Map produkId -> jumlah di cart, dipakai untuk menampilkan tombol minus + jumlah
    private var cartQuantities: Map<Int, Int> = emptyMap()

    /**
     * Dipanggil dari Fragment setiap kali cart berubah, supaya tampilan
     * minus/jumlah/plus di setiap card produk selalu sinkron dengan cart.
     */
    fun updateCartQuantities(quantities: Map<Int, Int>) {
        cartQuantities = quantities
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProdukKasirBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(produk: Produk) {
            binding.tvNamaProduk.text = produk.nama
            binding.tvStok.text = "Stok: ${produk.stok}"

            loadProductImage(produk.imagePath)

            val isAvailable = produk.stok > 0
            binding.root.alpha = if (isAvailable) 1f else 0.5f
            binding.overlay.visibility = if (isAvailable) View.GONE else View.VISIBLE
            binding.btnTambah.alpha = if (isAvailable) 1f else 0.4f

            val jumlahDiCart = cartQuantities[produk.produkId] ?: 0
            val adaDiCart = jumlahDiCart > 0

            binding.tvStok.visibility = if (adaDiCart) View.GONE else View.VISIBLE

            // Tombol minus & jumlah hanya muncul jika produk sudah ada di cart
            binding.btnKurang.visibility = if (adaDiCart) View.VISIBLE else View.GONE
            binding.tvJumlahDiCart.visibility = if (adaDiCart) View.VISIBLE else View.GONE
            binding.tvJumlahDiCart.text = jumlahDiCart.toString()

            binding.btnTambah.setOnClickListener {
                if (isAvailable) onAdd(produk)
            }
            binding.btnKurang.setOnClickListener {
                onRemove(produk)
            }
            binding.root.setOnClickListener {
                if (isAvailable) onAdd(produk)
            }
        }

        private fun loadProductImage(imagePath: String?) {
            if (!imagePath.isNullOrEmpty() && imagePath.startsWith("drawable://")) {
                val drawableName = imagePath.removePrefix("drawable://")
                val context = binding.root.context
                val resId = context.resources.getIdentifier(
                    drawableName, "drawable", context.packageName
                )
                if (resId != 0) {
                    binding.ivProductIcon.setImageResource(resId)
                    binding.ivProductIcon.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    binding.ivProductIcon.setPadding(0, 0, 0, 0)
                    binding.ivProductIcon.setBackgroundResource(0)
                    return
                }
            }
            // Foto hasil upload dari galeri, disimpan sebagai path file lokal di internal storage
            if (!imagePath.isNullOrEmpty() && !imagePath.startsWith("drawable://")) {
                val file = java.io.File(imagePath)
                if (file.exists()) {
                    binding.ivProductIcon.setImageURI(android.net.Uri.fromFile(file))
                    binding.ivProductIcon.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    binding.ivProductIcon.setPadding(0, 0, 0, 0)
                    binding.ivProductIcon.setBackgroundResource(0)
                    return
                }
            }
            binding.ivProductIcon.setImageResource(com.bekalmu.pos.R.drawable.ic_food_placeholder)
            binding.ivProductIcon.scaleType = android.widget.ImageView.ScaleType.CENTER_INSIDE
            binding.ivProductIcon.setPadding(16, 16, 16, 16)
            binding.ivProductIcon.setBackgroundResource(com.bekalmu.pos.R.drawable.bg_icon_green_light)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProdukKasirBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Produk>() {
        override fun areItemsTheSame(oldItem: Produk, newItem: Produk) =
            oldItem.produkId == newItem.produkId
        override fun areContentsTheSame(oldItem: Produk, newItem: Produk) = oldItem == newItem
    }
}