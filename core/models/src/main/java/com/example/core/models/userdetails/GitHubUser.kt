package com.example.core.models.userdetails

data class GitHubUser(
    val login: String,
    val avatar_url: String,
    val followers: Int,
    val following: Int
)