package media.uqab.quranapi.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ApiDao {
    @Query("SELECT * FROM verses_content WHERE surahNo = :surahNo AND verseNo = :verseNo ORDER BY verseNo ASC")
    fun contentByVerse(surahNo: Int, verseNo: Int): Content

    @Query("SELECT * FROM verses_content WHERE surahNo = :surahNo ORDER BY verseNo ASC")
    fun contentBySurah(surahNo: Int): List<Content>

    @Query("SELECT * FROM surah_info_table WHERE surahNo = :surahNo")
    fun surahInfo(surahNo: Int): SurahInfo
}