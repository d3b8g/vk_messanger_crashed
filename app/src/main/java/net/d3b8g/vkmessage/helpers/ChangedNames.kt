package net.d3b8g.vkmessage.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DATABASE_NAME_1 = "ChangedNames"
val TABLE_NAME_1 = "Names"
val COL_ID_USER_1="user_id"
val COL_NAME_1 = "username"
val COL_ID_1 = "id"

class ChangedNames(context:Context):SQLiteOpenHelper(context, DATABASE_NAME_1,null,1){

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE IF NOT EXISTS  $TABLE_NAME_1 ($COL_ID_1 INTEGER PRIMARY KEY AUTOINCREMENT, "+
        "$COL_ID_USER_1 INTEGER, $COL_NAME_1 VARCHAR(256))"
        db?.execSQL(createTable)
    }


    fun insertData(user_id:String,username:String):Boolean{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_ID_USER_1,user_id)
        cv.put(COL_NAME_1,username)
        var res = db.insert(TABLE_NAME_1,null,cv)
        db.close()
        return res!= (-1).toLong()
    }

    fun isChanged(id:String):String?{
        val db = this.writableDatabase
        var mCur: Cursor = db.rawQuery("SELECT $COL_NAME_1 FROM $TABLE_NAME_1 WHERE $COL_ID_USER_1 = $id", arrayOf())
        mCur.moveToFirst()
        while (!mCur.isAfterLast){
            return mCur.getString(mCur.getColumnIndex(COL_NAME_1))
            db.close()
        }
        db.close()
        return null
    }

    

}