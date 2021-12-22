package com.example.models.resources

data class LoginOutput(val userId: String, val token: String, val refreshToken: String) : BaseOutput()