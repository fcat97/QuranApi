package media.uqab.apidemo

import android.graphics.Color
import android.text.Spanned
import android.text.SpannedString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import media.uqab.quranapi.TajweedApi
import media.uqab.quranapi.Verse
import kotlin.text.StringBuilder

class Adapter: RecyclerView.Adapter<Adapter.AyahHolder>() {
    private val TAG = "Adapter"
    private var verses: List<Verse> = listOf()
    private var spannedVerse: MutableList<Spanned> = mutableListOf()

    class AyahHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val verseNo: TextView = itemView.findViewById(R.id.verseNo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_ayah, parent, false)
        return AyahHolder(view)
    }

    override fun onBindViewHolder(holder: AyahHolder, position: Int) {
        if (position % 2 == 0) {
            val color = ContextCompat.getColor(holder.itemView.context, R.color.lightColor)
            holder.itemView.setBackgroundColor(color)
        } else holder.itemView.setBackgroundColor(Color.WHITE)

        holder.verseNo.text = (position + 1).toString()

        val verse = verses[position].verse
        holder.textView.text = TajweedApi.getTajweedColored(verse)
    }

    private fun getItem(position: Int): Spanned {
        return spannedVerse[position]
    }
    override fun getItemCount(): Int {
        return verses.size
    }

    fun submitList(verses: List<Verse>) {
        this.verses = verses
//        for (verse in verses) { spannedVerse.add(TajweedApi.getTajweedColored(verse.verseIndo)) }
        notifyDataSetChanged()
    }
}