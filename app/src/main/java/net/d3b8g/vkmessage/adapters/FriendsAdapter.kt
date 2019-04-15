package net.d3b8g.vkmessage.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.helpers.ChangedNames
import net.d3b8g.vkmessage.models.FriendsModel
import net.d3b8g.vkmessage.popupActivity.UserActions

class FriendsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var mSourceOnline:ArrayList<FriendsModel> = ArrayList()
    private var allFriends:ArrayList<FriendsModel> = ArrayList()

    private var mSource:ArrayList<FriendsModel> = ArrayList()

    var mFriendsList : ArrayList<FriendsModel> = ArrayList()

    fun setupFriends(friends:ArrayList<FriendsModel>,friends_online:ArrayList<FriendsModel>){

        mSource.clear()
        mSource.addAll(friends)

        mSourceOnline.clear()
        mSourceOnline.addAll(friends_online)

        allFriends.clear()
        allFriends.addAll(mSourceOnline+mSource)

        filter(query = "")
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        mFriendsList.clear()
        allFriends.forEach {
            if(it.username.contains(query, ignoreCase = false)){
                mFriendsList.add(it)
            }else{
                it.city?.let { city-> if (city.contains(query, ignoreCase = false)){
                    mFriendsList.add(it)
                }}
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val itemView = layoutInflater.inflate(R.layout.cell_friend,p0,false)
        return FriendsViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return mFriendsList.count()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if(p0 is FriendsViewHolder){
            p0.bind(friendModel =  mFriendsList[p1])
        }
    }

    class FriendsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        private var mCivAvatar: CircleImageView= itemView.findViewById(R.id.friends_civ_avatar)
        private var lFriend:LinearLayout = itemView.findViewById(R.id.layout_of_profile_friend)
        private var mTxtUsername:TextView = itemView.findViewById(R.id.friend_txt_username)
        private var mTxtCity:TextView = itemView.findViewById(R.id.friend_online)
        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("SetTextI18n")
        fun bind(friendModel: FriendsModel){
            friendModel.avatar?.let { url-> Picasso.with(itemView.context).load(url).into(mCivAvatar) }
            val cNames = ChangedNames(itemView.context)

            if(cNames.isChanged(friendModel.user_id.toString()) != null){
                mTxtUsername.text = cNames.isChanged(friendModel.user_id.toString())
            }else{mTxtUsername.text = friendModel.username}


            if(friendModel.isOnline){
                mTxtCity.visibility = View.VISIBLE
            }else{
                mTxtCity.visibility = View.GONE
            }

            lFriend.setOnClickListener {
                val UserActionsWindow = UserActions(itemView.context,friendModel.user_id)
                if(!UserActionsWindow.isShow())
                    UserActionsWindow.show("copy_id")
            }
        }

    }

}