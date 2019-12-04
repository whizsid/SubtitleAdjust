package com.whizsid.subtitleadjust.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.BaseAdapter
import android.widget.EditText
import com.whizsid.subtitleadjust.R
import com.whizsid.subtitleadjust.lib.Subtitle
import com.whizsid.subtitleadjust.lib.SubtitleAdjust
import com.whizsid.subtitleadjust.lib.Time

class SubtitleAdjustListAdapter(pContext: Context,private val dataSource: MutableList<SubtitleAdjust>):BaseAdapter() {

    var inflater: LayoutInflater = pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val context = pContext

    private var subtitleList:MutableList<Subtitle> = mutableListOf()

    override fun getCount():Int {
        return dataSource.size
    }

    override fun getItem(position: Int):Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(currentPosition: Int, convertView: View?, parent: ViewGroup?): View {
        // Getting a subtitle adjust row
        val rowView = inflater.inflate(R.layout.subtitle_adjust_item,parent,false)

        // Subtitle picker
        var subtitlePicker = rowView.findViewById<AutoCompleteTextView>(R.id.subtitlePicker)
        val pickerAdapter = SubtitleAutoSuggestAdapter(context,R.layout.subtitle_item,subtitleList)

        subtitlePicker.setOnItemClickListener { parent, view, position, id ->
            val subtitle = pickerAdapter.getItem(position)
            if(subtitle!==null){
                dataSource[currentPosition].setSubtitle( subtitleList[  subtitle.getIncrementalIndex()])
            }
        }

        subtitlePicker.setOnKeyListener { v, keyCode, event ->
            // Clearing whole text input with a single backspace or a delete
            if(keyCode==KeyEvent.KEYCODE_DEL||keyCode==KeyEvent.KEYCODE_FORWARD_DEL){
                subtitlePicker.setText("")
            }
            
            true
        }

        subtitlePicker.setText(dataSource[currentPosition].getSubtitle().toString())

        subtitlePicker.setAdapter(pickerAdapter)

        // Time picker
        val timePicker = rowView.findViewById<EditText>(R.id.timePicker)

        timePicker.setText(dataSource[currentPosition].getTime().toString())

        timePicker.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s!=null){
                    val text = s.toString()

                    val validated = Time.validateString(text)

                    if(validated){
                        dataSource[currentPosition].setTime(Time(text))
                    }

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        return rowView
    }

    fun setSubtitles(subtitles:MutableList<Subtitle>):Unit{
        subtitleList = subtitles
    }
}