package com.dimantik.tyteda.data.model.custom

import com.dimantik.tyteda.data.model.base.CartItem
import com.dimantik.tyteda.data.model.base.Category
import com.dimantik.tyteda.data.model.base.Dish

class Menu(
    var categoryList: List<Category> = listOf(),
    var dishMap: Map<Int, List<Dish>> = mapOf(),
    var cartItemList: MutableList<CartItem> = mutableListOf(),
    var favoriteList: MutableList<Int> = mutableListOf()
) {

    constructor(menu: Menu) : this(
        categoryList = menu.categoryList,
        dishMap = menu.dishMap,
        cartItemList = menu.cartItemList,
        favoriteList = menu.favoriteList
    )

    val size: Int
        get() {
            var result = 0
            dishMap.entries.forEach {
                result += it.value.size
            }
            return result
        }
}