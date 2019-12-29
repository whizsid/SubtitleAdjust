package com.whizsid.subtitleadjust

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.whizsid.subtitleadjust.threads.SubtitleReadThread
import info.abdolahi.CircularMusicProgressBar
import info.abdolahi.OnCircularSeekBarChangeListener

import kotlinx.android.synthetic.main.app_bar.*
import java.io.BufferedReader
import java.io.InputStreamReader
import android.net.Uri


class MainActivity : AppCompatActivity() {

    lateinit var fileChooseIcon: CircularMusicProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Binding action to file choosing icon
        fileChooseIcon = findViewById(R.id.fileSelector)

        fileChooseIcon.setValue((0).toFloat())

        fileChooseIcon.setOnCircularBarChangeListener(object: OnCircularSeekBarChangeListener{
            override fun onClick(circularBar: CircularMusicProgressBar?) {
                this@MainActivity.onClickFileChooseIcon()
            }

            override fun onLongPress(circularBar: CircularMusicProgressBar?) {
            }

            override fun onProgressChanged(
                circularBar: CircularMusicProgressBar?,
                progress: Int,
                fromUser: Boolean
            ) {
            }
        })

        val message = intent.extras?.get("message_from_previous")

        if(message!=null){
            val toast = Toast.makeText(this,message as String,Toast.LENGTH_LONG)
            toast.show()
        }

        // If some one opened the file via file explorer

        val action = intent.action

        if (Intent.ACTION_VIEW == action) {
            val uri = intent.data

            if(uri!=null){
                this.onFileSelected(uri)
            }
        }


    }

    /**
     * This function is calling after the file icon clicked
     */
    private fun onClickFileChooseIcon(){
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
                this.onFileSelected(selectedFile)
            }

        }

    }

    private fun onFileSelected(selectedFile:Uri){
        // Creating a read buffer from the Uri
        val reader = BufferedReader(InputStreamReader(contentResolver.openInputStream(selectedFile)))

        val activityToast = Toast.makeText(this,"Please wait! Reading your file",Toast.LENGTH_LONG)
        activityToast.show()

        // Read the file in an another thread. Reader will taking a longer time.
        val readThread = SubtitleReadThread(this,reader)

        AsyncTask.execute(readThread)
    }
}
