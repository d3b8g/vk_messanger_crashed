package net.d3b8g.vkmessage.views

import com.arellomobile.mvp.MvpView
import net.d3b8g.vkmessage.models.CommentsModel

interface CommentsView:MvpView {

    fun setupEmptyCommentsList()
    fun startLoadingComments()
    fun setupCommentsList(comments:ArrayList<CommentsModel>)
    fun endLoadingComments()
    fun showErrorLoadingComments(textRes:Int)
}