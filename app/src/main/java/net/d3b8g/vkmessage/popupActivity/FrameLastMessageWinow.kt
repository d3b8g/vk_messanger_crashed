package net.d3b8g.vkmessage.popupActivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.helpers.getChangeInDate
import net.d3b8g.vkmessage.models.HeavyWithAttachmentsMessage
import net.d3b8g.vkmessage.providers.HeavyMessageProvider

class FrameLastMessageWinow(val context: Context,val message_id:Int){

    lateinit var item:HeavyWithAttachmentsMessage

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    fun show(item:HeavyWithAttachmentsMessage){
        val frame = Dialog(context)
        frame.setContentView(R.layout.alert_last_message_view)
        val avatar = frame.findViewById<CircleImageView>(R.id.alert_last_message_avatar)
        val username = frame.findViewById<TextView>(R.id.alert_last_message_username)
        val last_message = frame.findViewById<TextView>(R.id.alert_last_message_message)
        val last_pic = frame.findViewById<ImageView>(R.id.alert_last_message_picture)
        val user_last_seen = frame.findViewById<TextView>(R.id.alert_last_message_online)
        val last_message_date = frame.findViewById<TextView>(R.id.alert_last_message_date)


        item.last_seen?.let { time->
            //user_last_seen.text = getChangeInDate(time,"${usr_params.sexChangerForDate(usr_params.getSex(null))} в сети в")
        }

        avatar.setOnLongClickListener {
            val UserActionsWindow = UserActions(context,item.user_id)
            if(!UserActionsWindow.isShow())
                UserActionsWindow.show("friend_action")
            true
        }

        if(item.last_seen==null){
            user_last_seen.text = item.username_lm
        }

        last_message_date.text = getChangeInDate(item.date_lm,"")

        if(item.typeAttachments.size>0){
            for((count_type, type) in item.typeAttachments.withIndex()){
                when(type){
                    "text"->{
                        last_message.visibility = View.VISIBLE
                        //last_message.text = item.lastMessage
                    }
                    "sticker"->{
                        last_pic.visibility= View.VISIBLE
                        item.attach_link!![count_type]?.let {
                                url-> Picasso.with(context).load(url).into(last_pic)
                        }
                    }
                    "photo"->{
                        last_pic.visibility = View.VISIBLE
                        item.attach_link!![count_type]?.let {
                                url->Picasso.with(context).load(url).into(last_pic)
                        }
                    }
                }
            }

        }

        username.text = item.username_lm

        username.text = item.username_lm

        item.avatar_lm?.let { url-> Picasso.with(context).load(url).into(avatar) }


        frame.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        frame.show()
    }

    fun isShow():Boolean{
        val frame = Dialog(context)
        return frame.isShowing
    }
}
