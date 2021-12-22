package com.example.models.resources

data class TokenOutput(val userId: String, val token: String, val refreshToken: String) : BaseOutput() {
    constructor() : this("", "", "")
}
