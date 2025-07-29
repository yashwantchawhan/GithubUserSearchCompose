package com.example.core.remote

import com.example.core.models.userdetails.GitHubRepo
import com.example.core.models.userdetails.GitHubUser
import com.example.core.models.userlist.GitHubUserBrief
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): GitHubUser

    @GET("users/{username}/repos")
    suspend fun getRepos(@Path("username") username: String): List<GitHubRepo>

    @GET("users")
    suspend fun getUsers(): List<GitHubUserBrief>
}
