package com.whizsid.subtitleadjust.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.whizsid.subtitleadjust.R
import com.whizsid.subtitleadjust.lib.Subtitle

class SubtitleAutoSuggestAdapter (context: Context, viewResourceId: Int,subtitles: MutableList<Subtitle>)
    :ArrayAdapter<Subtitle>(context,viewResourceId,subtitles){

    private val subtitleList = subtitles
    private var suggestions: MutableList<Subtitle> = mutableListOf()
    var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    inner class SubtitleFilter : Filter() {


        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var results = FilterResults()

            if(constraint!=null) {
                suggestions.clear()

                subtitleList.forEach {
                    if (it.getContent().toLowerCase().startsWith(constraint.toString().toLowerCase()) && suggestions.size <= 30) {
                        suggestions.add(it)
                    }
                }

                results.values = suggestions
                results.count = suggestions.count()
            }

            return results

        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if(results?.values!= null&&results.count>0){
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }

    private val subtitleFilter = SubtitleFilter()

    override fun getFilter(): Filter {
        return subtitleFilter
    }

    override fun getCount(): Int {
        return suggestions.size
    }

    override fun getItem(position: Int): Subtitle? {
        return suggestions[position]
    }

    override fun getItemId(position: Int): Long {
        return suggestions[position].getIncrementalIndex().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = inflater.inflate(R.layout.subtitle_item,parent,false)

        val contentTextView = item.findViewById<TextView>(R.id.subTitleItemContent)
        val timeTextView = item.findViewById<TextView>(R.id.subtitleItemTime)

        val subtitle = suggestions[position]

        contentTextView.text = subtitle.getContent()
        timeTextView.text = "${subtitle.getStartTime()} - ${subtitle.getEndTime()}"

        return item
    }
}