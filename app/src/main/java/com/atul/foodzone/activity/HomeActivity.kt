package com.atul.foodzone.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.atul.foodzone.R
import com.atul.foodzone.fragment.*
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var drawer : DrawerLayout
    lateinit var coordinator : CoordinatorLayout
    lateinit var toolbar : Toolbar
    lateinit var frame : FrameLayout
    lateinit var navigationView : NavigationView
    var preMenuItem: MenuItem ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawer = findViewById(R.id.drawer)
        coordinator = findViewById(R.id.coordinator)
        toolbar = findViewById(R.id.toolbar)
        frame = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)

        setupToolbar()
        openDashboardFragment()

        navigationView.setNavigationItemSelectedListener {
            if(preMenuItem != null){
                preMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            preMenuItem = it
            when(it.itemId){
                R.id.home->{
                    openDashboardFragment()
                    supportActionBar?.title = "All Restaurants"
                    drawer.closeDrawers()
                }
                R.id.favourites->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,FavouritesFragment()).commit()
                    supportActionBar?.title = "Favourite Restaurants"
                    drawer.closeDrawers()
                }
                R.id.MyProfile->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,ProfileFragment()).commit()
                    supportActionBar?.title = "My Profile"
                    drawer.closeDrawers()
                }
                R.id.AboutApp->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,AboutAppFragment()).commit()
                    supportActionBar?.title = "About App"
                    drawer.closeDrawers()
                }
                R.id.AboutCreator->{
                   supportFragmentManager.beginTransaction().replace(R.id.frame,AboutCreator()).commit()
                    supportActionBar?.title = "About Creator"
                    drawer.closeDrawers()
                }
                R.id.faqs->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,FaqFragment()).commit()
                    supportActionBar?.title = "FAQs"
                    drawer.closeDrawers()
                }
                R.id.Logout->{
                    supportFragmentManager.beginTransaction().replace(R.id.frame,LogoutFragment()).commit()
                    supportActionBar?.title = "Logout"
                    drawer.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        val actionToggle = ActionBarDrawerToggle(this,drawer,R.string.open_drawer,R.string.close_drawer)
        drawer.addDrawerListener(actionToggle)
        actionToggle.syncState()
    }
    fun setupToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "HOME"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home){
            drawer.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openDashboardFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.frame,HomeFragment()).commit()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        if(frag !is HomeFragment){
            openDashboardFragment()
        }else {
            ActivityCompat.finishAffinity(this) }
    }
}
