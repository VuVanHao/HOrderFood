package com.hao.horderfood

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home_page.*

class ActivityHomePage : AppCompatActivity() {

    private var username:String? = "";
    private var fragmentFirstTypeFood:FragmentFirstTypeFood? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        initUserLogin()
        initView()
        func()
    }

    private fun func() {
        tvGoListLoaiThucDon.setOnClickListener {
            val intent = Intent(this,ActivityListLoaiThucDon::class.java)
            intent.putExtra("username",username)
            startActivity(intent)
        }

        fg_1stLoaiThucDon.setOnClickListener {
            val intent = Intent(this,ActivityListLoaiThucDon::class.java)
            intent.putExtra("username",username)
            startActivity(intent)
        }

        fragmentFirstTypeFood = FragmentFirstTypeFood.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fg_1stLoaiThucDon,fragmentFirstTypeFood!!)
            .commit()
        imgNhanVien.setOnClickListener {
            startActivity(Intent(this,ActivityListStaff::class.java))
        }


    }

    private fun initUserLogin() {
        username = MySharedPreferences.getNameUser(this)
    }

    private fun initView() {
        val hView = navigation_drawer.getHeaderView(0)
        val tvNameUser = hView.findViewById(R.id.tvNameUser) as TextView
        tvNameUser.text = username
        navigation_drawer.setCheckedItem(R.id.android)
        navigation_drawer?.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.android -> {
                    startActivity(Intent(this,ActivityHomePage::class.java))
                }
                R.id.staff ->
                {
                    startActivity(Intent(this,ActivityListStaff::class.java))
                }
                R.id.logout ->
                {
                    startActivity(Intent(this,ActivityLogin::class.java))
                }
            }
            drawer_layout!!.closeDrawer(GravityCompat.START)
            true
        })
        toolBar?.title = "Trang chủ"
        toolBar?.setTitleTextColor(Color.BLACK)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_open)
        drawer_layout?.addDrawerListener(toggle)
        toggle.syncState()
    }


}