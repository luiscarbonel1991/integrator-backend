package com.devhighlevel.application.config

import com.devhighlevel.infraestructure.clients.AwsClient
import com.devhighlevel.infraestructure.clients.MongoClient
import com.devhighlevel.infraestructure.db.repository.UserRepository
import com.devhighlevel.application.services.AuthenticationService
import com.devhighlevel.application.services.UserService
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
    singleOf(::Config)
}

fun clientModule() = module {
    singleOf(::MongoClient)
    single {AwsClient(get())}
}

fun repositoryModule() = module {
    single { UserRepository(get()) }
}

fun serviceModule() = module {
    single { UserService(get()) }
    single { AuthenticationService(get()) }
}

