package com.hao.horderfood

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hao.horderfood.datahelper.ManageFoodHelper
import com.hao.horderfood.datahelper.ManageTypeFoodHelper
import com.hao.horderfood.model.Food
import kotlinx.android.synthetic.main.activity_sua_loai_thuc_don.*
import kotlinx.android.synthetic.main.activity_sua_mon_an.*
import kotlinx.android.synthetic.main.activity_them_mon_an.*
import java.io.ByteArrayOutputStream

class ActivitySuaMonAn : AppCompatActivity() {

    var id_food:Int? = null
    var id_type:Int? = null
    var manageFoodHelper: ManageFoodHelper? = null
    var manageTypeFoodHelper: ManageTypeFoodHelper? = null
    var nameTypeFood :String? = null
    var pickedPhoto : Uri? = null
    var pickedBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sua_mon_an)
        manageFoodHelper = ManageFoodHelper(this)
        manageFoodHelper?.closeSql()
        manageTypeFoodHelper = ManageTypeFoodHelper(this)
        manageTypeFoodHelper?.closeSql()
        initFunc()
    }

    private fun initFunc() {

        id_food = intent.getIntExtra("ID_FOOD",0)
        id_type = intent.getIntExtra("ID_TYPE_FOOD",0)

        imgGoToBackListMonAn1.setOnClickListener {
            onBackPressed()
        }

        btnGoToBackListMonAn1.setOnClickListener {
            onBackPressed()
        }

        var food : Food? = manageFoodHelper?.getFoodFromID(id_food!!)

        var img: ByteArray? = food?.imgFood
        var bitmap : Bitmap = BitmapFactory.decodeByteArray(img,0,img!!.size)

        imgNewFood.setImageBitmap(bitmap)
        edtTenMoiMonAn.setText(food?.nameFood)
        edtGiaTienMoiMonAn.setText(food?.priceFood.toString())

        nameTypeFood = manageTypeFoodHelper?.getNameTypeFromID(id_type!!)

        edtLoaiMoiThucDon.setText(nameTypeFood)

        imgNewFood.setOnClickListener {
            pickPhoto()
        }

        btnSuaMonAn.setOnClickListener {
            val newName:String = edtTenMoiMonAn.text.toString()
            val newPrice:String = edtGiaTienMoiMonAn.text.toString()
            if (TextUtils.isEmpty(newName))
            {
                Toast.makeText(this, "Vui lòng nhập tên món ăn !!!", Toast.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(newPrice))
            {
                Toast.makeText(this, "Vui lòng nhập giá tiền món ăn !!!", Toast.LENGTH_SHORT).show()
            }
            else if (countSizeImage(imgNewFood) > 5000000)
            {
                Toast.makeText(this, "Hình ảnh size quá lớn !!!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                manageFoodHelper?.updateFood(id_food!!,newName,newPrice.toInt(),convertToArrayByte(imgNewFood))
                Toast.makeText(this, "Sửa thành công !!!", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,ActivityListMonAn::class.java)
        intent.putExtra("ID_TYPE_FOOD",id_type)
        intent.putExtra("NAME_TYPE_FOOD",nameTypeFood)
        startActivity(intent)
    }

    private fun pickPhoto( )
    {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else
        {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null)
        {
            pickedPhoto = data.data
            if (pickedPhoto != null)
            {
                if (Build.VERSION.SDK_INT >= 28)
                {
                    val source = ImageDecoder.createSource(this.contentResolver,pickedPhoto!!)
                    pickedBitmap = ImageDecoder.decodeBitmap(source)
                    imgNewFood.setImageBitmap(pickedBitmap)
                }
                else
                {
                    pickedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,pickedPhoto)
                    imgNewFood.setImageBitmap(pickedBitmap)
                }
            }
        }
    }
    private fun convertToArrayByte(img: ImageView): ByteArray {
        val bitmapDrawable = img.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun countSizeImage(img: ImageView): Long {
        val bitmapDrawable = img.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        var imageInByte: ByteArray = stream.toByteArray()

        return imageInByte.size.toLong()
    }
}