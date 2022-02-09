package app.khodko.catfood.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.khodko.catfood.R
import app.khodko.catfood.api.onliner.ProductResponse

class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val name: TextView = view.findViewById(R.id.nameView)
    private val description: TextView = view.findViewById(R.id.descriptionView)
    private var product: ProductResponse? = null

    init {
        /*
        view.setOnClickListener {
            product?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
         */
    }

    fun bind(product: ProductResponse?) {
        if (product == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            description.visibility = View.GONE
        } else {
            showRepoData(product)
        }
    }

    private fun showRepoData(product: ProductResponse) {
        this.product = product

        name.text = product.name
        description.text = product.description
        description.visibility = View.VISIBLE
    }

    companion object {
        fun create(parent: ViewGroup): SearchViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.onliner_view_item, parent, false)
            return SearchViewHolder(view)
        }
    }
}
