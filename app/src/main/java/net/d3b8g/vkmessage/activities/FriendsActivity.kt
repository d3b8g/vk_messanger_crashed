package net.d3b8g.vkmessage.activities

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.internal.NavigationMenu
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import de.hdodenhof.circleimageview.CircleImageView
import io.github.yavski.fabspeeddial.FabSpeedDial
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.adapters.FriendsAdapter
import net.d3b8g.vkmessage.models.FriendsModel
import net.d3b8g.vkmessage.popupActivity.ChangeNameWindow
import net.d3b8g.vkmessage.presenters.FriendsPresenter
import net.d3b8g.vkmessage.views.FriendsView

class FriendsActivity : MvpAppCompatActivity(),FriendsView{

    @InjectPresenter
    lateinit var friendsPres: FriendsPresenter

    private lateinit var mRvFriends: RecyclerView
    private lateinit var mTxtNoItems: TextView
    private lateinit var mCpvWait: CircularProgressView
    private lateinit var menu_fab: FabSpeedDial
    private lateinit var mAdapter: FriendsAdapter
    private lateinit var btnSearch:ImageButton
    private lateinit var popupWindow:Dialog
    private lateinit var changeName:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        
        mRvFriends = findViewById(R.id.recycler_friends)
        mTxtNoItems = findViewById(R.id.txt_friends_no_items)
        mCpvWait = findViewById(R.id.cpv_friends)
        menu_fab = findViewById(R.id.menu_fab)
        btnSearch = findViewById(R.id.friends_search_btn)
        changeName = findViewById(R.id.friends_change_name_btn)

        var changeNameWindow = ChangeNameWindow(this)

        menu_fab.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onPrepareMenu(navigationMenu: NavigationMenu?): Boolean {
                return super.onPrepareMenu(navigationMenu)
                true
            }

            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                when(menuItem?.title.toString()){
                    getString(R.string.menu_item_friends) -> {
                        startActivity(Intent(this@FriendsActivity, FriendsActivity::class.java))
                        finish()
                    }
                    getString(R.string.menu_item_message) -> startActivity(Intent(this@FriendsActivity,MessageActivity::class.java))
                    getString(R.string.menu_item_profile) -> startActivity(Intent(this@FriendsActivity,ProfileActivity::class.java))
                }
                return super.onMenuItemSelected(menuItem)
            }
        })

        friendsPres.loadFriends()
        mAdapter = FriendsAdapter()

        changeName.typeface = Typeface.createFromAsset(applicationContext.assets, "fonts/RobotoCondensed-Light.ttf")
        changeName.setOnClickListener {
            if(!changeNameWindow.isShow())
                changeNameWindow.changeNameWindow()
        }

        val mTxtSearch:EditText = findViewById(R.id.txt_friends_search)
        mTxtSearch.onSumbit { mAdapter.filter(mTxtSearch.text.toString())  }
        btnSearch.setOnClickListener {
            if(mTxtSearch.visibility == View.VISIBLE){
                mAdapter.filter(mTxtSearch.text.toString())
            }else{
                val anumation = ObjectAnimator.ofFloat(mTxtSearch,"translationX",80f).apply {
                    duration = 1000
                    start()
                }
                mTxtSearch.visibility = View.VISIBLE
            }
        }

        mRvFriends.adapter = mAdapter
        mRvFriends.layoutManager = LinearLayoutManager(applicationContext,OrientationHelper.VERTICAL,false)
        mRvFriends.setHasFixedSize(true)
    }

    private fun EditText.onSumbit(func:()->Unit){
        setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                func()
            }
            true
        }
    }

    override fun onBackPressed() {
        popupWindow = Dialog(this)
        popupWindow.setContentView(R.layout.alert_custom_popup)
        var avatar = popupWindow.findViewById<CircleImageView>(R.id.logout_info_avatar)
        var username =popupWindow.findViewById<TextView>(R.id.logout_info_username)
        var text_info = popupWindow.findViewById<TextView>(R.id.logout_info_text_id)

        //Button
        var yes = popupWindow.findViewById<Button>(R.id.logout_info_btn_yes)
        var no = popupWindow.findViewById<Button>(R.id.logout_info_btn_no)
        yes.setOnClickListener {
            VKSdk.logout()
            startActivity(Intent(this@FriendsActivity, LoginActivity::class.java))
        }
        no.setOnClickListener { popupWindow.dismiss() }

        var font = Typeface.createFromAsset(applicationContext.assets, "fonts/Roboto-Bold.ttf")
        text_info.typeface = font

        var req = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,"photo_100"))
        req.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                val req = jsonParser.parse(response?.json.toString()).asJsonObject
                username.text = "${req.get("response").asJsonArray.get(0).asJsonObject.get("first_name").asString} ${req.get("response").asJsonArray.get(0).asJsonObject.get("last_name").asString}"
                Picasso.with(applicationContext).load(req.get("response").asJsonArray.get(0).asJsonObject.get("photo_100").asString).into(avatar)
            }
        })
        popupWindow.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.show()
    }


    override fun showError(textResource: Int) {
        mTxtNoItems.text = getString(textResource)
    }

    override fun setupEmptyList() {
        mRvFriends.visibility = View.GONE
        mTxtNoItems.visibility = View.VISIBLE
    }

    override fun startLoading() {
        mRvFriends.visibility = View.GONE
        mTxtNoItems.visibility = View.GONE
        mCpvWait.visibility = View.VISIBLE
    }

    override fun endLoading() {
        mCpvWait.visibility = View.GONE
    }

    override fun setUpFriendsList(friends: ArrayList<FriendsModel>,friends_online:ArrayList<FriendsModel>) {
        mRvFriends.visibility = View.VISIBLE
        mTxtNoItems.visibility = View.GONE

        mAdapter.setupFriends(friends = friends,friends_online = friends_online)
    }
}
