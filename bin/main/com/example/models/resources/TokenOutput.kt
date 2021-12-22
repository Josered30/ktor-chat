package com.example.models.resources

data class TokenOutput(val token: String, val refreshToken: String) : BaseOutput() {
    constructor() : this("", "")
}
