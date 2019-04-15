package net.d3b8g.vkmessage.helpers

import android.content.Context
import android.util.Log
import android.widget.Toast
import net.d3b8g.vkmessage.R

class HolderAction(val context:Context){

    val PREFS_FILENAME = "com.vkmessage.conversations.logs"
    val PREFS_FILENAME_NUM="com.vkmessage.conversations.id"

    inline fun new_log(chat_id:Long,message_id:String,event:String){
        val prefs = context.getSharedPreferences("$PREFS_FILENAME.$chat_id",0)
        val editor = prefs!!.edit()
        editor.putString(message_id,event)
        editor.apply()
    }

    inline fun add_id(user_id:Long){
        val prefs_id = context.getSharedPreferences(PREFS_FILENAME_NUM,0)
        val num = (prefs_id.all.size+1).toString()
        val editor = prefs_id!!.edit()
        editor.putLong(num,user_id)
        editor.apply()
        Log.d("RRR","добавил лог")
    }

    inline fun list_listner():ArrayList<Long>?{
        val prefs_id = context.getSharedPreferences(PREFS_FILENAME_NUM,0)
        val allEntries=prefs_id.all
        var mrt:ArrayList<Long>? = ArrayList()
        allEntries?.forEach{
            mrt?.add(it.value.toString().toLong())
        }
        return mrt
    }

    inline fun get_log(chat_id: Long):ArrayList<String>{
        val prefs = context.getSharedPreferences("$PREFS_FILENAME.$chat_id",0)
        val allEntries = prefs.all
        var mrt:ArrayList<String> = ArrayList()
        allEntries?.forEach{
            mrt.add("${it.value}")
        }
        return mrt
    }

    inline fun destroy_log(chat_id: Long){
        val prefs = context.getSharedPreferences("$PREFS_FILENAME.$chat_id",0)
        prefs.edit().clear().apply()
    }

    inline fun stop_writting_log(chat_id: Long):Boolean{

        return try{
            val prefs_id = context.getSharedPreferences(PREFS_FILENAME_NUM,0)
            prefs_id.edit().remove(chat_id.toString()).apply()
            Toast.makeText(context,context.getString(R.string.logging_stop),Toast.LENGTH_SHORT).show()
            true
        }catch (e:Exception){
            false
        }

        return false
    }
}