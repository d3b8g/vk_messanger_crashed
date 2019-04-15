package net.d3b8g.vkmessage.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import net.d3b8g.vkmessage.models.SimpleMessageModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MessageView:MvpView {
    fun showError(textResource:Int)
    fun setupEmptyDialogs()
    fun setupDialogsList(dialogsList:ArrayList<SimpleMessageModel>)
    fun startLoading()
    fun endLoading()
}