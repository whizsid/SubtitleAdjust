package com.whizsid.subtitleadjust.threads

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.whizsid.subtitleadjust.MainActivity
import com.whizsid.subtitleadjust.R
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

        activityContext.subtitles = subtitles

        // Parsing subtitles to the adapter
        activityContext.subtitleAdapter.setSubtitles(subtitles)


        if(subtitles.size>2){

            for (i in 0..1){
                val subtitle = subtitles[i]
                activityContext.listItems.add(SubtitleAdjust(subtitle,subtitle.getStartTime()))
            }

            activityContext.runOnUiThread(Runnable {
                val fabIcon = activityContext.findViewById<FloatingActionButton>(R.id.subtitleAdjustAddButton)
                fabIcon.show()
                activityContext.subtitleAdapter.notifyDataSetChanged()
            })
        }

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
    }
}