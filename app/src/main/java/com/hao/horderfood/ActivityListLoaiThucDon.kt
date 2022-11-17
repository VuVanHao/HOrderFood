package com.hao.horderfood

import android.content.Intent
import android.database.CursorWindow
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.hao.horderfood.datahelper.ManageTypeFoodHelper
import com.hao.horderfood.model.TypeFood
import kotlinx.android.synthetic.main.activity_home_page.drawer_layout
import kotlinx.android.synthetic.main.activity_home_page.navigation_drawer
import kotlinx.android.synthetic.main.activity_home_page.toolBar
import kotlinx.android.synthetic.main.activity_list_loai_thuc_don.*
import java.lang.reflect.Field

class ActivityListLoaiThucDon : AppCompatActivity() {

    private var username:String? = "";
    var manageTypeFoodHelper: ManageTypeFoodHelper? = null
    var list_typeFood:ArrayList<TypeFood>?= null
    var listTypeFoodAdapter:ListTypeFoodAdapter?= null
    var flag:Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_loai_thuc_don)
        manageTypeFoodHelper = ManageTypeFoodHelper(this)
        manageTypeFoodHelper?.closeSql()
        manageTypeFoodHelper?.deleteRecord(9)
        initUserLogin()
        initView()
        initData()

    }

    private fun initData() {
        list_typeFood = manageTypeFoodHelper?.getListTypeFood()

        listTypeFoodAdapter = ListTypeFoodAdapter(this,list_typeFood!!)
        lvListThucDon.adapter = listTypeFoodAdapter
        listTypeFoodAdapter?.notifyDataSetChanged()

        lvListThucDon.setOnItemClickListener { _, _, i, _ ->
            val position: Int? = list_typeFood?.get(i)?.idType
            val nameType:String? = list_typeFood?.get(i)?.nameType
            if (!flag)
            {
                val intent = Intent(this,ActivityListMonAn::class.java)
                intent.putExtra("ID_TYPE_FOOD",position)
                intent.putExtra("NAME_TYPE_FOOD",nameType)
                startActivity(intent)
            }
            else
            {
                flag = false
                listTypeFoodAdapter?.activateButtons(false,i)
            }


        }

        lvListThucDon.setOnItemLongClickListener { _, _, i, _ ->
            val position: Int? = list_typeFood?.get(i)?.idType
            if (!flag)
            {
                flag = true
                listTypeFoodAdapter?.activateButtons(true,i)
            }

            return@setOnItemLongClickListener(true)
        }



    }

    fun DelTyeFood( pos : Int)
    {
        val nameType: String? = list_typeFood?.get(pos)?.nameType
        val position: Int? = list_typeFood?.get(pos)?.idType
        val typeFood = list_typeFood?.get(pos)
        var builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Thông báo")
        builder.setMessage("Bạn có muốn xóa $nameType khỏi danh sách không ? ")
        builder.setPositiveButton("Có") { _, i ->
            manageTypeFoodHelper?.deleteRecord(position!!)
            list_typeFood?.remove(typeFood)
            listTypeFoodAdapter?.notifyDataSetChanged()
            flag = false
            listTypeFoodAdapter?.activateButtons(false,i)
        }
        builder.setNegativeButton("Không"){ _,i ->
            flag = false
            listTypeFoodAdapter?.activateButtons(false,i)
        }

        val alert = builder.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    fun GoToActivitySua(pos : Int)
    {
        val intent = Intent(this,ActivitySuaLoaiThucDon::class.java)
        intent.putExtra("ID_TYPE",pos)
        startActivity(intent)
    }

    private fun initUserLogin() {
        username = MySharedPreferences.getNameUser(this)
    }

    private fun initView() {
        val hView = navigation_drawer.getHeaderView(0)
        val tvNameUser = hView.findViewById(R.id.tvNameUser) as TextView
        tvNameUser.text = username



        navigation_drawer?.setNavigationItemSelectedListener { item ->
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
        }
        toolBar?.title = "Quản lí thực đơn"
        toolBar.inflateMenu(R.menu.item_menu)
        toolBar?.setTitleTextColor(Color.BLACK)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_open)
        drawer_layout?.addDrawerListener(toggle)
        toggle.syncState()

        toolBar.setOnMenuItemClickListener { 
            when(it.itemId)
            {
                R.id.imgThemLoaiThucDon ->
                {
                    startActivity(Intent(this,ActivityThemLoaiThucDon::class.java))
                }
                
            }
            true
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,ActivityHomePage::class.java)
        startActivity(intent)
    }

}