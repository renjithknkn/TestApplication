package com.llyod.directory.di



import com.jakewharton.espresso.OkHttp3IdlingResource
import com.llyod.directory.repository.retrofit.DirectoryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val baseURL  = "https://61e947967bc0550017bc61bf.mockapi.io/api/v1/"
val httpClient  = OkHttpClient()
val idlingResource = OkHttp3IdlingResource.create("okkhttp",httpClient)


@Module
@InstallIn(SingletonComponent::class)
class DirectoryModule {

    @Provides
    fun directoryApi(retrofit: Retrofit) : DirectoryApi =  retrofit.create(DirectoryApi::class.java)


    @Provides
    fun retrofit() : Retrofit  = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}
