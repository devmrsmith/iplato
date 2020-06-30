package com.iplato.spacex.di

import com.iplato.spacex.ui.ListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface ApplicationComponent {
    fun inject(activity: ListFragment)
}