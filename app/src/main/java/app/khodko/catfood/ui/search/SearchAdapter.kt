package app.khodko.catfood.ui.search

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import app.khodko.catfood.api.onliner.ProductResponse


class SearchAdapter : ListAdapter<ProductResponse, SearchViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<ProductResponse>() {
            override fun areItemsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean =
                oldItem == newItem
        }
    }
}
