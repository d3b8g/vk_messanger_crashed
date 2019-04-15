package net.d3b8g.vkmessage.providers

import android.util.Log
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.vk.sdk.api.*
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.activities.MessageActivity
import net.d3b8g.vkmessage.helpers.UserGetParameters
import net.d3b8g.vkmessage.models.HeavyWithAttachmentsMessage

class HeavyMessageProvider{
    fun unparse_json(message_id: Int):HeavyWithAttachmentsMessage{
        val request = VKRequest("messages.getById", VKParameters.from(VKApiConst.FIELDS,"first_name,last_name,online",VKApiConst.EXTENDED,true,"message_ids",message_id,"version","5.92"))
        var mr:HeavyWithAttachmentsMessage? = null
        request.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                var parser = JsonParser().parse(response?.json.toString()).asJsonObject
                val items = parser.get("response").asJsonObject.getAsJsonArray("items").get(0)
                val user_params = UserGetParameters(parser.get("response"),items.asJsonObject.get("user_id").asLong)
                val haveAttachments = (items.asJsonObject?.get("attachments").toString()!="[]")&&(items.asJsonObject?.get("attachments") != null)
                Log.d("RRR","$items")
                val mrd = HeavyWithAttachmentsMessage(
                    user_id = items.asJsonObject.get("user_id").asLong,
                    avatar_lm = user_params.getAvatar(null),
                    username_lm = user_params.getsName(null),
                    attach_link = if(haveAttachments){
                        getAttachmentLink(items.asJsonObject.get("attachments").asJsonArray)
                    }else{null},
                    typeAttachments = if(haveAttachments){
                        parseAttachType(items.asJsonObject.get("attachments").asJsonArray)
                    }else{arrayListOf("text")},
                    last_seen = user_params.lastOnline(),
                    date_lm = items.asJsonObject.get("date").asLong
                )
                mr=mrd
            }

            override fun onError(error: VKError?) {
                super.onError(error)
                Toast.makeText(MessageActivity(),R.string.error_load_message,Toast.LENGTH_SHORT).show()
            }
        })
        return mr!!
    }

    inline fun parseAttachType(attach_obj: JsonArray):ArrayList<String>{
        var ber:ArrayList<String> = arrayListOf()
        for(item in attach_obj){
            ber.add(item.asJsonObject.get("type").asString)
        }
        return ber
    }
    inline fun getAttachmentLink(attach_obj: JsonArray):ArrayList<String>{
        var bear:ArrayList<String> = arrayListOf()
        for(item in attach_obj){
            val linked = item.asJsonObject.get("type").asString
            when(linked){
                "photo"->{
                    if(item.asJsonObject.get(linked).asJsonObject?.get("photo_103")?.asString!=null)
                        bear.add(item.asJsonObject.get(linked).asJsonObject.get("photo_604").asString)
                    else
                        bear.add(item.asJsonObject.get(linked).asJsonObject.get("photo_604").asString)
                }
                "video"->bear.add(item.asJsonObject.get(linked).asJsonObject.get("photo_320").asString)
                "audio"->bear.add(item.asJsonObject.get(linked).asJsonObject.get("url").asString)
                "doc"->bear.add(item.asJsonObject.get(linked).asJsonObject.get("url").asString)
                "link"->bear.add(item.asJsonObject.get(linked).asJsonObject.get("url").asString)
                "market"->bear.add(item.asJsonObject.get(linked).asJsonObject.get("thumb_photo").asString)
                "market_album"->bear.add(item.asJsonObject.get(linked).asJsonObject.get("photo").asString)
                "wall"->bear.add(item.asJsonObject.get(linked).asJsonObject.get("owner_id").asString)
                "wall_reply"->bear.add(item.asJsonObject.get(linked).asJsonObject.get("wall_reply").asString)
                "sticker"->bear.add(item.asJsonObject.get(linked).asJsonObject.get("photo_352").asString)
                "gift"->bear.add(item.asJsonObject.get(linked).asJsonObject.get("thumb_256").asString)
            }
        }
        return bear
    }
}