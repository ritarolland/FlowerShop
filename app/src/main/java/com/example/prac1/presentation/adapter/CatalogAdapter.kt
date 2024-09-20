package com.example.prac1.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.prac1.data.model.FlowerDataModel
import com.example.prac1.databinding.FlowerItemBinding
import com.example.prac1.domain.model.Flower

class CatalogAdapter(private val onItemClicked: (Flower) -> Unit) :
    RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {

    var flowerList: List<Flower>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    private val diffCallback = object : DiffUtil.ItemCallback<Flower>() {
        override fun areItemsTheSame(oldItem: Flower, newItem: Flower): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Flower,
            newItem: Flower
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    class CatalogViewHolder(private val binding: FlowerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(flower: Flower, onItemClicked: (Flower) -> Unit) {
            binding.flowerName.text = flower.name
            binding.root.setOnClickListener {
                onItemClicked(flower)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val binding = FlowerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return flowerList.size
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val flower = flowerList[position]
        holder.bind(flower, onItemClicked)
    }
}