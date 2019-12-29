package com.whizsid.subtitleadjust.adapters

import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.whizsid.subtitleadjust.R
import com.whizsid.subtitleadjust.lib.SubtitleAdjust
import com.whizsid.subtitleadjust.lib.Time

class SubtitleAdjustListAdapter(pContext: Context,private val dataSource: MutableList<SubtitleAdjust>):BaseAdapter() {

    private var inflater: LayoutInflater = pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val context = pContext

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
        val pickerAdapter = SubtitleAutoSuggestAdapter(context,R.layout.subtitle_item)

        subtitlePicker.setOnItemClickListener { parent, view, position, id ->
            val subtitle = pickerAdapter.getItem(position)
            if(subtitle!==null){
                dataSource[currentPosition].setSubtitle( subtitle)
                dataSource[currentPosition].setTime(subtitle.getStartTime())
                this.notifyDataSetChanged()
            }
        }

        subtitlePicker.setOnKeyListener { v, keyCode, event ->
            // Clearing whole text input with a single backspace or a delete
            if(keyCode==KeyEvent.KEYCODE_DEL||keyCode==KeyEvent.KEYCODE_FORWARD_DEL){
                subtitlePicker.setText("")
            }
            
            true
        }

        subtitlePicker.setText(dataSource[currentPosition].getSubtitle().getContent())

        subtitlePicker.setAdapter(pickerAdapter)

        // Time picker
        val timePicker = rowView.findViewById<EditText>(R.id.timePicker)

        timePicker.setText(dataSource[currentPosition].getTime().toString())

        timePicker.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setTitle("Set the actual start time")
            dialog.setContentView(R.layout.time_picker)

            val okButton = dialog.findViewById<Button>(R.id.timeOK)
            val cancelButton = dialog.findViewById<Button>(R.id.timeCancel)

            val hourPicker = dialog.findViewById<NumberPicker>(R.id.hourPicker)
            val minutePicker = dialog.findViewById<NumberPicker>(R.id.minutePicker)
            val secondPicker = dialog.findViewById<NumberPicker>(R.id.secondPicker)
            val milliPicker = dialog.findViewById<NumberPicker>(R.id.milliPicker)

            hourPicker.maxValue = 23
            hourPicker.minValue = 0
            hourPicker.wrapSelectorWheel = true

            minutePicker.maxValue = 59
            minutePicker.minValue =0
            minutePicker.wrapSelectorWheel = true

            secondPicker.maxValue = 59
            secondPicker.minValue = 0
            secondPicker.wrapSelectorWheel = true

            milliPicker.maxValue = 999
            milliPicker.minValue = 0
            milliPicker.wrapSelectorWheel = true

            val time = dataSource[currentPosition].getTime()

            hourPicker.value = time.getHour()
            minutePicker.value = time.getMinute()
            secondPicker.value = time.getSecond()
            milliPicker.value = time.getMilli()

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            okButton.setOnClickListener{
                val hour = hourPicker.value
                val minute = minutePicker.value
                val second = secondPicker.value
                val milli = milliPicker.value

                val changedTime = milli + second*1000 + minute*1000*60 + hour * 1000 * 3600

                dataSource[currentPosition].setTime(Time(changedTime.toLong()))
                this.notifyDataSetChanged()

                dialog.dismiss()
            }

            dialog.show()
        }

        return rowView
    }
}