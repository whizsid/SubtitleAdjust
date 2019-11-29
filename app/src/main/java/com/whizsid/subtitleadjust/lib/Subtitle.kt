package com.whizsid.subtitleadjust.lib

/**
 * This class is using for manipulate to subtitles
 *
 * @author WhizSid <whizsid@aol.com>
 */
class Subtitle(subId:Int,start_time: Time, end_time: Time, sub_title: String) {
    private val startTimestamp:Time = start_time

    private val endTimestamp:Time = end_time

    private val subtitle:String = sub_title

    private val id = subId

    /**
     * Returning the sub title start time
     */
    fun getStartTime():Time {
        return startTimestamp
    }

    /**
     * Returning the ending time of the subtitle
     */
    fun getEndTime():Time {
        return endTimestamp
    }

    /**
     * Returning the content of the subtitle.
     */
    fun getContent():String {
        return subtitle
    }

    /**
     * Returning the subtitle id
     */
    fun getId():Int {
        return id
    }

    override fun toString(): String {
        return "${getContent()} (${getStartTime()}:${getEndTime()})"
    }
}