package com.abrutsze.tableview.connections

import com.abrutsze.tableview.models.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {

    /*Gets news*/
    @GET("main/test")
    fun getNews(@Query("page") page: Int): Call<NewsResponse>

}