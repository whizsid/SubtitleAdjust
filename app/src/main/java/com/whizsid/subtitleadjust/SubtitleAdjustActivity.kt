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
import android.widget.Toast
import androidx.core.content.PermissionChecker
import net.rdrei.android.dirchooser.DirectoryChooserActivity
import net.rdrei.android.dirchooser.DirectoryChooserConfig


class SubtitleAdjustActivity : AppCompatActivity() {

    var listItems:MutableList<SubtitleAdjust> = mutableListOf()
    lateinit var subtitleAdapter:SubtitleAdjustListAdapter
    lateinit var dir: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_adjust)
        setSupportActionBar(toolbar)

        val dbHelper = DBOpenHelper(this,null)

        // Loading two subtitles. two subtitles is required to find offset and speed.
        val subtitles = dbHelper.search(SubtitleModel,"",2)

        subtitles.forEach {
            listItems.add(SubtitleAdjust(it,Time(it.getStartTime().toString())))
        }

        // Setting the list view adapter
        subtitleAdapter =  SubtitleAdjustListAdapter(this,listItems)

        val listView = findViewById<ListView>(R.id.subtitleList)
        listView?.adapter = subtitleAdapter

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
            // Check read write permissions for the external storage
            val readPermission = PermissionChecker.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            val writePermission = PermissionChecker.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (readPermission == PermissionChecker.PERMISSION_GRANTED&&writePermission == PermissionChecker.PERMISSION_GRANTED) {

                val chooserIntent = Intent(this, DirectoryChooserActivity::class.java)


                val config = DirectoryChooserConfig.builder()
                    .newDirectoryName("Subtitles")
                    .initialDirectory("")
                    .allowReadOnlyDirectory(true)
                    .allowNewDirectoryNameModification(true)
                    .build()

                chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config)

                // REQUEST_DIRECTORY is a constant integer to identify the request, e.g. 0
                startActivityForResult(chooserIntent, REQUEST_DIRECTORY)

            } else {
                val toast = Toast.makeText(this,"This app required read,write permissions for your external storage. Otherwise this app won't work.",Toast.LENGTH_LONG)
                toast.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_DIRECTORY) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                val directory =  data!!.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR)

                this.directorySelected(directory)
            }
        }
    }

    private fun directorySelected(directory: String){

    }

    companion object {
        const val REQUEST_DIRECTORY = 123
    }
}