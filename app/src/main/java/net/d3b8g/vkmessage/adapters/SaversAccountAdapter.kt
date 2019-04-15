package net.d3b8g.vkmessage.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.models.SavesAccountUsers

class SaversAccountAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mSource:ArrayList<SavesAccountUsers> = ArrayList()

    fun setupAccount(account:ArrayList<SavesAccountUsers>){
        mSource.clear()
        mSource.addAll(account)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_save_account_form,parent,false)
        return SaversAccountAdapterView(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return mSource.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is SaversAccountAdapterView){
            holder.bind(mSource[position])
        }
    }

    class SaversAccountAdapterView(itemView: View):RecyclerView.ViewHolder(itemView){
        private var username:TextView = itemView.findViewById(R.id.login_save_username)
        private var layout:FrameLayout = itemView.findViewById(R.id.saves_user_layout_to_login)
        private var date:TextView = itemView.findViewById(R.id.login_save_lastdate_activ)
        private var avatar:CircleImageView = itemView.findViewById(R.id.login_save_avatar)
        fun bind(account: SavesAccountUsers){
            username.text = account.username
            date.text = account.last_data
            Picasso.with(itemView.context).load(account.avatar).into(avatar)
        }

    }
}