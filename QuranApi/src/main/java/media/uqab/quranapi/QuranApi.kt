package media.uqab.quranapi

import android.content.Context
import android.os.Looper
import android.util.Log
import media.uqab.quranapi.database.ApiDatabase
import media.uqab.quranapi.database.Content
import java.util.concurrent.Executors

class QuranApi(context: Context) {
    private val TAG = "QuranApi"
    private val dao = ApiDatabase.getInstance(context).apiDao

    fun getVerse(surahNo: Int, verseNo: Int, callback: VerseResultCallback) {
        Thread {
            val content = dao.contentByVerse(surahNo, verseNo)
            callback.result(Verse(content.verse, content.verseAr, content.verseIndo, content.verseNo, content.surahNo))
        }.start()
    }

    fun getSurah(surahNo: Int, callback: SurahResultCallback) {
        Thread {
            val contents = dao.contentBySurah(surahNo)
            val verses = mutableListOf<Verse>()
            contents.forEach { verses.add(Verse(it.verse, it.verseAr, it.verseIndo, it.verseNo, it.surahNo)) }

            val surahInfo = dao.surahInfo(surahNo)
            val surah = Surah(surahNo, surahInfo.name, surahInfo.nameAr, surahInfo.type,verses)

            callback.result(surah)
        }.start()
    }

    fun interface VerseResultCallback { fun result(verse: Verse) }
    fun interface SurahResultCallback { fun result(surah: Surah) }
}