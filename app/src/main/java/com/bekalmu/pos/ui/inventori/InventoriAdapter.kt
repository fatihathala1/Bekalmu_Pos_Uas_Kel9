package com.bekalmu.pos.ui.inventori

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bekalmu.pos.R
import com.bekalmu.pos.data.model.Produk
import com.bekalmu.pos.databinding.ItemInventoriBinding

class InventoriAdapter(
    private val isOwner: Boolean = false,
    private val onEdit: (Produk) -> Unit,
    private val onDelete: (Produk) -> Unit,
    private val onRestok: (Produk) -> Unit
) : ListAdapter<Produk, InventoriAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemInventoriBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(produk: Produk) {
            binding.tvNamaProduk.text = produk.nama
            binding.tvKategori.text = produk.kategori
            binding.tvStok.text = "Stok: ${produk.stok}"

            val stokColor = when {
                produk.stok <= 5 -> R.color.red_danger
                produk.stok <= 20 -> R.color.yellow_warning
                else -> R.color.green_primary
            }
            binding.tvStok.setTextColor(binding.root.context.getColor(stokColor))

            // Load gambar
            loadProductImage(produk.imagePath)

            if (isOwner) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnEdit.setOnClickListener { onEdit(produk) }
                binding.btnMore.setOnClickListener { view ->
                    showPopupMenuOwner(view, produk)
                }
            } else {
                binding.btnEdit.visibility = View.GONE
                binding.btnMore.setOnClickListener { view ->
                    showPopupMenuKasir(view, produk)
                }
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
                    binding.ivProductImage.setImageResource(resId)
                    binding.ivProductImage.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    binding.ivProductImage.setPadding(0, 0, 0, 0)
                    binding.ivProductImage.setBackgroundResource(0)
                    // Hapus tint hijau dari XML supaya warna foto asli tidak tertutup
                    binding.ivProductImage.imageTintList = null
                    return
                }
            }
            // Foto hasil upload dari galeri, disimpan sebagai path file lokal di internal storage
            if (!imagePath.isNullOrEmpty() && !imagePath.startsWith("drawable://")) {
                val file = java.io.File(imagePath)
                if (file.exists()) {
                    binding.ivProductImage.setImageURI(android.net.Uri.fromFile(file))
                    binding.ivProductImage.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    binding.ivProductImage.setPadding(0, 0, 0, 0)
                    binding.ivProductImage.setBackgroundResource(0)
                    binding.ivProductImage.imageTintList = null
                    return
                }
            }
            // Fallback placeholder
            binding.ivProductImage.setImageResource(R.drawable.ic_food_placeholder)
            binding.ivProductImage.scaleType = android.widget.ImageView.ScaleType.CENTER_INSIDE
            binding.ivProductImage.setPadding(10, 10, 10, 10)
            binding.ivProductImage.setBackgroundResource(R.drawable.bg_icon_green_light)
            // Pasang ulang tint hijau khusus untuk placeholder icon
            binding.ivProductImage.imageTintList =
                android.content.res.ColorStateList.valueOf(
                    binding.root.context.getColor(R.color.green_primary)
                )
        }

        private fun showPopupMenuOwner(view: View, produk: Produk) {
            val popup = android.widget.PopupMenu(view.context, view)
            popup.menu.add(0, 1, 0, "Tambah Stok")
            popup.menu.add(0, 2, 0, "Hapus Produk")
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> { onRestok(produk); true }
                    2 -> { onDelete(produk); true }
                    else -> false
                }
            }
            popup.show()
        }

        private fun showPopupMenuKasir(view: View, produk: Produk) {
            val popup = android.widget.PopupMenu(view.context, view)
            popup.menu.add(0, 1, 0, "Tambah Stok")
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> { onRestok(produk); true }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInventoriBinding.inflate(
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