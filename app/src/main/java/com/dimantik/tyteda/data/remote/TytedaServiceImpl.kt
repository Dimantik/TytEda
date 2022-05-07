package com.dimantik.tyteda.data.remote

import com.dimantik.tyteda.data.model.base.Category
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.Order
import com.dimantik.tyteda.data.model.base.RegOrderResult
import com.dimantik.tyteda.data.model.base.User
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface TytedaServiceImpl {

    @GET("/categories")
    fun fetchCategoryList(): Single<List<Category>>

    @GET("/categories/{id}")
    fun fetchDishListByCategory(@Path("id") categoryId: Int): Single<List<Dish>>

    @POST("/register")
    fun login(): Single<User>

    @POST("/orders")
    fun regOrder(@Body oder: Order): Single<RegOrderResult>

}