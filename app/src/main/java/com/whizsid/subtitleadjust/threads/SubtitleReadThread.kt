package com.whizsid.subtitleadjust.threads

import com.whizsid.subtitleadjust.MainActivity
import com.whizsid.subtitleadjust.lib.SubtitleAdjust
import com.whizsid.subtitleadjust.lib.SubtitleFile
import java.io.BufferedReader

/**
 * Reading subtitle file in a background process
 */
class SubtitleReadThread(context: MainActivity,private val reader:BufferedReader) :Runnable {

    private var activityContext = context

    override fun run() {
        // Parse the subtitle file and subtitles
        val subFile = SubtitleFile(reader)

        val subtitles = subFile.getSubtitles()

        // Parsing subtitles to the adapter
        activityContext.subtitleAdapter.setSubtitles(subtitles)

        if(subtitles.size>2){

            for (i in 0..1){
                val subtitle = subtitles[i]
                activityContext.listItems.add(SubtitleAdjust(subtitle,subtitle.getStartTime()))
            }

            activityContext.runOnUiThread(Runnable {
                activityContext.subtitleAdapter.notifyDataSetChanged()
            })
        }

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
    }
}