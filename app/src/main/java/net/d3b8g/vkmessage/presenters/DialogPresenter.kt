package net.d3b8g.vkmessage.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import net.d3b8g.vkmessage.models.ChatSettingsModel
import net.d3b8g.vkmessage.models.DialogModel
import net.d3b8g.vkmessage.providers.ChatSettingsProvider
import net.d3b8g.vkmessage.providers.DialogProvider
import net.d3b8g.vkmessage.views.DialogView

@InjectViewState
class DialogPresenter:MvpPresenter<DialogView>() {

    fun load_dialog(user_id:Long){
        viewState.loadDialog()
        DialogProvider(this).load_all_message(user_id)
    }
    fun show_error_load(text:Int){
        viewState.errorOfLoad(text)
    }
    fun loading_dialog(m_history:ArrayList<DialogModel>){
        if(m_history.size>0){
            viewState.loadCompontntsOkey(m_history)
        }else{
            viewState.nullDialog()
        }
    }

    fun getChatSettings(chat_id:Long){
        ChatSettingsProvider(this).load_chat_settings(chat_id)
    }

    fun setChatSettings(setting:ChatSettingsModel){
        viewState.updateChatSettings(setting)
    }
}