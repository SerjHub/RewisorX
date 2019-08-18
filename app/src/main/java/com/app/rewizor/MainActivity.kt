package com.app.rewizor

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.app.rewizor.data.model.Account
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.replaceFragment
import com.app.rewizor.ui.TopicTabFragment
import com.app.rewizor.ui.utils.TOPIC
import com.app.rewizor.ui.utils.TOPIC_KEY
import com.app.rewizor.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_drawer_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent

class MainActivity : AppCompatActivity(),KoinComponent,  NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MainViewModel by inject()
    var toolbarTitle: CharSequence
        get() = toolbar.title
        set(value) {
            toolbar.title = value
        }
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private val navHeaderView
        get() = nav_view.getHeaderView(0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigation()
        setViewModel(viewModel)
        supportActionBar?.title = TOPIC.MAIN.title

    }

    private fun setNavigation() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_menu)

        drawer = drawer_layout

        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navOpen,
            R.string.navClose
        )
        drawer_layout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)
/*
        var drawer: DrawerLayout = drawer_layout
        var toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,drawer,toolbar,, 2)
        )
*/
    }

    private fun setViewModel(viewModel: MainViewModel) {
        with(viewModel) {
            anonModelLiveData.observeViewModel(this@MainActivity) { if (it) hideProfile() }
            profileLiveData.observeViewModel(this@MainActivity) { setProfileView(it) }
            onNavigationSettingsLiveData.observeViewModel(this@MainActivity) { openTopic() }
            onViewCreated()
        }
    }

    private fun openTopic() {
        replaceFragment(fragment = TopicTabFragment.getInstance(
            Bundle().apply { putString(TOPIC_KEY, TOPIC.MAIN.title) }
        ))
    }

    private fun hideProfile() {
        authButtons.isVisible = true
        profileName.isVisible = false
        avatar.setImageResource(R.drawable.ic_cancel)
    }

    private fun setProfileView(account: Account) {
        authButtons.isVisible = false
        profileName.isVisible = true
        Glide
            .with(this)
            .load(account.avatar.guid)
            .into(avatar)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //        var categoryId = item.itemId
   //     toolbarTitle = item.title
   //     supportActionBar?.title = item.title
        var drawer: DrawerLayout = drawer_layout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val FRAGMENT_CONTAINER = R.id.fr_container
    }
}
