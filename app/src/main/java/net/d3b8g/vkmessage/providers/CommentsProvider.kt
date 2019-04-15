package net.d3b8g.vkmessage.providers

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.vk.sdk.api.*
import net.d3b8g.vkmessage.models.CommentsModel
import net.d3b8g.vkmessage.presenters.CommentsPresenter

class CommentsProvider(var presenter: CommentsPresenter) {
    fun load(wall_id:Int){
        val request = VKApi.wall().getComments(VKParameters.from(VKApiConst.COUNT,3))
        request.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                val req = jsonParser.parse(response?.json.toString()).asJsonObject
                var commentsList:ArrayList<CommentsModel> = ArrayList()
                req.get("response").asJsonObject.getAsJsonArray("items").forEach {
                    var comment = CommentsModel(
                        from_id = it.asJsonObject.get("from_id").asLong,
                        avatar = getAvatar(req.get("response"),it.asJsonObject.get("from_id").asLong),
                        text = it.asJsonObject.get("text").asString,
                        name = getName(req.get("response"),it.asJsonObject.get("from_id").asLong),
                        comments_id = it.asJsonObject.get("id").asInt,
                        date = it.asJsonObject.get("date").asLong,
                        haveAttach = it?.asJsonObject?.getAsJsonArray("attachments") != null,
                        haveThread = it?.asJsonObject?.getAsJsonArray("thread") != null,
                        count_thread = it.asJsonObject?.getAsJsonArray("thread")?.asJsonObject?.get("count")?.asInt,
                        likes = it.asJsonObject.get("likes").asJsonObject.get("count").asInt,
                        can_like =  it.asJsonObject.get("likes").asJsonObject.get("can_like").asInt==1,
                        is_online = isOnline(req.get("response"),it.asJsonObject.get("from_id").asLong)
                    )
                    commentsList.add(comment)
                }
                presenter.commentsLoading(commentsList)
            }
        })
    }

    private fun isOnline(get: JsonElement, id: Long): Boolean {
        if (id > 0) {
            get.asJsonObject.getAsJsonArray("profiles").forEach {
                if(it.asJsonObject.get("id").asLong == id){
                    return it.asJsonObject.get("online").asInt == 1
                }
            }
        } else {
            get.asJsonObject.getAsJsonArray("groups").forEach {
                if(it.asJsonObject.get("id").asLong == id){
                    return it.asJsonObject.get("online").asInt == 1
                }
            }
        }
        return false
    }

    private fun getAvatar(get: JsonElement, id: Long): String {
        if (id > 0) {
            get.asJsonObject.getAsJsonArray("profiles").forEach {
                if(it.asJsonObject.get("id").asLong == id){
                    return it.asJsonObject.get("photo_100").asString
                }
            }
        } else {
            get.asJsonObject.getAsJsonArray("groups").forEach {
                if(it.asJsonObject.get("id").asLong == id){
                    return it.asJsonObject.get("photo_100").asString
                }
            }
        }
        return ""
    }

    private fun getName(get: JsonElement, id:Long): String {
        if (id > 0) {
            get.asJsonObject.getAsJsonArray("profiles").forEach {
                if(it.asJsonObject.get("id").asLong == id){
                    return "${it.asJsonObject.get("first_name")} ${it.asJsonObject.get("last_name")}"
                }
            }
        } else {
            get.asJsonObject.getAsJsonArray("groups").forEach {
                if(it.asJsonObject.get("id").asLong == id){
                    return it.asJsonObject.get("name").asString
                }
            }
        }
        return "Error"
    }
}