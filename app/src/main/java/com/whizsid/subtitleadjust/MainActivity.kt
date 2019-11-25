package com.whizsid.subtitleadjust

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import com.whizsid.subtitleadjust.adapters.SubtitleAdjustListAdapter
import com.whizsid.subtitleadjust.models.Subtitle
import com.whizsid.subtitleadjust.models.SubtitleAdjust
import com.whizsid.subtitleadjust.models.SubtitleFile

import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    var listItems:MutableList<SubtitleAdjust> = mutableListOf()
    private lateinit var subtitleAdapter:SubtitleAdjustListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Setting the list view adapter
        subtitleAdapter =  SubtitleAdjustListAdapter(this,listItems)
        val listView = findViewById<ListView>(R.id.subtitleList)
        listView?.adapter = subtitleAdapter

        // Binding action to file choosing icon
        val fileChooseIcon = findViewById<ImageView>(R.id.fileChooseIcon)
        fileChooseIcon.setOnClickListener {
            this.onClickFileChooseIcon(it)
        }
    }

    private fun onClickFileChooseIcon(view:View){
        val intent = Intent()
            .setType("application/x-subrip")
            .setAction(Intent.ACTION_GET_CONTENT)


        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode!= RESULT_CANCELED && requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data

            if(selectedFile!=null){

                val reader = BufferedReader(InputStreamReader(contentResolver.openInputStream(selectedFile)))

                val subFile = SubtitleFile(reader)

                val subtitles = subFile.getSubtitles()

                if(subtitles.size>2){

                    for (i in 0..2){
                        val subtitle = subtitles[i]
                        listItems.add(SubtitleAdjust(subtitle,1213))

                    }

                    subtitleAdapter.notifyDataSetChanged()
                }
            }

        }

    }
}
