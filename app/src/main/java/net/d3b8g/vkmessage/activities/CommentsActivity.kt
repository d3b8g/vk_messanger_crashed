package net.d3b8g.vkmessage.activities

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.rahatarmanahmed.cpv.CircularProgressView
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.adapters.CommentsAdapter
import net.d3b8g.vkmessage.models.CommentsModel
import net.d3b8g.vkmessage.presenters.CommentsPresenter
import net.d3b8g.vkmessage.views.CommentsView


class CommentsActivity: MvpAppCompatActivity(),CommentsView {

    @InjectPresenter
    lateinit var commentsPresenter: CommentsPresenter

    //Interface
    private lateinit var title:TextView
    private lateinit var cpvLoad:CircularProgressView
    private lateinit var text_error:TextView
    private lateinit var recycler:RecyclerView
    //Adapter
    private lateinit var mAdapter:CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        //Initialize
        var fontAlegreya = Typeface.createFromAsset(applicationContext.assets, "fonts/RobotoCondensed-Light.ttf")
        title = findViewById(R.id.comments_title_text)
        cpvLoad = findViewById(R.id.cpv_comments)
        text_error = findViewById(R.id.txt_comments_error)
        recycler = findViewById(R.id.recylcer_comments_view)

        mAdapter = CommentsAdapter()

        commentsPresenter.loadCommets(4280)

        recycler.adapter = mAdapter
        recycler.layoutManager = LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL,false)
        recycler.setHasFixedSize(true)
    }

    override fun setupEmptyCommentsList() {
        text_error.visibility = View.VISIBLE
        text_error.text = getString(R.string.empty_list_comments)
    }

    override fun startLoadingComments() {
        cpvLoad.visibility = View.VISIBLE

    }

    override fun setupCommentsList(comments: ArrayList<CommentsModel>) {
        mAdapter.setupComments(comments)
    }

    override fun endLoadingComments() {
        cpvLoad.visibility = View.GONE
    }

    override fun showErrorLoadingComments(textRes: Int) {
        text_error.visibility = View.VISIBLE
    }
}