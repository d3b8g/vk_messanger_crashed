package net.d3b8g.vkmessage.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface LogFileConversationView:MvpView {
    fun startLoadLog()
    fun loadingLogFile(log_string:ArrayList<String>)
    fun endLoad()
    fun emptyLogFile()
}