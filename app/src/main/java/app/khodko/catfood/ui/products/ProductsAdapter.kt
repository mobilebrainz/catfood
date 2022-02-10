package app.khodko.catfood.ui.products

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import app.khodko.catfood.api.onliner.ProductResponse


class ProductsAdapter : ListAdapter<ProductResponse, ProductViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
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
