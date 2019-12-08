package com.whizsid.subtitleadjust.lib

import java.io.BufferedReader

/**
 * User opened subtitle file
 *
 * @author WhizSid <whizsid@aol.com>
 */
class SubtitleFile (bufReader:BufferedReader){

    private val reader:BufferedReader = bufReader
    private var subtitles: MutableList<Subtitle> = mutableListOf()

    init {
        parseFile()
    }

    /**
     * Parsing the file and making subtitle instances
     */
    private fun parseFile(){
        var lastId = 0
        var lastStartTime = ""
        var lastEndTime = ""
        var count = 0


        reader.forEachLine {
            // Regex pattern for line that containing subtitle id "3232"
            val regexId = Regex("^(\\d+)$")

            // Regex pattern for line that containing times "01:05:30,300 -> 01:06:34,200"
            val regexTime = Regex("^(\\d+:\\d+:\\d+,\\d+)\\s\\-\\-\\>\\s(\\d+:\\d+:\\d+,\\d+)$")

            // Regex pattern for line that containing subtitle content. This pattern matching anything. "Hy 232!"
            val regexSubtitle = Regex("^(.+)$")

            when {
                regexId.containsMatchIn(it)->{
                    val found  = regexId.find(it)?.groupValues?.get(1)

                    lastId = found?.toInt()?:0
                }
                regexTime.containsMatchIn(it)->{
                    val found = regexTime.find(it)?.groupValues

                    lastStartTime = found?.get(1)?:""
                    lastEndTime = found?.get(2)?:""
                }
                regexSubtitle.containsMatchIn(it)->{
                    val found = regexSubtitle.find(it)?.groupValues

                    // Adding to the subtitle to the subtitle list if reached the last line.
                    subtitles.add(Subtitle(count,lastId,Time(lastStartTime),Time(lastEndTime),found?.get(0)?:""))

                    count++
                }
            }

        }
    }

    /**
     * Returning the parsed subtitle list from the file.
     */
    fun getSubtitles():MutableList<Subtitle>{
        return subtitles
    }
}