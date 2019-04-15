package net.d3b8g.vkmessage.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.models.AttachementsWallPost
import net.d3b8g.vkmessage.models.WallPostModel
import net.d3b8g.vkmessage.providers.WallPostProvider
import net.d3b8g.vkmessage.views.WallPostView

@InjectViewState
class WallPostPresenter:MvpPresenter<WallPostView>() {

    fun loadPosts() {
        viewState.startLoadingPosts()
        WallPostProvider(this).loadWallPost()
    }

    fun postsLoading(postList: ArrayList<WallPostModel> , attPost:ArrayList<AttachementsWallPost>){
        viewState.endLoadingPosts()
        if(postList.size == 0){
            viewState.setupEmptyList()
            viewState.showError(textResource =  R.string.show_no_items)
        }else{
            viewState.setWallPostList(postList,attPost)
        }
    }

    fun showError(textResource:Int){
        viewState.showError(textResource = textResource)
    }

}