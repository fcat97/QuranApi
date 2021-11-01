package media.uqab.quranapi

class Surah (
    val surahNo: Int,
    val name: String,
    val nameAr: String,
    val type: String,
    val verses: List<Verse>
) {

    /**
     * Get all verses As A SINGLE STRING
     * with verse number at the end.
     */
    fun getIndoVerses(): String {
        val stringBuilder = StringBuilder()

        verses.forEach {
            stringBuilder.append(it.verseIndo)
            stringBuilder.append("(${it.verseNo})")
        }
        return stringBuilder.toString()
    }
}

