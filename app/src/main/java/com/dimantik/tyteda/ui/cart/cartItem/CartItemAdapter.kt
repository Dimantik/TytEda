package com.dimantik.tyteda.ui.cart.cartItem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.CartItem
import com.dimantik.tyteda.databinding.ViewHolderCartItemBinding

class CartItemAdapter(
    private val cartItemAmountController: CartItemViewHolder.CartItemAmountController
) : RecyclerView.Adapter<CartItemViewHolder>() {

    var dishList = listOf<Dish>()
    var cartItemList = listOf<CartItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CartItemViewHolder(
        ViewHolderCartItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        parent.context,
        cartItemAmountController
    )

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        dishList.find { it.id == cartItemList[position].dishId }?.let { noNullDish ->
            holder.onBind(cartItemList[position], noNullDish)
        }
    }

    override fun getItemCount() =
        if (dishList.isEmpty()) 0
        else cartItemList.size

}