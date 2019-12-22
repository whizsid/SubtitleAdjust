package com.whizsid.subtitleadjust

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.whizsid.subtitleadjust.adapters.SubtitleAdjustListAdapter
import com.whizsid.subtitleadjust.lib.Subtitle
import com.whizsid.subtitleadjust.lib.SubtitleAdjust
import com.whizsid.subtitleadjust.lib.SubtitleFile
import com.whizsid.subtitleadjust.lib.Time
import com.whizsid.subtitleadjust.threads.SubtitleReadThread

import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    var listItems:MutableList<SubtitleAdjust> = mutableListOf()
    lateinit var subtitleAdapter:SubtitleAdjustListAdapter
    var subtitles: MutableList<Subtitle> = mutableListOf()

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


        // Hiding fab icon on creation
        val fabIcon = findViewById<FloatingActionButton>(R.id.subtitleAdjustAddButton)
        fabIcon.hide()

        fabIcon.setOnClickListener {

            if(subtitles.size>0){

                val index = listItems.size
                val subtitle = subtitles[index]
                listItems.add(SubtitleAdjust(subtitle,subtitle.getStartTime()))

                subtitleAdapter.notifyDataSetChanged()
            }
        }

    }

    /**
     * This function is calling after the file icon clicked
     */
    private fun onClickFileChooseIcon(view:View){
        // Open the file picker
        val intent = Intent()
            .setType("*/*")
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
                // Creating a read buffer from the Uri
                val reader = BufferedReader(InputStreamReader(contentResolver.openInputStream(selectedFile)))

                val activityToast = Toast.makeText(this,"Please wait! Reading your file",Toast.LENGTH_LONG)
                activityToast.show()

                // Read the file in an another thread. Reader will taking a longer time.
                val readThread = SubtitleReadThread(this,reader)

                AsyncTask.execute(readThread)
            }

        }

    }
}
