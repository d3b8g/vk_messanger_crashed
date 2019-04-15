package net.d3b8g.vkmessage.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import net.d3b8g.vkmessage.models.ChatSettingsModel
import net.d3b8g.vkmessage.models.DialogModel

@StateStrategyType(value =  AddToEndSingleStrategy::class)
interface DialogView: MvpView {
    fun loadComponentsDeletedUser()
    fun loadCompontntsOkey(m_history:ArrayList<DialogModel>)
    fun loadDialog()
    fun nullDialog()
    fun blockedDialog()
    fun textEmptyDialog()
    fun updateChatSettings(setting:ChatSettingsModel)
    fun errorOfLoad(text:Int)
}