package net.d3b8g.vkmessage.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.rahatarmanahmed.cpv.CircularProgressView
import kotlinx.android.synthetic.main.activity_show_log_conversation.*

import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.adapters.LogFileConversationAdapter
import net.d3b8g.vkmessage.helpers.HolderAction
import net.d3b8g.vkmessage.popupActivity.DeleteAllLogFilesWindow
import net.d3b8g.vkmessage.presenters.LogFileConversationPresenter
import net.d3b8g.vkmessage.views.LogFileConversationView

class ShowLogConversationActivity:MvpAppCompatActivity(),LogFileConversationView {

    @InjectPresenter
    lateinit var logFilePresenter: LogFileConversationPresenter
    lateinit var mAdapter:LogFileConversationAdapter

    lateinit var rc_logfile:RecyclerView
    lateinit var cpv_logfile:CircularProgressView
    lateinit var textbox:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_log_conversation)

        rc_logfile = findViewById(R.id.rv_logfile_conversation)
        cpv_logfile = findViewById(R.id.cpv_logfile_conv)
        textbox = findViewById(R.id.textview_logfile_conv_error)

        logFilePresenter.startAct(this,intent.getLongExtra("user_id",0))

        mAdapter = LogFileConversationAdapter()

        delete_log_file_conversation.setOnClickListener {
            if(!DeleteAllLogFilesWindow(this).isShow()){
                DeleteAllLogFilesWindow(this).show(intent.getLongExtra("user_id",0))
            }
        }

        stop_log_file_conversation.setOnClickListener {
            if(HolderAction(this).stop_writting_log(intent.getLongExtra("user_id",0))){
                stop_log_file_conversation.setBackgroundDrawable(getDrawable(R.drawable.ic_replay_logging))
            }
        }

        rc_logfile.adapter = mAdapter
        rc_logfile.layoutManager = LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL,false)
        rc_logfile.setHasFixedSize(true)
    }

    override fun startLoadLog() {
        cpv_logfile.visibility = View.VISIBLE
    }

    override fun loadingLogFile(log_string: ArrayList<String>) {
        mAdapter.updateLog(log_string)
    }

    override fun endLoad() {
        cpv_logfile.visibility = View.GONE
        rc_logfile.visibility = View.VISIBLE
    }

    override fun emptyLogFile() {
        rc_logfile.visibility = View.GONE
        cpv_logfile.visibility = View.GONE
        textbox.text = resources.getString(R.string.logfile_conversation_isempty)
    }
}