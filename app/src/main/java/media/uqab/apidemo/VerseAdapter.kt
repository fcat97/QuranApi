package media.uqab.apidemo

import android.graphics.Color
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import media.uqab.quranapi.TajweedApi
import media.uqab.quranapi.Verse

class VerseAdapter: RecyclerView.Adapter<VerseAdapter.AyahHolder>() {
    private val TAG = "Adapter"
    private var verses: List<Verse> = listOf()
    private var spannedVerse: MutableList<Spanned> = mutableListOf()
    @Volatile private var cached = false

    class AyahHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val verseNo: TextView = itemView.findViewById(R.id.verseNo)
        val surahInfo: RelativeLayout = itemView.findViewById(R.id.surahInfo)
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

        holder.verseNo.text = verses[position].verseNo.toString()
        holder.surahInfo.visibility = if (verses[position].verseNo == 1) { View.VISIBLE } else { View.GONE }

        val verse = verses[position].verseIndo
        holder.textView.text = TajweedApi.getTajweedColored(verse)
        /*
        if (cached) {
            holder.textView.text = spannedVerse[position]
            Log.d(TAG, "onBindViewHolder: using cached")
        } else {
            Log.d(TAG, "onBindViewHolder: using raw")
            val verse = verses[position].verseIndo
            holder.textView.text = TajweedApi.getTajweedColored(verse)
        }*/
    }

    private fun getItem(position: Int): Spanned {
        return spannedVerse[position]
    }
    override fun getItemCount(): Int {
        return verses.size
    }

    fun submitList(verses: List<Verse>) {
        this.verses = verses
        notifyDataSetChanged()
//        Thread {
//            // make a cache for spannedString.. It will make UI load faster
//            for (verse in verses) { spannedVerse.add(TajweedApi.getTajweedColored(verse.verseIndo)) }
//            cached = true
//        }.start()
    }
}