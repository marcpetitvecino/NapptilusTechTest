package com.example.napptilustechtest.network

import com.google.gson.annotations.SerializedName

data class OompaListResponse(
    @SerializedName("current")
    var currentPage: Int,
    @SerializedName("total")
    var total: Int,
    @SerializedName("results")
    var result: List<OompaDetailItem>
)
