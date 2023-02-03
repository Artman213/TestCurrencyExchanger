package com.example.testcurrencyexchanger.di

import android.content.Context
import android.util.Log
import com.example.testcurrencyexchanger.data.SecureRepository
import com.example.testcurrencyexchanger.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    private const val baseUrl = Constants.BASE_URL


    @Provides
    @Singleton
    fun provideSecureRepository(@ApplicationContext context: Context): SecureRepository {
        return SecureRepository(context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(debugHttpClientBuilder.build())
            .build()
    }

    private val debugHttpClientBuilder: OkHttpClient.Builder
        get() = releaseHttpClientBuilder
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

    private val releaseHttpClientBuilder: OkHttpClient.Builder
        get() = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().build()
                try {
                    chain.proceed(request)
                } catch (e: Throwable) {
                    Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_2)
                        .message(e.toString())
                        .code(500)
                        .body(e.toString().toResponseBody())
                        .build()
                }
            }
}