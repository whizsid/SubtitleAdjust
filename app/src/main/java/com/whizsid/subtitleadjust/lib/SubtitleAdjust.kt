package com.whizsid.subtitleadjust.lib

/**
 * This class is using for user adjusted subtitles
 *
 * @author WhizSid <whizsid@aol.com>
 */
class SubtitleAdjust constructor (sub_title:Subtitle,sub_time:Time){
    private var subTitle: Subtitle=sub_title
    private var time: Time = sub_time

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

    /**
     * Modify the subtitle
     */
    fun setSubtitle(sub:Subtitle){
        subTitle = sub
    }

    /**
     * Changing the time
     */
    fun setTime(pTime:Time){
        time = pTime
    }
}