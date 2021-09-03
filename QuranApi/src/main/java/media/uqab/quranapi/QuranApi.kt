package media.uqab.quranapi

import android.content.Context
import android.os.Looper
import android.util.Log
import media.uqab.quranapi.database.ApiDatabase
import media.uqab.quranapi.database.Content
import media.uqab.quranapi.database.SurahInfo
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
            contents.forEach { verses.add(Verse(it)) }

            val surahInfo = dao.surahInfo(surahNo)
            val surah = Surah(surahNo, surahInfo.name, surahInfo.nameAr, surahInfo.type,verses)

            callback.result(surah)
        }.start()
    }

    fun getSurahInfo(callback: SurahInfoCallback) {
        Thread { callback.result(dao.surahInfo()) }.start()
    }

    fun getByPage(pageNo: Int, callback: PagesCallback) {
        Thread {
            val content = dao.contentByPage(pageNo)
            val verse: MutableList<Verse> = mutableListOf()
            content.forEach { verse.add(Verse(it)) }

            callback.result(listOf(Page(pageNo, verse)))
        }.start()
    }
    fun getBySurah(surahNo: Int, pagesCallback: PagesCallback) {
        Thread {
            val pageNum = dao.getPageNumbersOfSurah(surahNo)
            val pages: MutableList<Page> = mutableListOf()

            for (num in pageNum) {
                Log.d(TAG, "getBySurah: pageNo $num")
                val verses: MutableList<Verse> = mutableListOf()
                val content = dao.contentByPage(num, surahNo)
                content.forEach { verses.add(Verse(it))
                    Log.d(TAG, "getBySurah: content ${it.verseIndo}")
                }

                pages.add(Page(num, verses))
            }

            pagesCallback.result(pages)
        }.start()
    }

    fun interface VerseResultCallback { fun result(verse: Verse) }
    fun interface SurahResultCallback { fun result(surah: Surah) }
    fun interface SurahInfoCallback { fun result(surahList: List<SurahInfo>) }
    fun interface PageCallback { fun result(page: Page) }
    fun interface PagesCallback { fun result(page: List<Page>) }
}