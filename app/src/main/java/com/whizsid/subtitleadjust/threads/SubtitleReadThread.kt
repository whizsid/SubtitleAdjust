package com.whizsid.subtitleadjust.threads

import android.content.Intent
import com.whizsid.subtitleadjust.MainActivity
import com.whizsid.subtitleadjust.SubtitleAdjustActivity
import com.whizsid.subtitleadjust.database.DBOpenHelper
import com.whizsid.subtitleadjust.database.SubtitleModel
import com.whizsid.subtitleadjust.lib.Subtitle
import com.whizsid.subtitleadjust.lib.Time
import java.io.BufferedReader

/**
 * Reading subtitle file in a background process
 */
class SubtitleReadThread(context: MainActivity,private val reader:BufferedReader) :Runnable {

    private var activityContext = context

    override fun run() {
        var lastId = 0
        var lastStartTime = ""
        var lastEndTime = ""
        var lastContent = ""
        var lastMode = LAST_READ_ID
        var count = 0

        val dbHelper = DBOpenHelper(this.activityContext,null)

        dbHelper.empty()

        // Regex pattern for line that containing subtitle id "3232"
        val regexId = Regex("^(\\d+)$")

        // Regex pattern for line that containing times "01:05:30,300 -> 01:06:34,200"
        val regexTime = Regex("^(\\d+:\\d+:\\d+,\\d+)\\s\\-\\-\\>\\s(\\d+:\\d+:\\d+,\\d+)$")

        // Regex pattern for line that containing subtitle content. This pattern matching anything. "Hy 232!"
        val regexSubtitle = Regex("^(.+)$")

        reader.forEachLine {
            val trimmed = it.trim()
            when {
                regexId.containsMatchIn(trimmed)->{
                    if(lastMode==LAST_READ_CONTENT){

                        val subtitle = Subtitle(count,lastId,
                            Time(lastStartTime),
                            Time(lastEndTime),lastContent)


                        dbHelper.insert(SubtitleModel,subtitle)
                        count++
                    }

                    val found  = regexId.find(trimmed)?.groupValues?.get(1)

                    lastId = found?.toInt()?:0
                    lastMode = LAST_READ_ID

                }
                regexTime.containsMatchIn(trimmed)->{
                    val found = regexTime.find(trimmed)?.groupValues

                    lastStartTime = found?.get(1)?:""
                    lastEndTime = found?.get(2)?:""
                    lastMode = LAST_READ_TIME
                }
                regexSubtitle.containsMatchIn(trimmed)->{
                    val found = regexSubtitle.find(trimmed)?.groupValues

                    // Adding to the subtitle to the subtitle list if reached the last line.
                    if(lastMode==LAST_READ_TIME){
                        lastContent = found?.get(0)?:""
                    } else if( lastMode==LAST_READ_CONTENT) {
                        lastContent += "\n${found?.get(0)?:""}"
                    }

                    activityContext.runOnUiThread {
                        activityContext.fileChooseIcon.setValueWithNoAnimation(count.rem(100).toFloat())
                    }
                    lastMode = LAST_READ_CONTENT
                }
            }

        }

        activityContext.runOnUiThread {
            val intent = Intent(activityContext,SubtitleAdjustActivity::class.java)

            activityContext.startActivity(intent)

            activityContext.finish()
        }

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
    }

    companion object {

        private const val LAST_READ_ID = 0
        private const val LAST_READ_CONTENT = 1
        private const val LAST_READ_TIME = 2
    }
}