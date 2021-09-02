package media.uqab.apidemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import media.uqab.apidemo.databinding.FragmentSurahBinding
import media.uqab.quranapi.QuranApi

class FragmentSurah(private val surahNo: Int): Fragment() {
    private lateinit var binding: FragmentSurahBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSurahBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val api = QuranApi(requireContext())
        val adapter = Adapter()
        binding.recyclerView.adapter = adapter

        api.getSurah(surahNo) { requireActivity().runOnUiThread { adapter.submitList(it.verses) } }
    }


}