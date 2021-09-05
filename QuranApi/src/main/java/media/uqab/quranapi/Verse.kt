package media.uqab.quranapi

import android.text.Spanned
import media.uqab.quranapi.database.Content

data class Verse(
    val verseID: Int,
    val verse: String,
    val verseAr: String,
    val verseIndo: String,
    val verseNo: Int,
    val surahNo: Int
) {
    lateinit var spannedIndo: Spanned

    init { ThreadExecutor.execute { spannedIndo = TajweedApi.getTajweedColored(verseIndo) } }

    constructor(content: Content): this(
        content.verseID,
        content.verse,
        content.verseAr,
        content.verseIndo,
        content.verseNo,
        content.surahNo
    )
}