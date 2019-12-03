package com.whizsid.subtitleadjust.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.BaseAdapter
import com.whizsid.subtitleadjust.R
import com.whizsid.subtitleadjust.lib.Subtitle
import com.whizsid.subtitleadjust.lib.SubtitleAdjust

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
        val rowView = inflater.inflate(R.layout.subtitle_adjust_item,parent,false)

        var subtitlePicker = rowView.findViewById<AutoCompleteTextView>(R.id.subtitlePicker)
        val pickerAdapter = SubtitleAutoSuggestAdapter(context,R.layout.subtitle_item,subtitleList)


        subtitlePicker.setOnItemClickListener { parent, view, position, id ->
            val subtitle = pickerAdapter.getItem(position)
            if(subtitle!==null){
                dataSource[currentPosition].setSubtitle( subtitleList[  subtitle.getIncrementalIndex()])
            }
        }

        subtitlePicker.setText(dataSource[currentPosition].getSubtitle().toString())


        subtitlePicker.setAdapter(pickerAdapter)



//
//
//        val timePicker = rowView.findViewById<EditText>(R.id.timePickerTextInput)
//
//        val subtitleAdjust = dataSource[position]



        return rowView
    }

    fun setSubtitles(subtitles:MutableList<Subtitle>):Unit{
        subtitleList = subtitles
    }
}