package media.uqab.quranapi.database

data class SurahInfo(
    val surahNo: Int,
    val name: String,
    val nameAr: String,
    val place: String,
    val type: String,
    val verseCount: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SurahInfo) return false

        if (surahNo != other.surahNo) return false
        if (name != other.name) return false
        if (nameAr != other.nameAr) return false
        if (place != other.place) return false
        if (type != other.type) return false
        if (verseCount != other.verseCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = surahNo
        result = 31 * result + name.hashCode()
        result = 31 * result + nameAr.hashCode()
        result = 31 * result + place.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + verseCount
        return result
    }
}
