package com.whizsid.subtitleadjust

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.whizsid.subtitleadjust.adapters.SubtitleAdjustListAdapter
import com.whizsid.subtitleadjust.database.DBOpenHelper
import com.whizsid.subtitleadjust.database.SubtitleModel
import com.whizsid.subtitleadjust.lib.SubtitleAdjust
import com.whizsid.subtitleadjust.lib.Time
import kotlinx.android.synthetic.main.app_bar.*
import android.content.Intent
import kotlinx.android.synthetic.main.content_adjust.*
import net.rdrei.android.dirchooser.DirectoryChooserActivity
import net.rdrei.android.dirchooser.DirectoryChooserConfig






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

        // Again to main activity after cancel pressed
        val cancelButton = findViewById<Button>(R.id.adjustCancelButton)

        cancelButton.setOnClickListener {
            val newIntent = Intent(this, MainActivity::class.java)
            this.startActivity(newIntent)
            finish()
        }

        val confirmButton = findViewById<Button>(R.id.adjustConfirmButton)

        confirmButton.setOnClickListener {
            val chooserIntent = Intent(this, DirectoryChooserActivity::class.java)

            val config = DirectoryChooserConfig.builder()
                .newDirectoryName("DirChooserSample")
                .allowReadOnlyDirectory(true)
                .allowNewDirectoryNameModification(true)
                .build()

            chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config)

            // REQUEST_DIRECTORY is a constant integer to identify the request, e.g. 0
            startActivityForResult(chooserIntent, REQUEST_DIRECTORY)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        listItems = mutableListOf()

        val dbHelper = DBOpenHelper(this,null)
        dbHelper.empty()

        subtitleAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_DIRECTORY) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                val dir =  data!!.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR)

                this.directorySelected(dir)
            } else {
                // Nothing selected
            }
        }
    }

    private fun directorySelected(dir: String){

    }

    companion object {
        const val REQUEST_DIRECTORY = 123
    }
}