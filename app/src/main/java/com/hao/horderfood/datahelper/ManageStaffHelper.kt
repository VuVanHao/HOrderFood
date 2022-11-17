package com.hao.horderfood.datahelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hao.horderfood.model.Food
import com.hao.horderfood.model.Staff

class ManageStaffHelper(
    context: Context?
) : SQLiteOpenHelper(context, "STAFF_MANAGE", null, 1) {

    val TABLE_STAFF_MANAGE = "STAFF"
    val ID_STAFF = "id"
    val NAME_STAFF = "NAME"
    val PASS_STAFF = "PASSWORD"
    val GENDER_STAFF = "GENDER"
    val DATE_STAFF = "DATE"
    val CMND_STAFF = "CMND"

    var sqLiteDatabase:SQLiteDatabase? = null;


    override fun onCreate(p0: SQLiteDatabase?) {
        var sql = """CREATE TABLE $TABLE_STAFF_MANAGE ( $ID_STAFF INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , $NAME_STAFF TEXT NOT NULL , $PASS_STAFF TEXT NOT NULL, $GENDER_STAFF TEXT NOT NULL, $DATE_STAFF TEXT NOT NULL, $CMND_STAFF TEXT NOT NULL ) """
        p0?.execSQL(sql);
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        if (p2 > p1)
        {
            p0?.delete(TABLE_STAFF_MANAGE,null,null);
        }
    }

    fun closeSql()
    {
        sqLiteDatabase = readableDatabase;
        sqLiteDatabase?.close();
    }

    fun addNewStaff(name: String, pass:String, gender:String, date:String, cmnd:String)
    {
        sqLiteDatabase = writableDatabase;
        val contentValues = ContentValues()
        contentValues.put(NAME_STAFF,name)
        contentValues.put(PASS_STAFF,pass)
        contentValues.put(GENDER_STAFF,gender)
        contentValues.put(DATE_STAFF,date)
        contentValues.put(CMND_STAFF,cmnd)
        sqLiteDatabase!!.insert(TABLE_STAFF_MANAGE,null,contentValues)
        sqLiteDatabase!!.close()
    }

    fun checkAccountStaff(name:String, pass:String): Boolean? {
        var check:Boolean? = false
        sqLiteDatabase = readableDatabase
        var sql = """SELECT*FROM $TABLE_STAFF_MANAGE WHERE $NAME_STAFF = '$name' AND $PASS_STAFF = '$pass' """
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst())
        {
            check = cursor.count != 0
        }
        cursor?.close()
        return check
    }

    fun getListStaff() : ArrayList<Staff>
    {
        val list: ArrayList<Staff> = ArrayList()
        sqLiteDatabase = readableDatabase
        var sql = " SELECT * FROM $TABLE_STAFF_MANAGE"
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val pass = cursor.getString(2)
                val gender = cursor.getString(3)
                val date = cursor.getString(4)
                val idCard = cursor.getString(5)
                list.add(Staff(id,name,pass,gender,date,idCard))
            } while (cursor.moveToNext())
        }
        return list
    }

    fun deleteRecord(id : Int )
    {
        sqLiteDatabase = readableDatabase
        var sql = "DELETE FROM $TABLE_STAFF_MANAGE WHERE $ID_STAFF == $id "
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext())
        }
    }
    fun updateStaff( id: Int, name:String, pass:String, gender:String, date:String, cmnd:String )
    {
        sqLiteDatabase = readableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME_STAFF,name)
        contentValues.put(PASS_STAFF,pass)
        contentValues.put(GENDER_STAFF,gender)
        contentValues.put(DATE_STAFF,date)
        contentValues.put(CMND_STAFF,cmnd)
        sqLiteDatabase!!.update(TABLE_STAFF_MANAGE,contentValues, "$ID_STAFF = ? ",arrayOf<String>("$id"))
        sqLiteDatabase!!.close()

    }

    fun getStaffFromID(id :Int) : Staff
    {
        val staff = Staff()
        sqLiteDatabase = readableDatabase
        var sql = " SELECT * FROM $TABLE_STAFF_MANAGE WHERE $ID_STAFF == $id "
        val cursor = sqLiteDatabase?.rawQuery(sql,null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                staff.idStaff = cursor.getInt(0)
                staff.nameStaff = cursor.getString(1)
                staff.passStaff = cursor.getString(2)
                staff.genderStaff = cursor.getString(3)
                staff.dateStaff = cursor.getString(4)
                staff.idCardStaff = cursor.getString(5)
            } while (cursor.moveToNext())
        }
        return staff
    }

    fun getProfilesCount(): Int {
        val countQuery = "SELECT  * FROM $TABLE_STAFF_MANAGE"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(countQuery, null)
        val count: Int = cursor.count
        cursor.close()
        return count
    }
}