package com.dimantik.tyteda.data.model.base

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("name"            )    val name:            String,
    @SerializedName("phone"           )    val phone:           String,
    @SerializedName("email"           )    val email:           String,
    @SerializedName("cartItemList"    )    val cartItemList:    List<CartItem>
)