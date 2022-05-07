package com.dimantik.tyteda.data

import com.dimantik.tyteda.data.local.LocalStorage
import com.dimantik.tyteda.data.model.base.*
import com.dimantik.tyteda.data.model.custom.Menu
import com.dimantik.tyteda.data.remote.LoginResult
import com.dimantik.tyteda.data.remote.TytEdaService
import com.dimantik.tyteda.data.remote.ServiceResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy

class Repository(
    private val service: TytEdaService,
    private val localStorage: LocalStorage
) {

    private lateinit var categoryList: List<Category>
    private lateinit var dishMap: Map<Int, List<Dish>>
    private lateinit var cartItemList: MutableList<CartItem>
    private lateinit var favoriteList: MutableList<Int>

    //Get
    fun getDishById(dishId: Int): Dish? {
        dishMap.entries.forEach { entry ->
            entry.value.find { it.id == dishId }?.let { return it }
        }
        return null
    }

    //Update (Кусок дерьма, который надо переделать)
    fun updateMenu(): Observable<ServiceResult<Menu>> = Observable.create { emitter ->
        emitter.onNext(ServiceResult.Load())
        service.fetchCategoryList()
            .subscribeBy(
                onSuccess = { categoryList ->
                    this.categoryList = categoryList
                    Observable.fromIterable(categoryList)
                        .flatMap { service.fetchDishListByCategory(it.id!!).toObservable() }
                        .toList()
                        .subscribeBy(
                            onSuccess = { dishListList ->
                                this.dishMap = mutableMapOf<Int, List<Dish>>().apply {
                                    dishListList.forEach { dishList ->
                                        if (dishList.isNotEmpty()) {
                                            put(dishList.first().category_id!!, dishList)
                                        }
                                    }
                                }
                                localStorage.fetchCart().subscribeBy(
                                    onSuccess = { cartItemList ->
                                        this.cartItemList = cartItemList
                                        localStorage.fetchFavoriteList().subscribeBy(
                                            onSuccess = { favoriteList ->
                                                this.favoriteList = favoriteList
                                                Menu(
                                                    categoryList = categoryList,
                                                    cartItemList = cartItemList,
                                                    favoriteList = favoriteList,
                                                    dishMap = dishMap
                                                ).apply {
                                                    emitter.onNext(ServiceResult.Success(this))
                                                    emitter.onComplete()
                                                }
                                            },
                                            onError = { emitter.onNext(ServiceResult.Failure(it.message)) }
                                        )
                                    },
                                    onError = { emitter.onNext(ServiceResult.Failure(it.message)) }
                                )
                            },
                            onError = { emitter.onNext(ServiceResult.Failure(it.message)) }
                        )
                },
                onError = { emitter.onNext(ServiceResult.Failure(it.message)) }
            )

    }

    //Reg order
    fun regOrder(
        name: String,
        phone: String,
        email: String
    ): Observable<ServiceResult<RegOrderResult>> = Observable.create { emitter ->
        emitter.onNext(ServiceResult.Load())
        service.regOrder(
            Order(
                name = name,
                phone = phone,
                email = email,
                cartItemList = cartItemList
            )
        ).subscribeBy(
            onSuccess = { regOrderResult ->
                emitter.onNext(ServiceResult.Success(regOrderResult))
            },
            onError = { emitter.onError(it) }
        )
    }

    //CRUD
    fun changeFavoriteStatus(dish: Dish): Single<List<Int>> = Single.create { emitter ->
        if (!favoriteList.contains(dish.id)) favoriteList.add(dish.id!!)
        else favoriteList.remove(dish.id)

        localStorage.saveFavoriteList(favoriteList)
        emitter.onSuccess(favoriteList)
    }

    fun reduceCartItemAmount(cartItem: CartItem): Single<List<CartItem>> = Single.create { emitter ->
        cartItem.reduceAmount()
        when(cartItem.amount) {
            0 -> cartItemList.removeAll { it.dishId == cartItem.dishId }
        }
        localStorage.saveCart(cartItemList)
        emitter.onSuccess(cartItemList)
    }

    fun increaseCartItemAmount(cartItem: CartItem): Single<List<CartItem>> = Single.create { emitter ->
        cartItem.increaseAmount()
        localStorage.saveCart(cartItemList)
        emitter.onSuccess(cartItemList)
    }

    fun addDishToCart(dish: Dish, amount: Int): Single<Boolean> = Single.create { emitter ->
        when(val cartItem = cartItemList.find { it.dishId == dish.id }) {
            null -> cartItemList.add(CartItem(dish.id!!, amount))
            else -> cartItem.increaseAmount(amount)
        }
        localStorage.saveCart(cartItemList)
        emitter.onSuccess(true)
    }

    fun updateCart(): Single<List<CartItem>> = Single.create { emitter ->
        emitter.onSuccess(cartItemList)
    }

    fun updateDishList(): Single<List<Dish>> = Single.create { emitter ->
        val dishList = mutableListOf<Dish>()

        dishMap.entries.forEach { entry ->
            dishList.addAll(entry.value)
        }

        emitter.onSuccess(dishList)
    }

    fun updateTotal(): Single<Int> = Single.create { emitter ->
        var total = 0

        cartItemList.forEach { cartItem ->
            total += (cartItem.amount * getDishById(cartItem.dishId)!!.price!!)
        }

        emitter.onSuccess(total)
    }

}