package com.iplato.spacex.di

import androidx.lifecycle.ViewModelProvider
import com.iplato.spacex.viewmodels.ListViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @Binds
    abstract fun bindsViewModelFactory(factory: ListViewModelFactory): ViewModelProvider.Factory
}