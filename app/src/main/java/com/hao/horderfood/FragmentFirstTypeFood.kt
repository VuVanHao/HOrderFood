package com.hao.horderfood

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.hao.horderfood.datahelper.ManageTypeFoodHelper
import com.hao.horderfood.model.TypeFood
import kotlinx.android.synthetic.main.fragment_first_type_food.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFirstTypeFood.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentFirstTypeFood : Fragment() {

    private var manageTypeFoodHelper: ManageTypeFoodHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first_type_food, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        manageTypeFoodHelper = ManageTypeFoodHelper(context)
        manageTypeFoodHelper?.closeSql()

        var firstTypeFood:TypeFood = manageTypeFoodHelper!!.getFirstTypeFood()

        var img: ByteArray? = firstTypeFood?.imgType
        var bitmap : Bitmap = BitmapFactory.decodeByteArray(img,0,img!!.size)

        var tvFirstTypeFood:TextView? = view?.findViewById(R.id.tvFirstTypeFood)
        var imgFirstLoaiThucDon:ImageView? = view?.findViewById(R.id.imgFirstLoaiThucDon)

        imgFirstLoaiThucDon?.setImageBitmap(bitmap)
        tvFirstTypeFood?.text = firstTypeFood.nameType
    }

    companion object {
        @JvmStatic
        fun newInstance(): FragmentFirstTypeFood {
            val fragment = FragmentFirstTypeFood()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}