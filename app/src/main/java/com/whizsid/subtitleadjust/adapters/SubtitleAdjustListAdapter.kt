package com.whizsid.subtitleadjust.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.BaseAdapter
import android.widget.EditText
import com.whizsid.subtitleadjust.R
import com.whizsid.subtitleadjust.models.SubtitleAdjust

class SubtitleAdjustListAdapter(private val context: Context,private val dataSource: MutableList<SubtitleAdjust>):BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount():Int {
        return dataSource.size
    }

    override fun getItem(position: Int):Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.subtitle_item,parent,false)

//        val subtitlePicker = rowView.findViewById<AutoCompleteTextView>(R.id.subtitlePicker)
//
//        val timePicker = rowView.findViewById<EditText>(R.id.timePickerTextInput)
//
//        val subtitleAdjust = dataSource[position]



        return rowView
    }

}