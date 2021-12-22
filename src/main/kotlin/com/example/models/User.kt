package com.example.models

import java.util.*

data class User(val id: String, val email: String, val name: String, val password: String) {
    constructor(email: String, name: String, password: String) : this("", email, name, password)
}
