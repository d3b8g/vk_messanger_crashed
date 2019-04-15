package net.d3b8g.vkmessage.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.activities.getRr
import net.d3b8g.vkmessage.models.SimpleMessageModel
import net.d3b8g.vkmessage.providers.MessageProvider
import net.d3b8g.vkmessage.views.MessageView

@InjectViewState
class MessagePresenter:MvpPresenter<MessageView>() {

    fun loadDialogs() {
        viewState.startLoading()
        MessageProvider(presenter = this).loadDialogs()
    }

    fun showError(textResource: Int) {
        viewState.showError(textResource)
    }

    fun dialogsLoading(messageList: ArrayList<SimpleMessageModel>) {
        viewState.endLoading()
        if(messageList.size == 0){
            viewState.setupEmptyDialogs()
            viewState.showError(textResource =  R.string.no_message_item)
        }else{
            viewState.setupDialogsList(messageList)
            android.os.Handler().postDelayed({
                getRr()
            },1000)
        }
    }

}