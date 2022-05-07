package com.dimantik.tyteda

import android.app.Application
import com.dimantik.tyteda.data.Repository
import com.dimantik.tyteda.data.local.LocalStorage
import com.dimantik.tyteda.data.remote.TytEdaService

class TytedaApplication : Application() {

    private lateinit var service: TytEdaService
    private lateinit var localStorage: LocalStorage
    private lateinit var repository: Repository
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate() {
        super.onCreate()

        service = TytEdaService()
        localStorage = LocalStorage(applicationContext)
        repository = Repository(service, localStorage)
        viewModelFactory = ViewModelFactory(repository)
    }
}