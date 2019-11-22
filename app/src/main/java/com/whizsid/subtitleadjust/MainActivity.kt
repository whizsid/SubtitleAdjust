package com.whizsid.subtitleadjust

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import com.whizsid.subtitleadjust.adapters.SubtitleAdjustListAdapter
import com.whizsid.subtitleadjust.models.Subtitle
import com.whizsid.subtitleadjust.models.SubtitleAdjust

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var listView:ListView? = null
    var listItems:MutableList<SubtitleAdjust> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listView = findViewById(R.id.subtitleList)

        for (i in 0..1){

            val subtitleAdjust = SubtitleAdjust(Subtitle("00:00:01,200","00:00:02,300","Hey how are you"),0)

            listItems.add(subtitleAdjust)

        }


        val subtitleAdapter = SubtitleAdjustListAdapter(this,listItems)

        listView?.adapter = subtitleAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
