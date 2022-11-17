package com.hao.horderfood

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.hao.horderfood.model.Staff
import java.util.zip.Inflater

class ListStaffAdapter(context: Context, list: ArrayList<Staff>) : BaseAdapter() {

    var listStaff: ArrayList<Staff>? = null
    var inflater: LayoutInflater? = null
    var activate: Boolean = false
    var flag: Boolean = false
    var position: Int = 9999
    var mContext: Context? = null

    init {
        listStaff = list
        inflater = LayoutInflater.from(context)
        mContext = context
    }

    override fun getCount(): Int {
        return listStaff!!.size
    }

    override fun getItem(p0: Int): Staff? {
        return listStaff?.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
        var view: View? = null
        view = inflater?.inflate(R.layout.custom_list_nhan_vien, p2, false)

        val tvNameStaff: TextView = view?.findViewById(R.id.tvnameStaff) as TextView
        val tvGenderStaff: TextView = view?.findViewById(R.id.tvGenderStaff) as TextView
        val tvDateStaff: TextView = view?.findViewById(R.id.tvDateStaff) as TextView

        val staff = getItem(p0)!!
        tvNameStaff.text = staff.nameStaff
        tvGenderStaff.text = staff.genderStaff
        tvDateStaff.text = staff.dateStaff

        var arrow_up: ImageView = view.findViewById(R.id.arrow_up) as ImageView
        var btnSuaItem: TextView = view.findViewById(R.id.btnSuaStaffItem) as TextView
        var btnXoaItem: TextView = view.findViewById(R.id.btnXoaStaffItem) as TextView
        val nameUser: String? = MySharedPreferences.getNameUser(mContext)
        if (nameUser == "admin" && activate && p0 == position) {
            arrow_up.visibility = View.VISIBLE
            btnSuaItem.visibility = View.VISIBLE
            btnXoaItem.visibility = View.VISIBLE
        } else {
            arrow_up.visibility = View.GONE
            btnSuaItem.visibility = View.GONE
            btnXoaItem.visibility = View.GONE
        }

        btnXoaItem.setOnClickListener {
            (mContext as ActivityListStaff).deleteItemStaff(p0)
        }

        btnSuaItem.setOnClickListener {
            (mContext as ActivityListStaff).goToSuaItemStaff(p0)

        }

        return view
    }

    fun activateButtons(activate: Boolean, position: Int) {
        this.activate = activate
        this.position = position
        notifyDataSetChanged() //need to call it for the child views to be re-created with buttons.
    }
}