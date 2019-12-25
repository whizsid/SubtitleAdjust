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
        var count = 0

        val dbHelper = DBOpenHelper(this.activityContext,null)

        dbHelper.empty()

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

                    val subtitle = Subtitle(count,lastId,
                            Time(lastStartTime),
                            Time(lastEndTime),found?.get(0)?:"")


                    dbHelper.insert(SubtitleModel,subtitle)

                    activityContext.runOnUiThread {
                        activityContext.fileChooseIcon.setValueWithNoAnimation(count.rem(100).toFloat())
                    }

                    count++
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
}