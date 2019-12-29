package com.whizsid.subtitleadjust

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.whizsid.subtitleadjust.adapters.SubtitleAdjustListAdapter
import com.whizsid.subtitleadjust.database.DBOpenHelper
import com.whizsid.subtitleadjust.database.SubtitleModel
import com.whizsid.subtitleadjust.lib.SubtitleAdjust
import com.whizsid.subtitleadjust.lib.Time
import kotlinx.android.synthetic.main.app_bar.*
import android.content.Intent
import android.view.View
import android.widget.*
import androidx.core.content.PermissionChecker
import net.rdrei.android.dirchooser.DirectoryChooserActivity
import net.rdrei.android.dirchooser.DirectoryChooserConfig
import java.io.File


class SubtitleAdjustActivity : AppCompatActivity() {

    var listItems:MutableList<SubtitleAdjust> = mutableListOf()
    lateinit var subtitleAdapter:SubtitleAdjustListAdapter
    lateinit var dir: String
    private lateinit var pathDialog:Dialog

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
            this.gotoMainActivity(null)
        }

        val confirmButton = findViewById<Button>(R.id.adjustConfirmButton)

        confirmButton.setOnClickListener {
            this.onConfirmClick()
        }

        pathDialog = Dialog(this)
        // Path dialog
        pathDialog.setTitle("Please pick a file name to save.")
        pathDialog.setContentView(R.layout.path_picker)
        val errorText = pathDialog.findViewById<TextView>(R.id.pathPickerError)
        errorText.visibility = View.INVISIBLE
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

                this.onDirectorySelected(directory)
            }
        }
    }

    private fun onConfirmClick(){
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

    private fun onDirectorySelected(directory: String){
        dir = directory

        if(pathDialog!=null){
            pathDialog.show()

            val okButton = pathDialog.findViewById<Button>(R.id.pathOK)
            val cancelButton = pathDialog.findViewById<Button>(R.id.pathCancel)
            val errorText = pathDialog.findViewById<TextView>(R.id.pathPickerError)

            cancelButton.setOnClickListener {
                pathDialog.hide()
            }

            okButton.setOnClickListener {
                val fileNameInput = pathDialog.findViewById<EditText>(R.id.pathPickerPath)
                val fileName = fileNameInput.text
                val filePath = "$dir/$fileName.srt"

                val file = File(filePath)

                if(!file.exists()){
                    errorText.visibility = View.INVISIBLE
                    pathDialog.hide()

                    this.onFilePicked(file)
                } else {
                    errorText.visibility = View.VISIBLE
                }

            }
        }
    }

    private fun onFilePicked(file: File){
        val dbHelper = DBOpenHelper(this,null)

        val subtitles = dbHelper.search(SubtitleModel,"",null)

        val toast = Toast.makeText(this,"Please wait writing to your file.",Toast.LENGTH_LONG)
        toast.show()

        file.printWriter().use { out ->
            subtitles.forEach {
                val id = it.getId()
                val content = it.getContent()
                val startTime = it.getStartTime()
                val endTime = it.getEndTime()

                out.println(id)
                out.println("$startTime --> $endTime")
                out.println(content)
                out.println("")
            }
        }

        this.gotoMainActivity("Successfully saved your adjusted file!")
    }

    private fun gotoMainActivity(message: String?){
        val newIntent = Intent(this, MainActivity::class.java)
        if(message!=null){
            newIntent.putExtra("message_from_previous",message)
        }
        if(pathDialog!=null){
            pathDialog.dismiss()
        }
        this.startActivity(newIntent)
        finish()
    }

    companion object {
        const val REQUEST_DIRECTORY = 123
    }
}