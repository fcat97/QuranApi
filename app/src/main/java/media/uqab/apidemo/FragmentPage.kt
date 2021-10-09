package media.uqab.apidemo

import android.os.Bundle
import android.util.Log
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

        val api = QuranApi.getInstance(requireContext())

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager

        if (pageNo == -1001 && surahNo != -1001) {
            val verseAdapter = VerseAdapter()
            binding.recyclerView.adapter = verseAdapter
            binding.toolbar.title = "${surahNo}. ${QuranApi.getSurahInfo(surahNo).name}"

            api.getSurah(surahNo) { verseAdapter.submitList(it.verses) }
        }
        else if (pageNo != -1001 && surahNo == -1001) {
            val pageAdapter = PageAdapter()
            binding.recyclerView.adapter = pageAdapter
            api.getByPage(pageNo) {
                pageAdapter.submitPage(it)
            }
        }

        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }
    }
}