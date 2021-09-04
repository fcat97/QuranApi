package media.uqab.quranapi

import media.uqab.quranapi.database.Content

data class Verse(
    val verseID: Int,
    val verse: String,
    val verseAr: String,
    val verseIndo: String,
    val verseNo: Int,
    val surahNo: Int
) {
    constructor(content: Content): this(
        content.verseID,
        content.verse,
        content.verseAr,
        content.verseIndo,
        content.verseNo,
        content.surahNo
    )
}