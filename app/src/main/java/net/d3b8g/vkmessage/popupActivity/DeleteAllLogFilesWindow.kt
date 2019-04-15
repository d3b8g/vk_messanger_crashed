package net.d3b8g.vkmessage.popupActivity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.Toast
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.helpers.HolderAction

class DeleteAllLogFilesWindow(val context: Context) {

    fun show(chat_id:Long){
        val frame = Dialog(context)
        frame.setContentView(R.layout.alert_delete_logfiles)
        val agree_to_delete = frame.findViewById<Button>(R.id.agree_to_delete_logfiles)
        agree_to_delete.setOnClickListener {
            HolderAction(context).destroy_log(chat_id)
            Toast.makeText(context,context.getString(R.string.clear_all_logfiles),Toast.LENGTH_SHORT).show()
            frame.dismiss()
        }
        frame.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        frame.show()
    }
    fun isShow():Boolean{
        val frame = Dialog(context)
        return frame.isShowing
    }
}