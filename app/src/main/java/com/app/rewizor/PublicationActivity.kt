package com.app.rewizor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.rewizor.data.model.PublicationDetailed
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.ui.utils.TOPIC
import com.app.rewizor.viewmodel.PublicationViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_publication.*
import kotlinx.android.synthetic.main.view_publication_tag.*
import org.koin.android.ext.android.inject

class PublicationActivity : AppCompatActivity() {

    private val publicationViewModel: PublicationViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publication)
        getExtras()
        onViewPrepared(publicationViewModel)

   //     window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setToolbar()


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

            intent.getStringExtra(PARENT_INTENT_KEY)
                .also { parent ->
                    TOPIC.valueOf(parent).title.toUpperCase()
                        .also {
                            publication_tag.text = if (p.categoryView != "") "$it • $categoryView"
                            else it
                        }
                }


         //   category?.let { this@PublicationActivity.category.setContent(categoryView) }
            name?.let { this@PublicationActivity.name.text = it }
            address?.let { this@PublicationActivity.address.setContent(it) }
            description?.let { this@PublicationActivity.description.setContent(it) }
           // city?.let { this@PublicationActivity.city.setContent(it) }

            image.url?.let {
                Glide.with(this@PublicationActivity)
                    .applyDefaultRequestOptions(RequestOptions.centerCropTransform())
                    .load(it)
                    .into(main_image)
            }
        }
    }


    companion object {
        const val ID_INTENT_KEY = "publication_id_intent"
        const val PARENT_INTENT_KEY = "publication_parent_name"
    }
}
