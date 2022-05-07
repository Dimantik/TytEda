package com.dimantik.tyteda.ui.cart.cartItem

import androidx.recyclerview.widget.DiffUtil
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.CartItem

class CartItemDiffUtilsCallback(
    private val oldCartItemList: List<CartItem>,
    private val newCartItemList: List<CartItem>,
    private val oldDishList: List<Dish>,
    private val newDishList: List<Dish>
) : DiffUtil.Callback() {

    init {
//        val builder = StringBuilder()
//
//        Log.i("TYT_EDA", "${oldCartItemList::class.java}")
//        Log.i("TYT_EDA", "${newCartItemList::class.java}")
//
//        builder.append("old = ")
//        oldCartItemList.forEach {
//            builder.append(it.amount).append(", ")
//        }
//
//        builder.append(" new = ")
//        newCartItemList.forEach {
//            builder.append(it.amount).append(", ")
//        }
//        Log.i("TYT_EDA", "$builder")
    }

    override fun getOldListSize() = oldCartItemList.size
    override fun getNewListSize() = newCartItemList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldCartItemList[oldItemPosition].dishId == newCartItemList[newItemPosition].dishId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldCartItemList[oldItemPosition].amount != newCartItemList[newItemPosition].amount) return false

        oldDishList.find { it.id == oldCartItemList[oldItemPosition].dishId }?.let { oldDish ->
            newDishList.find { it.id == newCartItemList[newItemPosition].dishId }?.let { newDish ->
                if (oldDish.image != newDish.image) return false
                if (oldDish.name != newDish.name) return false
                if (oldDish.price != newDish.price) return false
                if (oldDish.summary != newDish.summary) return false
            }
        }

        return true
    }

}