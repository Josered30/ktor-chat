package com.example.di

import com.example.repositories.RefreshTokenRepository
import com.example.repositories.UserRepository
import com.example.services.JwtService
import com.example.services.UserService
import com.example.services.impl.JwtServiceImpl
import com.example.services.impl.UserServiceImpl
import io.ktor.application.*
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.dsl.single


fun appModule(environment: ApplicationEnvironment): Module {
    return module {
        single<UserService> { UserServiceImpl(get()) } // get() Will resolve HelloRepository
        single<JwtService> { JwtServiceImpl(get(), get(), environment) }
        single { UserRepository() }
        single { RefreshTokenRepository() }
    }
}

