package com.hao.horderfood.datahelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hao.horderfood.model.TypeFood

class ManageTypeFoodHelper(
    context: Context?
) : SQLiteOpenHelper(context, "TYPE_FOOD_MANAGE", null, 1) {

    val TABLE_TYPE_FOOD = "TYPE_FOOD"
    val ID_TYPE = "id"
    val NAME_TYPE = "NAME"
    val IMG_TYPE = "IMG";

    var sqLiteDatabase:SQLiteDatabase? = null;

    override fun onCreate(p0: SQLiteDatabase?) {
        val sql1 = """CREATE TABLE IF NOT EXISTS $TABLE_TYPE_FOOD ( $ID_TYPE INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, $NAME_TYPE TEXT NOT NULL , $IMG_TYPE Blob )  """
        p0?.execSQL(sql1)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        if (p2 > p1)
        {
            p0?.delete(TABLE_TYPE_FOOD,null,null);
        }
    }

    fun closeSql()
    {
        sqLiteDatabase = readableDatabase;
        sqLiteDatabase?.close();
    }

    fun addNewTypeFood(name: String, hinh: ByteArray?)
    {
        sqLiteDatabase = writableDatabase;
        val contentValues = ContentValues()
        contentValues.put(NAME_TYPE,name)
        contentValues.put(IMG_TYPE,hinh)
        sqLiteDatabase!!.insert(TABLE_TYPE_FOOD,null,contentValues)
        sqLiteDatabase!!.close()
    }

    fun getListTypeFood() : ArrayList<TypeFood>
    {
        val list: ArrayList<TypeFood> = ArrayList()
        sqLiteDatabase = readableDatabase
        var sql = " SELECT * FROM $TABLE_TYPE_FOOD "
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val img = cursor.getBlob(2)
                list.add(TypeFood(id,name,img))
            } while (cursor.moveToNext())
        }
        return list
    }

    fun getFirstTypeFood() : TypeFood
    {
        val type = TypeFood()
        sqLiteDatabase = readableDatabase
        var sql = " SELECT * FROM $TABLE_TYPE_FOOD WHERE $ID_TYPE == 1 "
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                type.idType = cursor.getInt(0)
                type.nameType = cursor.getString(1)
                type.imgType = cursor.getBlob(2)

            } while (cursor.moveToNext())
        }
        return type
    }

    fun getNameTypeFromID(id : Int) : String?
    {
        var nameType:String? = null
        sqLiteDatabase = readableDatabase
        var sql = "SELECT * FROM $TABLE_TYPE_FOOD WHERE $ID_TYPE == $id "
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                nameType = cursor.getString(1)
            } while (cursor.moveToNext())
        }
        return nameType
    }

    fun deleteRecord(id : Int )
    {
        sqLiteDatabase = readableDatabase
        var sql = "DELETE FROM $TABLE_TYPE_FOOD WHERE $ID_TYPE == $id "
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext())
        }
    }

    fun getTypeFoodFromID( id : Int) : TypeFood
    {
        val type = TypeFood()
        sqLiteDatabase = readableDatabase
        var sql = " SELECT * FROM $TABLE_TYPE_FOOD WHERE $ID_TYPE == $id "
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                type.idType = cursor.getInt(0)
                type.nameType = cursor.getString(1)
                type.imgType = cursor.getBlob(2)

            } while (cursor.moveToNext())
        }
        return type
    }

    fun updateTypeFood( id: Int, name:String, hinh: ByteArray )
    {
        sqLiteDatabase = readableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME_TYPE,name)
        contentValues.put(IMG_TYPE,hinh)
        sqLiteDatabase!!.update(TABLE_TYPE_FOOD,contentValues, "$ID_TYPE = ? ",arrayOf<String>("$id"))
        sqLiteDatabase!!.close()

    }

}