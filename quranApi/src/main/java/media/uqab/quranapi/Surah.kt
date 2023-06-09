package media.uqab.quranapi

class Surah (
    /**
     * Index of Surah
     */
    val surahNo: Int,

    /**
     * Surah name in English
     */
    val name: String,

    /**
     * Surah name in Arabic
     */
    val nameAr: String,

    /**
     * Makkiah or Madaniyah
     */
    val type: String,

    /**
     * [Verse]s of this surah
     */
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

