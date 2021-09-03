package media.uqab.quranapi

class Surah (
    val surahNo: Int,
    val name: String,
    val nameAr: String,
    val type: String,
    val verses: List<Verse>
) {
    fun getIndoVerses(): String {
        val stringBuilder = StringBuilder()

        verses.forEach {
            stringBuilder.append(it.verseIndo)
            stringBuilder.append("(${it.verseNo})")
        }
        return stringBuilder.toString()
    }
}

