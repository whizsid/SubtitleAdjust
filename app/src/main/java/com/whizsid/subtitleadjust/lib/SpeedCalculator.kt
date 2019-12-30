package com.whizsid.subtitleadjust.lib

/**
 * This class is the method behind the app. This class will calculate the offset and speed of the subtitle file.
 *
 * @author WhizSid <whizsid@aol.com>
 */
class SpeedCalculator(private var adjustList: MutableList<SubtitleAdjust>) {
    private val adjustedItems = adjustList

    private var offset = 0

    private var speed = 0

    /**
     * Calculating the offset and speed
     *
     * @return weather that calculation done or note
     */
    fun calculate():Boolean{
        var speeds = 0
        var offsets = 0

        val length = adjustedItems.size

        if(length<2){
            return false
        }

        var index = 0

        adjustList.forEach {
            if(index+1 < length){
                val first = it
                val second = adjustList[index+1]

                speeds += this.calculateSpeed(
                    first.getTime().toInt(),
                    second.getTime().toInt(),
                    first.getSubtitle().getStartTime().toInt(),
                    second.getSubtitle().getStartTime().toInt()
                )

                offsets += this.calculateOffset(
                    first.getTime().toInt(),
                    second.getTime().toInt(),
                    first.getSubtitle().getStartTime().toInt(),
                    second.getSubtitle().getStartTime().toInt()
                )
            }

            index++
        }

        this.speed = speeds/index
        this.offset = offsets/index

        return true
    }

    /**
     * Calculating the actual speed by two actual points and two error points
     *
     * @param x First actual time in unix format
     * @param y Second actual time in unix format
     * @param a First error time associated with x time in unix format
     * @param b Second error time associated with y time in unix format
     */
    private fun calculateSpeed (x:Int,y:Int,a:Int,b:Int):Int {
        return (y - x)/(b - a)
    }

    /**
     * Calculating the actual offset by two actual points and two error points
     *
     * @param x First actual time in unix format
     * @param y Second actual time in unix format
     * @param a First error time associated with x time in unix format
     * @param b Second error time associated with y time in unix format
     */
    private fun calculateOffset(x:Int,y:Int,a:Int,b:Int):Int {
        return (a*y - b*x)/(y - x)
    }

    /**
     * Returning the corrected subtitle after calculating the offset and speed
     */
    fun getCorrectedSubtitle(errorSubtitle: Subtitle):Subtitle{

        val startTime = errorSubtitle.getStartTime().toInt()*this.speed - this.offset
        val endTime = errorSubtitle.getEndTime().toInt()*this.speed - this.offset

        return Subtitle(
            errorSubtitle.getIncrementalIndex(),
            errorSubtitle.getId(),
            Time(startTime.toLong()),
            Time(endTime.toLong()),
            errorSubtitle.getContent()
        )

    }
}