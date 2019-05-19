package com.example.skylog

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentContainer
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.support.v7.widget.RecyclerView
import com.example.skylog.Fragments.MainPage
import com.example.skylog.Fragments.bbox
import com.example.skylog.Fragments.send_msg
import com.example.skylog.Fragments.system_info
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var fragment: Fragment? = null
    var Main_Page_flag:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        fab.setOnClickListener { view ->
            displaySelectedScreen(R.id.nav_send)
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        //displaySelectedScreen(R.id.nav_system_info)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun displaySelectedScreen(itemId:Int)
    {
        when (itemId) {
            R.id.nav_system_info -> {

                fragment= system_info()
                Main_Page_flag=false
                /*layoutInflater.inflate(R.layout.app_bar_main, null)
                setContentView(R.layout.app_bar_main);*/
                // Handle the camera action
            }
            R.id.nav_bbox -> {
                fragment= bbox()
                Main_Page_flag=false

                /*layoutInflater.inflate(R.layout.bbox_layout, null)
                setContentView(R.layout.bbox_layout);*/
            }
            R.id.nav_overload_table -> {

            }
            R.id.nav_graphics -> {
                fragment= MainPage()
                Main_Page_flag=true
            }
            /* R.id.nav_share -> {

             }*/
            R.id.nav_send -> {
                fragment= send_msg()
                Main_Page_flag=false

            }
        }
        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_frame, fragment!!)
            ft.commit()
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if(Main_Page_flag==true && fragment!!.isResumed()){
            //do nothing here if we're showing the fragment
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR); // otherwise lock in portrait
        }
        super.onConfigurationChanged(newConfig);
    }


}
