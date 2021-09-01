package media.uqab.apidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import media.uqab.quranapi.QuranApi

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val api = QuranApi(this)
        val recyclerView = findViewById<RecyclerView>(R.id.textView)
//        val surahNameAr = findViewById<TextView>(R.id.surahNameAr)
//        val surahNameEn = findViewById<TextView>(R.id.surahName)
        val adapter = Adapter()
        recyclerView.adapter = adapter

        api.getSurah(2) {
            runOnUiThread {
//                surahNameAr.text = it.nameAr
//                surahNameEn.text = it.name
                adapter.submitList(it.verses)
            }
        }
    }
}