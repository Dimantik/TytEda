package com.dimantik.tyteda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dimantik.tyteda.data.Repository
import com.dimantik.tyteda.ui.cart.CartViewModel
import com.dimantik.tyteda.ui.dish.DishViewModel
import com.dimantik.tyteda.ui.launcher.LauncherViewModel
import com.dimantik.tyteda.ui.menu.MenuViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)){
            return MenuViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(DishViewModel::class.java)){
            return DishViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(CartViewModel::class.java)){
            return CartViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(LauncherViewModel::class.java)){
            return LauncherViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown View Model Class")
    }

}