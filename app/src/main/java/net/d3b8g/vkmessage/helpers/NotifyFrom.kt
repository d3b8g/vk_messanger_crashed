package net.d3b8g.vkmessage.helpers

import android.content.Context
import android.widget.Toast
import net.d3b8g.vkmessage.R

class NotifyFrom(val context: Context){

    val PREFS_FILENAME = "com.vkmessage.conversations.user_notify_id."

    inline fun new_notify_user(chat_id:Long,user_id:Long):Boolean{
        return if(wereTheyNotify(chat_id,user_id).contains(user_id.toString())){
            false
        }else{
            val prefs = context.getSharedPreferences("$PREFS_FILENAME$chat_id",0)
            val editor=prefs!!.edit()
            editor.putLong(getUnixDataNow().toString(),user_id)
            editor.apply()
            Toast.makeText(context,context.getString(R.string.right_add_notify_id),Toast.LENGTH_SHORT).show()
            true
        }
        return false
    }

    inline fun wereTheyNotify(chat_id:Long,user_id:Long):ArrayList<String>{
        val prefs = context.getSharedPreferences("$PREFS_FILENAME$chat_id",0)
        val allEntries = prefs.all
        var mrt:ArrayList<String> = ArrayList()
        allEntries?.forEach{
            mrt.add("${it.value}")
        }
        return mrt
    }

}