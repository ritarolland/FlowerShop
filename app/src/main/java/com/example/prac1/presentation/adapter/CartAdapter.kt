package com.example.prac1.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.prac1.databinding.CartItemBinding
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower

class CartAdapter(private val getFlowerById: (String) -> Flower?) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var flowerList: List<CartItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    private val diffCallback = object : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CartItem,
            newItem: CartItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    class CartViewHolder(private val binding: CartItemBinding, private val getFlowerById: (String) -> Flower?) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            val flower = getFlowerById(item.flowerId)
            if (flower != null) binding.flowerName.text = flower.name
            else binding.flowerName.text = "Not found"
            binding.quantity.text = item.quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding, getFlowerById)
    }

    override fun getItemCount(): Int {
        return flowerList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val flower = flowerList[position]
        holder.bind(flower)
    }
}