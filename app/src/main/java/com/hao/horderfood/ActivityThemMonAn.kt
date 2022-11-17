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
import com.hao.horderfood.datahelper.ManageFoodHelper
import com.hao.horderfood.datahelper.ManageTypeFoodHelper
import kotlinx.android.synthetic.main.activity_them_mon_an.*
import java.io.ByteArrayOutputStream

class ActivityThemMonAn : AppCompatActivity() {

    var manageFoodHelper:ManageFoodHelper ? = null
    var manageTypeFoodHelper:ManageTypeFoodHelper? = null
    var pickedPhoto : Uri? = null
    var pickedBitmap : Bitmap? = null
    private var idTypeFood:Int? = null
    private var nameTypeFood:String? = null
    var countClick:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_them_mon_an)
        manageFoodHelper = ManageFoodHelper(this)
        manageFoodHelper?.closeSql()
        manageTypeFoodHelper = ManageTypeFoodHelper(this)
        manageTypeFoodHelper?.closeSql()
        initFunc()
        imgFood.setOnClickListener {
            pickPhoto()
        }
    }

    private fun initFunc() {
        imgGoToBackListMonAn.setOnClickListener {
            onBackPressed()
        }
        btnGoToBackListMonAn.setOnClickListener {
            onBackPressed()
        }
        idTypeFood = intent.getIntExtra("ID_TYPE",1)

        nameTypeFood = manageTypeFoodHelper!!.getNameTypeFromID(idTypeFood!!)

        edtLoaiThucDon.setText(nameTypeFood)

        btnThemMonAn.setOnClickListener {
            val nameFood = edtTenMonAn.text.toString()
            val priceFood = edtGiaTienMonAn.text.toString()
            if (TextUtils.isEmpty(nameFood))
            {
                Toast.makeText(this, "Vui lòng nhập tên món ăn", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(priceFood))
            {
                Toast.makeText(this, "Vui lòng nhập giá tiền", Toast.LENGTH_SHORT).show()
            }
            else if(countClick == 0)
            {
                Toast.makeText(this, "Vui lòng chọn ảnh cho món ăn", Toast.LENGTH_SHORT).show()
            }
            else if (countSizeImage(imgFood) > 5000000)
            {
                Toast.makeText(this, "Hình ảnh size quá lớn !!!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                manageFoodHelper?.addNewFood(nameFood,priceFood.toInt(),convertToArrayByte(imgFood),idTypeFood!!)
                Toast.makeText(this, "Thêm món ăn thành công !!!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,ActivityListMonAn::class.java)
                intent.putExtra("ID_TYPE_FOOD",idTypeFood)
                startActivity(intent)
            }
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this,ActivityListMonAn::class.java)
        intent.putExtra("ID_TYPE_FOOD",idTypeFood)
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
                    imgFood.setImageBitmap(pickedBitmap)
                    countClick = 1
                }
                else
                {
                    pickedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,pickedPhoto)
                    imgFood.setImageBitmap(pickedBitmap)
                    countClick = 1
                }
            }
        }
    }

    fun convertToArrayByte(img: ImageView): ByteArray {
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