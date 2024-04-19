package com.example.onestop

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.onestop.quicknotes.QuickNotes
import com.example.onestop.reminder.ReminderFragment
import com.example.onestop.taskmanager.TaskManager
import com.google.android.material.navigation.NavigationView

class LandingPage : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    lateinit var  drawer: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var usertv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        val choice=intent.getStringExtra("id")
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        usertv = headerView.findViewById(R.id.navusername)

        val username = intent.getStringExtra("username")
        usertv.text = username

        var toolbar: Toolbar
                = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        var actionBarDrawerToggle = ActionBarDrawerToggle(this,drawer,toolbar,
            R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        when(choice)
        {
            "tm"->
            {
                val task_manager = TaskManager()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, task_manager)
                    .commit()
            }
            "qn"->
            {
                val quick_notes = QuickNotes()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, quick_notes)
                    .commit()
            }
            else ->{
                val task_manager = TaskManager()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, task_manager)
                    .commit()
            }

        }


        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.reminder -> {
                val reminderFragment = ReminderFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, reminderFragment)
                    .commit()
            }
            R.id.task_manager -> {
                val task_manager = TaskManager()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, task_manager)
                    .commit()
            }
            R.id.notes -> {
                val quick_notes = QuickNotes()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, quick_notes)
                    .commit()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true

    }
}