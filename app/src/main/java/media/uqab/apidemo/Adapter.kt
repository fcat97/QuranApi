package media.uqab.apidemo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import media.uqab.quranapi.Verse

class Adapter: RecyclerView.Adapter<Adapter.AyahHolder>() {
    private var verses: List<Verse> = listOf()

    class AyahHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
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

        holder.textView.text = getItem(position).verseIndo
    }

    private fun getItem(position: Int): Verse {
        return verses[position]
    }
    override fun getItemCount(): Int {
        return verses.size
    }

    fun submitList(verses: List<Verse>) {
        this.verses = verses
        notifyDataSetChanged()
    }
}