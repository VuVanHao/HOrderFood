package com.hao.horderfood

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.hao.horderfood.datahelper.ManageTypeFoodHelper
import kotlinx.android.synthetic.main.activity_them_loai_thuc_don.*
import java.io.ByteArrayOutputStream

class ActivityThemLoaiThucDon : AppCompatActivity() {

    var pickedPhoto : Uri? = null
    var pickedBitmap : Bitmap? = null
    var manageTypeFoodHelper:ManageTypeFoodHelper? = null
    var countClick:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_them_loai_thuc_don)
        manageTypeFoodHelper = ManageTypeFoodHelper(this)
        manageTypeFoodHelper?.closeSql()
        imgTypeOfFood.setOnClickListener {
            pickPhoto()
        }
        initFunc()
    }

    private fun initFunc() {
        imgGoToBackListLoaiThucDon.setOnClickListener {
            startActivity(Intent(this,ActivityListLoaiThucDon::class.java))
        }
        btnGoToBackListLoaiThucDon.setOnClickListener {
            startActivity(Intent(this,ActivityListLoaiThucDon::class.java))
        }

        btnThemLoaiThucDon.setOnClickListener {
            val nameTypeFood = edtTenLoaiThucDon.text.toString()
            if (TextUtils.isEmpty(nameTypeFood))
            {
                Toast.makeText(this, "Vui lòng nhập tên loại thực đơn !!!", Toast.LENGTH_SHORT).show()
            }
            else if (countClick == 0)
            {
                Toast.makeText(this, "Vui lòng chọn ảnh cho loại thực đơn !!!", Toast.LENGTH_SHORT).show()
            }
            else if (countSizeImage(imgTypeOfFood) > 5000000)
            {
                Toast.makeText(this, "Hình ảnh size quá lớn !!!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                manageTypeFoodHelper?.addNewTypeFood(nameTypeFood,convertToArrayByte(imgTypeOfFood))
                Toast.makeText(this, "Thêm thành công !!!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,ActivityListLoaiThucDon::class.java))
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this,ActivityListLoaiThucDon::class.java))
    }

    private fun pickPhoto( )
    {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else
        {
            val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                    imgTypeOfFood.setImageBitmap(pickedBitmap)
                    countClick = 1
                }
                else
                {
                    pickedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,pickedPhoto)
                    imgTypeOfFood.setImageBitmap(pickedBitmap)
                    countClick = 1
                }
            }
        }
    }
    private fun convertToArrayByte(img: ImageView): ByteArray? {
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