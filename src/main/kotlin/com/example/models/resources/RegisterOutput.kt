package com.example.models.resources

data class RegisterOutput(val id: String, val email: String, val name: String) : BaseOutput() {
    constructor() : this("", "", "")
}
