package net.d3b8g.vkmessage.activities

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.design.internal.NavigationMenu
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKParameters
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import io.github.yavski.fabspeeddial.FabSpeedDial
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.activity_message.*
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.adapters.MessageAdapter
import net.d3b8g.vkmessage.helpers.*
import net.d3b8g.vkmessage.models.SimpleMessageModel
import net.d3b8g.vkmessage.presenters.MessagePresenter
import net.d3b8g.vkmessage.providers.MessageProvider
import net.d3b8g.vkmessage.views.MessageView
import java.net.HttpURLConnection
import java.net.URL

private lateinit var mAdapter: MessageAdapter
private lateinit var manager:LinearLayoutManager
private lateinit var mRvDialogs:RecyclerView
private lateinit var menu_fab_dial:FabSpeedDial
private lateinit var context:Context
private var mRd:ArrayList<SimpleMessageModel> = ArrayList()


class MessageActivity:MvpAppCompatActivity(),MessageView {

    @InjectPresenter
    lateinit var messagePresenter: MessagePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        context = this
        menu_fab_dial = findViewById(R.id.menu_fab)
        menu_fab_dial.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onPrepareMenu(navigationMenu: NavigationMenu?): Boolean {
                return super.onPrepareMenu(navigationMenu)
                true
            }

            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                when(menuItem?.title.toString()){
                    getString(R.string.menu_item_friends) -> startActivity(Intent(this@MessageActivity,FriendsActivity::class.java))
                    getString(R.string.menu_item_message) -> {startActivity(Intent(this@MessageActivity,MessageActivity::class.java))
                        AsyncTaskHandleJson().cancel(true)
                        finish()
                    }
                    getString(R.string.menu_item_profile) -> startActivity(Intent(this@MessageActivity,ProfileActivity::class.java))
                }
                return super.onMenuItemSelected(menuItem)
            }
        })

        mRvDialogs = findViewById(R.id.recycler_messages)

        messagePresenter.loadDialogs()

        mAdapter = MessageAdapter()
        manager = LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL,false)

        mRvDialogs.adapter = mAdapter
        mRvDialogs.layoutManager = manager
        mRvDialogs.setHasFixedSize(true)

    }

    override fun showError(textResource: Int) {
        txt_messages_no_items.text = getString(R.string.no_message_item)
    }

    override fun setupEmptyDialogs() {
        recycler_messages.visibility = View.GONE
        txt_messages_no_items.visibility = View.VISIBLE
    }

    override fun setupDialogsList(dialogsList: ArrayList<SimpleMessageModel>) {
        txt_messages_no_items.visibility = View.GONE
        recycler_messages.visibility = View.VISIBLE
        mRd = dialogsList
        mAdapter.setupDialogs(dialogsList = dialogsList)
    }

    override fun startLoading() {
        recycler_messages.visibility = View.GONE
        txt_messages_no_items.visibility = View.GONE
        cpv_messages.visibility = View.VISIBLE
    }

    override fun endLoading() {
        cpv_messages.visibility = View.GONE
    }
}
var key:String = ""
var server:String = ""
var ts:Long = 0

var lpModel = MessageLongPollModel(0, JsonParser().parse("[[0,0,0,0]]").asJsonArray)


fun getRr(){
    if(server.isNotEmpty() && key.isNotEmpty()){
        AsyncTaskHandleJson().execute("https://$server?act=a_check&key=$key&ts=$ts&wait=25&mode=202&version=6")
    }else{
    val data = VKRequest("messages.getLongPollServer", VKParameters.from("Ip_version",3,"version","5.92"))
    data.executeSyncWithListener(object : VKRequest.VKRequestListener(){
        override fun onComplete(response: VKResponse?) {
            val jsonParser = JsonParser()
            val data = jsonParser.parse(response?.json.toString()).asJsonObject
            key =  data.get("response").asJsonObject.get("key").asString
            server =  data.get("response").asJsonObject.get("server").asString
            ts =  data.get("response").asJsonObject.get("ts").asLong
            AsyncTaskHandleJson().execute("https://$server?act=a_check&key=$key&ts=$ts&wait=25&mode=202&version=6")
        }

        override fun onError(error: VKError?) {
            super.onError(error)
            Handler().postDelayed({
                getRr()
            },2000)
            Log.e("RRR","$error \n OnLongPoll")
        }
    })}
}

class AsyncTaskHandleJson: AsyncTask<String, String, String>(){
    override fun doInBackground(vararg url: String?): String {
        var text:String = ""
        var connection = URL(url[0]).openConnection() as HttpURLConnection
        try{
            connection.connect()
            text = connection.inputStream.use { it.reader().use { reader-> reader.readText()} }
        }finally {
            connection.disconnect()
        }
        return text
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        hadnleJson(result)
    }
}

private fun hadnleJson(jsonString: String?) {
    val jsonParser = JsonParser()
    val jsonEnd = jsonParser.parse(jsonString).asJsonObject
    lpModel = MessageLongPollModel(
        ts = jsonEnd.get("ts").asLong,
        updates = if(jsonEnd.getAsJsonArray("updates").asJsonArray.size()>0)
            jsonEnd.getAsJsonArray("updates").asJsonArray
        else{
            JsonParser().parse("[0,0,0,0]").asJsonArray}
    )
    if(globalPosts()){
        mAdapter.setupDialogs(realDoing(lpModel))
        ts = lpModel.ts
        if(mRd.size==0){
            MessageProvider(MessagePresenter()).loadDialogs()
        }else{
            getRr()
        }
    }
}


fun globalPosts():Boolean{
    return ts != lpModel.ts
}
//оброботчик ответов Сервера

fun realDoing(rb:MessageLongPollModel):ArrayList<SimpleMessageModel>{
    Log.e("longpoll_vk","${rb.updates}")
    var react:ArrayList<SimpleMessageModel> = ArrayList()
    for(itemr in rb.updates) {
        val items = itemr as JsonArray
        var message_id:String = ""
        val code =  items[0].asInt
        var id:Long = 0
        react.clear()
        var timeout:Boolean
        var time :String
        when(code) {
            1 -> {
                react.clear()
                react = mRd
            }
            2 -> {
                react.clear()
                id = items[3].asLong
                message_id = items[1].asString + "_2"
                Log.e("RRR", "удалил $id")
            }
            3 -> {
                react = mRd
            }
            4 -> {
                id = items[3].asLong
                message_id = items[1].asString
                time = getUnixData(items[4].asLong, "HH:mm")
                var body_message = items[5].asString
                if (body_message.equals("")) {
                    if (items[6].asJsonObject.toString().contains("source_act") && items[6].asJsonObject.toString().contains(
                            "source_mid"
                        )
                    )
                        body_message = CordinalMessage().getChatAction(
                            null,
                            items[6].asJsonObject.get("source_act").asString,
                            items[6].asJsonObject.get("source_mid").asLong,
                            items[6].asJsonObject.get("from").asLong
                        )
                    else if (items[7].asJsonObject.toString().contains("attach1_type"))
                        body_message = CordinalMessage().getAttachment(
                            null,
                            items[7].asJsonObject.get("attach1_type").asString
                        )
                    else if (items[6].asJsonObject.toString().contains("fwd_count")) {
                        var compil = WordEndings().wordEndingsEdit(
                            items[6].asJsonObject.get("fwd_count").asInt,
                            arrayOf("переслан", "сообще")
                        )
                        body_message = "${items[6].asJsonObject.get("fwd_count").asInt} ${compil[0]} ${compil[1]}"
                    }

                }
                for (i in mRd) {
                    if (i.user_id == id) {
                        i.message_body = body_message
                        i.message_id = items[1].asInt
                        if (items[3].asLong >= 2000000000) {
                            i.message_avatar =
                                UserGetParameters(null, items[6].asJsonObject.get("from").asLong).onlyName()
                            if (i.count_unread == null && items[6].asJsonObject.get("from").asLong != VKSdk.getAccessToken().userId.toLong())
                                i.count_unread = 1
                            else if (items[6].asJsonObject.get("from").asLong != VKSdk.getAccessToken().userId.toLong())
                                i.count_unread = i.count_unread!!.plus(1)
                            else
                                i.count_unread = null
                        } else {
                            i.message_avatar = if (items[2].asInt == 51) {
                                "Вы: "
                            } else {
                                UserGetParameters(null, items[3].asLong).onlyName()
                            }
                            if (i.count_unread == null && items[2].asInt != 51)
                                i.count_unread = 1
                            else if (items[2].asInt != 51)
                                i.count_unread = i.count_unread!!.plus(1)
                            else
                                i.count_unread = null
                        }
                        react.add(0, i)
                    } else {
                        react.add(i)
                    }
                }

                mRd.addAll(react)
            }
            5 -> {
                react = mRd
            }
            6 -> {
                id = items[1].asLong
                val read_id = items[2].asLong
                val count_read = items[3].asInt
                for (q in mRd) {
                    if (q.user_id == id) {
                        q.count_unread = null
                        react.add(q)
                    } else {
                        react.add(q)
                    }
                }

            }
            7 -> {
                react = mRd
            }
            8 -> {
                react = mRd
            }
            9 -> {
                id = -items[1].asLong
                for (n in mRd) {
                    if (n.user_id == id) {
                        n.user_online = false
                        react.add(n)
                    } else {
                        react.add(n)
                    }
                }


            }
            10 -> {
                react = mRd
            }
            11 -> {
                react = mRd
            }
            12 -> {
                react = mRd
            }
            13 -> {
                react = mRd
            }
            14 -> {
                react = mRd
            }
            51 -> {
                id = items[1].asLong + 2000000000
                react.clear()
                val body_message = "вернулись в беседу"
                for (r in mRd) {
                    if (r.user_id == id) {
                        react.add(0, r)
                    } else {
                        react.add(r)
                    }
                }

                mRd.addAll(react)
            }
            52 -> {
                react = mRd
            }
            63 -> {
                id = items[1].asLong
                react.clear()
                for (i in mRd) {
                    if (i.user_id == id) {
                        if (items[3].asInt > 1) {
                            i.message_avatar = ""
                            i.message_body = "${UserGetParameters(
                                null,
                                items[2].asJsonArray.get(0).asLong
                            ).onlyName()} и еще ${items[3].asInt - 1} печатают"
                        } else {
                            i.message_avatar =
                                UserGetParameters(null, items[2].asJsonArray.get(0).asLong).onlyName()
                            i.message_body = "набирает сообщение"
                        }
                        react.add(i)
                    } else {
                        react.add(i)
                    }
                }


            }
            64 -> {
                id = items[1].asLong
                react.clear()
                for (i in mRd) {
                    if (i.user_id == id) {
                        if (items[2].asJsonArray.size() > 1) {
                            i.message_avatar = ""
                            i.message_body = "${UserGetParameters(
                                null,
                                items[2].asJsonArray.get(0).asLong
                            ).onlyName()} и еще ${items[2].asJsonArray.size() - 1} печатают"
                        } else {
                            i.message_avatar =
                                UserGetParameters(null, items[2].asJsonArray.get(0).asLong).onlyName()
                            i.message_body = "записывает голосовое сообщение"
                        }
                        react.add(i)
                    } else {
                        react.add(i)
                    }
                }


            }
            70 -> {
                react = mRd
            }
            80 -> {
                var unread = items[1].asInt
                react = mRd
            }
            112 -> {
                react = mRd
            }
        }
        mRd.clear()
        mRd = react
        Log.d("RRR","${mRd.size}")
        if(HolderAction(context).list_listner()!!.contains(id)){ HolderAction(context).new_log(id,message_id,items.toString()) }}

    return react
}

class MessageLongPollModel(
    var ts:Long,
    var updates: JsonArray
)

