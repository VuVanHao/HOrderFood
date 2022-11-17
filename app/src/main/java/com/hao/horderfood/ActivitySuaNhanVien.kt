package com.hao.horderfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.hao.horderfood.datahelper.ManageStaffHelper
import com.hao.horderfood.model.Staff
import kotlinx.android.synthetic.main.activity_sua_nhan_vien.*

class ActivitySuaNhanVien : AppCompatActivity() {

    var idStaff: Int? = null
    var staff: Staff? = null
    var manageStaffHelper: ManageStaffHelper? = null
    var listStaff:ArrayList<Staff>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sua_nhan_vien)
        manageStaffHelper = ManageStaffHelper(this)
        manageStaffHelper?.closeSql()
        initFunc()
        initData()
        btnSuaNhanVien.setOnClickListener {
            suaNhanVien()
        }
    }

    private fun suaNhanVien() {
        val newName = tvUsernameSuaStaff.text.toString()
        val newPass = tvPasswordSuaStaffAgain.text.toString()
        var gender: String? = null
        if (rbNamSuaStaff.isChecked) {
            gender = "Nam"
        } else {
            gender = "Nu"
        }
        val oldPass = staff?.passStaff
        val edtOldPass = tvPasswordSuaStaff.text.toString()
        val newDate = tvDateSuaStaff.text.toString()
        val newIdCard = tvCMNDSuaStaff.text.toString()
        if (TextUtils.isEmpty(newName)) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập !!!", Toast.LENGTH_SHORT).show()
        }
        else if (checkUsernameStaff(listStaff!!,newName)) {
            Toast.makeText(this, "Tên tài khoản đã tồn tại !!!", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(edtOldPass)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu cũ !!!", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(newPass)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu mới !!!", Toast.LENGTH_SHORT).show()
        } else if (edtOldPass != oldPass) {
            Toast.makeText(this, "Mật khẩu không chính xác !!!", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(newDate)) {
            Toast.makeText(this, "Vui lòng nhập ngày sinh !!!", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(newIdCard)) {
            Toast.makeText(this, "Vui lòng nhập CMND !!!", Toast.LENGTH_SHORT).show()
        } else {
            manageStaffHelper?.updateStaff(idStaff!!, newName, newPass, gender, newDate, newIdCard)
            Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

    }

    private fun initData() {
        listStaff = manageStaffHelper?.getListStaff()
        idStaff = intent.getIntExtra("ID_STAFF", 0)
        staff = manageStaffHelper?.getStaffFromID(idStaff!!)
        if (staff?.nameStaff == "admin")
        {
            tvUsernameSuaStaff.isEnabled = false
        }
        tvUsernameSuaStaff.setText(staff?.nameStaff)
        val gender: String? = staff?.genderStaff
        if (gender == "Nam") {
            rbNamSuaStaff.isChecked = true
        } else {
            rbNuSuaStaff.isChecked = true
        }

        tvDateSuaStaff.setText(staff?.dateStaff)
        tvCMNDSuaStaff.setText(staff?.idCardStaff)
    }

    private fun initFunc() {

        imgGoToBackListStaff1.setOnClickListener {
            onBackPressed()
        }
        btnGoToBackListStaff1.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onBackPressed() {
        startActivity(Intent(this, ActivityListStaff::class.java))
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
}