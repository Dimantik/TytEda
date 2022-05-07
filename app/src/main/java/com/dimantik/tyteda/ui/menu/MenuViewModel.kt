package com.dimantik.tyteda.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dimantik.tyteda.data.Repository
import com.dimantik.tyteda.data.model.base.Dish
import com.dimantik.tyteda.data.model.base.CartItem
import com.dimantik.tyteda.data.remote.ServiceResult
import com.dimantik.tyteda.data.model.custom.Menu
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy


class MenuViewModel(
    private val repository: Repository
) : ViewModel() {

    private val updateResultMLD = MutableLiveData<ServiceResult<Menu>>()
    private val favoriteListMLD = MutableLiveData<List<Int>>()
    private val cartItemListMLD = MutableLiveData<List<CartItem>>()
    private val totalMLD = MutableLiveData<Int>()

    val serviceResultLD: LiveData<ServiceResult<Menu>> = updateResultMLD
    val favoriteListLD: LiveData<List<Int>> = favoriteListMLD
    val cartItemListLD: LiveData<List<CartItem>> = cartItemListMLD
    val totalLD: LiveData<Int> = totalMLD

    private val compositeDisposable = CompositeDisposable()

    var isInitPrevious = false

    init {
        updateMenu()
    }

    fun updateMenu() {
        repository.updateMenu().subscribeBy { updateResult ->
            updateResultMLD.value = updateResult
            if (updateResult is ServiceResult.Success) {
                isInitPrevious = true
                updateTotal()
            }

        }.addTo(compositeDisposable)
    }

    fun changeFavoriteStatus(dish: Dish) {
        repository.changeFavoriteStatus(dish).subscribeBy { favoriteList ->
            favoriteListMLD.value = favoriteList
        }.addTo(compositeDisposable)
    }

    fun updateCart() {
        repository.updateCart().subscribeBy { cartItemList ->
            cartItemListMLD.value = cartItemList
            updateTotal()
        }
    }

    fun updateTotal() {
        repository.updateTotal().subscribeBy { total ->
            totalMLD.value = total
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}