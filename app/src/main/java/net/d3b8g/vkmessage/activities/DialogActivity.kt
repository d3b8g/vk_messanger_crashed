package net.d3b8g.vkmessage.activities


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dialog.*
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.adapters.DialogAdapter
import net.d3b8g.vkmessage.helpers.HolderAction
import net.d3b8g.vkmessage.helpers.NotifyFrom
import net.d3b8g.vkmessage.models.ChatSettingsModel
import net.d3b8g.vkmessage.models.DialogModel
import net.d3b8g.vkmessage.presenters.DialogPresenter
import net.d3b8g.vkmessage.views.DialogView

class DialogActivity: MvpAppCompatActivity(),DialogView {
    @InjectPresenter
    lateinit var dialogPres:DialogPresenter

    lateinit var adapter: DialogAdapter
    lateinit var cpv:CircularProgressView
    lateinit var text_error:TextView
    lateinit var dialog:RecyclerView

    var user_id:Long = 0
    lateinit var popup_mnu:PopupMenu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        user_id = intent.getLongExtra("dialog_id",1)
        dialog_usr_name.text = intent.getStringExtra("username")
        Picasso.with(this)
            .load(intent.getStringExtra("avatar"))
            .resize(45,45)
            .into(dialog_usr_avatar)

        val send = findViewById<ImageButton>(R.id.send_message)
        dialog = findViewById(R.id.recycler_dialog)

        dialogPres.load_dialog(user_id)
        adapter = DialogAdapter()
        cpv = findViewById(R.id.cpv_dialogs)
        text_error = findViewById(R.id.txt_dialog_message_no_items)

        dialog.adapter = adapter
        dialog.layoutManager = LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL,false)
        dialog.setHasFixedSize(true)


        val popup_style:Context = ContextThemeWrapper(this@DialogActivity,R.style.popup_menu_style_baby)
        popup_mnu = PopupMenu(popup_style,dialog_usr_menu)
        val menuInflater = popup_mnu.menuInflater
        menuInflater.inflate(R.menu.dialog_menu,popup_mnu.menu)
        dialog_usr_menu.setOnClickListener{
            popup_mnu.show()
        }
        var logged_status: Boolean
        if(HolderAction(this).list_listner()!!.equals(user_id)){
            popup_mnu.menu.findItem(R.id.writting_event).title = getString(R.string.off_logged)
            logged_status = false
        }else
            logged_status = true
        if(user_id>=2000000000) popup_mnu.menu.findItem(R.id.change_name).isVisible = false

        popup_mnu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.only_text ->Toast.makeText(this,it.title,Toast.LENGTH_SHORT).show()
                R.id.only_my->Toast.makeText(this,it.title,Toast.LENGTH_SHORT).show()
                R.id.only_them->Toast.makeText(this,it.title,Toast.LENGTH_SHORT).show()
                R.id.get_notify_from->getNotifyFrom()
                R.id.change_name->Toast.makeText(this,it.title,Toast.LENGTH_SHORT).show()
                R.id.destroy_conversation->Toast.makeText(this,it.title,Toast.LENGTH_SHORT).show()
                R.id.writting_event-> if(logged_status)HolderAction(this).add_id(user_id)
                R.id.show_log->if(logged_status)open_window_log()
            }
            true
        }

        send.setOnClickListener {
            if(hint_message.text.isNullOrEmpty())
                sendMessage()
        }
    }

    private fun getNotifyFrom() {
        val popup_id:Dialog = Dialog(this)
        popup_id.setContentView(R.layout.alert_write_id)
        val ident_from = popup_id.findViewById<EditText>(R.id.notify_from_id)
        ident_from.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val max_lenght = 9
                s?.let { text->
                    if(text.length>max_lenght){
                    Toast.makeText(this@DialogActivity,getString(R.string.u_cant_edit_limit),Toast.LENGTH_SHORT).show()
                    ident_from.text.clear()
                    Thread.sleep(300) }
                    if(text.matches(".*[a-z].*".toRegex())){
                        Toast.makeText(this@DialogActivity,getString(R.string.u_cant_use_alphabet_limit),Toast.LENGTH_SHORT).show()
                        ident_from.text.clear()
                        Thread.sleep(300)
                    }
                }
            }
        })
        ident_from.onSumbit {
            if(NotifyFrom(this@DialogActivity).new_notify_user(user_id,ident_from.text.toString().toLong())){
                Log.d("RRR","OKEY SUKA")
            }else{
                Log.d("RRR","OKEY S!KA")
            }
        }
        popup_id.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup_id.show()
    }

    private fun EditText.onSumbit(func:()->Unit){
        setOnKeyListener{ _, actionId, _ ->
            if(actionId == KeyEvent.ACTION_DOWN || actionId == KeyEvent.KEYCODE_ENTER){
                func()
            }
            true
        }
    }

    private fun sendMessage() {

    }

    private fun open_window_log() {
        val intent = Intent(this,ShowLogConversationActivity::class.java)
        intent.putExtra("user_id",user_id)
        startActivity(intent)
    }

    override fun updateChatSettings(setting: ChatSettingsModel) {
        popup_mnu.menu.findItem(R.id.get_notify_from).isVisible = user_id>=2000000000 && !setting.push_settings_sounds
        popup_mnu.menu.findItem(R.id.access_notify_conversation).title = if(setting.push_settings_sounds) "Включить уведомления" else "Отключить уведомления"
        if(HolderAction(this).list_listner()!!.contains(user_id)) popup_mnu.menu.findItem(R.id.show_log).isVisible=true
    }


    override fun loadComponentsDeletedUser() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadCompontntsOkey(m_history:ArrayList<DialogModel>) {
        cpv.visibility = View.GONE
        text_error.visibility = View.GONE
        dialog.visibility = View.VISIBLE
        adapter.source_adpater(m_history,-1)
        if(user_id>=2000000000) dialogPres.getChatSettings(user_id)
    }

    override fun loadDialog() {
        dialog.visibility = View.GONE
        cpv.visibility = View.VISIBLE
    }

    override fun nullDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun blockedDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun textEmptyDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun errorOfLoad(text: Int) {
        cpv.visibility = View.GONE

    }

}