package com.dimantik.tyteda.data.remote

import com.dimantik.tyteda.data.model.base.User
import com.dimantik.tyteda.data.model.custom.Menu

sealed class LoginResult<T>(val message: String? = null, val user: User? = null) {
    class Success(user: User) : LoginResult<Menu>(user = user)
    class Load : LoginResult<Menu>()
    class Failure(message: String?) : LoginResult<Menu>(message)
}