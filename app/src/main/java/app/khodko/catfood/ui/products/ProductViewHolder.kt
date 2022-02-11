package app.khodko.catfood.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.khodko.catfood.R
import app.khodko.catfood.api.onliner.ProductResponse
import app.khodko.catfood.core.extension.showExt

class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val productImage: ImageView = view.findViewById(R.id.productImage)
    private val nameView: TextView = view.findViewById(R.id.nameView)
    private val descriptionView: TextView = view.findViewById(R.id.descriptionView)
    private val priceView: TextView = view.findViewById(R.id.priceView)
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
            nameView.text = resources.getString(R.string.loading)
        } else {
            showRepoData(product)
        }
    }

    private fun showRepoData(product: ProductResponse) {
        this.product = product

        nameView.text = product.fullName
        descriptionView.text = product.description

        showPrice(product)
        showImage(product)
    }

    private fun showPrice(product: ProductResponse) {
        product.prices?.let {
            var priceStr = ""
            it.priceMin?.let { p -> priceStr += p.amount + " " + p.currency }
            it.priceMax?.let { p ->
                if (p.amount != it.priceMin?.amount) {
                    priceStr += " - " + p.amount + " " + p.currency
                }
            }
            if (priceStr.isNotEmpty()) {
                it.offers?.count?.let { c ->
                    priceStr += " ($c)"
                }
                priceView.text = priceStr
            }
        }
    }

    private fun showImage(product: ProductResponse) {
        val imageUrl = product.images?.header
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            productImage.showExt("https://$imageUrl")
        } else {
            productImage.setImageResource(R.drawable.ic_image_24)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ProductViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_view_item, parent, false)
            return ProductViewHolder(view)
        }
    }
}
