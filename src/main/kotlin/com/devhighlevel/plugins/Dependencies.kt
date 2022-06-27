package com.devhighlevel.plugins

import com.devhighlevel.config.MongoClient
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
                    clientModule(),
                    repositoryModule(),
                    serviceModule()
                )
            )
        }
    }
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

