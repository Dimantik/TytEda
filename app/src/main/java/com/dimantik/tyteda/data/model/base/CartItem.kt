package com.dimantik.tyteda.data.model.base

import com.google.gson.annotations.SerializedName

class CartItem(
    @SerializedName("dishId"  )    val dishId    : Int = 0,
    @SerializedName("amount"  )    var amount    : Int = 0,
) {

    fun increaseAmount(field: Int = 1) {
        amount += field
    }

    fun reduceAmount(field: Int = 1) {
        amount -= field
        if (amount < 0) amount = 0
    }

}