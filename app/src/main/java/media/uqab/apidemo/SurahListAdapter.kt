package media.uqab.apidemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import media.uqab.quranapi.QuranApi
import media.uqab.quranapi.database.SurahInfo

class SurahListAdapter(val listener: ItemClickListener): RecyclerView.Adapter<SurahListAdapter.SurahHolder>() {
    private var surahInfo: List<SurahInfo> = listOf()

    class SurahHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var surahName: TextView = itemView.findViewById(R.id.name)
        var surahNo: TextView = itemView.findViewById(R.id.index)
        var info: TextView = itemView.findViewById(R.id.info)
        var calligraphy: TextView = itemView.findViewById(R.id.calligraphyName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_surah, parent, false)
        return SurahHolder(view)
    }

    override fun onBindViewHolder(holder: SurahHolder, position: Int) {
        val surahInfo = surahInfo[position]

        holder.surahName.text = surahInfo.name
        holder.surahNo.text = (surahInfo.surahNo).toString()
        val info = "Verses ${surahInfo.verseCount}. ${surahInfo.type}"
        holder.info.text = info
        holder.calligraphy.text = QuranApi.getSurahInfo(surahInfo.surahNo).nameAr

        holder.itemView.setOnClickListener { listener.onClick(surahInfo.surahNo) }
    }

    override fun getItemCount(): Int {
        return surahInfo.size
    }

    fun submitSurah(surahInfo: List<SurahInfo>) {
        this.surahInfo = surahInfo
        notifyDataSetChanged()
    }

    fun interface ItemClickListener {
        fun onClick(surahNo: Int)
    }
}