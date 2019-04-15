package net.d3b8g.vkmessage.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.models.SavesAccountUsers
import net.d3b8g.vkmessage.views.SaversAccountView

@InjectViewState
class SaversAccountPresenter:MvpPresenter<SaversAccountView>() {

    fun loadAccount(){
        viewState.startLoadingAccount()
    }
    fun loadingAccount(acc:ArrayList<SavesAccountUsers>){
        viewState.endLoadingAccount()
        if(acc.size == 0){
            viewState.showErrorAccount(textResource =  R.string.accounts_saves_list_empty)
        }else{
            viewState.setupList(acc)
        }
    }
    fun showError(textResource:Int){
        viewState.showErrorAccount(textResource = textResource)
    }
}