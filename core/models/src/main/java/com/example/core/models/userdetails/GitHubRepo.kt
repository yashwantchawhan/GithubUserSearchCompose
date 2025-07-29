package com.example.core.models.userdetails

data class GitHubRepo(
    val name: String,
    val description: String?,
    val language: String?,
    val stargazers_count: Int,
    val html_url: String
)