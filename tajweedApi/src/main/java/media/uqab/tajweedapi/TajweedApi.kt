package media.uqab.tajweedapi

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan

/**
 * @author shahriar zaman
 * This is an object to decorate the quranic verses with color tazweed
 * currently this only works for IndoQuran format.
 */
object TajweedApi {
    private const val TAG = "TajweedApi"
    private val iqfaa = listOf('ت','ث','ج','د','ذ','ز','س','ش','ص','ض','ط','ظ','ف','ق','ك','ک') // U+06a9(alt kaaf, keheh)
    private val qalqalah = listOf('ق','ط','ب','ج','د')
    private const val alif = 'ا' // U+0627
    private const val meem = 'م' // U+0645
    private const val nuun = 'ن' // U+0646
    private const val baa = 'ب' // U+0628
    private val harqat = listOf('َ','ِ','ُ') // U+064e, U+0650, U+064f
    private val tanween = listOf('ً','ٍ','ٌ') // U+064b, U+064d, U+064c
    private const val tashdeed = 'ّ' // U+0651
    private const val maddah = 'ٓ' // U+0653
    private const val small_high_maddah = 'ۤ' // U+06e4
    private const val superscriptAlif = 'ٰ' //  U+0670 vertical fatha/খাড়া যবর
    private const val subscriptAlif = 'ٖ' //  U+0656 vertical kasra/খাড়া জের
    private const val invertedDamma = 'ٗ' // U+0657
    private val sakin = listOf('ۡ','ْ') // U+06e1, U+0652
    private val meem_isolated = listOf('ۢ', 'ۭ') // U+06e2, U+06ed
    private val harf_idgam_withGunnah = listOf('ی','ى','و','م','ن') // U+06cc(farsi ya) U+064a, U+0648, U+0645, U+0646
    private val harf_idgam_withoutGunnah = listOf('ر','ل') // U+0631, U+0644
    private val stops = listOf("مـ", "قلى", '\u06da', '\u06d9', '\u06dc','\u06d9', '\u066a', '\u0615')

    private val pattern_nuun_sakin = getNuunSakin().toRegex()
    private val pattern_qalqalah = getQalqalahInMiddlePattern().toRegex()
    private val pattern_qalqalah_stop = getQalqalahInStopPattern().toRegex()
    private val pattern_iqfaa = getIqfaaPattern().toRegex()
    private val pattern_iqlab = getIqlabPattern().toRegex()
    private val pattern_idgaan_wg = getIdgaamWithGunnahPattern().toRegex()
    private val pattern_idgaan_wog = getIdgaamWithOutGunnahPattern().toRegex()
    private val pattern_wazeebGunnah = getWazeebGunnah().toRegex()

    private var qalqalahColor = Color.parseColor("#00aa00")
    private var iqfaaColor = Color.RED
    private var iqlabColor = Color.BLUE
    private var idgamWithGunnahColor = Color.MAGENTA
    private var idgamWithOutGunnahColor = Color.LTGRAY
    private var wazeebGunnahColor = Color.parseColor("#F07624") // orange-dark

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

        //if (!verse.contains("صُمٌّۢ بُکۡم")) return spannable
        // Log.d(TAG, "getTajweedColored: ${getIqlabPattern()}")

        // TODO: this needs too much computation.. So make it fast
        applySpan(pattern_wazeebGunnah, verse, wazeebGunnahColor, spannable, logTag = "wazeebGunnah")
        applySpan(pattern_iqfaa, verse, iqfaaColor, spannable, endOffset = -1, logTag = "iqfaa")
        applySpan(pattern_iqlab, verse, iqlabColor, spannable, logTag = "iqlab")
        applySpan(pattern_idgaan_wg, verse, idgamWithGunnahColor, spannable, logTag = "idgam_wg")
        applySpan(pattern_idgaan_wog, verse, idgamWithOutGunnahColor, spannable, endOffset = -3, logTag = "idgam_wog")
        applySpan(pattern_qalqalah, verse, qalqalahColor, spannable, logTag = "qalqalah")
        applySpan(pattern_qalqalah_stop, verse, qalqalahColor, spannable, endOffset = -1, logTag = "qalqalah_stop")

        return spannable
    }

    private fun getNuunSakin() = buildString {
        this.append('(')
        this.append(nuun)
        this.append('[')
        for (c in sakin) this.append(c)
        this.append(']')
        // tanween also included
        this.append('|')
        this.append("\\p{L}?")
        this.append(tashdeed)
        this.append('?')
        this.append('[')
        for (c in tanween) this.append(c)
        this.append(']')
        this.append(alif)
        this.append('?')
        this.append(')')
        this.append(" ?")
    }

    private fun getHarqatPattern() = buildString {
        this.append('[')
        for (c in harqat) this.append(c)
        for (c in tanween) this.append(c)
        this.append(superscriptAlif)
        this.append(subscriptAlif)
        this.append(invertedDamma)
        this.append("]?")
    }

    private fun getQalqalahInMiddlePattern() = buildString {
        append("([")
        for (c in qalqalah) append(c)
        append(']')
        append('[')
        for (c in sakin) append(c)
        append("])")
    }

    private fun getQalqalahInStopPattern() = buildString {
        append("[")
        for (c in qalqalah) append(c)
        append(']')

        append(tashdeed)
        append('?')

        append('[')
        for (c in harqat) append(c)
        for (c in tanween) append(c)
        append("]")

        append('\u2009') // thin space
        append('?')

        append('(')
        append(stops.joinToString("|"))
        append(')')
    }

    private fun getIqfaaPattern() = buildString {
        this.append(getNuunSakin())
        this.append('[')
        for (c in iqfaa) this.append(c)
        this.append(']')
        this.append(getHarqatPattern())
    }

    private fun getIqlabPattern() =  buildString {
        this.append(getNuunSakin())
        this.append('[')
        for (c in meem_isolated) this.append(c)
        this.append(']')
        this.append("? ?")
        this.append(baa)
        this.append(getHarqatPattern())
    }

    private fun getIdgaamWithGunnahPattern() = buildString {
        this.append(getNuunSakin())
        this.append('[')
        for (c in harf_idgam_withGunnah) this.append(c)
        this.append(']')

        // here we don't use the getHarqatPattern()
        // since `U+06cc` sometime acts as extra harf which has no gunnah
        this.append('[')
        for (c in harqat) this.append(c)
        for (c in tanween) this.append(c)
        this.append(superscriptAlif)
        this.append(subscriptAlif)
        this.append(invertedDamma)
        this.append(']') // <--- here we don't add '?' i.e. it's must, not optional

        this.append(tashdeed)
        this.append('?')
    }

    private fun getIdgaamWithOutGunnahPattern() = buildString {
        this.append(getNuunSakin())
        this.append('[')
        for (c in harf_idgam_withoutGunnah) this.append(c)
        this.append(']')
        this.append(tashdeed)
        this.append('?')
        this.append(getHarqatPattern())
    }

    private fun getWazeebGunnah() = buildString {
        this.append('[')
        this.append(nuun)
        this.append(meem)
        this.append(']')
        this.append(tashdeed)
        this.append(getHarqatPattern())
        this.append(maddah)
        this.append('?')
//        this.append(getHarqatPattern())
    }

    private fun applySpan(regex: Regex,
                          verse: String,
                          color: Int,
                          spannable: Spannable,
                          endOffset: Int = 1,
        /*debug*/ logTag: String = "applySpan") {
        val range = regex.findAll(verse)
        for (r in range) {
            val spans = spannable.getSpans(r.range.first, r.range.last, ForegroundColorSpan::class.java)
            spans.forEach { spannable.removeSpan(it) }
//            Log.d(TAG, "$logTag: ${verse.subSequence(r.range.first, r.range.last)}")
            spannable.setSpan(ForegroundColorSpan(color),
                r.range.first, r.range.last + endOffset,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    // for debugging only
    private fun toUnicode(c: Char): String {
        return String.format("\\%04x", c.code)
    }
}
