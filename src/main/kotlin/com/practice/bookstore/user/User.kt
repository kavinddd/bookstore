package com.practice.bookstore.user


data class User(
        val email: String,
        val password: String,
        val roles: MutableCollection<Role>
)