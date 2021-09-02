package media.uqab.quranapi

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log

/**
 * @author shahriar zaman
 * This is an object to decorate the quranic verses with color tazweed
 * currently this only works for IndoQuran format.
 */
object TajweedApi {
    private val TAG = "TajweedApi"
    private val iqfaa = listOf('ت','ث','ج','د','ذ','ز','س','ش','ص','ض','ط','ظ','ف','ق','ك')
    private val qalqalah = listOf('ق','ط','ب','ج','د')
    private val meem = 'م' // U+0645
    private val nuun = 'ن' // U+0646
    private val baa = 'ب' // U+0628
    private val harqat = listOf('َ','ِ','ُ') // U+064e, U+0650, U+064f
    private val tanween = listOf('ً','ٍ','ٌ') // U+064b, U+064d, U+064c
    private val tashdeed = 'ّ' // U+0651
    private val maddah = 'ٓ' // U+0653
    private val superscriptAlif = 'ٰ' //  U+0670 vertical fatha/খাড়া যবর
    private val subscriptAlif = 'ٖ' //  U+0656 vertical kasra/খাড়া জের
    private val invertedDamma = 'ٗ' // U+0657
    private val sakin = listOf('ۡ','ْ') // U+06e1, U+0652
    private val meem_isolated = 'ۢ' // U+06e2
    private val harf_idgam_withGunnah = listOf('ی','ى','و','م','ن') // U+06cc(farsi ya) U+064a, U+0648, U+0645, U+0646
    private val harf_idgam_withoutGunnah = listOf('ر','ل') // U+0631, U+0644

    var qalqalahColor = Color.GREEN
    var iqfaaColor = Color.RED
    var iqlabColor = Color.BLUE
    var idgamWithGunnahColor = Color.MAGENTA
    var idgamWithOutGunnahColor = Color.LTGRAY
    var wazeebGunnahColor = Color.parseColor("#F07624")

    /**
     * This method only works for indoQuran format
     * IndoQuran is written slightly different than Saudi Quran
     * This format has less character than Saudi one
     *
     * @param verse Verse to decorate with color in IndoQuran Format
     * @return a spanned Object with Tazweed decoration.
     */
    fun getTajweedColored(verse: String): Spanned {
        val spannable = SpannableString(verse)

        Log.d(TAG, "getTajweedColored: ${getNuunSakin()}")

        for (i in verse.indices) {
            Log.d(TAG, "getTajweedColored: ${verse[i]} --> ${toUnicode(verse[i])}")
        }
        applySpan(getWazeebGunnah().toRegex(), verse, wazeebGunnahColor, spannable)
        applySpan(getIqfaaPattern().toRegex(), verse, iqfaaColor, spannable)
        applySpan(getIqalabPattern().toRegex(), verse, iqlabColor, spannable)
        applySpan(getIdgaamWithGunnahPattern().toRegex(), verse, idgamWithGunnahColor, spannable)
        applySpan(getIdgaamWithOutGunnahPattern().toRegex(), verse, idgamWithOutGunnahColor, spannable, endOffset = -3)
        applySpan(getQalqalahPatter().toRegex(), verse, qalqalahColor, spannable)

        return spannable
    }

    private fun getNuunSakin() = buildString {
        this.append("(")
        this.append(nuun)
        this.append("[")
        for (c in sakin) this.append(c)
        this.append("]")
        // tanween also included
        this.append("|")
        this.append("\\p{L}?")
        this.append(tashdeed)
        this.append("?")
        this.append("[")
        for (c in tanween) this.append(c)
        this.append("]")
        this.append(")")
        this.append(" ?")
    }
    private fun getHarqatPattern() = buildString {
        this.append("[")
        for (c in harqat) this.append(c)
        for (c in tanween) this.append(c)
        this.append(superscriptAlif)
        this.append(subscriptAlif)
        this.append(invertedDamma)
        this.append("]?")
    }

    private fun getQalqalahPatter() = buildString {
        this.append("[")
        for (c in qalqalah) this.append(c)
        this.append("]")
        this.append("[")
        for (c in sakin) this.append(c)
        this.append("]")
    }

    private fun getIqfaaPattern() = buildString {
        this.append(getNuunSakin())
        this.append("[")
        for (c in iqfaa) this.append(c)
        this.append("]")
        this.append(getHarqatPattern())
    }

    private fun getIqalabPattern() =  buildString {
        this.append(getNuunSakin())
        this.append(meem_isolated)
        this.append("? ?")
        this.append(baa)
        this.append(getHarqatPattern())
    }

    private fun getIdgaamWithGunnahPattern() = buildString {
        this.append(getNuunSakin())
        this.append("[")
        for (c in harf_idgam_withGunnah) this.append(c)
        this.append("]")
        this.append(tashdeed)
        this.append("?")

        // here we don't use the getHarqatPattern()
        // since U+06cc sometime acts as extra harf which has no gunnah
        this.append("[")
        for (c in harqat) this.append(c)
        for (c in tanween) this.append(c)
        this.append(superscriptAlif)
        this.append(subscriptAlif)
        this.append(invertedDamma)
        this.append("]") // <--- here we don't add '?' i.e. not optional
    }

    private fun getIdgaamWithOutGunnahPattern() = buildString {
        this.append(getNuunSakin())
        this.append("[")
        for (c in harf_idgam_withoutGunnah) this.append(c)
        this.append("]")
        this.append(tashdeed)
        this.append("?")
        this.append(getHarqatPattern())
    }

    private fun getWazeebGunnah() = buildString {
        this.append("[")
        this.append(nuun)
        this.append(meem)
        this.append("]")
        this.append(tashdeed)
        this.append(maddah)
        this.append("?")
        this.append(getHarqatPattern())
    }

    private fun applySpan(regex: Regex,
                          verse: String,
                          color: Int,
                          spannable: Spannable,
                          endOffset: Int = 1,
                          logTag: String = "applySpan") {
        val range = regex.findAll(verse)
        for (r in range) {
            Log.d(TAG, "$logTag: ${verse.subSequence(r.range.first, r.range.last + 2)}")
            spannable.setSpan(ForegroundColorSpan(color),
                r.range.first, r.range.last + endOffset,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    // for debugging only
    fun toUnicode(c: Char): String {
        return String.format("\\%04x", c.toInt())
    }
}