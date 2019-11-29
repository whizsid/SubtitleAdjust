package com.whizsid.subtitleadjust.lib

/**
 * This class is using for user adjusted subtitles
 *
 * @author WhizSid <whizsid@aol.com>
 */
class SubtitleAdjust constructor (sub_title:Subtitle,sub_time:Time){
    private val subTitle: Subtitle=sub_title
    private val time: Time = sub_time

    /**
     * Returning the subtitle instance
     */
    fun getSubtitle():Subtitle{
        return subTitle
    }

    /**
     * Returning the adjusted time
     */
    fun getTime():Time{
        return time
    }
}