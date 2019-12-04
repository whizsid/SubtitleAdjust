package com.whizsid.subtitleadjust.lib

import kotlin.math.floor

/**
 * This class is using for all timestamps in subtitle adjust app
 *
 * @author WhizSid <whizsid@aol.com>
 */
class Time {

    private var hour:Int =0

    private var minute:Int = 0

    private var second:Int = 0

    private var milli:Int =0

    companion object {
        /**
         * Validating a time string before parsing it
         */
        fun validateString(time:String):Boolean{
            val regex = Regex("(\\d+):(\\d+):(\\d+),(\\d+)")

            if(regex.containsMatchIn(time)){
                val hours = regex.find(time)?.groupValues?.get(1)?.toInt() as Int
                val minutes = regex.find(time)?.groupValues?.get(2)?.toInt() as Int
                val seconds = regex.find(time)?.groupValues?.get(3)?.toInt() as Int
                val millis = regex.find(time)?.groupValues?.get(4)?.toInt() as Int
                if(
                    hours>=0 &&
                    minutes>=0 &&
                    seconds>=0 &&
                    millis>=0 &&
                    hours<=60 &&
                    minutes<=60 &&
                    seconds<=60 &&
                    millis<=1000
                ){
                    return true
                }
            }

            return false
        }
    }

    /**
     * Creating a time by milliseconds
     *
     * @param pTime current millisecond timestamp
     */
    constructor(pTime:Double){
        hour = floor(pTime/(1000*60*60)) as Int

        minute = floor((pTime-hour*1000*60*60)/1000*60) as Int

        second = floor( (pTime-minute*1000*60)/1000) as Int

        milli = floor(pTime - second*1000) as Int
    }

    /**
     * Create a time by a string
     *
     * @param pTime in HH:MM:SS,mmm format
     */
    constructor(pTime:String){
        val explodedTime = pTime.split(Regex("\\,|\\:"))

        hour = explodedTime[0].toInt()
        minute = explodedTime[1].toInt()
        second = explodedTime[2].toInt()
        milli = explodedTime[3].toInt()
    }

    /**
     * Converting the time to string
     *
     * @return in HH:MM:SS,mmm format
     */
    override fun toString():String{
        return "${hour.toString().padStart(2,'0')}:${minute.toString().padStart(2,'0')}:${second.toString().padStart(2,'0')},${milli.toString().padStart(3,'0')}"
    }

    /**
     * Casting the time to integer.
     *
     * @return time in millis
     */
    fun toInt():Int{
        return hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000 + milli
    }

    /**
     * Returning the number of hours in the time
     *
     * @return  02:55:12,600 returning 2
     */
    fun getHour():Int{
        return hour
    }

    /**
     * Returning the minute section of the timestamp
     *
     * @return 02:55:12,600 returning 55
     */
    fun getMinute():Int{
        return minute
    }

    /**
     * Returning the seconds section of the timestamp
     *
     * @return 02:55:12,600 returning 12
     */
    fun getSecond():Int{
        return second
    }

    /**
     * Returning the millis section of the timestamp
     *
     * @return 02:55:12,600 returning 600
     */
    fun getMilli():Int{
        return milli
    }


}