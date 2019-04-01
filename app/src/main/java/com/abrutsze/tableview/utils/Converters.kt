package com.abrutsze.tableview.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    companion object {
        fun convertRawDateToYMDFormat(publicationDateString: String): String {

            val inputPattern = "E MMM dd HH:mm:ss ZZZZ yyyy"
            val outputPattern = "MMM dd, yyyy kk:mm a"

            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            val date: Date
            var str = ""

            try {
                date = inputFormat.parse(publicationDateString)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }
    }

}