package com.example.plugins

import com.example.di.appModule
import io.ktor.application.*
import org.koin.ktor.ext.Koin
import org.koin.logger.SLF4JLogger

fun Application.configureKoin(environment: ApplicationEnvironment) {

    install(Koin) {
        SLF4JLogger()
        modules(appModule(environment))
    }
}