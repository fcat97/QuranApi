package media.uqab.quranapi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verses_content")
data class Content(
    @PrimaryKey val verseID: Int,
    val page: Int,
    @ColumnInfo(name = "page_indo") val pageIndo: Int,
    val juz: Int,
    val surahNo: Int,
    val verseNo: Int,
    @ColumnInfo(name = "verse_ar") val verseAr: String,
    @ColumnInfo(name = "verse_indo") val verseIndo: String,
    val verse: String
)
