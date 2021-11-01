package media.uqab.quranapi.database

data class SurahInfo(
    val surahNo: Int,
    val name: String,
    val nameAr: String,
    val place: String,
    val type: String,
    val verseCount: Int
)
