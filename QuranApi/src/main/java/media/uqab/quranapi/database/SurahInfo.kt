package media.uqab.quranapi.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surah_info_table")
data class SurahInfo(
    @PrimaryKey
    val surahNo: Int,
    val name: String,
    val nameAr: String,
    val place: String,
    val type: String,
    val verseCount: Int
)
