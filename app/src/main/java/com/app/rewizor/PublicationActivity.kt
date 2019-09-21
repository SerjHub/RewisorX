package com.app.rewizor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

class PublicationActivity : AppCompatActivity() {

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
            publicationModelLiveData.observeViewModel(this@PublicationActivity) {
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

            name?.let { this@PublicationActivity.name.text = it }
            (address ?: parentAddress)?.let { this@PublicationActivity.address.setContent(it) }
            description?.let { this@PublicationActivity.description.setContent(it) }

            website?.let { setLink(this@PublicationActivity.website, it) }
            source?.let { setLink(this@PublicationActivity.source, it) }

            this@PublicationActivity.age.text = "$age+"
            (fullDescription ?: description)
                ?.let { this@PublicationActivity.description.setContent(it) }


            phone?.let { this@PublicationActivity.phone.setContent(it) }


            city?.let { this@PublicationActivity.city.setContent(it) }
            workingDaysHours?.let {  }
            category?.let { }






            image?.url?.let {
                Glide.with(this@PublicationActivity)
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

    private fun setParent(p: PublicationDetailed) {
        with(p) {
            parentName?.let {
                if (checkParent(it)) {
                    publication_tag.text = "${topic.title} • ${categoryView}".toUpperCase()
                    this@PublicationActivity.age.isVisible = false
                } else {
                    this@PublicationActivity.place.setContent(it)
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
