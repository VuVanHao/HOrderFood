package com.hao.horderfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.hao.horderfood.datahelper.ManageStaffHelper
import kotlinx.android.synthetic.main.activity_main.*

class ActivityLogin : AppCompatActivity() {

    var manageStaffHelper: ManageStaffHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manageStaffHelper = ManageStaffHelper(this)
        manageStaffHelper?.closeSql()
        val countRecord = manageStaffHelper?.getProfilesCount()
        if (countRecord == 0)
        {
            manageStaffHelper?.addNewStaff("admin","123456","Nam","1/1/2000","#")
        }
        goToRegister()
        login()

    }

    private fun login() {
        btnLogin.setOnClickListener {
            val username = tvUsernameLogin.text.toString()
            val password = tvPasswordLogin.text.toString()
            if (TextUtils.isEmpty(username))
            {
                Toast.makeText(this, "Please enter your username !!!", Toast.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Please enter your password !!!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if (manageStaffHelper?.checkAccountStaff(username,password) == true)
                {
                    val intent = Intent(this,ActivityHomePage::class.java)
                    MySharedPreferences.setNameUser(this,username)
                    startActivity(intent)
                    Toast.makeText(this, "Login Successfully !!!", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this, "Login Failed !!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToRegister() {
        tvGoToRegister.setOnClickListener {
            val intent = Intent(this@ActivityLogin,ActivityRegister::class.java)
            startActivity(intent)
        }
    }
}