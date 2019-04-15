package net.d3b8g.vkmessage.providers

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vk.sdk.api.*
import net.d3b8g.vkmessage.models.ProfileModel
import net.d3b8g.vkmessage.presenters.ProfilePresenter


class ProfileProvider(var presenter:ProfilePresenter) {

    var count_followers:Int = -1
    var count_video:String = "-1"
    var count_photo:String = "-1"
    var count_audio:String = "-1"
    var count_wall:String = "-1"


    fun loadProfile(){

        followersCount()
        wallCount()
        videoCount()
        audioCount()
        photoCount()

        val VKreq = VKApi.users().get(VKParameters.from(VKApiConst.EXTENDED,true,VKApiConst.FIELDS,"sex,relation,bdate,city,country,photo_100,online,status"))
        when{
            count_followers == -1 -> followersCount()
            count_audio == "-1" -> audioCount()
            count_photo == "-1" -> photoCount()
            count_wall == "-1" -> wallCount()
            count_video == "-1" -> videoCount()
        }
        VKreq.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                val pardesJson = jsonParser.parse(response?.json.toString()).asJsonObject
                val city = if(pardesJson.asJsonObject.get("response").asJsonArray.get(0).asJsonObject.get("city")==null){
                    "Отсутствует"
                }else{
                    pardesJson.asJsonObject.get("response").asJsonArray.get(0).asJsonObject.get("city").asJsonObject.get("title").asString
                }

                val model:ProfileModel = ProfileModel(
                    user_avatar = pardesJson.asJsonObject.get("response").asJsonArray.get(0).asJsonObject.get("photo_100").asString,
                    user_city = city,
                    user_bdate = pardesJson.asJsonObject.get("response").asJsonArray.get(0).asJsonObject.get("bdate").asString,
                    user_family_status = pardesJson.asJsonObject.get("response").asJsonArray.get(0).asJsonObject.get("relation").asInt,
                    relation_partner = relationPartner(pardesJson),
                    user_name = pardesJson.asJsonObject.get("response").asJsonArray.get(0).asJsonObject.get("first_name").asString,
                    user_surname = pardesJson.asJsonObject.get("response").asJsonArray.get(0).asJsonObject.get("last_name").asString,
                    user_status = pardesJson.asJsonObject.get("response").asJsonArray.get(0).asJsonObject.get("status").asString,
                    sex = pardesJson.asJsonObject.get("response").asJsonArray.get(0).asJsonObject.get("sex").asInt,
                    count_video = count_video,
                    count_audio = count_audio,
                    user_id = pardesJson.asJsonObject.get("response").asJsonArray.get(0).asJsonObject.get("id").asLong,
                    count_photo = count_photo,
                    count_wallpost = count_wall,
                    count_followers = count_followers
                )
                presenter.profileLoading(model)
            }
        })
    }

    fun followersCount(){
        val followers = VKApi.friends().getRequests(VKParameters.from())
        followers.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?){
                super.onComplete(response)
                val jsonParser = JsonParser()
                val answer = jsonParser.parse(response?.json.toString()).asJsonObject
                count_followers = answer.asJsonObject.get("response").asJsonObject.get("count").asInt
            }
        })
    }
    fun wallCount(){

        val wallC = VKApi.wall().get(VKParameters.from())

        wallC.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                val parsedJson = jsonParser.parse(response?.json.toString()).asJsonObject

                count_wall = parsedJson.asJsonObject.get("response").asJsonObject.get("count").asString
            }

        })

    }
    fun photoCount(){
        val photo = VKRequest("photos.getAll", VKParameters.from())

        photo.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                val parsedJson = jsonParser.parse(response?.json.toString()).asJsonObject
                count_photo = parsedJson.asJsonObject.get("response").asJsonObject.get("count").asString
            }
        })

    }
    fun videoCount(){
        val video = VKApi.video().get(VKParameters.from())
        video.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                val parsedJson = jsonParser.parse(response?.json.toString()).asJsonObject
                count_video = parsedJson.asJsonObject.get("response").asJsonObject.get("count").asString
            }
        })
    }
    fun audioCount(){
        val audio = VKApi.audio().get(VKParameters.from())

        audio.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                val parsedJson = jsonParser.parse(response?.json.toString()).asJsonObject
                count_audio = parsedJson.asJsonObject.get("response").asJsonObject.get("count").asString
            }
        })
    }
    fun relationPartner(jsObj: JsonObject):String{
        return jsObj.get("response").asJsonArray.get(0).asJsonObject?.get("relation_partner")?.asJsonObject?.get("first_name")?.asString + " "+
                jsObj.get("response").asJsonArray.get(0).asJsonObject?.get("relation_partner")?.asJsonObject?.get("last_name")?.asString
    }
}