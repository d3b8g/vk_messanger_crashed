package net.d3b8g.vkmessage.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import net.d3b8g.vkmessage.providers.LogFileConversationProvider
import net.d3b8g.vkmessage.views.LogFileConversationView

@InjectViewState
class LogFileConversationPresenter:MvpPresenter<LogFileConversationView>() {
    fun startAct(context: Context,chat_id:Long){
        viewState.startLoadLog()
        LogFileConversationProvider(this).load_logfile(context,chat_id)
    }

    fun get_logile(logfile:ArrayList<String>){
        if(logfile.size>0){
            viewState.endLoad()
            viewState.loadingLogFile(logfile)
        }else{
            viewState.endLoad()
            viewState.emptyLogFile()
        }
    }
}