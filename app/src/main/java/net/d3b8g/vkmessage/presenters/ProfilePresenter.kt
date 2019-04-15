package net.d3b8g.vkmessage.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.models.ProfileModel
import net.d3b8g.vkmessage.providers.ProfileProvider
import net.d3b8g.vkmessage.views.ProfileView

@InjectViewState
class ProfilePresenter: MvpPresenter<ProfileView>() {

    fun openProfile(){
        viewState.startLoading()
        ProfileProvider(this).loadProfile()
    }

    fun showError(textResource: Int){
        viewState.showError(textResource)
    }

    fun profileLoading(params:ProfileModel){
        viewState.endLoading()
        if(params.user_name.isEmpty()){
            viewState.setupUserEmpty()
            viewState.showError(textResource =  R.string.no_message_item)
        }else{
            viewState.setUpProfile(params)
        }
    }
}