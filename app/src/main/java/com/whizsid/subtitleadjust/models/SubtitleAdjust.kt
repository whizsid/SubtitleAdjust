package com.whizsid.subtitleadjust.models

class SubtitleAdjust constructor (sub_title:Subtitle,sub_time:Int){
    private val subTitle: Subtitle=sub_title
    private val time: Int = sub_time

    fun getSubtitle():Subtitle{
        return subTitle
    }

    fun getTime():Int{
        return time
    }
}