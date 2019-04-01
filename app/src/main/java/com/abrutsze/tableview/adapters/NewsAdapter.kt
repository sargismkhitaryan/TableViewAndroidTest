package com.abrutsze.tableview.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abrutsze.tableview.R
import com.abrutsze.tableview.connections.ServerConnections
import com.abrutsze.tableview.holders.NewsHolder
import com.abrutsze.tableview.models.NewsResponse
import com.abrutsze.tableview.utils.Converters
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer
import java.util.*


class NewsAdapter : RecyclerView.Adapter<NewsHolder>() {

    private var newsResponse: ArrayList<NewsResponse.Post> = arrayListOf()

    fun addNews(newsResponse: ArrayList<NewsResponse.Post>) {
        this.newsResponse.addAll(newsResponse)
        notifyItemInserted(this.newsResponse.size)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NewsHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.news_item, viewGroup, false)
        return NewsHolder(v)
    }

    override fun onBindViewHolder(viewHolder: NewsHolder, i: Int) {

        val imageOption = DisplayImageOptions.Builder()
                .displayer(RoundedBitmapDisplayer(1000))
                .showImageOnLoading(android.R.color.transparent)
                .bitmapConfig(Bitmap.Config.RGB_565).build()

        ImageLoader.getInstance().displayImage(newsResponse[i].userPic, viewHolder.vProfilePicture, imageOption)
        ImageLoader.getInstance().displayImage(newsResponse[i].photo, viewHolder.vPicture)

        viewHolder.vName.text = newsResponse[i].userName
        viewHolder.vDate.text = newsResponse[i].date?.let { Converters.convertRawDateToYMDFormat(it) }
        viewHolder.vDescription.text = newsResponse[i].message

        if (i == (itemCount - 1)) {
            ServerConnections.getServerConnectionsInstance().getNews()
        }
    }

    override fun getItemCount(): Int {
        return newsResponse.size
    }

}