package media.uqab.apidemo

import android.graphics.Color
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import media.uqab.apidemo.databinding.ItemAyahBinding
import media.uqab.quranapi.QuranApi
import media.uqab.quranapi.TajweedApi
import media.uqab.quranapi.Verse

import android.util.TypedValue
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red


class VerseAdapter: RecyclerView.Adapter<VerseAdapter.AyahHolder>() {
    private val TAG = "Adapter"
    private var verses: List<Verse> = listOf()
    private var spannedVerse: MutableList<Spanned> = mutableListOf()
    private lateinit var quranApi: QuranApi
    @Volatile private var cached = false

    inner class AyahHolder(private val binding: ItemAyahBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(verse: Verse) {
            if (verse.verseNo % 2 == 0) {
                val typedValue = TypedValue()
                binding.root.context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
                val colorPrimary = typedValue.data
                val color = Color.argb(15, colorPrimary.red, colorPrimary.green, colorPrimary.blue)
                binding.root.setBackgroundColor(color)
            } else binding.root.setBackgroundColor(Color.WHITE)

            binding.surahInfoLayout.visibility = if (verse.verseNo == 1) {
                when (verse.surahNo){
                    1 -> {
                        binding.taAudh.visibility = View.VISIBLE
                        binding.basmalah.visibility = View.GONE
                    }
                    9 -> {
                        binding.taAudh.visibility = View.GONE
                        binding.basmalah.visibility = View.GONE
                    }
                    else -> {
                        binding.taAudh.visibility = View.GONE
                        binding.basmalah.visibility = View.VISIBLE
                    }
                }
                View.VISIBLE
            } else View.GONE


            binding.surahName.text = QuranApi.getSurahInfo(verse.surahNo).nameAr
            binding.verseNo.text = verse.verseNo.toString()
            binding.textView.text = TajweedApi.getTajweedColored(verse.verseIndo)

            /*if (cached) {
                binding.textView.text = spannedVerse[adapterPosition]
                Log.d(TAG, "onBindViewHolder: using cached")
            } else {
                Log.d(TAG, "onBindViewHolder: using raw")
                binding.textView.text = TajweedApi.getTajweedColored(verse.verseIndo)
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAyahBinding.inflate(inflater, parent, false)
        quranApi = QuranApi.getInstance(parent.context) // singleton so no problem
        return AyahHolder(binding)
    }

    override fun onBindViewHolder(holder: AyahHolder, position: Int) { holder.bind(verses[position]) }

    private fun getItem(position: Int): Spanned { return spannedVerse[position] }

    override fun getItemCount(): Int { return verses.size }

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