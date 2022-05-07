package com.dimantik.tyteda.data.model.base

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Dish (
    @SerializedName("id"           )  var id          : Int?    = null,
    @SerializedName("name"         )  var name        : String? = null,
    @SerializedName("summary"      )  var summary     : String? = null,
    @SerializedName("price"        )  var price       : Int?    = null,
    @SerializedName("image"        )  var image       : String? = null,
    @SerializedName("category_id"  )  var category_id : Int?    = null
) : Serializable