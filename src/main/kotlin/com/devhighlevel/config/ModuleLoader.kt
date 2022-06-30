package com.devhighlevel.config

import com.devhighlevel.clients.MongoClient
import com.devhighlevel.config.Config
import com.devhighlevel.db.repository.UserRepository
import com.devhighlevel.services.AuthenticationService
import com.devhighlevel.services.UserService
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object ModuleLoader {
    fun init() {
        startKoin {
            modules(
                listOf(
                    environmentModule(),
                    clientModule(),
                    repositoryModule(),
                    serviceModule()
                )
            )
        }
    }
}

fun environmentModule() = module {
    single { Config() }
}

fun clientModule() = module {
    singleOf(::MongoClient)
}

fun repositoryModule() = module {
    single { UserRepository(get()) }
}

fun serviceModule() = module {
    single { UserService(get()) }
    single { AuthenticationService(get()) }
}

