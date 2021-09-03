package media.uqab.apidemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import media.uqab.quranapi.Page

class PageAdapter: RecyclerView.Adapter<PageAdapter.PageHolder>(){
    private var pageList: List<Page> = listOf()

    private val viewPool = RecyclerView.RecycledViewPool()

    inner class PageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
        private val adapter: VerseAdapter = VerseAdapter()
        private val textView: TextView = itemView.findViewById(R.id.textView)


        init {
            recyclerView.adapter = adapter
            val layoutManager =  object: LinearLayoutManager(recyclerView.context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            layoutManager.initialPrefetchItemCount = 4
            recyclerView.layoutManager = layoutManager
        }

        fun bind(page: Page) {
            val text = "Page: ${page.pageNo}"
            textView.text = text
            adapter.submitList(page.verse)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_page, parent, false)
        return PageHolder(view)
    }

    override fun onBindViewHolder(holder: PageHolder, position: Int) {
        holder.bind(getItem(position))
        holder.recyclerView.setRecycledViewPool(viewPool)
    }

    override fun getItemCount(): Int {
        return pageList.size
    }

    private fun getItem(position: Int): Page {
        return pageList[position]
    }

    fun submitPage(pageList: List<Page>) {
        this.pageList = pageList
        notifyDataSetChanged()
    }
}