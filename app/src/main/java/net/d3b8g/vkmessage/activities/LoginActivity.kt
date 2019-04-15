package net.d3b8g.vkmessage.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.gson.JsonParser
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import kotlinx.android.synthetic.main.activity_login.*
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.adapters.SaversAccountAdapter
import net.d3b8g.vkmessage.helpers.DBSavesUserAccountHandler
import net.d3b8g.vkmessage.models.SavesAccountUsers
import net.d3b8g.vkmessage.presenters.LoginPresenter
import net.d3b8g.vkmessage.presenters.SaversAccountPresenter
import net.d3b8g.vkmessage.views.LoginView
import net.d3b8g.vkmessage.views.SaversAccountView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LoginActivity : MvpAppCompatActivity(), LoginView, SaversAccountView  {

    private lateinit var mTxtLoginHello: TextView
    private lateinit var mBtnEntry: Button
    private lateinit var mCpv: CircularProgressView
    private lateinit var saveUsers:RecyclerView
    private lateinit var mAdapter:SaversAccountAdapter
    private lateinit var mCpvAccount:CircularProgressView
    private lateinit var mTextAccountEmpty:TextView

    @InjectPresenter
    lateinit var loginPresenter:LoginPresenter

    @InjectPresenter
    lateinit var accounts: SaversAccountPresenter

    val db = DBSavesUserAccountHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        VKAccessToken.currentToken()?.let {
            startActivity(Intent(applicationContext,MessageActivity::class.java))
            Log.e("RRR","${VKSdk.getAccessToken().accessToken}")
            DBSavesUserAccountHandler(applicationContext).updateTime(VKSdk.getAccessToken().userId.toLong())
            finish()
        }
        mTxtLoginHello = findViewById(R.id.txt_login_hello)
        mBtnEntry= findViewById(R.id.btn_login_enter)
        mCpv = findViewById(R.id.progress_view)
        saveUsers = findViewById(R.id.have_account_login)
        mCpvAccount = findViewById(R.id.cpv_savers_account)
        mAdapter = SaversAccountAdapter()
        mTextAccountEmpty = findViewById(R.id.txt_savers_account_no_items)

        var fontAlegreya = Typeface.createFromAsset(applicationContext.assets, "fonts/RobotoCondensed-Light.ttf")

        mTextAccountEmpty.typeface = fontAlegreya
        text_logo_login.typeface = fontAlegreya

        accounts.loadAccount()

        saveUsers.adapter = mAdapter
        saveUsers.layoutManager = LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL,false)
        saveUsers.setHasFixedSize(true)


        mBtnEntry.setOnClickListener {
            VKSdk.login(this@LoginActivity,VKScope.FRIENDS,VKScope.MESSAGES,VKScope.GROUPS,VKScope.OFFLINE,VKScope.PHOTOS,VKScope.AUDIO,VKScope.VIDEO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(!loginPresenter.loginVk(requestCode=requestCode, resultCode = resultCode, data = data))
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun startLoading() {
        mBtnEntry.visibility = View.GONE
        mCpv.visibility = View.VISIBLE
    }

    override fun endLoading() {
        mBtnEntry.visibility = View.VISIBLE
        mCpv.visibility = View.GONE
    }

    override fun showError(textResource: Int) {
        Toast.makeText(applicationContext,getString(textResource),Toast.LENGTH_SHORT).show()

    }

    override fun openFriends() {
        startActivity(Intent(applicationContext,MessageActivity::class.java))

        val VKreq = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,"photo_100"))
        VKreq.executeWithListener(object : VKRequest.VKRequestListener(){
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)
                val jsonParser = JsonParser()
                val pardesJson = jsonParser.parse(response?.json.toString()).asJsonObject
                val acc= SavesAccountUsers(
                    username = "${pardesJson.get("response").asJsonArray.get(0).asJsonObject.get("first_name").asString} ${pardesJson.get("response").asJsonArray.get(0).asJsonObject.get("last_name").asString}",
                    avatar = pardesJson.get("response").asJsonArray.get(0).asJsonObject.get("photo_100").asString,
                    user_id = pardesJson.get("response").asJsonArray.get(0).asJsonObject.get("id").asLong,
                    last_data = SimpleDateFormat("EEE, dd MMM yyyy, HH:mm").toString()
                )

                if(db.saveId(VKSdk.getAccessToken().userId.toLong()) == VKSdk.getAccessToken().userId){
                    DBSavesUserAccountHandler(applicationContext).updateTime(VKSdk.getAccessToken().userId.toLong())
                    Log.e("RRR","должен апдейтить")
                }else{
                    DBSavesUserAccountHandler(applicationContext).insertData(acc!!)
                    Log.e("RRR","должен сохранить акк")
                }

                if(db.allIdD==null){
                    DBSavesUserAccountHandler(applicationContext).insertData(acc!!)
                    Log.e("RRR","должен сохранить акк when is null")
                }
                Log.e("RRR","простопропустил")
            }

            override fun onError(error: VKError?) {
                super.onError(error)
                return
            }
        })

        finish()
    }


    override fun setupList(acc:ArrayList<SavesAccountUsers>) {
        saveUsers.visibility = View.VISIBLE
        if(acc!=null)
            mAdapter.setupAccount(acc)
        else{
            mTextAccountEmpty.visibility = View.VISIBLE
            saveUsers.visibility = View.GONE
        }

    }
    override fun showErrorAccount(textResource: Int) {
        mTextAccountEmpty.text = getString(textResource)
    }

    override fun startLoadingAccount() {
        mCpvAccount.visibility = View.VISIBLE
        var base:ArrayList<SavesAccountUsers> = ArrayList()
        db.allIdD?.let {
            Log.e("RRR","${it.size} acc on load returned")
            mCpvAccount.visibility = View.GONE
            base = it
        }
        accounts.loadingAccount(base)
    }

    override fun endLoadingAccount() {
        mCpvAccount.visibility = View.GONE

    }
}
