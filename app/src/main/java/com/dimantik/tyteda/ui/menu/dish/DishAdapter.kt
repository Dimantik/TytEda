package com.dimantik.tyteda.ui.menu.dish

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dimantik.tyteda.data.model.base.Category
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.CartItem
import com.dimantik.tyteda.databinding.ViewHolderCategoryLargeBinding
import com.dimantik.tyteda.databinding.ViewHolderDishBinding
import com.dimantik.tyteda.databinding.ViewHolderPlugBinding
import java.lang.ClassCastException

@SuppressLint("NotifyDataSetChanged")
class DishAdapter(
    private val onDishClickListener: DishViewHolder.OnDishClickListener,
    private val onFavoriteClickListener: DishViewHolder.OnDishFavoriteClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val CATEGORY_VIEW_TYPE = 0
        private const val DISH_VIEW_TYPE = 1
        private const val PLUG_VIEW_TYPE = 2
    }

    private var menuList = mutableListOf<Any>()

    var categoryList = listOf<Category>()
        set(value) {
            field = value
            updateMenu()
        }
    var dishMap = mapOf<Int, List<Dish>>()
        set(value) {
            field = value
            updateMenu()
        }
    var favoriteList = listOf<Int>()
    var cartItemList = listOf<CartItem>()

    fun updateMenu() {
        menuList = mutableListOf()

        categoryList.forEach { category ->
            menuList.add(category)
            dishMap[category.id]?.let { noNullDishList ->
                noNullDishList.forEach { dish ->
                    menuList.add(dish)
                }
            }
        }

        menuList.add(Any())

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        DISH_VIEW_TYPE -> DishViewHolder(
            ViewHolderDishBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.context,
            onDishClickListener,
            onFavoriteClickListener
        )
        CATEGORY_VIEW_TYPE -> CategoryLargeViewHolder(
            ViewHolderCategoryLargeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        PLUG_VIEW_TYPE -> PlugViewHolder(
            ViewHolderPlugBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        else -> throw ClassCastException()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is CategoryLargeViewHolder -> holder.onBind(menuList[position] as Category)
            is DishViewHolder -> {
                val hideDivider =
                    if (position == menuList.size - 1) true
                    else (getItemViewType(position + 1) == CATEGORY_VIEW_TYPE)
                            || (getItemViewType(position + 1) == PLUG_VIEW_TYPE)

                val dish = menuList[position] as Dish
                val isFavorite = favoriteList.contains(dish.id)

                holder.onBind(
                    dish = dish,
                    isFavorite = isFavorite,
                    hideDivider = hideDivider,
                    cartItem = cartItemList.find { it.dishId == dish.id }
                )
            }
        }
    }

    override fun getItemViewType(position: Int) = when(menuList[position]) {
        is Category -> CATEGORY_VIEW_TYPE
        is Dish -> DISH_VIEW_TYPE
        else -> PLUG_VIEW_TYPE
    }

    override fun getItemCount() = menuList.size

    fun getCategoryPosition(categoryId: Int) = menuList.indexOfFirst { it is Category && it.id == categoryId }

    fun getCategoryByPosition(position: Int): Category? =
        if (menuList[position] is Category) menuList[position] as Category
        else null

}