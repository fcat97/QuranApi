package media.uqab.apidemo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import media.uqab.apidemo.databinding.ItemAyahBinding
import media.uqab.quranapi.QuranApi
import media.uqab.quranapi.Verse

import android.util.TypedValue
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import media.uqab.tajweedapi.TajweedApi


class VerseAdapter: RecyclerView.Adapter<VerseAdapter.AyahHolder>() {
    private val TAG = "Adapter"
    private var verses: List<Verse> = listOf()
    private var alternateViewColor = -1000
    private var pageTitleColor = -1000
    private val showPageNoAt = mutableListOf<Int>()

    inner class AyahHolder(private val binding: ItemAyahBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(verse: Verse) {
            if (verse.verseNo % 2 == 0) {
                binding.root.setBackgroundColor(alternateViewColor)
            } else binding.root.setBackgroundColor(Color.WHITE)

            binding.pageNo.apply {
                setBackgroundColor(pageTitleColor)
                val pageText = "${resources.getString(R.string.page)} : ${verse.pageIndoNo}"
                text = pageText
                visibility = if (showPageNoAt.contains(adapterPosition)) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            binding.surahInfoLayout.visibility = if (verse.verseNo == 1) {
                when (verse.surahNo){
                    1 -> {
                        binding.taAudh.visibility = View.VISIBLE
                        binding.basmalah.visibility = View.GONE
                    }
                    9 -> {
                        binding.taAudh.visibility = View.VISIBLE
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

            try { binding.verseText.text = verse.spannedIndo }
            catch (ignored: Exception) { binding.verseText.text = TajweedApi.getTajweedColored(verse.verseIndo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAyahBinding.inflate(inflater, parent, false)

        if (alternateViewColor == -1000) {
            val typedValue = TypedValue()
            parent.context.theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)
            val colorPrimary = typedValue.data
            this.alternateViewColor = Color.argb(15, colorPrimary.red, colorPrimary.green, colorPrimary.blue)
            this.pageTitleColor = Color.argb(120, colorPrimary.red, colorPrimary.green, colorPrimary.blue)
        }

        return AyahHolder(binding)
    }

    override fun onBindViewHolder(holder: AyahHolder, position: Int) { holder.bind(verses[position]) }

    override fun getItemCount(): Int { return verses.size }

    private fun pageStartsAt(verses: List<Verse>) {
        var trial = -1
        for (index in verses.indices) {
            if (trial != verses[index].pageIndoNo) {
                showPageNoAt.add(index)
                trial = verses[index].pageIndoNo
            }
        }
    }

    fun submitList(verses: List<Verse>) {
        this.verses = verses
        pageStartsAt(verses)
        notifyDataSetChanged()
    }
}