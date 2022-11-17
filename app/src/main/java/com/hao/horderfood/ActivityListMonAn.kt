package com.hao.horderfood

import android.content.Intent
import android.database.CursorWindow
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
import com.hao.horderfood.datahelper.ManageFoodHelper
import com.hao.horderfood.model.Food
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.activity_home_page.drawer_layout
import kotlinx.android.synthetic.main.activity_home_page.navigation_drawer
import kotlinx.android.synthetic.main.activity_home_page.toolBar
import kotlinx.android.synthetic.main.activity_list_mon_an.*
import java.lang.reflect.Field

class ActivityListMonAn : AppCompatActivity() {

    private var idTypeFood: Int? = null
    private var nameTypeFood: String? = null
    var foodList: ArrayList<Food>? = null
    var manageFoodHelper: ManageFoodHelper? = null
    var listFoodRCAdapter: ListFoodRCAdapter? = null
    var posItem: Int? = 9999
    var flag: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_mon_an)
        manageFoodHelper = ManageFoodHelper(this)
        manageFoodHelper?.closeSql()
        manageFoodHelper?.deleteRecordFood(6)
        initUser()
        initView()
    }

    private fun initUser() {
        idTypeFood = intent.getIntExtra("ID_TYPE_FOOD", 1)
        nameTypeFood = intent.getStringExtra("NAME_TYPE_FOOD")
        tvTitle.text = nameTypeFood
    }

    private fun initView() {
        val hView = navigation_drawer.getHeaderView(0)
        val tvNameUser = hView.findViewById(R.id.tvNameUser) as TextView
        tvNameUser.text = MySharedPreferences.getNameUser(this)

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
        toolBar?.title = "Quản lí thực đơn"
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
                    var intent = Intent(this, ActivityThemMonAn::class.java)
                    intent.putExtra("ID_TYPE", idTypeFood)
                    startActivity(intent)
                }

            }
            true
        }

        recycListFood.layoutManager = GridLayoutManager(this, 2)
        foodList = ArrayList()
        foodList = manageFoodHelper?.getListFood(idTypeFood!!)
        listFoodRCAdapter = ListFoodRCAdapter(foodList!!, this)
        recycListFood.adapter = listFoodRCAdapter
        listFoodRCAdapter?.notifyDataSetChanged()

    }

    override fun onBackPressed() {
        var intent = Intent(this, ActivityListLoaiThucDon::class.java)
        startActivity(intent)
    }

    fun getItemClicked(pos: Int) {
        this.posItem = pos
        if (!flag) {
            flag = true
            listFoodRCAdapter?.activateButtons(true, pos)
        }
        listFoodRCAdapter?.activateButtons(true, pos)
    }

    fun clickItemListener(pos: Int) {
        if (flag) {
            flag = false
            listFoodRCAdapter?.activateButtons(false, pos)
        }

    }

    fun goToActivitySuaMonAn(pos: Int) {
        val id_food: Int? = foodList!!.get(pos).idFood
        val id_type: Int? = foodList!!.get(pos).idType
        val intent = Intent(this, ActivitySuaMonAn::class.java)
        intent.putExtra("ID_FOOD", id_food)
        intent.putExtra("ID_TYPE_FOOD", id_type)
        startActivity(intent)
    }

    fun deleteFoodItem(pos : Int)
    {
        val id_food_delete : Int? = foodList?.get(pos)?.idFood
        val name_food_delete :String ? = foodList!!.get(pos).nameFood
        val food_delete: Food = foodList!!.get(pos)
        var builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Thông báo")
        builder.setMessage("Bạn có muốn xóa $name_food_delete khỏi danh sách không ? ")
        builder.setPositiveButton("Có") { _, i ->
            manageFoodHelper?.deleteRecordFood(id_food_delete!!)
            foodList?.remove(food_delete)
            listFoodRCAdapter?.notifyDataSetChanged()
            flag = false
            listFoodRCAdapter?.activateButtons(false,i)
        }
        builder.setNegativeButton("Không"){ _,i ->
            flag = false
            listFoodRCAdapter?.activateButtons(false,i)
        }

        val alert = builder.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()

    }
}