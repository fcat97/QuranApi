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
    private var surahNo: Int = -1001): Fragment() {
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

        val pageAdapter = PageAdapter()
        binding.recyclerView.adapter = pageAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }

        if (pageNo == -1001 && surahNo != -1001) {
            binding.toolbar.title = "${surahNo}. ${QuranApi.getSurahInfo(surahNo).name}"
            api.getBySurah(surahNo) {
                Log.d(TAG, "onViewCreated: ${it.size}")
//                Thread.sleep(1) // to remove glitch
                pageAdapter.submitPage(it)
            }
        }
        else if (pageNo != -1001 && surahNo == -1001) {
            api.getByPage(pageNo) {
//                Thread.sleep(1) // to remove glitch
                pageAdapter.submitPage(it)
            }
        }
    }
}