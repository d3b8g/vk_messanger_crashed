package net.d3b8g.vkmessage.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.models.FriendsModel
import net.d3b8g.vkmessage.providers.FriendsProvider
import net.d3b8g.vkmessage.views.FriendsView

@InjectViewState
class FriendsPresenter:MvpPresenter<FriendsView>() {

    fun loadFriends() {
        viewState.startLoading()
        FriendsProvider(presenter = this).loadFriends()
    }

    fun freindsLoading(friends: ArrayList<FriendsModel>, friends_online:ArrayList<FriendsModel>){
        viewState.endLoading()
        if(friends.size == 0 && friends_online.size == 0){
            viewState.setupEmptyList()
            viewState.showError(textResource =  R.string.show_no_items)
        }else{
            viewState.setUpFriendsList(friends=friends,friends_online = friends_online)
        }
    }

    fun showError(textResource:Int){
        viewState.showError(textResource = textResource)
    }

}