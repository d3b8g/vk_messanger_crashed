package net.d3b8g.vkmessage.presenters

import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.views.LoginView


@InjectViewState
class LoginPresenter: MvpPresenter<LoginView>() {

    fun loginVk(requestCode: Int, resultCode: Int, data: Intent?):Boolean {
        if(!VKSdk.onActivityResult(requestCode,resultCode,data,object : VKCallback<VKAccessToken>{
                override fun onResult(res: VKAccessToken?) {
                    viewState.openFriends()
                }

                override fun onError(error: VKError?) {
                    viewState.showError(textResource = R.string.login_error_credent)
                }
            })){
            return false
        }

        return true
    }

}