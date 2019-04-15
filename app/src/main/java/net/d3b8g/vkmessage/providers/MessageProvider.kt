package net.d3b8g.vkmessage.providers

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKApiConst
import com.vk.sdk.api.VKParameters
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import net.d3b8g.vkmessage.helpers.CordinalMessage
import net.d3b8g.vkmessage.helpers.UserGetParameters
import net.d3b8g.vkmessage.models.SimpleMessageModel
import net.d3b8g.vkmessage.presenters.MessagePresenter

class MessageProvider(var presenter:MessagePresenter) {

    fun loadDialogs(){
        val vkRequest = VKRequest("messages.getConversations", VKParameters.from(VKApiConst.FIELDS,"name,photo_100,first_name,last_name,online,sex",VKApiConst.COUNT,35,VKApiConst.EXTENDED,true))
        vkRequest.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                var messageList:ArrayList<SimpleMessageModel> = ArrayList()
                val pardesJson = jsonParser.parse(response?.json.toString()).asJsonObject
                pardesJson.get("response").asJsonObject.getAsJsonArray("items").forEach{
                    var message:SimpleMessageModel = when{
                        it.asJsonObject.get("conversation").asJsonObject.get("peer").asJsonObject.get("type").asString == "chat" -> {
                            var haveAttach = it.asJsonObject.get("last_message").asJsonObject.get("attachments").asJsonArray.toString()!="[]"
                            var haveAction =  it.asJsonObject.get("last_message")?.asJsonObject?.get("action")?.isJsonNull
                            SimpleMessageModel(
                                user_id = it.asJsonObject.get("conversation").asJsonObject.get("peer").asJsonObject.get("id").asLong,
                                user_name = it.asJsonObject.get("conversation").asJsonObject.get("chat_settings").asJsonObject.get("title").asString,
                                user_online = false,
                                user_avatar = it.asJsonObject.get("conversation").asJsonObject.get("chat_settings").asJsonObject?.get("photo")
                                    ?.asJsonObject?.get("photo_100")?.asString,
                                message_body = if (haveAttach) {
                                    CordinalMessage().getAttachment(
                                        attach_obj = it.asJsonObject.get("last_message").asJsonObject.get("attachments").asJsonArray,
                                        last_message = it.asJsonObject.get("last_message").asJsonObject.get("text").asString
                                    )
                                }else if(haveAction!=null && !haveAction){
                                    CordinalMessage().getChatAction(
                                        pardesJson.get("response"),
                                        it.asJsonObject.get("last_message").asJsonObject.get("action").asJsonObject.get("type").asString,
                                        it.asJsonObject.get("last_message").asJsonObject.get("action")?.asJsonObject?.get("member_id")?.asLong,
                                        it.asJsonObject.get("last_message").asJsonObject.get("from_id").asLong
                                    )
                                } else {
                                    it.asJsonObject.get("last_message").asJsonObject.get("text").asString
                                },
                                message_avatar =identAbonent(it.asJsonObject.get("last_message").asJsonObject.get("from_id").asLong,false,pardesJson.get("response")),
                                message_id = it.asJsonObject.get("last_message").asJsonObject.get("id").asInt,
                                count_unread = it.asJsonObject.get("conversation").asJsonObject?.get("unread_count")?.asInt
                            )
                        }
                        it.asJsonObject.get("conversation").asJsonObject.get("peer").asJsonObject.get("type").asString == "group" -> {
                            var haveAttach = it.asJsonObject.get("last_message").asJsonObject.get("attachments").toString()!="[]"
                            SimpleMessageModel(
                                user_id = it.asJsonObject.get("conversation").asJsonObject.get("peer").asJsonObject.get("id").asLong,
                                user_name = UserGetParameters(pardesJson.get("response"),it.asJsonObject.get("conversation").asJsonObject.get("peer").asJsonObject.get("id").asLong)
                                    .getsName(null),
                                user_online = false,
                                user_avatar = UserGetParameters(pardesJson.get("response"),it.asJsonObject.get("conversation").asJsonObject.get("peer").asJsonObject.get("id").asLong)
                                    .getAvatar(null),
                                message_body = if(haveAttach) {
                                    CordinalMessage().getAttachment(
                                        attach_obj = it.asJsonObject.get("last_message").asJsonObject.get("attachments").asJsonArray,
                                        last_message = it.asJsonObject.get("last_message").asJsonObject.get("text").asString
                                    )}else{
                                    it.asJsonObject.get("last_message").asJsonObject.get("text").asString
                                },
                                message_avatar =identAbonent(it.asJsonObject.get("last_message").asJsonObject.get("from_id").asLong,false,pardesJson.get("response")),
                                message_id = it.asJsonObject.get("last_message").asJsonObject.get("id").asInt,
                                count_unread = it.asJsonObject.get("conversation").asJsonObject?.get("unread_count")?.asInt
                            )
                        }
                        else -> {
                            var haveAttach = it.asJsonObject.get("last_message").asJsonObject.get("attachments").toString()!="[]"
                            SimpleMessageModel(
                                user_id = it.asJsonObject.get("conversation").asJsonObject.get("peer").asJsonObject.get("id").asLong,
                                user_name =UserGetParameters(pardesJson.get("response"),it.asJsonObject.get("conversation").asJsonObject.get("peer").asJsonObject.get("id").asLong)
                                    .getsName(null),
                                user_online = UserGetParameters(pardesJson.get("response"),it.asJsonObject.get("conversation").asJsonObject.get("peer").asJsonObject.get("id").asLong)
                                    .getOnline(),
                                user_avatar = UserGetParameters(pardesJson.get("response"),it.asJsonObject.get("conversation").asJsonObject.get("peer").asJsonObject.get("id").asLong)
                                    .getAvatar(null),
                                message_body = if (haveAttach) {
                                    CordinalMessage().getAttachment(
                                        attach_obj = it.asJsonObject.get("last_message").asJsonObject.get("attachments").asJsonArray,
                                        last_message = it.asJsonObject.get("last_message").asJsonObject.get("text").asString
                                    )
                                }else {
                                    it.asJsonObject.get("last_message").asJsonObject.get("text").asString
                                },
                                message_avatar = identAbonent(it.asJsonObject.get("last_message").asJsonObject.get("from_id").asLong,true,pardesJson.get("response")),
                                message_id = it.asJsonObject.get("last_message").asJsonObject.get("id").asInt,
                                count_unread = it.asJsonObject.get("conversation").asJsonObject?.get("unread_count")?.asInt
                            )
                        }
                    }
                    messageList.add(message)
                }
                presenter.dialogsLoading(messageList)
            }
        })
    }

    private fun identAbonent(from_id: Long,isr:Boolean,item:JsonElement): String {
        val param = UserGetParameters(item,from_id)
        return if(VKSdk.getAccessToken().userId.toLong()==from_id) "Вы:"
        else if(isr) {
            if(param.getSex(null)==1) "Она:"
            else "Он:"
        }else{
            param.onlyName()
        }

        return " :"
    }
}