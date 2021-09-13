package media.uqab.apidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import media.uqab.quranapi.QuranApi
import media.uqab.quranapi.TajweedApi


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        debug()

        val fragmentSurahList = FragmentSurahList { openFragmentBySurah(it) }
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragmentSurahList)
            .commit();
    }

    private fun debug() {
        val api = QuranApi(this)
        api.getVerse(39, 6) { Log.d(TAG, "debug: $it")}
    }

    private fun openFragmentBySurah(surahNo: Int) {
        val fragmentPage = FragmentPage(surahNo = surahNo)

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.container, fragmentPage)
            .addToBackStack(fragmentPage::class.java.name)
            .commit();
    }

    private fun openFragmentByPage(pageNo: Int) {
        val fragmentPage = FragmentPage(pageNo = pageNo)

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.container, fragmentPage)
            .addToBackStack(fragmentPage::class.java.name)
            .commit();
    }

    var tik = System.currentTimeMillis()
    override fun onBackPressed() {
        val i = supportFragmentManager.backStackEntryCount
        if (i == 0 && System.currentTimeMillis() - tik > 2000) {
            Toast.makeText(this, R.string.exit_warning, Toast.LENGTH_SHORT).show()
            tik = System.currentTimeMillis()
        } else super.onBackPressed()
    }
}