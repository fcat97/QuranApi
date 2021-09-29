package media.uqab.quranapi.database

import androidx.room.*

@Dao
interface ApiDao {
    @Query("SELECT * FROM verses_content WHERE verseID IS :verseID")
    fun contentByVerseID(verseID: Int): Content

    @Query("SELECT * FROM verses_content WHERE surahNo = :surahNo AND verseNo = :verseNo ORDER BY verseID ASC")
    fun contentByVerse(surahNo: Int, verseNo: Int): Content

    @Query("SELECT * FROM verses_content WHERE surahNo = :surahNo ORDER BY verseID ASC")
    fun contentBySurah(surahNo: Int): List<Content>

    @Query("SELECT * FROM verses_content WHERE page_indo = :pageNo ORDER BY verseID ASC")
    fun contentByPage(pageNo: Int): List<Content>

    @Query("SELECT * FROM verses_content WHERE page_indo = :pageNo AND surahNo = :surahNo ORDER BY verseID ASC")
    fun contentByPage(pageNo: Int, surahNo: Int): List<Content>

    @Query("SELECT DISTINCT page_indo FROM verses_content WHERE surahNo = :surahNo ORDER BY verseID ASC")
    fun getPageNumbersOfSurah(surahNo: Int): List<Int>

    @Query("SELECT * FROM surah_info_table WHERE surahNo = :surahNo")
    fun surahInfo(surahNo: Int): SurahInfo

    @Query("SELECT * FROM surah_info_table ORDER BY surahNo ASC")
    fun surahInfo(): List<SurahInfo>
}