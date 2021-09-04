package media.uqab.quranapi

import android.content.Context
import android.util.Log
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


class QuranApi(context: Context) {
    private val dao = ApiDatabase.getInstance(context).apiDao

    companion object {
        private const val TAG = "QuranApi"
        private const val reciteProfileFileName = "reciteProfile"
        private lateinit var api: QuranApi
        private lateinit var infoList: List<SurahInfo>
        private lateinit var reciteProfile: ReciteProfile

        fun getInstance(context: Context): QuranApi {
            if (! this::api.isInitialized) { api = QuranApi(context) }

            if (! this::reciteProfile.isInitialized) {
                val fileDir = context.getExternalFilesDirs("data").toString()
                val file = File(fileDir, reciteProfileFileName)
                reciteProfile = if (file.exists()) {
                    val gson = Gson()
                    val type = object : TypeToken<ReciteProfile>(){}.type
                    gson.fromJson(file.readText(), type)
                } else ReciteProfile()
            }

            return api
        }

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

        fun saveReciteProfile(context: Context,
                              verseID: Int,
                              profile: ReciteProfile.Profile) {
            reciteProfile.setProfile(verseID, profile)
            val fileDir = context.getExternalFilesDir("data").toString()
            val file = File(fileDir, reciteProfileFileName)
            ThreadExecutor.execute {
                val gson = Gson()
                val type = object : TypeToken<ReciteProfile>(){}.type
                val json = gson.toJson(reciteProfile, type)
                file.writeText(json, StandardCharsets.UTF_8)
            }
        }
    }

    fun getVerse(surahNo: Int, verseNo: Int, callback: VerseResultCallback) {
        ThreadExecutor.execute {
            val content = dao.contentByVerse(surahNo, verseNo)
            callback.result(Verse(content))
        }
    }
    fun getSurah(surahNo: Int, callback: SurahResultCallback) {
        ThreadExecutor.execute {
            val contents = dao.contentBySurah(surahNo)
            val verses = mutableListOf<Verse>()
            contents.forEach { verses.add(Verse(it)) }

            val surahInfo = dao.surahInfo(surahNo)
            val surah = Surah(surahNo, surahInfo.name, surahInfo.nameAr, surahInfo.type,verses)

            callback.result(surah)
        }
    }

    fun getByPage(pageNo: Int, callback: PageCallback) {
        ThreadExecutor.execute {
            val content = dao.contentByPage(pageNo)
            val verse: MutableList<Verse> = mutableListOf()
            content.forEach { verse.add(Verse(it)) }

            callback.result(listOf(Page(pageNo, verse)))
        }
    }
    fun getBySurah(surahNo: Int, pageCallback: PageCallback) {
        ThreadExecutor.execute {
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

            pageCallback.result(pages)
        }
    }

    fun interface VerseResultCallback { fun result(verse: Verse) }
    fun interface SurahResultCallback { fun result(surah: Surah) }
    fun interface PageCallback { fun result(page: List<Page>) }
}
