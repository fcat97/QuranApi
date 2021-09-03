package media.uqab.apidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentSurahList = FragmentSurahList { openFragmentBySurah(it) }
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragmentSurahList)
            .commit();
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