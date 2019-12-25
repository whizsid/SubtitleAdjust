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

    }

    /**
     * Returning the parsed subtitle list from the file.
     */
    fun getSubtitles():MutableList<Subtitle>{
        return subtitles
    }
}