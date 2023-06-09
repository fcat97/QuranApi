package media.uqab.quranapi

import android.text.Spanned
import media.uqab.quranapi.database.Content
import media.uqab.tajweedapi.TajweedApi

data class Verse(
    /**
     * The verse index in the Quran out of 6236 verses
     */
    val verseID: Int,

    /**
     * International style of verse
     */
    val verse: String,

    /**
     * Arabic style with no Harqat
     */
    val verseAr: String,

    /**
     * IndoPak style of writing
     */
    val verseIndo: String,

    /**
     * Index of Verse in Surah
     */
    val verseNo: Int,

    /**
     * Index of Surah between 1 to 114
     */
    val surahNo: Int,

    /**
     * Page number of this verse in Hafeji(IndoPak) Quran
     */
    val pageIndoNo: Int
) {

    /**
     * Spanned object of IndoPak style verse. Created on separate thread, so use with `try...catch`
     */
    lateinit var spannedIndo: Spanned

    init { ThreadExecutor.executeParallel { spannedIndo = TajweedApi.getTajweedColored(verseIndo) } }

    constructor(content: Content): this(
        content.verseID,
        content.verse,
        content.verseAr,
        content.verseIndo,
        content.verseNo,
        content.surahNo,
        content.pageIndo
    )
}