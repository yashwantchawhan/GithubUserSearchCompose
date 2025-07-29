package com.example.core.remote.di

import com.example.core.remote.GitHubService
import com.example.core.remote.userdetails.UserDetailRepository
import com.example.core.remote.userlist.UserListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.github.com/"
    private const val GITHUB_TOKEN = "ADD YOUR GITHUB TOKEN"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "token $GITHUB_TOKEN")
                .build()
            chain.proceed(request)
        }
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubService(retrofit: Retrofit): GitHubService {
        return retrofit.create(GitHubService::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubRepository(
        service: GitHubService,
        @Named("ioDispatcher") coroutineDispatcher: CoroutineDispatcher
    ): UserListRepository {
        return UserListRepository(service, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideGitHubDetailRepository(
        service: GitHubService,
        @Named("ioDispatcher") coroutineDispatcher: CoroutineDispatcher
    ): UserDetailRepository = UserDetailRepository(service, coroutineDispatcher)
}