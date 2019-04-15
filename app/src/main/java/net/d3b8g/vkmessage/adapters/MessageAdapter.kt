package net.d3b8g.vkmessage.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.activities.DialogActivity
import net.d3b8g.vkmessage.helpers.ChangedNames
import net.d3b8g.vkmessage.models.HeavyWithAttachmentsMessage
import net.d3b8g.vkmessage.models.SimpleMessageModel
import net.d3b8g.vkmessage.popupActivity.FrameLastMessageWinow

class MessageAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    open var mSource:ArrayList<SimpleMessageModel> = ArrayList()


    fun setupDialogs(dialogsList:ArrayList<SimpleMessageModel>){
        mSource.clear()
        mSource.addAll(dialogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_dialogs,parent,false)
        return MessageViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return mSource.count()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is MessageViewHolder){
            holder.bind(messageModel =  mSource[position])
        }
    }

    class MessageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        private var mCivAvatar: CircleImageView = itemView.findViewById(R.id.message_civ_avatar)
        private var mTxtUsername: TextView = itemView.findViewById(R.id.dialogs_txt_username)
        private var mImgOnline: CircleImageView = itemView.findViewById(R.id.message_inject_online)
        private var mTxtLastMessage:TextView = itemView.findViewById(R.id.last_message)
        private var mAvatarLast:TextView = itemView.findViewById(R.id.chatuser_avatar_last_message)
        private var mCountUnread:TextView = itemView.findViewById(R.id.count_unread_message)
        private var FrameLayoutID:FrameLayout = itemView.findViewById(R.id.message_ident)

        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("SetTextI18n")
        fun bind(messageModel: SimpleMessageModel){

            FrameLayoutID.setOnClickListener {
                var dialogActivity = Intent(itemView.context.applicationContext,DialogActivity::class.java)
                dialogActivity.putExtra("username",messageModel.user_name)
                dialogActivity.putExtra("avatar",messageModel.user_avatar)
                dialogActivity.putExtra("dialog_id",messageModel.user_id)
                dialogActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                itemView.context.applicationContext.startActivity(dialogActivity)
            }
            //LongPushFrameDialog
            FrameLayoutID.setOnLongClickListener {
                if(!FrameLastMessageWinow(itemView.context,messageModel.message_id).isShow()) {

                }
                true
            }

            if(messageModel.count_unread==null){
                mCountUnread.visibility = View.GONE
            }else{
                mCountUnread.visibility = View.VISIBLE
                mCountUnread.text = messageModel.count_unread.toString()
            }

            if(messageModel.user_online){
                mImgOnline.visibility = View.VISIBLE
            }else{
                mImgOnline.visibility = View.GONE
            }

            messageModel.user_avatar?.let { url->Picasso.with(itemView.context).load(url).resize(60,60).into(mCivAvatar) }

            val cNames = ChangedNames(itemView.context)
            if(cNames.isChanged(messageModel.user_id.toString()) != null){
                mTxtUsername.text = cNames.isChanged(messageModel.user_id.toString())
            }else{mTxtUsername.text = messageModel.user_name}
            mTxtUsername.text = messageModel.user_name

            mTxtLastMessage.text = messageModel.message_body


            mAvatarLast.text = messageModel.message_avatar

        }



    }
}