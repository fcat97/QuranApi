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

    /**
     * This is a Blocking Function. Use [getVerseAsync] instead.
     */
    fun getVerse(
        verseIndex: Int
    ): Verse {
        val content = dao.contentByVerseID(verseIndex)
        return Verse(content)
    }

    fun getVerseAsync(
        verseIndex: Int,
        callback: VerseResultCallback
    ) = ThreadExecutor.execute {
        val verse = getVerse(verseIndex)
        mainExe.execute { callback.result(verse) }
    }

    /**
     * This is a Blocking Function. Use [getVerseAsync] instead.
     */
    fun getVerse(
        surahNo: Int,
        verseNo: Int
    ): Verse {
        val content = dao.contentByVerse(surahNo, verseNo)
        return Verse(content)
    }

    fun getVerseAsync(
        surahNo: Int,
        verseNo: Int,
        callback: VerseResultCallback
    ) = ThreadExecutor.execute {
        val verse = getVerse(surahNo, verseNo)
        mainExe.execute { callback.result(verse) }
    }

    /**
     * This is a Blocking Function. Use [getSurahAsync] instead.
     */
    fun getSurah(
        surahNo: Int
    ): Surah {
        val contents = dao.contentBySurah(surahNo)
        val verses = mutableListOf<Verse>()
        contents.forEach { verses.add(Verse(it)) }

        val surahInfo = getSurahInfo(surahNo)
        return Surah(
            surahNo = surahNo,
            name = surahInfo.name,
            nameAr = surahInfo.nameAr,
            type = surahInfo.type,
            verses = verses
        )
    }

    fun getSurahAsync(
        surahNo: Int,
        callback: SurahResultCallback
    ) = ThreadExecutor.execute {
        val surah = getSurah(surahNo)
        mainExe.execute { callback.result(surah) }
    }

    /**
     * This is a Blocking Function. Use [getPageAsync] instead.
     */
    fun getPage(
        pageNo: Int
    ): Page {
        val content = dao.contentByPage(pageNo)
        val verse: MutableList<Verse> = mutableListOf()
        content.forEach { verse.add(Verse(it)) }
        return Page(pageNo, verse)
    }

    fun getPageAsync(
        pageNo: Int,
        callback: PageCallback
    ) = ThreadExecutor.execute {
        val page = getPage(pageNo)
        mainExe.execute { callback.result(page) }
    }

    /**
     * This is a Blocking Function. Use [getBySurahAsync] instead.
     */
    fun getBySurah(
        surahNo: Int
    ): List<Page> {
        val pageNum = dao.getPageNumbersOfSurah(surahNo)
        val pages: MutableList<Page> = mutableListOf()

        for (num in pageNum) {
//                Log.d(TAG, "getBySurahAsync: pageNo $num")
            val verses: MutableList<Verse> = mutableListOf()
            val content = dao.contentByPage(num, surahNo)
            content.forEach { verses.add(Verse(it))
//                    Log.d(TAG, "getBySurahAsync: content ${it.verseIndo}")
            }

            pages.add(Page(num, verses))
        }

        return pages
    }

    fun getBySurahAsync(
        surahNo: Int,
        callback: PageListCallback
    ) = ThreadExecutor.execute {
        val pages = getBySurah(surahNo)

        mainExe.execute { callback.result(pages) }
    }


    fun interface VerseResultCallback { fun result(verse: Verse) }
    fun interface SurahResultCallback { fun result(surah: Surah) }
    fun interface PageCallback { fun result(page: Page) }
    fun interface PageListCallback { fun result(pages: List<Page>) }
}
