package net.d3b8g.vkmessage.popupActivity

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Button
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.helpers.UserGetParameters

class UserActions(val context: Context,val usr_id:Long?) {
    lateinit var usr_act:Dialog

    @RequiresApi(Build.VERSION_CODES.M)
    fun show(param:String){
        usr_act = Dialog(context)
        usr_act.setContentView(R.layout.is_friend_sector)
        var func: Button = usr_act.findViewById(R.id.alert_is_friend_func)
        var changeName: Button = usr_act.findViewById(R.id.alert_is_friend_changeName)
        changeName.setOnClickListener {
            if(!ChangeNameWindow(usr_act.context).isShow())
                ChangeNameWindow(usr_act.context).changeNameWindow()
        }
        var user_id:Long = 0
        when(param){
            "friend_action"->{
                var isFriend:Boolean = false
                usr_id?.let { idd->
                    isFriend = UserGetParameters(null,idd).isMyFriend()
                    user_id = idd
                }
                if(isFriend){
                    func.text = "Удалить из друзей"
                    func.setTextColor(context.getColor(R.color.red_text))
                }else{
                    func.text = "Добавить в друзья"
                }
                func.setOnClickListener {
                    var usr = UserGetParameters(null,user_id)
                    if(!isFriend){
                        if(usr.addToFriends(context))
                            func.text = "Удалить из друзей"
                        func.setTextColor(context.getColor(R.color.red_text))
                    }
                    else{
                        if(usr.removeFromFriends(context))
                            func.text = "Добавить в друзья"
                    }
                }
            }
            "copy_id"->{
                usr_id?.let { idd->
                    user_id = idd
                }
                func.text = "Скопировать ID"
                func.setOnClickListener {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Ссылка скопирована",user_id.toString())
                    clipboard.primaryClip = clip
                }
            }
        }

        usr_act.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        usr_act.show()
    }

    fun isShow():Boolean{
        usr_act = Dialog(context)
        return usr_act.isShowing
    }
}