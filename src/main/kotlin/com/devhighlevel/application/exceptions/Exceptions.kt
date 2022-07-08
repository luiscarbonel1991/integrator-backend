package com.devhighlevel.application.exceptions


class AwsException(message: String): RuntimeException(message)
class AuthenticationException(message: String) : RuntimeException(message)
class AuthorizationException(message: String) : RuntimeException(message)