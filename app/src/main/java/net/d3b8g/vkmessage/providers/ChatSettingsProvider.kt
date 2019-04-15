package net.d3b8g.vkmessage.providers

import android.util.Log
import com.google.gson.JsonParser
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKParameters
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import net.d3b8g.vkmessage.models.ChatSettingsModel
import net.d3b8g.vkmessage.presenters.DialogPresenter

class ChatSettingsProvider(val presenter:DialogPresenter){

    fun load_chat_settings(chat_id:Long){
        val VK_req = VKRequest("messages.getChat", VKParameters.from("chat_id",chat_id-2000000000,"version","5.92"))
        VK_req.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val parser = JsonParser()
                val settings = parser.parse(response?.json.toString()).asJsonObject
                var setSettings = ChatSettingsModel(
                    admin_id = settings.get("response").asJsonObject.get("admin_id").asLong,
                    member_count = settings.get("response").asJsonObject.get("members_count").asInt,
                    push_settings_sounds = if(settings.get("response")?.asJsonObject?.get("push_settings")!=null)
                        {settings.get("response").asJsonObject.get("push_settings").asJsonObject.get("sound").asInt==0}
                        else {false}
                )
                presenter.setChatSettings(setSettings)
            }

            override fun onError(error: VKError?) {
                super.onError(error)
                Log.d("RRR","$error $chat_id")
            }
        })
    }

}