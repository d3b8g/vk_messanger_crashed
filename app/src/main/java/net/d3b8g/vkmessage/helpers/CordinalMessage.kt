package net.d3b8g.vkmessage.helpers

import com.google.gson.JsonArray
import com.google.gson.JsonElement

class CordinalMessage() {

    inline fun parseAttachType(attach_obj: JsonArray?):ArrayList<String>{
        var ber:ArrayList<String> = arrayListOf()
        for(item in attach_obj!!){
            ber.add(item.asJsonObject.get("type").asString)
        }
        return ber
    }
    inline fun getAttachmentLink(attach_obj: JsonArray?):ArrayList<String>{
        var bear:ArrayList<String> = arrayListOf()
        for(item in attach_obj!!){
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
    inline fun getChatAction(jsonElement: JsonElement?, type: String, member_id: Long?, from_id:Long): String {
        var username_member:String? = null
        var username_from:String? = null
        var usersex:String?=null
        var usersex_from:String?=null
        var userParams = UserGetParameters(jsonElement,from_id)

        if(from_id==member_id){
            usersex = "3"
        }else{
            usersex = userParams.getSex(member_id).toString()
            usersex_from = userParams.getSex(null).toString()
        }


        when(type){
            "chat_kick_user" -> {
                return if(from_id==member_id)
                    "${if(usersex=="1")"вышла" else if(usersex=="3") "вышли" else "вышел"} из беседы"
                else
                    "${if(usersex_from=="1")"исключила" else if(usersex=="3") "вышли" else "исключил"} из беседы $username_member"
            }
            "chat_photo_update" -> return "${if(usersex=="1")"обновила" else if(usersex=="3") "обновили" else "обновил"} фотографию беседы"
            "chat_photo_remove" -> return "${if(usersex=="1")"удалила" else if(usersex=="3") "удалили" else "удалил"} фотографию беседы"
            "chat_create" -> return "${if(usersex=="1")"создала" else if(usersex=="3") "создали" else "создал"} беседу"
            "chat_title_update" -> return "${if(usersex=="1")"обновила" else if(usersex=="3") "обновили" else "обновил"} название беседы"
            "chat_invite_user" -> {
                return if(from_id==member_id){
                    "${if(usersex=="1")"вернулась" else if(usersex=="3") "вернулись" else "вернулся"} в беседу"
                }else{
                    "${if(usersex_from=="1")"пригласила" else if(usersex=="3") "пригласили" else "пригласил"} в беседу"
                }
            }
            "chat_pin_message" -> return "${if(usersex=="1")"закрепила" else if(usersex=="3") "закрепили" else "закрепил"} сообщение"
            "chat_unpin_message" -> return "${if(usersex=="1")"открепила" else if(usersex=="3") "открепили" else "открепил"} сообщение"
            "chat_invite_user_by_link" -> return "${if(usersex=="1")"присоединилась" else if(usersex=="3") "присоединились" else "присоединился"} по ссылке"
        }
        return ""
    }
    inline fun getAttachment(attach_obj: JsonArray? , last_message: String): String {
        attach_obj?.let {
            for(item in it){
                if(last_message.isEmpty()) {
                    when (item.asJsonObject.get("type").asString) {
                        "doc" -> {
                            when (attach_obj?.get(0)?.asJsonObject?.get("doc")?.asJsonObject?.get("ext")?.asString) {
                                "ogg" -> return "Голосовое сообщение"
                            }
                            return "Документ"
                        }
                        "photo" -> return "Фотография"
                        "video" -> return "Видеозапись"
                        "audio" -> return "Аудиозапись"
                        "link" -> {
                                if(attach_obj?.get(0)?.asJsonObject?.get("link")?.asJsonObject?.get("url")?.asString!!.substring(0,22)=="https://m.vk.com/story") {
                                    return "История"
                                }
                            }
                        "market" -> return "Товар"
                        "market_album" -> return "Подборка товаров"
                        "wall" -> return "Запись на стене"
                        "wall_reply" -> return "Комментарий к записи"
                        "sticker" -> return "Стикер"
                        "gift" -> return "Подарок"
                    }
                }
            }
        }

        if(attach_obj==null){
            when(last_message){
                "doc" -> {
                    when(attach_obj?.get(0)?.asJsonObject?.get("doc")?.asJsonObject?.get("ext")?.asString){
                        "ogg" -> return "Голосовое сообщение"
                    }
                    return "Документ"
                }
                "photo" -> return "Фотография"
                "video"-> return "Видеозапись"
                "audio"-> return "Аудиозапись"
                "market"->return "Товар"
                "market_album"-> return "Подборка товаров"
                "wall"->return "Запись на стене"
                "wall_reply"->return "Комментарий к записи"
                "sticker"-> return "Стикер"
                "gift"->return "Подарок"
                "story" -> return "История"
            }
        }
        return "Ответил с медиавложением"
    }
}