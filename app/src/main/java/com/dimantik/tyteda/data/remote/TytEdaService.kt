package com.dimantik.tyteda.data.remote

import com.dimantik.tyteda.data.model.base.Category
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.Order
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TytEdaService {

    private val serviceImpl: TytedaServiceImpl

    companion object {
        private const val URL = "http://31.31.203.10:8081/"
    }

    init {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .apply {
                serviceImpl = create(TytedaServiceImpl::class.java)
            }
    }

    fun fetchCategoryList() = serviceImpl.fetchCategoryList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun fetchDishListByCategory(
        categoryId: Int
    ) = serviceImpl.fetchDishListByCategory(categoryId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun regOrder(
        order: Order
    ) = serviceImpl.regOrder(order)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}