package com.whizsid.subtitleadjust.models

class Subtitle(start_time: String, end_time: String, sub_title: String) {
    private val startTimestamp:Int = strToTime(start_time)

    private val endTimestamp:Int = strToTime(end_time)

    private val subtitle:String = sub_title

    private fun strToTime(time: String): Int {
        val explodedTime = time.split(Regex("\\,|\\:"))

        return explodedTime[0].toInt() * 60 * 60 * 1000 + explodedTime[1].toInt() * 60 * 1000 + explodedTime[2].toInt() * 1000 + explodedTime[3].toInt()
    }

    fun getStartTime():Int {
        return startTimestamp
    }

    fun getEndTime():Int {
        return endTimestamp
    }

    fun getContent():String {
        return subtitle
    }
}