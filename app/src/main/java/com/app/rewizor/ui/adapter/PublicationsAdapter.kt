package com.app.rewizor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.rewizor.R
import com.app.rewizor.data.model.CommonPublication
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_publication.view.*
import kotlinx.android.synthetic.main.view_date.view.*
import kotlinx.android.synthetic.main.view_publication_actions.view.*
import kotlinx.android.synthetic.main.view_publication_tag.view.*

class PublicationsAdapter(
    private val topic: String
) : RecyclerView.Adapter<PublicationsAdapter.PublicationViewHolder>() {

    private val itemsList: MutableList <CommonPublication> = mutableListOf()

    fun updateItems(list: List<CommonPublication>) {
        val startSize = itemsList.size
        itemsList.addAll(itemsList.size, list)
        notifyItemRangeInserted(startSize, itemsList.size )
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
        holder.bind(itemsList[position], topic)
    }


    class PublicationViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer
    {
        fun bind(item: CommonPublication, topic: String) = with(containerView) {
            title.text = item.name
            val category = item.categoryTitle?.let { " * $it" }
            val titleTxt = topic.toUpperCase().plus(category ?: "")
            publication_tag.text = titleTxt


            description.isVisible = item.description?.isNotEmpty() ?: false
            description.text = item.description

            item.image.url?.let { setBanner(it) }


            if (item.date != null) {
                start_date.isVisible = true
                publication_item_event_date.text = item.date.toString()
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
    }
}
