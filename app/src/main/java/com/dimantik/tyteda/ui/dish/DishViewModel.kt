package com.dimantik.tyteda.ui.dish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dimantik.tyteda.data.Repository
import com.dimantik.tyteda.data.model.base.Dish
import io.reactivex.rxjava3.kotlin.subscribeBy


class DishViewModel(
    private val repository: Repository
) : ViewModel() {

    private val dishMLD = MutableLiveData<Dish>()
    private val amountMLD = MutableLiveData<Int>()
    private val priceMLD = MutableLiveData<Int>()
    private val addCompleteMLD = MutableLiveData<Boolean>()

    val dishLD: LiveData<Dish> = dishMLD
    val amountLD: LiveData<Int> = amountMLD
    val priceLD: LiveData<Int> = priceMLD
    val addCompleteLD: LiveData<Boolean> = addCompleteMLD

    private lateinit var dish: Dish
    private var amount = 1

    init {
        amountMLD.value = amount
    }


    fun getDish(dishId: Int) {
        repository.getDishById(dishId)?.let { noNullDish ->
            dish = noNullDish
            priceMLD.value = noNullDish.price!! * amount
            dishMLD.value = noNullDish
        }
    }

    fun addToCart() {
        repository.addDishToCart(dish, amount).subscribeBy { result ->
            addCompleteMLD.value = result
        }
    }

    fun reduceAmount() {
        if (amount > 1) {
            amount--
            amountMLD.value = amount
            priceMLD.value = amount * dish.price!!
        }
    }

    fun increaseAmount() {
        amount++
        amountMLD.value = amount
        priceMLD.value = amount * dish.price!!
    }

}