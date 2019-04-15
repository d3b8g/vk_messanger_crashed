package net.d3b8g.vkmessage.providers


import android.content.Context
import net.d3b8g.vkmessage.helpers.HolderAction
import net.d3b8g.vkmessage.presenters.LogFileConversationPresenter

class LogFileConversationProvider(val presenter:LogFileConversationPresenter) {
    fun load_logfile(context: Context,chat_id:Long){
        presenter.get_logile(HolderAction(context).get_log(chat_id))
    }
}