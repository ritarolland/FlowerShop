package com.example.prac1.di

import com.example.prac1.presentation.view.FragmentAuth
import com.example.prac1.presentation.view.FragmentCart
import com.example.prac1.presentation.view.FragmentCatalog
import com.example.prac1.presentation.view.FragmentDetails
import com.example.prac1.presentation.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: FragmentCatalog)
    fun inject(fragment: FragmentDetails)
    fun inject(activity: FragmentCart)
    fun inject(fragmentAuth: FragmentAuth)
}