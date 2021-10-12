package media.uqab.apidemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import media.uqab.apidemo.databinding.FragmentPageBinding
import media.uqab.quranapi.QuranApi

class FragmentPage(
    private val pageNo: Int = -1001,
    private var surahNo: Int = -1001
): Fragment() {
    private lateinit var binding: FragmentPageBinding
    private val TAG = "FragmentPage"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get Api
        val api = QuranApi.getInstance(requireContext())

        // Set Layout Manger
        // can also be set by xml
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        // init & set adapter
        val verseAdapter = VerseAdapter()
        binding.recyclerView.adapter = verseAdapter

        // if surahNo passed as arg
        if (pageNo == -1001 && surahNo != -1001) {
            // set title
            binding.toolbar.title = "${surahNo}. ${QuranApi.getSurahInfo(surahNo).name}"

            // submit surah to adapter
            api.getSurah(surahNo) { verseAdapter.submitList(it.verses) }
        }
        // if page is passed
        else if (pageNo != -1001 && surahNo == -1001) {
            // set verse from page to adapter
            api.getPage(pageNo) { verseAdapter.submitList(it.verses) }
        }

        // return to parent fragment on back button clicked
        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }
    }
}