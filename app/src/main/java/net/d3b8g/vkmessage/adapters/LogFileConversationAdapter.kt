package net.d3b8g.vkmessage.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.JsonParser
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.helpers.getUnixData

class LogFileConversationAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var all_log:ArrayList<String> = ArrayList()

    fun updateLog(log_upd:ArrayList<String>){
        all_log.clear()
        all_log.addAll(log_upd)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_logfile_type,parent,false)
        return LogFileConversationHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return all_log.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is LogFileConversationHolder)
            holder.bind(all_log[position])
    }

    class LogFileConversationHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        lateinit var textbox:TextView

        fun bind(new_string:String){
            textbox = itemView.findViewById(R.id.textview_logfile_conversation)
            // [4,3449994,8243,2000000026,1548885784,"🤔",{"emoji":"1","from":"412904874"},{}]
            val core = JsonParser().parse(new_string).asJsonArray
            val code = new_string.substring(1,2).toInt()
            val result_string = ""
            when(code){
                2->{
                    val resout = DeleteMessage(
                        id = core.get(0).asInt,
                        mes_id = core.get(1).asInt,
                        random_int = core.get(2).asInt,
                        chat_id = core.get(3).asLong
                    )
                    textbox.text =
                            "удалил сообщение ID: [${resout.mes_id}]"
                    textbox.setTextColor(Color.RED)
                }
                4->{
                    var resout:AddNewMessageLPJson = AddNewMessageLPJson(
                        id = core.get(0).asInt,
                        mes_id = core.get(1).asInt,
                        random_int = core.get(2).asInt,
                        chat_id = core.get(3).asLong,
                        time = core.get(4).asLong,
                        message = core.get(5).asString,
                        anrParams = "",
                        from_id = core.get(6).asJsonObject.get("from").asLong
                    )

                    textbox.text =
                        "[${resout.mes_id}]сообщение(${getUnixData(resout.time,"yyyy MM.dd HH:mm")}):${resout.message}"
                }
            }
        }
    }

    class AddNewMessageLPJson(
        var id:Int,
        var mes_id:Int,
        var random_int:Int,
        var chat_id:Long,
        var time:Long,
        var message:String,
        var from_id:Long,
        var anrParams:String
    )
    class DeleteMessage(
        var id: Int,
        val mes_id: Int,
        var random_int: Int,
        var chat_id: Long
    )
}