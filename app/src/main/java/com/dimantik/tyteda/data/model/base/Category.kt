package com.dimantik.tyteda.data.model.base

import com.google.gson.annotations.SerializedName


data class Category(
    @SerializedName("id"  )       var id      : Int?    = null,
    @SerializedName("name")       var name    : String? = null,
)
