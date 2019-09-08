package com.app.rewizor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.rewizor.R
import com.app.rewizor.data.model.PublicationCommon
import com.app.rewizor.ui.utils.DatePrinter
import com.app.rewizor.ui.utils.TOPIC
import com.app.rewizor.ui.utils.TagColorProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_publication.view.*
import kotlinx.android.synthetic.main.view_date.view.*
import kotlinx.android.synthetic.main.view_publication_actions.view.*
import kotlinx.android.synthetic.main.view_publication_tag.view.*

class PublicationsAdapter(
    private val topic: TOPIC,
    private val scrollListener: (Int) -> Unit,
    private val clickListener: (String) -> Unit
) : RecyclerView.Adapter<PublicationsAdapter.PublicationViewHolder>() {

    val itemsList: MutableList<PublicationCommon> = mutableListOf()

    fun updateItems(list: List<PublicationCommon>) {
        val startSize = itemsList.size
        itemsList.addAll(itemsList.size, list)
        notifyItemRangeInserted(startSize, itemsList.size)
    }

    fun refreshList() {
        itemsList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder =
        PublicationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_publication, parent, false)
        )

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: PublicationViewHolder, position: Int) {
        holder.bind(itemsList[position], topic.title) {
            clickListener(itemsList[it].guid)
        }
        scrollListener.invoke(holder.adapterPosition)
    }


    inner class PublicationViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: PublicationCommon, topic: String, clickListener: (Int) -> Unit) = with(containerView) {
            setOnClickListener { clickListener.invoke(adapterPosition) }
            title.text = item.name
            val category = item.categoryTitle?.let { " • $it" }
            val titleTxt = topic.toUpperCase().plus(category ?: "")
            publication_tag.text = titleTxt
            item.categoryTitle?.let {
                TagColorProvider.setColorToView(tag_color, it)
            }

            checkType(item)

            item.image.url?.let { setBanner(it) }

            if (item.date != null) {
                publication_item_event_date.text =
                    if (topic == TOPIC.MAIN.title) DatePrinter.simpleDate(item.date)
                    else DatePrinter.getDateForAdapter(item.date, item.end)
            } else {
                start_date.isGone = true
                publication_item_event_date.text = ""
            }

            seen_counter.text = "${item.views}"
            comments_counter.text = "${item.comments}"
            likes_counter.text = "${item.likes}"

            if (item.hasLike) {
                //TODO change color?
            }
        }

        private fun setBanner(url: String) {

            Glide
                .with(containerView)
                .applyDefaultRequestOptions(RequestOptions.centerCropTransform())
                .load(url)
                .into(containerView.image_banner)
        }

        private fun checkType(p: PublicationCommon) {
            with(p) {
                when (topic.title) {
                    TOPIC.MAIN.title -> {
                        containerView.description.isVisible = description != null
                        containerView.description.text = description
                        containerView.addressLayout.isVisible = false
                        containerView.placeLayout.isVisible = false
                    }
                    TOPIC.AFISHA.title -> {
                        containerView.description.isVisible = false
                        containerView.addressLayout.isVisible = true
                        containerView.addressLayout.address.text = p.parentAddress
                        containerView.placeLayout.isVisible = true
                        containerView.placeLayout.place.text = p.parentName
                    }
                    else -> {
                        containerView.description.isVisible = false
                        containerView.placeLayout.isVisible = false
                        containerView.addressLayout.isVisible = false
                    }
                }
            }

        }

    }

    companion object {
        const val NO_TIME = "L-"
        const val WITH_TIME = "LS"
    }
}
