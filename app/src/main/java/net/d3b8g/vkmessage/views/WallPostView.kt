package net.d3b8g.vkmessage.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import net.d3b8g.vkmessage.models.AttachementsWallPost
import net.d3b8g.vkmessage.models.WallPostModel


@StateStrategyType(value =  AddToEndSingleStrategy::class)
interface WallPostView : MvpView {
    fun showError(textResource:Int)
    fun setupEmptyList()
    fun setWallPostList(postsList: ArrayList<WallPostModel>, attachements:ArrayList<AttachementsWallPost>)
    fun startLoadingPosts()
    fun endLoadingPosts()
}