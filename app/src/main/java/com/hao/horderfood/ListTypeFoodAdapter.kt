package com.hao.horderfood

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.hao.horderfood.model.TypeFood


class ListTypeFoodAdapter(context: Context,list:ArrayList<TypeFood>) : BaseAdapter() {

    var typeFood_List:ArrayList<TypeFood>? = null
    var inflater:LayoutInflater? = null
    var activate:Boolean = false
    var position:Int = 9999
    var mContext :Context? = null


    init {
        typeFood_List = list
        inflater = LayoutInflater.from(context)
        mContext = context
    }

    override fun getCount(): Int {
        return typeFood_List!!.size
    }

    override fun getItem(p0: Int): TypeFood? {
        return  typeFood_List?.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view : View ?= null
        view = inflater?.inflate(R.layout.custom_list_loai_thuc_don,p2,false)

        val tvNameTypeFood:TextView = view?.findViewById(R.id.tvNameTypeFood) as TextView
        val imgTypeFood:ImageView = view.findViewById(R.id.imgItemLoaiThucDon) as ImageView

        val typeFood : TypeFood = getItem(p0)!!

        val img: ByteArray? = typeFood.imgType
        val bitmap : Bitmap = BitmapFactory.decodeByteArray(img,0,img!!.size)

        imgTypeFood.setImageBitmap(bitmap)
        tvNameTypeFood.text = typeFood.nameType

        var arrow_up : ImageView = view.findViewById(R.id.arrow_up) as ImageView
        var btnSuaItem:TextView = view.findViewById(R.id.btnSuaItem) as TextView
        var btnXoaItem:TextView = view.findViewById(R.id.btnXoaItem) as TextView

        if (activate && p0 == position)
        {
            arrow_up.visibility = View.VISIBLE
            btnSuaItem.visibility = View.VISIBLE
            btnXoaItem.visibility = View.VISIBLE
        }
        else
        {
            arrow_up.visibility = View.GONE
            btnSuaItem.visibility = View.GONE
            btnXoaItem.visibility = View.GONE
        }

        btnXoaItem.setOnClickListener {
            ( mContext as ActivityListLoaiThucDon).DelTyeFood(p0)
        }
        btnSuaItem.setOnClickListener {
            ( mContext as ActivityListLoaiThucDon).GoToActivitySua(p0)
        }

        return view
    }

    fun activateButtons(activate: Boolean, position:Int) {
        this.activate = activate
        this.position = position
        notifyDataSetChanged() //need to call it for the child views to be re-created with buttons.
    }
}