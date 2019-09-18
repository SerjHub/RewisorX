package com.app.rewizor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.rewizor.data.model.Account
import com.app.rewizor.data.model.Region
import com.app.rewizor.exstension.observeViewModel
import com.app.rewizor.exstension.removeFragment
import com.app.rewizor.exstension.replaceFragment
import com.app.rewizor.global.NEW_ACCOUNT
import com.app.rewizor.global.UPDATE_All_DATA_FOR_NEW_PROFILE
import com.app.rewizor.ui.*
import com.app.rewizor.ui.utils.*
import com.app.rewizor.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_drawer_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent,
    NavigationView.OnNavigationItemSelectedListener {

    val viewModel: MainViewModel by inject()
    private var backPressAction: BackClick? = null
    var toolbarTitle: CharSequence?
        get() = supportActionBar?.title
        set(value) {
            supportActionBar?.title = value
        }

    private var mToolBarNavigationListenerIsRegistered = false
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private val updaterReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when {
                    it.getBooleanExtra(UPDATE_All_DATA_FOR_NEW_PROFILE, true) -> {
                        viewModel.refreshData()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigation()
        setViewModel(viewModel)
        toolbarTitle = TOPIC.MAIN.title

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(updaterReceiver, IntentFilter(NEW_ACCOUNT))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updaterReceiver)
    }

    private fun setNavigation() {
        setSupportActionBar(toolbar)

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.drawerArrowDrawable.color = getColor(R.color.superWhite);
        } else {
            toggle.drawerArrowDrawable.color = resources.getColor(R.color.superWhite);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.navigationIcon?.setColorFilter(
                resources.getColor(R.color.superWhite, null),
                PorterDuff.Mode.SRC_ATOP
            )
        }


        nav_view.setNavigationItemSelectedListener(this)

    }

    private fun setViewModel(viewModel: MainViewModel) {
        with(viewModel) {
            anonModelLiveData.observeViewModel(this@MainActivity) { if (it) hideProfile() }
            regionModelLiveData.observeViewModel(this@MainActivity) { setRegion(it) }
            profileLiveData.observeViewModel(this@MainActivity) { setProfileView(it) }
            onNavigationSettingsLiveEvent.observeViewModel(this@MainActivity) { openTopic() }
            currentTopicLiveData.observeViewModel(this@MainActivity) { openTopic(it) }
            cityFilterOpenedLiveData.observeViewModel(this@MainActivity) { openCities(it) }
            aboutOpenedLiveData.observeViewModel(this@MainActivity) {
                openAdditionalView(
                    AboutFragment()
                )
            }
            supportOpenedLiveData.observeViewModel(this@MainActivity) {
                openAdditionalView(
                    SupportFragment()
                )
            }
            onLoadSettingsErrorLiveEvent.observeViewModel(this@MainActivity) {
                Alerts.showAlertToUser(this@MainActivity, it)
            }
            contentShowingLiveData.observeViewModel(this@MainActivity) {
                if (it) closeCities()
            }
            filterStateLiveData.observeViewModel(this@MainActivity) {
                filterOpened(it)
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
        nav_view.menu.findItem(R.id.main).isChecked = true
        nav_view.menu.findItem(R.id.city).apply {
            title = "Ваш город: $regionName"
            isCheckable = false
        }
    }

    private fun openAdditionalView(fragment: BaseFragment) {
        setDrawerDisabled(true) { closeAdditionalView(fragment) }
        fr_container.isVisible = false
        additional_container.isVisible = true
        replaceFragment(R.id.additional_container, fragment)
    }

    private fun closeAdditionalView(fragment: BaseFragment) {
        setDrawerDisabled(false)
        fr_container.isVisible = true
        additional_container.isVisible = false
        removeFragment(fragment)
    }

    private fun closeCities() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        viewModel.openLastTopic()
    }

    private fun openCities(currentCity: Region) {
        setDrawerDisabled(true) { closeCities() }
        toolbarTitle = "Ваш город ${currentCity.name}"
        replaceFragment(fragment = RegionsListFragment())
    }

    private fun openTopic(topic: TOPIC = TOPIC.MAIN) {
        toolbarTitle = topic.title
        setDrawerDisabled(false)
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
        profileDivider.isVisible = false
        val name =
            "${account.lastName ?: ""} ${account.firstName ?: ""} ${account.middleName ?: ""}"
        fio.text = name
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
    }


    private fun setDrawerDisabled(enabled: Boolean, backClick: BackClick? = null) {
        if (enabled) {
            drawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
            toggle.isDrawerIndicatorEnabled = false

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            if (!mToolBarNavigationListenerIsRegistered) {
                backPressAction = backClick
                toggle.toolbarNavigationClickListener =
                    View.OnClickListener { backPressAction!!.invoke() }
                mToolBarNavigationListenerIsRegistered = true
            }

        } else {
            mToolBarNavigationListenerIsRegistered = false
            drawer.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            backPressAction = null
            toggle.isDrawerIndicatorEnabled = true
            toggle.toolbarNavigationClickListener = null
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main -> {
                viewModel.menuClicked(TOPIC.MAIN)
            }
            R.id.afisha -> {
                viewModel.menuClicked(TOPIC.AFISHA)
            }
            R.id.materials -> {
                viewModel.menuClicked(TOPIC.MATERIALS)
            }
            R.id.news -> {
                viewModel.menuClicked(TOPIC.NEWS)
            }
            R.id.city -> {
                viewModel.cityClicked(); }
            R.id.about -> {
                viewModel.aboutClicked(); }
            R.id.support -> {
                viewModel.supportClicked(); }
        }

        var drawer: DrawerLayout = drawer_layout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun filterOpened(filter: FilterStateModel) {
        (supportFragmentManager
            .fragments
            .firstOrNull { it is TopicTabFragment } as? TopicTabFragment)
            ?.onOpenFilter(filter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        initClickListeners()
        menu?.findItem(R.id.filter)?.setOnMenuItemClickListener {
            viewModel.filterClicked(); return@setOnMenuItemClickListener true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (backPressAction != null) backPressAction?.invoke()
        else super.onBackPressed()
    }

    companion object {
        const val FRAGMENT_CONTAINER = R.id.fr_container
    }
}

typealias BackClick = () -> Unit
