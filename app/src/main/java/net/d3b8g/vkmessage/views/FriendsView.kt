package net.d3b8g.vkmessage.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import net.d3b8g.vkmessage.models.FriendsModel

@StateStrategyType(value =  AddToEndSingleStrategy::class)
interface FriendsView:MvpView {
    fun showError(textResource:Int)
    fun setupEmptyList()
    fun setUpFriendsList(friends: ArrayList<FriendsModel>,friends_online:ArrayList<FriendsModel>)
    fun startLoading()
    fun endLoading()
}