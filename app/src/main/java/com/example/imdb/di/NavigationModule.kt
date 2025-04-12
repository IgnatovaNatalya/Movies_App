package com.example.imdb.di

import com.example.imdb.navigation.Router
import com.example.imdb.navigation.RouterImpl
import org.koin.dsl.module

val navigationModule = module {
    val router = RouterImpl()

    single<Router> { router }
    single { router.navigatorHolder }
}