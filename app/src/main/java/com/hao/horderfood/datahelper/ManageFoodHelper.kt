package com.hao.horderfood.datahelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hao.horderfood.model.Food
import com.hao.horderfood.model.TypeFood

class ManageFoodHelper(
    context: Context?
) : SQLiteOpenHelper(context, "FOOD_MANAGE", null, 1) {

    val TABLE_FOOD = "TYPE_FOOD"
    val ID_FOOD = "id"
    val NAME_FOOD = "NAME"
    val PRICE_FOOD = "PRICE"
    val IMG_FOOD = "IMG";
    val ID_TYPE = "id_type"
    var sqLiteDatabase:SQLiteDatabase? = null;

    override fun onCreate(p0: SQLiteDatabase?) {
        val sql = """CREATE TABLE IF NOT EXISTS $TABLE_FOOD ( $ID_FOOD INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , $NAME_FOOD TEXT NOT NULL , $PRICE_FOOD INTEGER NOT NULL, $IMG_FOOD Blob , $ID_TYPE INTEGER NOT NULL )"""
        p0?.execSQL(sql)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        if (p2 > p1)
        {
            p0?.delete(TABLE_FOOD,null,null);
        }
    }

    fun closeSql()
    {
        sqLiteDatabase = readableDatabase;
        sqLiteDatabase?.close();
    }

    fun addNewFood(name:String, price:Int, hinh:ByteArray, id_type:Int)
    {
        sqLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME_FOOD,name)
        contentValues.put(PRICE_FOOD,price)
        contentValues.put(IMG_FOOD,hinh)
        contentValues.put(ID_TYPE,id_type)
        sqLiteDatabase!!.insert(TABLE_FOOD,null,contentValues)
        sqLiteDatabase!!.close()
    }

    fun getListFood(id : Int) : ArrayList<Food>
    {
        val list: ArrayList<Food> = ArrayList()
        sqLiteDatabase = readableDatabase
        var sql = " SELECT * FROM $TABLE_FOOD WHERE $ID_TYPE == $id"
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val price = cursor.getInt(2)
                val img = cursor.getBlob(3)
                val id_type = cursor.getInt(4)
                list.add(Food(id,name,price,img,id_type))
            } while (cursor.moveToNext())
        }
        return list
    }

    fun getFoodFromID( id : Int) : Food
    {
        val food = Food()
        sqLiteDatabase = readableDatabase
        var sql = " SELECT * FROM $TABLE_FOOD WHERE $ID_FOOD == $id "
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                food.idFood = cursor.getInt(0)
                food.nameFood = cursor.getString(1)
                food.priceFood = cursor.getInt(2)
                food.imgFood = cursor.getBlob(3)
                food.idType = cursor.getInt(4)
            } while (cursor.moveToNext())
        }
        return food
    }

    fun updateFood( id: Int, name:String, price:Int , hinh: ByteArray )
    {
        sqLiteDatabase = readableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME_FOOD,name)
        contentValues.put(PRICE_FOOD,price)
        contentValues.put(IMG_FOOD,hinh)
        sqLiteDatabase!!.update(TABLE_FOOD,contentValues, "$ID_FOOD = ? ",arrayOf<String>("$id"))
        sqLiteDatabase!!.close()
    }

    fun deleteRecordFood(id : Int )
    {
        sqLiteDatabase = readableDatabase
        var sql = "DELETE FROM $TABLE_FOOD WHERE $ID_FOOD == $id "
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext())
        }
    }
}