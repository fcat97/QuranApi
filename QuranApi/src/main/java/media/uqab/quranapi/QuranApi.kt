package media.uqab.quranapi

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import media.uqab.quranapi.database.ApiDatabase
import media.uqab.quranapi.database.SurahInfo
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets


class QuranApi private constructor(context: Context) {
    private val dao = ApiDatabase.getInstance(context).apiDao
    private val mainExe = ContextCompat.getMainExecutor(context)

    companion object: SingletonHolder<QuranApi, Context>(::QuranApi) {
        private const val TAG = "QuranApi"
        private lateinit var infoList: List<SurahInfo>

        /**
         * Get Information about all the Surah
         *
         * NOTE: This will block the thread for the first time.
         *
         * USE [getAsyncSurahInfoList] instead if needed.
         */
        fun getSurahInfoList(): List<SurahInfo> {
            if (! this::infoList.isInitialized) {
                val gson = Gson()
                val str: InputStream? = QuranApi::class.java.getResourceAsStream("/assets/surah_info.json")
                str?.let {
                    val isr = InputStreamReader(str, StandardCharsets.UTF_8)
                    val type: Type = object : TypeToken<ArrayList<SurahInfo>>(){}.type
                    infoList = gson.fromJson(isr, type)
                }
            }
            return infoList
        }

        /**
         * Get information about a surah
         * This will block calling thread for the first time
         */
        fun getSurahInfo(surahNo: Int): SurahInfo {
            return getSurahInfoList()[surahNo - 1]
        }
    }

    /**
     * Returns List of all [SurahInfo] asynchronously
     */
    fun getAsyncSurahInfoList(
        result: (info: List<SurahInfo>) -> Unit
    ) = ThreadExecutor.execute {
        val ll = getSurahInfoList()
        mainExe.execute { result(ll) }
    }

    fun getVerse(
        verseIndex: Int,
        callback: VerseResultCallback
    ) = ThreadExecutor.execute {
        val content = dao.contentByVerseID(verseIndex)
        val verse = Verse(content)

        mainExe.execute { callback.result(verse) }
    }

    fun getVerse(
        surahNo: Int,
        verseNo: Int,
        callback: VerseResultCallback
    ) = ThreadExecutor.execute {
        val content = dao.contentByVerse(surahNo, verseNo)

        mainExe.execute { callback.result(Verse(content)) }
    }


    fun getSurah(
        surahNo: Int,
        callback: SurahResultCallback
    ) = ThreadExecutor.execute {
        val contents = dao.contentBySurah(surahNo)
        val verses = mutableListOf<Verse>()
        contents.forEach { verses.add(Verse(it)) }

        val surahInfo = getSurahInfo(surahNo)
        Log.d(TAG, "getSurah: $surahInfo")
        val surah = Surah(
            surahNo = surahNo,
            name = surahInfo.name,
            nameAr = surahInfo.nameAr,
            type = surahInfo.type,
            verses = verses
        )

        mainExe.execute { callback.result(surah) }
    }


    fun getPage(
        pageNo: Int,
        callback: PageCallback
    ) = ThreadExecutor.execute {
        val content = dao.contentByPage(pageNo)
        val verse: MutableList<Verse> = mutableListOf()
        content.forEach { verse.add(Verse(it)) }

        mainExe.execute { callback.result(Page(pageNo, verse)) }
    }


    fun getBySurah(
        surahNo: Int,
        callback: PageListCallback
    ) = ThreadExecutor.execute {
        val pageNum = dao.getPageNumbersOfSurah(surahNo)
        val pages: MutableList<Page> = mutableListOf()

        for (num in pageNum) {
//                Log.d(TAG, "getBySurah: pageNo $num")
            val verses: MutableList<Verse> = mutableListOf()
            val content = dao.contentByPage(num, surahNo)
            content.forEach { verses.add(Verse(it))
//                    Log.d(TAG, "getBySurah: content ${it.verseIndo}")
            }

            pages.add(Page(num, verses))
        }

        mainExe.execute { callback.result(pages) }
    }


    fun interface VerseResultCallback { fun result(verse: Verse) }
    fun interface SurahResultCallback { fun result(surah: Surah) }
    fun interface PageCallback { fun result(page: Page) }
    fun interface PageListCallback { fun result(pages: List<Page>) }
}
