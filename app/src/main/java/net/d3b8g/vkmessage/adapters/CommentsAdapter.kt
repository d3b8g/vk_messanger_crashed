package net.d3b8g.vkmessage.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.models.CommentsModel

class CommentsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mSource:ArrayList<CommentsModel> = ArrayList()

    fun setupComments(comments:ArrayList<CommentsModel>){
        mSource.clear()
        mSource.addAll(comments)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_comments,parent,false)
        return CommentsViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return mSource.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is CommentsViewHolder){
            holder.bind(mSource[position])
        }
    }

    class CommentsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        //Interface
        private var usr_avatar:CircleImageView = itemView.findViewById(R.id.comments_cell_avatar)
        private var usr_name:TextView = itemView.findViewById(R.id.comments_cell_name)
        private var post_data:TextView = itemView.findViewById(R.id.comments_cell_date)


        fun bind(comments:CommentsModel){
            Picasso.with(itemView.context).load(comments.avatar).into(usr_avatar)
            usr_name.text = comments.name
            post_data.text = java.text.SimpleDateFormat("MM/dd/yyyy HH:mm").format(java.util.Date(comments.date*1000))

            if(comments.haveThread){
               // visible кнопку с хуйней для показа и отлавливать клик)))
            }
        }
    }
}