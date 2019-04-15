package net.d3b8g.vkmessage.providers

import android.os.AsyncTask
import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.vk.sdk.VKSdk
import net.d3b8g.vkmessage.models.AttachmentsTypes
import net.d3b8g.vkmessage.models.DialogModel
import net.d3b8g.vkmessage.presenters.DialogPresenter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

lateinit var presenter_global:DialogPresenter

class DialogProvider(var presenter:DialogPresenter) {

    fun load_all_message(user_id:Long){
        presenter_global = presenter
        AsyncGetMessageHistory().execute("https://api.vk.com/method/messages.getHistory?count=70&extended=0&user_id=$user_id&v=5.92&access_token=${VKSdk.getAccessToken().accessToken}")
        Log.e("RRR","хм,ну я начал")
    }

    class AsyncGetMessageHistory:AsyncTask<String,String,String>(){

        override fun doInBackground(vararg url: String?): String {
            var response:String = ""
            var connection = URL(url[0]).openConnection() as HttpsURLConnection
            try{
                connection.connect()
                response = connection.inputStream.use { it.reader().use { reader-> reader.readText()} }
            }finally {
                connection.disconnect()
            }
            return response
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            parserResponse_mHistory(result)
        }

        private fun parserResponse_mHistory(result: String) {
            val response = result
            val parser = JsonParser()
            val history = parser.parse(response).asJsonObject
            Log.d("RRR","$history")
            var list_dialog:ArrayList<DialogModel> = ArrayList()
            history.get("response").asJsonObject.getAsJsonArray("items").forEach {
                val mm = DialogModel(
                    m_id = it.asJsonObject.get("id").asInt,
                    date = it.asJsonObject.get("date").asLong,
                    from_id = it.asJsonObject.get("from_id").asLong,
                    attachments = attachments_impl(it.asJsonObject?.get("attachments")?.asJsonArray,it.asJsonObject.get("text").asString)
                )
                list_dialog.add(mm)
            }
            presenter_global.loading_dialog(list_dialog)
        }

        private inline fun attachments_impl(rbt: JsonArray?,msg:String): ArrayList<AttachmentsTypes> {
            if(rbt==null) return arrayListOf(AttachmentsTypes("text",msg,null,null,null))
            var rt:ArrayList<AttachmentsTypes> = ArrayList()
            rbt?.let { yeah->
                yeah.forEach {
                    when(it.asJsonObject.get("type").asString){
                        "photo"->{
                            it.asJsonObject.get("${it.asJsonObject.get("type").asString}").asJsonObject.getAsJsonArray("sizes").forEach {corcl->
                                val mini_url = if(corcl.asJsonObject.get("type").asString == "s"){corcl.asJsonObject.get("url").asString}else{null}
                                if(corcl.asJsonObject.get("type").asString == "q"){
                                    rt!!.add(AttachmentsTypes(
                                        type = it.asJsonObject.get("type").asString,
                                        url = corcl.asJsonObject.get("url").asString,
                                        mini_url = mini_url,
                                        width = corcl.asJsonObject.get("width").asInt,
                                        height =  corcl.asJsonObject.get("height").asInt))
                                }else if(corcl.asJsonObject.get("type").asString == "q"){
                                    rt!!.add(AttachmentsTypes(
                                        type = it.asJsonObject.get("type").asString,
                                        url = corcl.asJsonObject.get("url").asString,
                                        mini_url = mini_url,
                                        width = corcl.asJsonObject.get("width").asInt,
                                        height =  corcl.asJsonObject.get("height").asInt))
                                }
                            }
                        }
                        "sticker"->{
                            it.asJsonObject.get("${it.asJsonObject.get("type").asString}").asJsonObject.getAsJsonArray("images").forEach {corcl->
                                val mini_url = if(corcl.asJsonObject.get("width").asInt == 64){corcl.asJsonObject.get("url").asString}else{null}
                                if(corcl.asJsonObject.get("width").asInt == 256){
                                    rt!!.add(AttachmentsTypes(
                                        type = it.asJsonObject.get("type").asString,
                                        url =  corcl.asJsonObject.get("url").asString,
                                        mini_url = mini_url,
                                        width =  corcl.asJsonObject.get("width").asInt,
                                        height =  corcl.asJsonObject.get("height").asInt))
                                }
                            }
                        }
                        "audio_message"->{
                            Log.d("RRR","$it")
                            rt!!.add(AttachmentsTypes(
                                type = it.asJsonObject.get("type").asString,
                                height =null,
                                mini_url = it.asJsonObject.get(it.asJsonObject.get("type").asString).asJsonObject.get("link_mp3").asString,
                                width = null,
                                url = it.asJsonObject.get(it.asJsonObject.get("type").asString).asJsonObject.get("link_ogg").asString
                            ))
                        }
                    }

                }
            }
            return rt!!
        }
    }

}