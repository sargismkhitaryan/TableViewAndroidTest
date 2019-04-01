package com.abrutsze.tableview.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class NewsResponse(
        @Expose
        @SerializedName("page")
        val page: String?,
        @Expose
        @SerializedName("posts")
        val posts: ArrayList<Post>,
        @Expose
        @SerializedName("total_pages")
        val totalPages: Int?
) {
    data class Post(
            @Expose
            @SerializedName("date")
            val date: String?,
            @Expose
            @SerializedName("id")
            val id: Long?,
            @Expose
            @SerializedName("message")
            val message: String?,
            @Expose
            @SerializedName("photo")
            val photo: String?,
            @Expose
            @SerializedName("user_id")
            val userId: String?,
            @Expose
            @SerializedName("user_name")
            val userName: String?,
            @Expose
            @SerializedName("user_pic")
            val userPic: String?
    )
}