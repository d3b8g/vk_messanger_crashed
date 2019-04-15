package net.d3b8g.vkmessage.providers

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vk.sdk.api.VKApiConst
import com.vk.sdk.api.VKParameters
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import net.d3b8g.vkmessage.models.AttachementsWallPost
import net.d3b8g.vkmessage.models.PoolAnswersModel
import net.d3b8g.vkmessage.models.WallPostModel
import net.d3b8g.vkmessage.presenters.WallPostPresenter

class WallPostProvider(var presenter:WallPostPresenter) {

    var width:Int? = null
    var height:Int? = null
    var pool_answers:ArrayList<PoolAnswersModel>? = null

    fun loadWallPost(){
        val request = VKRequest("wall.get",VKParameters.from(VKApiConst.EXTENDED,1,VKApiConst.FIELDS,"photo_100,online",VKApiConst.COUNT,35,VKApiConst.VERSION,"5.92"))
        request.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)

                val jsonParser = JsonParser()
                val pardesJson = jsonParser.parse(response?.json.toString()).asJsonObject

                val postsList:ArrayList<WallPostModel> = ArrayList()
                val posts_attachments_List:ArrayList<AttachementsWallPost> = ArrayList()
                pardesJson.get("response").asJsonObject.getAsJsonArray("items").forEach{

                    val params = WallPostModel(
                        name = getNameAccount(pardesJson,it.asJsonObject.get("from_id").asLong),
                        wall_id = it.asJsonObject.get("id").asInt,
                        avatar = getUserAvatar(it.asJsonObject.get("from_id").asLong,pardesJson),
                        count_comments = it.asJsonObject.get("comments").asJsonObject.get("count").asInt,
                        count_likes = it.asJsonObject.get("likes").asJsonObject.get("count").asInt,
                        count_views = 0,
                        have_like = it.asJsonObject.get("likes").asJsonObject.get("can_like").asInt==0,// 1 = 1 true
                        count_share = it.asJsonObject.get("reposts").asJsonObject.get("count").asInt,
                        post_time =it.asJsonObject.get("date").asString,
                        wall_text = it.asJsonObject?.get("text")?.asString,
                        have_pinned_post = havePinned(it),
                        attachHave = it.asJsonObject?.get("attachments")?.asJsonArray !=null,
                        photo = getPhotoAttach(it.asJsonObject?.get("attachments")?.asJsonArray),
                        photo_height =height,
                        photo_width = width,
                        poll_ques = getPoolInfo(it.asJsonObject?.get("attachments")?.asJsonArray),
                        poll_answers = pool_answers
                    )

                    posts_attachments_List.add(getContent(it,pardesJson))

                    postsList.add(params)
                }

                presenter.postsLoading(postsList,posts_attachments_List)
            }
        })
    }

    private fun getPoolInfo(pool: JsonArray?): String? {
        var pool_back_ques:String? = null

        if(pool?.size()!=null){
            pool.forEach {
                if(it.asJsonObject.get("type").asString == "poll"){

                    pool_back_ques = it.asJsonObject.get("poll").asJsonObject.get("question").asString
                    var poll_massiv:ArrayList<PoolAnswersModel> = ArrayList()
                    it.asJsonObject.get("poll").asJsonObject.getAsJsonArray("answers").forEach {rr->
                        val answer = PoolAnswersModel(
                            id = rr.asJsonObject.get("id").asLong,
                            text = rr.asJsonObject.get("text").asString,
                            votes = rr.asJsonObject.get("votes").asInt,
                            rate = rr.asJsonObject.get("rate").asFloat
                        )
                        poll_massiv.add(answer)
                        pool_answers = poll_massiv
                    }
                    return pool_back_ques
                }else{
                    pool_back_ques = null
                    pool_answers = null
                }
            }
        }

        return pool_back_ques
    }

    private fun getPhotoAttach(get: JsonArray?): String? {
        var back:String? = null
        if(get?.size()!=null){
            get.forEach {
                if(it.asJsonObject.get("type").asString == "photo"){
                    back = it.asJsonObject.get("photo").asJsonObject.get("photo_604").asString
                    width = it.asJsonObject.get("photo").asJsonObject.get("width").asInt
                    height = it.asJsonObject.get("photo").asJsonObject.get("height").asInt
                }else{
                    back = null
                    width = null
                    height = null
                }
                return back
            }
        }else{
            return null
        }
        return back
    }

    private fun havePinned(it: JsonElement): Boolean {
        return !it.asJsonObject.get("copy_history")?.asJsonArray?.get(0)?.asJsonObject?.get("id")?.asString.equals(null)
    }

    private fun getNameAccount(obj: JsonObject,ident: Long): String {
        if(ident>0){
            obj.get("response").asJsonObject.getAsJsonArray("profiles").forEach {
                if(it.asJsonObject.get("id").asLong==ident){
                    return it.asJsonObject.get("first_name").asString + it.asJsonObject.get("last_name").asString
                }
            }
        }else{
            obj.get("response").asJsonObject.getAsJsonArray("groups").forEach {
                if(it.asJsonObject.get("id").asLong== -ident){
                    return it.asJsonObject.get("name").asString
                }
            }
        }
        return "null"
    }

    private fun getUserAvatar(ident: Long,obj:JsonObject): String {
        if(ident>0){
            obj.get("response").asJsonObject.getAsJsonArray("profiles").forEach {
                if(it.asJsonObject.get("id").asLong==ident){
                    return it.asJsonObject.get("photo_100").asString
                }
            }
        }else{
            obj.get("response").asJsonObject.getAsJsonArray("groups").forEach {
                if(it.asJsonObject.get("id").asLong== -ident){
                    return it.asJsonObject.get("photo_100").asString
                }
            }
        }
        return "null"
    }

    private fun getContent(it: JsonElement,obj: JsonObject):AttachementsWallPost {
        if(!it.asJsonObject.get("copy_history")?.asJsonArray?.get(0)?.asJsonObject?.get("id")?.asString.equals(null)){

            var id = it.asJsonObject.get("copy_history").asJsonArray.get(0).asJsonObject.get("id").asLong

            var time = it.asJsonObject.get("copy_history").asJsonArray.get(0).asJsonObject.get("date").asLong

            var avatar = getUserAvatar(it.asJsonObject.get("copy_history").asJsonArray.get(0).asJsonObject.get("from_id").asLong,obj)

            var name = getNameAccount(obj,it.asJsonObject.get("copy_history").asJsonArray.get(0).asJsonObject.get("from_id").asLong)

            var height = it.asJsonObject.get("copy_history").asJsonArray.get(0)?.asJsonObject?.get("attachments")
                ?.asJsonArray?.get(0)?.asJsonObject?.get("photo")?.asJsonObject?.get("height")?.asInt

            var width = it.asJsonObject.get("copy_history").asJsonArray.get(0)?.asJsonObject?.get("attachments")
                ?.asJsonArray?.get(0)?.asJsonObject?.get("photo")?.asJsonObject?.get("width")?.asInt

            var text = it.asJsonObject.get("copy_history").asJsonArray.get(0).asJsonObject?.get("text")?.asString

            var another = if(!it.asJsonObject.get("copy_history").asJsonArray.get(0).asJsonObject?.get("attachments")
                    ?.asJsonArray?.get(0)?.asJsonObject?.get("type")?.asString.equals(null)){
                it.asJsonObject.get("copy_history").asJsonArray.get(0).asJsonObject.get("attachments")
                    .asJsonArray.get(0).asJsonObject.get("type").asString
            }else{
                null
            }

            var image = if(another.equals("photo")){
                it.asJsonObject.get("copy_history").asJsonArray.get(0).asJsonObject.get("attachments")
                    .asJsonArray.get(0).asJsonObject.get("photo").asJsonObject.get("photo_130").asString
            }else{
                null
            }

            return AttachementsWallPost(
                post_id = id,
                attachments_pin = another,
                text_pin = text,
                image_pin = image,
                time_pin = time,
                name_pin = name,
                avatar = avatar,

                image_width = width,
                image_height = height
            )
        }
        return AttachementsWallPost(
            post_id = 99999999999,
            image_pin = null,
            text_pin = null,
            attachments_pin = null,
            time_pin = 1001,
            name_pin = "Error",
            avatar = "null",
            image_height = null,
            image_width = null
        )
    }

}