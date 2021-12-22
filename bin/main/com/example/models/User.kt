package com.example.models

import java.util.*

data class User(val id: String, val email: String, val password: String) {
    constructor(email: String, password: String) : this("", email, password)
}
