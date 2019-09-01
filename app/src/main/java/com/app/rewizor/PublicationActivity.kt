package com.app.rewizor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.rewizor.viewmodel.PublicationViewModel
import org.koin.android.ext.android.inject

class PublicationActivity : AppCompatActivity() {

    private val publicationViewModel: PublicationViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publication)
        getExtras()


    }

    private fun getExtras() {
        intent.getLongExtra("publication_id", -1)
            .also {
                publicationViewModel.setPublicationId(it)
            }
    }

    private fun onViewPrepared(vm: PublicationViewModel) {
        with(vm) {

        }
        vm.onViewCreated()
    }
}
