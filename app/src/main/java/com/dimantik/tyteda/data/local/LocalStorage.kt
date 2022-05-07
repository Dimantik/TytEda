package com.dimantik.tyteda.data.local

import android.content.Context
import com.dimantik.tyteda.data.model.base.CartItem
import io.reactivex.rxjava3.core.Single
import kotlin.text.StringBuilder

class LocalStorage(
    private val context: Context
) {

    companion object {
        private const val TYT_EDA_PREFERENCES = "tyt_eda_settings"

        private const val FAVORITE_LIST = "favorite"
        private const val CART = "cart"

        private const val ITEM_ARRAY_SEPARATOR = ","
        private const val ITEM_SEPARATOR = ":"
    }

    private val settings = context.getSharedPreferences(TYT_EDA_PREFERENCES, Context.MODE_PRIVATE);

    //fetch
    fun fetchCart(): Single<MutableList<CartItem>> = Single.create { emitter ->
        val cart = mutableListOf<CartItem>()

        if(settings.contains(CART)) {
            cart.addAll(parseCartString(settings.getString(CART, null)))
        }

        emitter.onSuccess(cart)
    }

    fun fetchFavoriteList(): Single<MutableList<Int>> = Single.create { emitter ->
        val favoriteList = mutableListOf<Int>()

        if(settings.contains(FAVORITE_LIST)) {
            favoriteList.addAll(parseFavoriteList(settings.getString(FAVORITE_LIST, null)))
        }

        emitter.onSuccess(favoriteList)
    }

    //save
    fun saveCart(cart: MutableList<CartItem>) {
        settings.edit().putString(CART, buildCartString(cart)).apply()
    }

    fun saveFavoriteList(favoriteList: List<Int>) {
        settings.edit().putString(FAVORITE_LIST, buildFavoriteListString(favoriteList)).apply()
    }


    //parse
    private fun parseCartString(cartString: String?): List<CartItem> {
        val result = mutableListOf<CartItem>()

        cartString?.let { noNullCartString ->
            val cartItemArray = noNullCartString.split(ITEM_ARRAY_SEPARATOR)
            cartItemArray.forEach { cartItem ->
                if (cartItem != "") {
                    val valueArray = cartItem.split(ITEM_SEPARATOR)
                    result.add(CartItem(
                        dishId = valueArray[0].toInt(),
                        amount = valueArray[1].toInt()
                    ))
                }
            }
        }

        return result
    }

    private fun parseFavoriteList(favoriteListString: String?): List<Int> {
        val result = mutableListOf<Int>()

        favoriteListString?.let { noNullFavoriteListString ->
            val favoriteArray = noNullFavoriteListString.split(ITEM_ARRAY_SEPARATOR)
            favoriteArray.forEach { favoriteItem ->
                if (favoriteItem != "") {
                    result.add(favoriteItem.toInt())
                }
            }
        }

        return result
    }

    //Build
    private fun buildFavoriteListString(favoriteList: List<Int>) = StringBuilder().apply {
        favoriteList.forEach { dishId ->
            append(ITEM_ARRAY_SEPARATOR)
            append(dishId)
        }
    }.toString()

    private fun buildCartString(cart: List<CartItem>) = StringBuilder().apply {
        cart.forEach { cartItem ->
            append(cartItem.dishId)
            append(ITEM_SEPARATOR)
            append(cartItem.amount)
            append(ITEM_ARRAY_SEPARATOR)
        }
    }.toString()

}