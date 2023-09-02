package com.example.nanohealthsuiteapp.di

import com.example.nanohealthsuiteapp.BuildConfig
import com.example.nanohealthsuiteapp.data.remote.ProductsApi
import com.example.nanohealthsuiteapp.data.repo.auth.AuthRepoImpl
import com.example.nanohealthsuiteapp.data.repo.product.ProductRepoImpl
import com.example.nanohealthsuiteapp.domain.repo.auth.AuthRepo
import com.example.nanohealthsuiteapp.domain.repo.product.ProductRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NanoAppModule {


    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    fun provideProductApi(okHttpClient: OkHttpClient): ProductsApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ProductsApi::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthRepo(productsApi: ProductsApi): AuthRepo = AuthRepoImpl(productsApi)

    @Provides
    @Singleton
    fun providesProductRepo(productsApi: ProductsApi): ProductRepo = ProductRepoImpl(productsApi)
}