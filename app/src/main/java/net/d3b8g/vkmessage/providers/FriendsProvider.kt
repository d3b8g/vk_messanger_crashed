package net.d3b8g.vkmessage.providers

import com.google.gson.JsonParser
import com.vk.sdk.api.*
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.models.FriendsModel
import net.d3b8g.vkmessage.presenters.FriendsPresenter

class FriendsProvider(var presenter: FriendsPresenter) {

    fun loadFriends(){
        val request = VKApi.friends().get(VKParameters.from(VKApiConst.COUNT,1000,VKApiConst.FIELDS,"sex,bdate,city,country,photo_100,online"))
        request.executeWithListener(object :VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                val pardesJson = jsonParser.parse(response?.json.toString()).asJsonObject
                val friendList_another:ArrayList<FriendsModel> = ArrayList()
                val friendList_online:ArrayList<FriendsModel> = ArrayList()

                pardesJson.get("response").asJsonObject.getAsJsonArray("items").forEach{
                    val city = if(it.asJsonObject.get("city")==null){
                        null
                    }else{
                        it.asJsonObject.get("city").asJsonObject.get("title").asString
                    }
                    val friend = FriendsModel(
                        username = "${it.asJsonObject.get("first_name").asString} ${it.asJsonObject.get("last_name").asString}",
                        user_id = it.asJsonObject.get("id").asLong,
                        city = city,
                        avatar = it.asJsonObject.get("photo_100").asString,
                        isOnline = it.asJsonObject.get("online").asInt==1)
                    if(it.asJsonObject.get("online").asInt==1)
                        friendList_online.add(friend)
                    else
                        friendList_another.add(friend)
                }

                presenter.freindsLoading(friends = friendList_another,friends_online = friendList_online)
            }

            override fun onError(error: VKError?) {
                super.onError(error)
                presenter.showError(textResource = R.string.friends_error_loading)
            }
        })
    }
}