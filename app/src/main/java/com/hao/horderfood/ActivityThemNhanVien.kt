package com.hao.horderfood

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hao.horderfood.datahelper.ManageStaffHelper
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_them_nhan_vien.*
import java.text.SimpleDateFormat
import java.util.*


class ActivityThemNhanVien : AppCompatActivity() {

    var manageStaffHelper: ManageStaffHelper? = null
    var calendar:Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_them_nhan_vien)
        manageStaffHelper = ManageStaffHelper(this)
        manageStaffHelper?.closeSql()
        initFunc()
        btnThemNhanVien.setOnClickListener {
            val userName = tvUsernameAddStaff.text.toString()
            val pass = tvPasswordAddStaff.text.toString()
            val gender:String = if (rbNamAddStaff.isChecked) {
                "Nam"
            } else {
                "Nu"
            }
            val date = tvDateAddStaff.text.toString()
            val cmnd = tvCMNDAddStaff.text.toString()
            if (TextUtils.isEmpty(userName))
            {
                Toast.makeText(this, "Please enter your username !!!", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Thêm thành công !!! ", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }
    }

    private fun initFunc() {
        imgGoToBackListStaff.setOnClickListener {
            onBackPressed()
        }
        btnGoToBackListStaff.setOnClickListener {
            onBackPressed()
        }
        val date = OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateEditText()
        }
        tvDateAddStaff.setOnClickListener{
            DatePickerDialog(
                this,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateEditText() {
        val format :String = "MM/dd/yyyy"
        val dateFormat:SimpleDateFormat = SimpleDateFormat(format,Locale.US)
        tvDateAddStaff.setText(dateFormat.format(calendar.time))
    }

    override fun onBackPressed() {
        startActivity(Intent(this,ActivityListStaff::class.java))
    }
}