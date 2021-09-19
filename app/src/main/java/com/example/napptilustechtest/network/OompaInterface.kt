package com.example.napptilustechtest.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface OompaInterface {

    @GET
    suspend fun getOompaListByPage(@Url url: String): Response<OompaListResponse>

    @GET
    suspend fun getOompaById(@Url url: String): Response<OompaDetailItem>
}