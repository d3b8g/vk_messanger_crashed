package net.d3b8g.vkmessage.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.internal.NavigationMenu
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.github.yavski.fabspeeddial.FabSpeedDial
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.activity_profile.*
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.adapters.HorizontalAdapterProfile
import net.d3b8g.vkmessage.adapters.WallPostAdapter
import net.d3b8g.vkmessage.helpers.DBSavesUserAccountHandler
import net.d3b8g.vkmessage.models.AttachementsWallPost
import net.d3b8g.vkmessage.models.ProfileModel
import net.d3b8g.vkmessage.models.SavesAccountUsers
import net.d3b8g.vkmessage.models.WallPostModel
import net.d3b8g.vkmessage.presenters.ProfilePresenter
import net.d3b8g.vkmessage.presenters.WallPostPresenter
import net.d3b8g.vkmessage.views.ProfileView
import net.d3b8g.vkmessage.views.WallPostView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity:MvpAppCompatActivity(),ProfileView,WallPostView {

    @InjectPresenter
    lateinit var model:ProfilePresenter
    @InjectPresenter
    lateinit var posts:WallPostPresenter

    private lateinit var mAdapter: HorizontalAdapterProfile
    private lateinit var mAdapterWallPosts: WallPostAdapter

    private lateinit var mAvatarUser:CircleImageView
    private lateinit var whoIS:TextView
    private lateinit var mStatusUser:EditText

    private lateinit var bDate:TextView
    private lateinit var mRelation:TextView
    private lateinit var mCity:TextView

    private lateinit var mItemsUser:RecyclerView
    private lateinit var mPostsUser:RecyclerView

    private lateinit var menu_fab:FabSpeedDial

    private var relation_man = listOf<String>("не женат","есть подруга","помолвлен","женат","всё сложно","в активном поиске","влюблён","в гражданском браке","не указано")
    private var relation_woman = listOf<String>("не замужем","есть подруга","помолвлена","замужем","всё сложно","в активном поиске","влюблена","в гражданском браке","не указано")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        menu_fab = findViewById(R.id.menu_fab)
        menu_fab.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onPrepareMenu(navigationMenu: NavigationMenu?): Boolean {
                return super.onPrepareMenu(navigationMenu)
                true
            }

            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                when(menuItem?.title.toString()){
                    getString(R.string.menu_item_friends) -> startActivity(Intent(this@ProfileActivity,FriendsActivity::class.java))
                    getString(R.string.menu_item_message) -> startActivity(Intent(this@ProfileActivity,MessageActivity::class.java))
                    getString(R.string.menu_item_profile) ->{ startActivity(Intent(this@ProfileActivity,ProfileActivity::class.java))
                    finish()
                    }
                }
                return super.onMenuItemSelected(menuItem)
            }
        })

        mAvatarUser = findViewById(R.id.user_avatar_profile)
        whoIS = findViewById(R.id.user_name_surname_profile)
        mStatusUser = findViewById(R.id.user_status_profile)

        bDate = findViewById(R.id.user_bdate_profile)
        mRelation = findViewById(R.id.user_family_status_profile)
        mCity = findViewById(R.id.user_city_profile)

        model.openProfile()
        posts.loadPosts()

        mAdapter = HorizontalAdapterProfile()
        mAdapterWallPosts = WallPostAdapter()

        mItemsUser = findViewById(R.id.recycler_view_image)
        mItemsUser.adapter = mAdapter
        mItemsUser.layoutManager = LinearLayoutManager(applicationContext, OrientationHelper.HORIZONTAL,false)

        mPostsUser = findViewById(R.id.recycler_wall_post_profile)
        mPostsUser.adapter = mAdapterWallPosts
        mPostsUser.layoutManager = LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL,false)

    }

    override fun showError(textResource: Int) {

    }

    override fun setupUserEmpty() {

    }

    override fun setUpProfile(params:ProfileModel) {
        mAdapter.setupCount(params)
        params.user_avatar.let { url-> Picasso.with(this).load(url).into(mAvatarUser)}
        params.user_avatar.let { url-> Picasso.with(this).load(url).into(wall_post_avatar)}
        whoIS.text = "${params.user_name} ${params.user_surname}"
        mStatusUser.text = Editable.Factory.getInstance().newEditable(params.user_status)
        bDate.text = params.user_bdate
        mCity.text = params.user_city
        if(params?.sex == 1){
            for(i in 1..10){
               if(i==params.user_family_status){
                   mRelation.text = relation_woman[i]
                   break
               }
            }
        }else if(params?.sex == 2 || params?.sex == 0){
            for(i in 1..10){
                if(i==params.user_family_status){
                    mRelation.text = relation_man[i]
                    break
                }
            }
        }
    }

    override fun startLoading() {

    }

    override fun endLoading() {

    }

    override fun setupEmptyList() {

    }

    override fun setWallPostList(postsList: ArrayList<WallPostModel>,attachemetns:ArrayList<AttachementsWallPost>) {
        mAdapterWallPosts.setWallPosts(postsList,attachemetns)
    }

    override fun startLoadingPosts() {
        cpv_profile_posts.visibility = View.VISIBLE
    }

    override fun endLoadingPosts() {
        cpv_profile_posts.visibility = View.GONE
    }
}