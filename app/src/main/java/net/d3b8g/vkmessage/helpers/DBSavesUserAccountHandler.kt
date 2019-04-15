package net.d3b8g.vkmessage.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import net.d3b8g.vkmessage.models.SavesAccountUsers
import java.io.File

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



val DATABASE_NAME = "UsersAccount"
val TABLE_NAME = "Accounts"
val COL_ID_USER="user_id"
val COL_NAME = "username"
val COL_AVATAR = "avatar"
val COL_DATA_LAST_ACTIV = "data_last"
val COL_ID = "id"

class DBSavesUserAccountHandler(val context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,1){

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE IF NOT EXISTS   $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_ID_USER INTEGER, $COL_NAME VARCHAR(256), $COL_AVATAR VARCHAR(256)," +
                " $COL_DATA_LAST_ACTIV VARCHAR(256))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData(user:SavesAccountUsers){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_ID_USER,user.user_id)
        cv.put(COL_NAME,user.username)
        cv.put(COL_AVATAR,user.avatar)
        cv.put(COL_DATA_LAST_ACTIV,user.last_data)
        db.insert(TABLE_NAME,null,cv)
        Log.e("RRR","добавил акк ${user.id}")
    }

    fun saveId(id:Long):String{
        val db = this.writableDatabase
        var mCur:Cursor = db.rawQuery("SELECT $COL_ID_USER FROM $TABLE_NAME WHERE $COL_ID_USER = $id", arrayOf())
        mCur.moveToFirst()
        while (!mCur.isAfterLast){
            return mCur.getString(mCur.getColumnIndex(COL_ID_USER))
        }
        return ""
    }

    val allIdD:ArrayList<SavesAccountUsers>?
    get() {
        val taskList:ArrayList<SavesAccountUsers> = ArrayList()
        val db = this.readableDatabase
        val db_r = this.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        if(!context.getDatabasePath("/data/data/net.d3b8g.vkmessage/databases/UsersAccount.db3").exists()){
            onCreate(db_r)
        }else{
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val rb:SavesAccountUsers = SavesAccountUsers(
                        user_id = cursor.getLong(cursor.getColumnIndex(COL_ID_USER)),
                        username = cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        avatar = cursor.getString(cursor.getColumnIndex(COL_AVATAR)),
                        last_data = cursor.getString(cursor.getColumnIndex(COL_DATA_LAST_ACTIV))
                    )
                    taskList.add(rb)
                } while (cursor.moveToNext())
            }
            db.close()
        }
        return taskList
    }

    fun updateTime(id:Long){
        val dataFormat: DateFormat = SimpleDateFormat("EEE, dd MMM yyyy, HH:mm")
        val db = this.writableDatabase
        db.execSQL("UPDATE $TABLE_NAME SET $COL_DATA_LAST_ACTIV = '${dataFormat.format(
            Calendar.getInstance().time)}' WHERE $COL_ID_USER = $id ")
        Log.d("RRR","time of visit updated")
        db.close()
    }

    fun delTable(){
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        Log.e("RRR","удалил")
    }

}