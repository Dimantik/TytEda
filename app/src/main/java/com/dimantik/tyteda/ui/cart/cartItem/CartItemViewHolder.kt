package com.dimantik.tyteda.ui.cart.cartItem

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.CartItem
import com.dimantik.tyteda.databinding.ViewHolderCartItemBinding
import java.lang.StringBuilder

class CartItemViewHolder(
    private val binding: ViewHolderCartItemBinding,
    private val context: Context,
    private val cartItemAmountController: CartItemAmountController
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var cartItem: CartItem
    private lateinit var dish: Dish

    init {
        binding.reduce.setOnClickListener {
            cartItemAmountController.reduceAmount(cartItem)
        }
        binding.increase.setOnClickListener {
            cartItemAmountController.increaseAmount(cartItem)
        }
    }

    fun onBind(cartItem: CartItem, dish: Dish) {
        this.cartItem = cartItem
        this.dish = dish

        binding.amount.text = cartItem.amount.toString()
        binding.name.text = dish.name
        binding.price.text = StringBuilder()
            .append(dish.price)
            .append(" Р")
            .toString()

        Glide
            .with(context)
            .load(dish.image)
            .into(binding.image)
    }

    interface CartItemAmountController {
        fun reduceAmount(cartItem: CartItem)
        fun increaseAmount(cartItem: CartItem)
    }

}