package com.dimantik.tyteda.ui.menu.dish

import androidx.recyclerview.widget.DiffUtil
import com.dimantik.tyteda.data.model.base.Dish

class DishDiffUtilsCallback(
    private val oldList: List<Dish>,
    private val newList: List<Dish>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList[oldItemPosition].name != newList[newItemPosition].name) return false
        if (oldList[oldItemPosition].category_id != newList[newItemPosition].category_id) return false
        if (oldList[oldItemPosition].image != newList[newItemPosition].image) return false
        if (oldList[oldItemPosition].price != newList[newItemPosition].price) return false
        if (oldList[oldItemPosition].summary != newList[newItemPosition].summary) return false

        return true
    }

}