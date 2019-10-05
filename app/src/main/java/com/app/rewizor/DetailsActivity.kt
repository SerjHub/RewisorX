package com.app.rewizor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.app.rewizor.data.model.PublicationDetailed
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.ui.custom.PublicationTextItem
import com.app.rewizor.ui.utils.DatePrinter
import com.app.rewizor.ui.utils.TOPIC
import com.app.rewizor.ui.utils.TagColorProvider
import com.app.rewizor.viewmodel.PublicationViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_publication.*
import kotlinx.android.synthetic.main.view_publication_actions.*
import kotlinx.android.synthetic.main.view_publication_tag.*
import kotlinx.android.synthetic.main.view_text_info.view.*
import org.joda.time.format.DateTimeFormat
import org.koin.android.ext.android.inject
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private val publicationViewModel: PublicationViewModel by inject()
    lateinit var topic: TOPIC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publication)
        dirtyUiFix()
        getExtras()
        onViewPrepared(publicationViewModel)

        //     window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setToolbar()
    }

    private fun dirtyUiFix() {
        messagesActionIcon.isVisible = false
        comments_counter.isVisible = false
    }

    private fun setToolbar() {
        setSupportActionBar(publicationToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        publicationToolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun getExtras() {
        intent.getStringExtra(ID_INTENT_KEY)
            .also {
                publicationViewModel.setPublicationId(it)
            }
        topic = intent.getStringExtra(PARENT_INTENT_KEY)
            .let { parent ->
                TOPIC.valueOf(parent)
            }
    }

    private fun onViewPrepared(vm: PublicationViewModel) {
        with(vm) {
            publicationModelLiveData.observeViewModel(this@DetailsActivity) {
                showPublication(it)
            }
        }
        vm.onViewCreated()
    }

    private fun showPublication(p: PublicationDetailed) {
        with(p) {

            setDate(p)
            setUnderGrounds(p)
            setActions(p)
            setParent(p)
            setColor(p)
            setDescription(p)
            setPlace(p)

            name?.let { this@DetailsActivity.name.text = it }
            (address ?: parentAddress)?.let { this@DetailsActivity.address.setContent(it) }


            website?.let { setLink(this@DetailsActivity.website, it) }
            source?.let { setLink(this@DetailsActivity.source, it) }

            this@DetailsActivity.age.text = "$age+"

            phone?.let { this@DetailsActivity.phone.setContent(it) }


            city?.let { this@DetailsActivity.city.setContent(it) }
            workingDaysHours?.let { this@DetailsActivity.workingHours.setContent(it)}
            category?.let { }


            image?.url?.let {
                Glide.with(this@DetailsActivity)
                    .applyDefaultRequestOptions(RequestOptions.centerCropTransform())
                    .load(it)
                    .into(main_image)
            }
        }
    }

    private fun setLink(view: PublicationTextItem, url: String) {
        view
            .apply {
                isLink = true
                setOnClickListener {
                    Intent(Intent.ACTION_VIEW)
                        .also {
                            it.data = Uri.parse("${infoBody.text}")
                            startActivity(it)
                        }
                }
                setContent(url)
            }
    }

    private fun setActions(p: PublicationDetailed) {
        with(p) {
            seen_counter.text = "$views"
            likes_counter.text = "$likes"
        }
    }

    private fun setColor(p: PublicationDetailed) {
        TagColorProvider.setColorToView(tag_color, p.categoryView)
    }

    private fun setPlace(p: PublicationDetailed) {
        with(p) {
            if (topic == TOPIC.AFISHA) {
                placeContainer.isVisible = true
                placeName.text = parentName
                placeAddress.text = parentAddress
                this@DetailsActivity.placeContainer.setOnClickListener {
                    startActivity(
                        Intent(this@DetailsActivity, DetailsActivity::class.java)
                            .apply {
                                putExtra(PARENT_INTENT_KEY, TOPIC.PLACES.name)
                                putExtra(ID_INTENT_KEY, parent)
                            }
                    )
                }
            }
        }
    }

    private fun setDescription(p: PublicationDetailed) {
        with(p) {
            when {
                fullDescriptionTextImages != null -> {
                    fullDescriptionTextImages.forEach {
                        (if (it.startsWith("http", true))
                            ImageView(this@DetailsActivity)
                                .also { image ->
                                    Glide
                                        .with(this@DetailsActivity)
                                        .load(it)
                                        .into(image)
                                }
                        else {
                            TextView(this@DetailsActivity)
                                .also { textView ->
                                    textView.text = it
                                }
                        })
                            .also { v ->
                                descriptionContainer.addView(
                                    v,
                                    ViewGroup.MarginLayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )
                                )
                                (v.layoutParams as LinearLayout.LayoutParams)
                                    .also { params ->
                                        params.setMargins(0, 36, 0, 0)
                                        v.layoutParams = params
                                    }
                            }


                    }
                }
                else -> {
                    (description ?: fullDescription)?.let {
                        this@DetailsActivity.description.setContent(
                            it
                        )
                    }
                }
            }
        }

    }


    private fun setParent(p: PublicationDetailed) {
        with(p) {
            parentName?.let {
                if (checkParent(it)) {
                    publication_tag.text = "${topic.title} • ${categoryView}".toUpperCase()
                    this@DetailsActivity.age.isVisible = false
                } else {
                    publication_tag.text = (
                            if (p.categoryView != "") "${topic.title} • $categoryView"
                            else topic.title)
                        .toUpperCase()
                }
            }
        }
    }

    private fun checkParent(str: String) =
        listOf(
            "материалы",
            "статьи",
            "новости",
            "интервью",
            "фотосюжеты",
            "ревизор-tv",
            "обзоры",
            "тесты",
            "блоги",
            "культура в лицах",
            "спецпроекты",
            "для детей",
            "для молодежи",
            "афиша",
            "ревизор рекомендует",
            "места",
            "фестивали",
            "за рубежом"
        )
            .let { l ->
                l.any { it == str.toLowerCase() }
            }


    private fun setDate(p: PublicationDetailed) {
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatterStyled = DateTimeFormat
            .forStyle(NO_TIME)
            .withLocale(Locale("ru"))
        val text: String? = when {
            topic == TOPIC.AFISHA -> {
                if (p.nearestDate != null) {
                    period.infoTitle.text = "Ближайшая дата:"
                    DatePrinter.simpleDate(p.nearestDate)
                } else DatePrinter.getDateForAdapter(p.date, p.end)
            }
            p.date != null && p.end != null -> {
                val date = formatter.parseDateTime(p.date)
                val end = formatter.parseDateTime(p.end)
                "${formatterStyled.print(date)} - ${formatterStyled.print(end)}"
            }
            p.date != null -> {
                period.infoTitle.text = "Дата:"
                val date = formatter.parseDateTime(p.date)
                formatterStyled.print(date)
            }
            else -> null
        }

        text?.let { period.setContent(it) }
    }

    private fun setUnderGrounds(p: PublicationDetailed) {
        with(p) {
            if (undergrounds.isNotEmpty()) {
                var str = ""
                undergrounds.forEach {
                    str = str
                        .plus("${it.name}, ")

                }
                nearMetro.setContent(str.removeRange(str.length - 2, str.length - 1))
            }
        }
    }


    companion object {
        const val ID_INTENT_KEY = "publication_id_intent"
        const val PARENT_INTENT_KEY = "publication_parent_name"

        const val NO_TIME = "M-"
    }
}
