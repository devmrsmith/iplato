package com.iplato.spacex

import android.app.Application
import com.iplato.spacex.di.DaggerApplicationComponent

class App : Application() {

    val appComponent = DaggerApplicationComponent.create()
}