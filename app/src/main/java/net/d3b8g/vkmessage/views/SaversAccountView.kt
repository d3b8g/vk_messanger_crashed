package net.d3b8g.vkmessage.views

import com.arellomobile.mvp.MvpView
import net.d3b8g.vkmessage.models.SavesAccountUsers

interface SaversAccountView:MvpView {
    fun showErrorAccount(textResource:Int)
    fun setupList(account:ArrayList<SavesAccountUsers>)
    fun startLoadingAccount()
    fun endLoadingAccount()
}