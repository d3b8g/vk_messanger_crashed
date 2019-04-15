package net.d3b8g.vkmessage.popupActivity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.*
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import com.vk.sdk.api.*
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.helpers.ChangedNames

class ChangeNameWindow {

    var context:Context

    constructor(context:Context){
        this.context = context
    }
    lateinit var changeWindow: Dialog

    fun changeNameWindow() {
        changeWindow = Dialog(context)
        changeWindow.setContentView(R.layout.alert_change_name_popup)
        var id_changed = changeWindow.findViewById<EditText>(R.id.change_user_id)
        var agree_change = changeWindow.findViewById<ImageButton>(R.id.change_user_agree)
        var username_changed = changeWindow.findViewById<EditText>(R.id.change_user_username)
        var result_layout = changeWindow.findViewById<LinearLayout>(R.id.change_user_layout_result)
        var result_avatar = changeWindow.findViewById<CircleImageView>(R.id.change_user_avatar_result)
        var result_name = changeWindow.findViewById<TextView>(R.id.change_user_name_result)

        agree_change.setOnClickListener {
            if(id_changed.text.toString().isEmpty() || username_changed.text.toString().isEmpty()){
                Toast.makeText(context,context.getString(R.string.change_lines_empty), Toast.LENGTH_SHORT).show()
            }else{
                val CN = ChangedNames(context)
                if(CN.insertData(id_changed.text.toString(),username_changed.text.toString())){
                    result_layout.visibility = View.VISIBLE
                    var avatar_ponse = VKApi.users().get(
                        VKParameters.from(
                            VKApiConst.FIELDS,"photo_100",
                            VKApiConst.USER_ID,id_changed.text.toString()))
                    avatar_ponse.executeWithListener(object : VKRequest.VKRequestListener(){
                        override fun onComplete(response: VKResponse?) {
                            super.onComplete(response)
                            val jsonParser = JsonParser()
                            val req = jsonParser.parse(response?.json.toString()).asJsonObject
                            Picasso.with(context)
                                .load(req.get("response").asJsonArray.get(0).asJsonObject.get("photo_100").asString)
                                .into(result_avatar)
                            result_name?.text = CN.isChanged(id_changed.text.toString())

                        }

                        override fun onError(error: VKError?) {
                            super.onError(error)
                            result_layout.visibility = View.GONE
                            Toast.makeText(context,context.getString(R.string.change_error_of_found_id), Toast.LENGTH_SHORT).show()

                        }
                    })
                }else{
                    Toast.makeText(context,context.getString(R.string.error_of_add_to_changednames), Toast.LENGTH_SHORT).show()
                }
            }
        }

        changeWindow.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        changeWindow.show()
    }

    fun isShow():Boolean{
        changeWindow = Dialog(context)
        return changeWindow.isShowing
    }
}