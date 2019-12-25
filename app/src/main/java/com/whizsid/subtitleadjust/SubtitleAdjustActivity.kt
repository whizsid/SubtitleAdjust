package com.whizsid.subtitleadjust

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.whizsid.subtitleadjust.adapters.SubtitleAdjustListAdapter
import com.whizsid.subtitleadjust.database.DBOpenHelper
import com.whizsid.subtitleadjust.database.SubtitleModel
import com.whizsid.subtitleadjust.lib.SubtitleAdjust
import com.whizsid.subtitleadjust.lib.Time
import kotlinx.android.synthetic.main.app_bar.*

class SubtitleAdjustActivity : AppCompatActivity() {


    var listItems:MutableList<SubtitleAdjust> = mutableListOf()
    lateinit var subtitleAdapter:SubtitleAdjustListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_adjust)
        setSupportActionBar(toolbar)

        val dbHelper = DBOpenHelper(this,null)

        val subtitles = dbHelper.search(SubtitleModel,"",2)

        subtitles.forEach {
            listItems.add(SubtitleAdjust(it,Time(it.getStartTime().toString())))
        }

        // Setting the list view adapter
        subtitleAdapter =  SubtitleAdjustListAdapter(this,listItems)



        val listView = findViewById<ListView>(R.id.subtitleList)
        listView?.adapter = subtitleAdapter

        // Hiding fab icon on creation
        val fabIcon = findViewById<FloatingActionButton>(R.id.subtitleAdjustAddButton)

        fabIcon.setOnClickListener {

            var subtitle = dbHelper.getNth(SubtitleModel,listItems.size.toLong())

            if(subtitle!=null){
                listItems.add(SubtitleAdjust(subtitle,subtitle.getStartTime()))

                subtitleAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        listItems = mutableListOf()

        val dbHelper = DBOpenHelper(this,null)
        dbHelper.empty()

        subtitleAdapter.notifyDataSetChanged()
    }
}