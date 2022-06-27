package com.devhighlevel

import java.lang.IllegalArgumentException

enum class Role {
    ADMIN,
    USER;

   companion object {
       fun of(name: String) = values().find { name == it.name } ?: throw IllegalArgumentException("Role $name does not exist")
   }
}