package com.dimantik.tyteda.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dimantik.tyteda.data.Repository
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.CartItem
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy

class CartViewModel(
    private val repository: Repository
) : ViewModel() {

    private val dishMapMLD = MutableLiveData<List<Dish>>()
    private val cartItemListMLD = MutableLiveData<List<CartItem>>()
    private val totalMLD = MutableLiveData<Int>()

    val dishListLD: LiveData<List<Dish>> = dishMapMLD
    val cartItemListLD: LiveData<List<CartItem>> = cartItemListMLD
    val totalLD: LiveData<Int> = totalMLD

    private val compositeDisposable = CompositeDisposable()


    init {
        repository.updateCart().subscribeBy { cartItemList ->
            cartItemListMLD.value = cartItemList
        }
        repository.updateDishList().subscribeBy { dishList ->
            dishMapMLD.value = dishList
        }
        repository.updateTotal().subscribeBy { total ->
            totalMLD.value = total
        }
    }

    fun reduceCartItemAmount(cartItem: CartItem) =
        repository.reduceCartItemAmount(cartItem).subscribeBy { cartItemList ->
            cartItemListMLD.value = cartItemList

            repository.updateTotal().subscribeBy { total ->
                totalMLD.value = total
            }
    }

    fun increaseCartItemAmount(cartItem: CartItem) =
        repository.increaseCartItemAmount(cartItem).subscribeBy { cartItemList ->
            cartItemListMLD.value = cartItemList

            repository.updateTotal().subscribeBy { total ->
                totalMLD.value = total
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun regOrder(name: String, phone: String, email: String) {
        repository.regOrder(name, phone, email).subscribeBy {

        }
    }

}