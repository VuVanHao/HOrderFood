package com.hao.horderfood

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import com.hao.horderfood.datahelper.ManageStaffHelper
import com.hao.horderfood.model.Staff
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.activity_home_page.drawer_layout
import kotlinx.android.synthetic.main.activity_home_page.navigation_drawer
import kotlinx.android.synthetic.main.activity_home_page.toolBar
import kotlinx.android.synthetic.main.activity_list_mon_an.*
import kotlinx.android.synthetic.main.activity_list_staff.*

class ActivityListStaff : AppCompatActivity() {

    var listStaff: ArrayList<Staff>? = null
    var listStaffAdapter: ListStaffAdapter? = null
    var manageStaffHelper: ManageStaffHelper? = null
    var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_staff)
        manageStaffHelper = ManageStaffHelper(this)
        manageStaffHelper?.closeSql()
        initView()
        initData()
    }

    private fun initData() {
        listStaff = manageStaffHelper?.getListStaff()
        listStaffAdapter = ListStaffAdapter(this, listStaff!!)
        lvListNhanvien.adapter = listStaffAdapter
        listStaffAdapter!!.notifyDataSetChanged()

        lvListNhanvien.setOnItemClickListener { _, _, i, _ ->
            if (flag) {
                flag = false
                listStaffAdapter?.activateButtons(false, i)
            }

        }

        lvListNhanvien.setOnItemLongClickListener { _, _, i, _ ->
            if (!flag) {
                flag = true
                listStaffAdapter?.activateButtons(true, i)
            }

            return@setOnItemLongClickListener (true)
        }
    }

    fun deleteItemStaff(pos: Int) {
        val idStaff: Int? = listStaff?.get(pos)?.idStaff
        val nameStaff: String? = listStaff?.get(pos)?.nameStaff
        val staff: Staff? = listStaff?.get(pos)
        if (nameStaff == "admin")
        {
            Toast.makeText(this, "Không thể xóa tài khoản này !!", Toast.LENGTH_SHORT).show()
            flag = false
            listStaffAdapter?.activateButtons(false, pos)
        }
        else
        {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Thông báo")
            builder.setMessage("Bạn có muốn xóa $nameStaff khỏi danh sách không ? ")
            builder.setPositiveButton("Có") { _, i ->
                manageStaffHelper?.deleteRecord(idStaff!!)
                listStaff?.remove(staff)
                listStaffAdapter?.notifyDataSetChanged()
                flag = false
                listStaffAdapter?.activateButtons(false, i)
            }
            builder.setNegativeButton("Không") { _, i ->
                flag = false
                listStaffAdapter?.activateButtons(false, i)
            }

            val alert = builder.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()
        }
    }

    fun goToSuaItemStaff(pos: Int) {
        val idStaff: Int? = listStaff?.get(pos)?.idStaff
        val intent = Intent(this, ActivitySuaNhanVien::class.java)
        intent.putExtra("ID_STAFF", idStaff)
        startActivity(intent)
    }


    private fun initView() {
        val hView = navigation_drawer.getHeaderView(0)
        val tvNameUser = hView.findViewById(R.id.tvNameUser) as TextView
        tvNameUser.text = MySharedPreferences.getNameUser(this)

        navigation_drawer?.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.android -> {
                    startActivity(Intent(this, ActivityHomePage::class.java))
                }
                R.id.staff -> {
                    startActivity(Intent(this, ActivityListStaff::class.java))
                }
                R.id.logout -> {
                    startActivity(Intent(this, ActivityLogin::class.java))
                }
            }
            drawer_layout!!.closeDrawer(GravityCompat.START)
            true
        }
        toolBar?.title = "Quản lí nhân viên"
        toolBar.inflateMenu(R.menu.item_menu)
        toolBar?.setTitleTextColor(Color.BLACK)
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolBar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_open
        )
        drawer_layout?.addDrawerListener(toggle)
        toggle.syncState()

        toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.imgThemLoaiThucDon -> {
                    val intent = Intent(this, ActivityThemNhanVien::class.java)
                    startActivity(intent)
                }

            }
            true
        }
    }
}