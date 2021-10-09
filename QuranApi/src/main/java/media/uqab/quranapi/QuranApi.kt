package media.uqab.quranapi

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import media.uqab.quranapi.database.ApiDatabase
import media.uqab.quranapi.database.SurahInfo

import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import media.uqab.quranapi.database.ReciteProfile
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets


class QuranApi private constructor(context: Context) {
    private val dao = ApiDatabase.getInstance(context).apiDao
    private val mainExe = ContextCompat.getMainExecutor(context)

//    companion object: SingletonHolder<QuranApi, Context>(::QuranApi)

    companion object: SingletonHolder<QuranApi, Context>(::QuranApi) {
        private const val TAG = "QuranApi"
        private lateinit var infoList: List<SurahInfo>

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

        fun getSurahInfo(surahNo: Int): SurahInfo {
            return getSurahInfoList()[surahNo - 1]
        }
    }

    fun getVerse(verseIndex: Int, callback: VerseResultCallback) {
        ThreadExecutor.execute {
            val content = dao.contentByVerseID(verseIndex)
            val verse = Verse(content)
            mainExe.execute { callback.result(verse) }
        }
    }

    fun getVerse(surahNo: Int, verseNo: Int, callback: VerseResultCallback) {
        ThreadExecutor.execute {
            val content = dao.contentByVerse(surahNo, verseNo)
            mainExe.execute { callback.result(Verse(content)) }
        }
    }

    fun getSurah(surahNo: Int, callback: SurahResultCallback) {
        ThreadExecutor.execute {
            val contents = dao.contentBySurah(surahNo)
            val verses = mutableListOf<Verse>()
            contents.forEach { verses.add(Verse(it)) }

            val surahInfo = dao.surahInfo(surahNo)
            val surah = Surah(surahNo, surahInfo.name, surahInfo.nameAr, surahInfo.type,verses)

            mainExe.execute { callback.result(surah) }
        }
    }

    fun getByPage(pageNo: Int, callback: PageCallback) {
        ThreadExecutor.execute {
            val content = dao.contentByPage(pageNo)
            val verse: MutableList<Verse> = mutableListOf()
            content.forEach { verse.add(Verse(it)) }

            mainExe.execute {
                callback.result(listOf(Page(pageNo, verse)))
            }
        }
    }

    fun getBySurah(surahNo: Int, pageCallback: PageCallback) {
        ThreadExecutor.execute {
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

            mainExe.execute {
                pageCallback.result(pages)
            }
        }
    }

    fun interface VerseResultCallback { fun result(verse: Verse) }
    fun interface SurahResultCallback { fun result(surah: Surah) }
    fun interface PageCallback { fun result(page: List<Page>) }
}
