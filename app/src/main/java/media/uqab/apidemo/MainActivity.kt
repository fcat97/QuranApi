package media.uqab.apidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import media.uqab.quranapi.QuranApi

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val api = QuranApi(this)
        val textView = findViewById<TextView>(R.id.textView)

        api.getSurah(1) {
            runOnUiThread { textView.text = it.getIndoVerses() }
        }
    }
}