package net.d3b8g.vkmessage.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import net.d3b8g.vkmessage.models.ProfileModel

@StateStrategyType(value =  AddToEndSingleStrategy::class)
interface ProfileView:MvpView {
    fun showError(textResource:Int)
    fun setupUserEmpty()
    fun setUpProfile(allPosition:ProfileModel)
    fun startLoading()
    fun endLoading()
}