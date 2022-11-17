package com.hao.horderfood

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.hao.horderfood.datahelper.ManageStaffHelper
import com.hao.horderfood.model.Staff
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivityRegister : AppCompatActivity() {

    var manageStaffHelper:ManageStaffHelper? = null
    var calendar:Calendar = Calendar.getInstance()
    var listStaff:ArrayList<Staff>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        manageStaffHelper = ManageStaffHelper(this)
        manageStaffHelper?.closeSql()
        initFunc()
        btnRegister.setOnClickListener {
            addNewStaff()
        }

    }

    private fun addNewStaff() {
        val userName = tvUsernameRegister.text.toString()
        val pass = tvPasswordRegister.text.toString()
        val gender:String = if (rbNam.isChecked) {
            "Nam"
        } else {
            "Nu"
        }
        val date = tvDataRegister.text.toString()
        val cmnd = tvCMNDRegister.text.toString()
        if (TextUtils.isEmpty(userName))
        {
            Toast.makeText(this, "Please enter your username !!!", Toast.LENGTH_SHORT).show()
        }
        else if (checkUsernameStaff(listStaff!!,userName))
        {
            Toast.makeText(this, "Tên tài khoản đã tồn tại !!!", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Please enter your password !!!", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(date))
        {
            Toast.makeText(this, "Please enter your birthday !!!", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(cmnd))
        {
            Toast.makeText(this, "Please enter your id card !!!", Toast.LENGTH_SHORT).show()
        }
        else
        {
            manageStaffHelper?.addNewStaff(userName,pass,gender,date,cmnd)
            Toast.makeText(this, "Register Successfully !!! ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ActivityLogin::class.java)
            startActivity(intent)
        }
    }

    private fun initFunc() {
        imgGoToLogin.setOnClickListener {
            val intent = Intent(this,ActivityLogin::class.java)
            startActivity(intent)
        }

        listStaff = manageStaffHelper?.getListStaff()

        val date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateEditText()
        }
        tvDataRegister.setOnClickListener{
            DatePickerDialog(
                this,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    fun checkUsernameStaff(list : ArrayList<Staff>, name:String ) : Boolean
    {
        var check : Boolean = false
        for ( item in list)
        {
            check = item.nameStaff == name
        }
        return check
    }

    private fun updateEditText() {
        val format :String = "MM/dd/yyyy"
        val dateFormat: SimpleDateFormat = SimpleDateFormat(format,Locale.US)
        tvDataRegister.setText(dateFormat.format(calendar.time))
    }
}