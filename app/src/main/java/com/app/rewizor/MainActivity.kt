package com.app.rewizor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.model.Region
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.replaceFragment
import com.app.rewizor.ui.RegionsListFragment
import com.app.rewizor.ui.TopicTabFragment
import com.app.rewizor.ui.utils.*
import com.app.rewizor.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_drawer_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent

class MainActivity : AppCompatActivity(),KoinComponent,  NavigationView.OnNavigationItemSelectedListener {

    val viewModel: MainViewModel by inject()
    var toolbarTitle: CharSequence?
        get() = supportActionBar?.title
        set(value) {
            supportActionBar?.title = value
        }

    var navigationBackClick
        get() =  { }
        set(value) {
            toolbar.setNavigationOnClickListener { value.invoke() }
        }
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigation()
        setViewModel(viewModel)
        toolbarTitle = TOPIC.MAIN.title
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
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        nav_view.setNavigationItemSelectedListener(this)


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
            regionModelLiveData.observeViewModel(this@MainActivity) { setRegion(it) }
            profileLiveData.observeViewModel(this@MainActivity) { setProfileView(it) }
            onNavigationSettingsLiveEvent.observeViewModel(this@MainActivity) { openTopic() }
            onTopicChosenLiveData.observeViewModel(this@MainActivity) { openTopic(it) }
            cityFilterOpenedLiveData.observeViewModel(this@MainActivity) { openCities(it) }
            onLoadSettingsErrorLiveEvent.observeViewModel(this@MainActivity) {
                Alerts.showAlertToUser(this@MainActivity, it)
            }
            contentShowingLiveData.observeViewModel(this@MainActivity) {
                if (it) closeCities()
            }
            onViewCreated()
        }
    }

    private fun initClickListeners() {
        login.setOnClickListener {
            startActivity(Intent(
                this, AuthorizationActivity::class.java
            ).apply { putExtra(AUTHORIZATION_INTENT_KEY, AUTHORIZATION.LOGIN.name) })
        }
        registration.setOnClickListener {
            startActivity(Intent(
                this, AuthorizationActivity::class.java
            ).apply { putExtra(AUTHORIZATION_INTENT_KEY, AUTHORIZATION.REGISTRATION.name) })
        }
        cancel.setOnClickListener { drawer.closeDrawer(GravityCompat.START) }
    }

    private fun setRegion(regionName: String) {
        nav_view.menu.findItem(R.id.city).title = "Ваш город: $regionName"
    }

    private fun closeCities() {
        viewModel.openLastTopic()
    }

    private fun openCities(currentCity: Region) {
        setDrawerEnabled(false)
        toolbarTitle = "Ваш город ${currentCity.name}"
        replaceFragment(fragment = RegionsListFragment())
    }

    private fun openTopic(topic: TOPIC = TOPIC.MAIN) {
        toolbarTitle = topic.title
        setDrawerEnabled(true)
        replaceFragment(fragment = TopicTabFragment.getInstance(
            Bundle().apply { putString(TOPIC_KEY, topic.name) }
        ))
    }

    private fun hideProfile() {
        authButtons.isVisible = true
        profileMenuItem.isVisible = false
        fio.text = "Неизвестный пользователь"
        avatar.setImageResource(R.drawable.ic_avatar_icons)
    }

    private fun setProfileView(account: Account) {
        authButtons.isVisible = false
        val name = "${account.lastName ?: ""} ${account.firstName ?: ""} ${account.middleName ?: ""}"
        fio.text = name
        profileMenuItem.isVisible = true
        Glide
            .with(this)
            .run {
                if (account.avatar?.url == null) {
                    load(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_avatar_icons))
                } else {
                    load(account.avatar.url)
                }
            }
            .into(avatar)



        getSharedPreferences("", Context.MODE_PRIVATE)
    }

    private val backClickListener = View.OnClickListener { closeCities() }

    private fun setDrawerEnabled(enabled: Boolean) {
        if (enabled) {
            drawer.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            toggle.isDrawerIndicatorEnabled = true
            toolbar.setNavigationIcon(R.drawable.ic_menu)
            supportActionBar?.setDisplayShowHomeEnabled(false)
            supportActionBar?.setHomeButtonEnabled(false)
            toggle.toolbarNavigationClickListener = null
        } else {
            drawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toggle.isDrawerIndicatorEnabled = false
            toggle.toolbarNavigationClickListener = backClickListener
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main -> { viewModel.menuClicked(TOPIC.MAIN) }
            R.id.afisha -> { viewModel.menuClicked(TOPIC.AFISHA) }
            R.id.city -> { viewModel.cityClicked(); item.isCheckable = false }
        }
        //        var categoryId = item.itemId
   //     toolbarTitle = item.title
   //     supportActionBar?.title = item.title
        var drawer: DrawerLayout = drawer_layout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        initClickListeners()
        menu?.findItem(R.id.filter)?.setOnMenuItemClickListener { viewModel.logout(); return@setOnMenuItemClickListener true }
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val FRAGMENT_CONTAINER = R.id.fr_container
    }
}
