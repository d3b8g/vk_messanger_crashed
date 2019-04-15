package net.d3b8g.vkmessage.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.activities.CommentsActivity
import net.d3b8g.vkmessage.models.AttachementsWallPost
import net.d3b8g.vkmessage.models.PoolAnswersModel
import net.d3b8g.vkmessage.models.WallPostModel

class WallPostAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private var mSource:ArrayList<WallPostModel> = ArrayList()
    private var mAttachement:ArrayList<AttachementsWallPost> = ArrayList()

    fun setWallPosts(params: ArrayList<WallPostModel>,attPost:ArrayList<AttachementsWallPost>){
        mSource.clear()
        mSource.addAll(params)
        mAttachement.clear()
        mAttachement.addAll(attPost)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_wall_post,parent,false)
        return WallPostViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
       return mSource.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is WallPostViewHolder){
            holder.bind(mSource[position],mAttachement[position])
        }
    }

    class WallPostViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        //Interface
        private var mCivAvatar: CircleImageView = itemView.findViewById(R.id.wall_post_civ_avatar)
        private var btnHavePinPost: ImageButton = itemView.findViewById(R.id.info_pin_post)
        private var alienPost:RelativeLayout = itemView.findViewById(R.id.alien_post)
        private var userTextPost:TextView = itemView.findViewById(R.id.user_text_post)
        private var username:TextView = itemView.findViewById(R.id.wall_post_username)
        private var posting_time:TextView = itemView.findViewById(R.id.wall_post_time)
        //Intefrace Another
        private var photo_var:ImageView = itemView.findViewById(R.id.user_photo_post)
        //Count
        private var likes: TextView = itemView.findViewById(R.id.wall_post_likes_count)
        private var shares: TextView = itemView.findViewById(R.id.wall_post_share_count)
        private var comments: TextView = itemView.findViewById(R.id.wall_post_comments_count)
        //Function
        private var like:ImageButton = itemView.findViewById(R.id.wall_post_likes_fun)
        private var share:ImageButton = itemView.findViewById(R.id.wall_post_share_fun)
        private var comment:ImageButton = itemView.findViewById(R.id.wall_post_comments_fun)


        fun bind(params:WallPostModel, attachements:AttachementsWallPost){
            params.avatar?.let { url-> Picasso.with(itemView.context).load(url).into(mCivAvatar) }

            likes.text = params.count_likes.toString()
            shares.text = params.count_share.toString()
            comments.text = params.count_comments.toString()

            username.text = params.name
            userTextPost.text = params.wall_text

            like.setOnClickListener {
                if(!params.have_like){
                    like.animation = AnimationUtils.loadAnimation(itemView.context,R.anim.zoom_in_like_fun)
                    Picasso.with(itemView.context).load(R.drawable.black_heart).into(like)
                    params.have_like = true
                }else{
                    Picasso.with(itemView.context).load(R.drawable.empty_heart).into(like)
                    params.have_like = false
                }
            }
            comment.setOnClickListener {
                var commentsActivity = Intent(itemView.context.applicationContext,CommentsActivity::class.java)
                commentsActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                itemView.context.applicationContext.startActivity(commentsActivity)
            }

            var epoch = java.text.SimpleDateFormat("MM/dd/yyyy HH:mm").format(java.util.Date(params.post_time.toLong()*1000))
            posting_time.text = epoch

            if(params.have_pinned_post)
                btnHavePinPost.visibility = View.VISIBLE

            btnHavePinPost.setOnClickListener {
                if(alienPost.visibility == View.GONE){
                    alienPost.visibility = View.VISIBLE
                    btnHavePinPost.rotation = 180f
                    startLoadAttachements(attachements)
                }else{
                    alienPost.visibility = View.GONE
                    btnHavePinPost.rotation = 360f
                }
            }

            if(params.attachHave)
                addAdttachments(params.photo,params.photo_width,params.photo_height,params.poll_ques,params.poll_answers)
        }

        private fun addAdttachments(photo:String?,width:Int?,height:Int?,
                                    poll_ques:String?,poll_answrs:ArrayList<PoolAnswersModel>?){
            if(photo != null){
                width?.let { width->photo_var.layoutParams.width = width }
                height?.let { height->photo_var.layoutParams.height = height }
                photo?.let { photo->Picasso.with(itemView.context).load(photo).into(photo_var)}
            }

        }

        private var avatar_at:CircleImageView = itemView.findViewById(R.id.wall_post_alien_avatar)
        private var time_at:TextView = itemView.findViewById(R.id.wall_post_date_alien)
        private var name_at:TextView = itemView.findViewById(R.id.wall_post_username_alien)
        private var text_at:TextView = itemView.findViewById(R.id.text_alien_post_reposted)
        private var photo_at:ImageView = itemView.findViewById(R.id.image_alien_post_reposted)

        private fun startLoadAttachements(attachements: AttachementsWallPost) {
            if(attachements.image_pin!=null){
                photo_at.visibility = View.VISIBLE
                attachements.image_height?.let { height -> photo_at.layoutParams.height = height }
                attachements.image_width?.let { width -> photo_at.layoutParams.width = width }
                attachements.image_pin?.let { url -> Picasso.with(itemView.context).load(url).into(photo_at) }
            }

            var epoch = java.text.SimpleDateFormat("MM/dd/yyyy HH:mm").format(java.util.Date(attachements.time_pin*1000))
            time_at.text = epoch
            name_at.text = attachements.name_pin
            text_at.text = attachements.text_pin
            attachements.avatar?.let { url -> Picasso.with(itemView.context).load(url).into(avatar_at) }
        }
    }
}