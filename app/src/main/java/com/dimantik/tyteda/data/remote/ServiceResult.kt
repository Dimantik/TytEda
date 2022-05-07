package com.dimantik.tyteda.data.remote

import com.dimantik.tyteda.data.model.custom.Menu

sealed class ServiceResult<T>(val message: String? = null, val data: T? = null) {
    class Success<T>(data: T) : ServiceResult<T>(data = data)
    class Load<T> : ServiceResult<T>()
    class Failure<T>(message: String?) : ServiceResult<T>(message)
}