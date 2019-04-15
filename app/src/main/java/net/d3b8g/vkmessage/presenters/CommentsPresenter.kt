package net.d3b8g.vkmessage.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import net.d3b8g.vkmessage.models.CommentsModel
import net.d3b8g.vkmessage.providers.CommentsProvider
import net.d3b8g.vkmessage.views.CommentsView

@InjectViewState
class CommentsPresenter:MvpPresenter<CommentsView>() {

    fun loadCommets(wall_id:Int) {
        viewState.startLoadingComments()
        CommentsProvider(this).load(wall_id)
    }

    fun commentsLoading(comments: ArrayList<CommentsModel>){
        viewState.endLoadingComments()
        if(comments.size == 0){
            viewState.setupEmptyCommentsList()
        }else{
            viewState.setupCommentsList(comments)
        }
    }

    fun showError(textResource:Int){
        viewState.showErrorLoadingComments(textRes =  textResource)
    }
}