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
import com.hao.horderfood.datahelper.ManageTypeFoodHelper
import com.hao.horderfood.model.TypeFood
import kotlinx.android.synthetic.main.activity_sua_loai_thuc_don.*
import kotlinx.android.synthetic.main.activity_them_loai_thuc_don.*
import java.io.ByteArrayOutputStream

class ActivitySuaLoaiThucDon : AppCompatActivity() {

    var pickedPhoto : Uri? = null
    var pickedBitmap : Bitmap? = null
    var manageTypeFoodHelper: ManageTypeFoodHelper? = null
    var pos :Int? = null
    var listTypeFood:ArrayList<TypeFood> ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sua_loai_thuc_don)

        manageTypeFoodHelper = ManageTypeFoodHelper(this)
        manageTypeFoodHelper?.closeSql()
        initFunc()
        initView()
        imgNewTypeOfFood.setOnClickListener {
            pickPhoto()
        }

    }

    private fun initView() {
        pos = intent.getIntExtra("ID_TYPE",0)
        listTypeFood = manageTypeFoodHelper?.getListTypeFood()
        val id_type:Int? = listTypeFood?.get(pos!!)?.idType

        val typeFood : TypeFood = manageTypeFoodHelper!!.getTypeFoodFromID(id_type!!)

        var img: ByteArray? = typeFood.imgType
        var bitmap : Bitmap = BitmapFactory.decodeByteArray(img,0,img!!.size)

        edtTenMoiLoaiThucDon.setText(typeFood.nameType)
        imgNewTypeOfFood.setImageBitmap(bitmap)

        btnSuaLoaiThucDon.setOnClickListener {
            val newName : String = edtTenMoiLoaiThucDon.text.toString()
            if (TextUtils.isEmpty(newName))
            {
                Toast.makeText(this, "Vui lòng nhập tên loại thực đơn !!!", Toast.LENGTH_SHORT).show()
            }
            else if (countSizeImage(imgNewTypeOfFood) > 5000000)
            {
                Toast.makeText(this, "Hình ảnh size quá lớn !!!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                manageTypeFoodHelper?.updateTypeFood(id_type,newName,convertToArrayByte(imgNewTypeOfFood))
                Toast.makeText(this, "Sửa thành công !!!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,ActivityListLoaiThucDon::class.java))
            }
        }
    }

    private fun initFunc() {
        imgGoToBackListLoaiThucDon1.setOnClickListener {
            onBackPressed()
        }

        btnGoToBackListLoaiThucDon1.setOnClickListener {
            onBackPressed()
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
                    imgNewTypeOfFood.setImageBitmap(pickedBitmap)
                }
                else
                {
                    pickedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,pickedPhoto)
                    imgNewTypeOfFood.setImageBitmap(pickedBitmap)
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