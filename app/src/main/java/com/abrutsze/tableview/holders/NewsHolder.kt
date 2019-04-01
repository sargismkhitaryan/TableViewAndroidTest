package com.abrutsze.tableview.holders

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.abrutsze.tableview.R

class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var vProfilePicture: AppCompatImageView = itemView.findViewById(R.id.vProfilePicture)
    var vName: AppCompatTextView = itemView.findViewById(R.id.vName)
    var vDate: AppCompatTextView = itemView.findViewById(R.id.vDate)
    var vDescription: AppCompatTextView = itemView.findViewById(R.id.vDescription)
    var vPicture: AppCompatImageView = itemView.findViewById(R.id.vPicture)

}