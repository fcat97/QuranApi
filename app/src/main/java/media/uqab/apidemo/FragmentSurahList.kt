package media.uqab.apidemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import media.uqab.apidemo.databinding.FragmentSurahListBinding
import media.uqab.quranapi.QuranApi
import media.uqab.quranapi.ThreadExecutor

class FragmentSurahList(private val listener: ItemClickedListener): Fragment() {
    private lateinit var binding: FragmentSurahListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurahListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SurahListAdapter { listener.onClick(it) }
        binding.recyclerView.adapter = adapter
        requireActivity().title = "QuranApi"

        ThreadExecutor.executeParallel {
            QuranApi.getInstance(requireContext())
                .getAsyncSurahInfoList {
                    ThreadExecutor.runOnUiThread {
                        adapter.submitSurah(it)
                    }
                }
        }
    }

    fun interface ItemClickedListener {
        fun onClick(surahNo: Int)
    }
}