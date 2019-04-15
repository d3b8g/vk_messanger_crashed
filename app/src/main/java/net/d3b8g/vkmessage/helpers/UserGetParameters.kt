package net.d3b8g.vkmessage.helpers

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import net.d3b8g.vkmessage.R

class UserGetParameters {
    var jsonElement:JsonElement?
    var user_id:Long

    constructor(jsonElement: JsonElement?, user_id:Long){
        this.jsonElement = jsonElement
        this.user_id = user_id
    }

    inline fun getsName(user_ident:Long?):String{
        if(user_ident!=null){
            var name = ""
            if(user_ident>0){
                var getNameFrom = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS,user_ident))
                getNameFrom.executeSyncWithListener(object : VKRequest.VKRequestListener(){
                    override fun onComplete(response: VKResponse?) {
                        super.onComplete(response)
                        val jsonParser = JsonParser()
                        val pardesJson = jsonParser.parse(response?.json.toString()).asJsonObject
                        name = "${pardesJson.get("response").asJsonArray.get(0).asJsonObject.get("first_name").asString} ${pardesJson.get("response").asJsonArray.get(0).asJsonObject.get("last_name").asString}"
                    }
                })
            }else{
                var getNameFrom = VKApi.groups().getById(VKParameters.from(VKApiConst.GROUP_ID,-user_ident))
                getNameFrom.executeSyncWithListener(object : VKRequest.VKRequestListener(){
                    override fun onComplete(response: VKResponse?) {
                        super.onComplete(response)
                        val jsonParser = JsonParser()
                        val pardesJson = jsonParser.parse(response?.json.toString()).asJsonObject
                        name = "${pardesJson.get("response").asJsonObject.get("name").asString}"
                    }
                })
            }
            return name
        }else{
            if (user_id > 0) {
                jsonElement?.asJsonObject?.getAsJsonArray("profiles")?.forEach {
                    if(it.asJsonObject.get("id").asLong == user_id){
                        return "${it.asJsonObject.get("first_name").asString} ${it.asJsonObject.get("last_name").asString}"
                    }
                }
            } else {
                jsonElement?.asJsonObject?.getAsJsonArray("groups")?.forEach {
                    if(-it.asJsonObject.get("id").asLong == user_id){
                        return it.asJsonObject.get("name").asString
                    }
                }
            }
        }
        return "Error"
    }

    inline fun getAvatar(add_params:Long?):String{
        if(add_params!=null){
            if (add_params > 0) {
                jsonElement?.asJsonObject?.getAsJsonArray("profiles")?.forEach {
                    if(it.asJsonObject.get("id").asLong == user_id){
                        return it.asJsonObject.get("photo_100").asString
                    }
                }
            } else {
                jsonElement?.asJsonObject?.getAsJsonArray("groups")?.forEach {
                    if(-it.asJsonObject.get("id").asLong == user_id){
                        return it.asJsonObject.get("photo_100").asString
                    }
                }
            }
        }else{
            if (user_id > 0) {
                jsonElement?.asJsonObject?.getAsJsonArray("profiles")?.forEach {
                    if(it.asJsonObject.get("id").asLong == user_id){
                        return it.asJsonObject.get("photo_100").asString
                    }
                }
            } else {
                jsonElement?.asJsonObject?.getAsJsonArray("groups")?.forEach {
                    if(-it.asJsonObject.get("id").asLong == user_id){
                        return it.asJsonObject.get("photo_100").asString
                    }
                }
            }
        }
        return "https://pp.userapi.com/c623616/v623616034/1c17c/15tS4tpV-k0.jpg"
    }

    inline fun lastOnline():Long?{
        return if(user_id>0){
            var last_seen_time:Long = 0
            val last_seen = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID,user_id,VKApiConst.FIELDS,"last_seen"))
            last_seen.executeSyncWithListener(object : VKRequest.VKRequestListener(){
                override fun onComplete(response: VKResponse?) {
                    super.onComplete(response)
                    val jsonParser = JsonParser()
                    val parse = jsonParser.parse(response?.json.toString()).asJsonObject
                    last_seen_time = parse.get("response").asJsonArray.get(0).asJsonObject.get("last_seen").asJsonObject.get("time").asLong
                }
            })
            last_seen_time
        } else null

        return null
    }

    inline fun getOnline():Boolean{
        if (user_id > 0) {
            jsonElement?.asJsonObject?.getAsJsonArray("profiles")?.forEach {
                if(it.asJsonObject.get("id").asLong == user_id){
                    return it.asJsonObject.get("online").asInt==1
                }
            }
        } else {
            jsonElement?.asJsonObject?.getAsJsonArray("groups")?.forEach {
                if(it.asJsonObject.get("id").asLong == user_id){
                    return it.asJsonObject.get("online").asInt==1
                }
            }
        }
        return false
    }

    inline fun getSex(user_ident:Long?):Int{
        var sex_id = 0

        jsonElement?.let { item->
            jsonElement?.asJsonObject?.getAsJsonArray("profiles")?.forEach {
                if(it.asJsonObject.get("id").asLong == user_id){
                    return it.asJsonObject.get("sex").asInt
                }
            }
        }

        if(jsonElement==null){
            if(user_ident!=null){
                val sex = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS,user_ident,VKApiConst.FIELDS,"sex"))
                sex.executeSyncWithListener(object : VKRequest.VKRequestListener(){
                    override fun onComplete(response: VKResponse?) {
                        super.onComplete(response)
                        val jsonParser = JsonParser()
                        val parse = jsonParser.parse(response?.json.toString()).asJsonObject
                        var sex_n= parse.get("response").asJsonArray.get(0).asJsonObject.get("sex").asInt
                        sex_id = sex_n
                    }
                })
            }else{
                val sex = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS,user_id,VKApiConst.FIELDS,"sex"))
                sex.executeSyncWithListener(object : VKRequest.VKRequestListener(){
                    override fun onComplete(response: VKResponse?) {
                        super.onComplete(response)
                        val jsonParser = JsonParser()
                        val parse = jsonParser.parse(response?.json.toString()).asJsonObject
                        var sex_n= parse.get("response").asJsonArray.get(0).asJsonObject.get("sex").asInt
                        sex_id = sex_n
                    }
                })
            }
        }
        return sex_id
    }

    inline fun onlyName():String{
        var name:String = "Error:"
        if(VKSdk.getAccessToken().userId.toLong() == user_id){
            return "Вы:"
        }
        if(user_id>0){
            if(jsonElement!=null){
                jsonElement?.asJsonObject?.getAsJsonArray("profiles")?.forEach {
                    if(it.asJsonObject.get("id").asLong == user_id){
                        return "${it.asJsonObject.get("first_name").asString}:"
                    }
                }
            }else{
                var getNameFrom = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS,user_id))
                getNameFrom.executeSyncWithListener(object : VKRequest.VKRequestListener(){
                    override fun onComplete(response: VKResponse?) {
                        super.onComplete(response)
                        val jsonParser = JsonParser()
                        val pardesJson = jsonParser.parse(response?.json.toString()).asJsonObject
                        name = "${pardesJson.get("response").asJsonArray.get(0).asJsonObject.get("first_name").asString}:"
                    }
                })
            }
        }else{
            return "От группы:"
        }
        return name
    }

    inline fun isMyFriend():Boolean {
        var isit = false
        val isFriend = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, user_id, VKApiConst.FIELDS, "is_friend"))
        isFriend.executeSyncWithListener(object : VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                val parse = jsonParser.parse(response?.json.toString()).asJsonObject
                var sex_n = parse.get("response").asJsonArray.get(0).asJsonObject.get("is_friend").asInt == 1
                isit = sex_n
            }
        })
        return isit
    }

    inline fun addToFriends(context:Context):Boolean{
        var rr = true
        val add = VKApi.friends().add(VKParameters.from(VKApiConst.USER_ID, user_id))
        add.executeWithListener(object : VKRequest.VKRequestListener() {
            override fun onError(error: VKError?) {
                super.onError(error)
                Toast.makeText(context,context.getString(R.string.error_add_friend), Toast.LENGTH_SHORT).show()
                rr = false
            }
        })
        return rr
    }

    inline fun removeFromFriends(context:Context):Boolean{
        var rr = true
        val remove = VKApi.friends().delete(VKParameters.from(VKApiConst.USER_ID, user_id))
        remove.executeWithListener(object : VKRequest.VKRequestListener() {
            override fun onError(error: VKError?) {
                super.onError(error)
                Toast.makeText(context,context.getString(R.string.error_remove_friend), Toast.LENGTH_SHORT).show()
                rr = false
            }
        })
        return rr
    }

    inline fun sexChangerForDate(sex:Int?):String{
        when(sex){
            0->return "был"
            1->return "была"
            2->return "был"
        }
        return "был"
    }
}