package net.d3b8g.vkmessage.adapters

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.models.ProfileModel

class HorizontalAdapterProfile: RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private var mSource: ProfileModel? = null

    fun setupCount(it:ProfileModel){
        mSource = it
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_case_profile_items,parent,false)
        return HorizontalViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is HorizontalViewHolder){
            holder.bind(position,mSource)
        }
    }

    class HorizontalViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        private var mCount:TextView = itemView.findViewById(R.id.count_user_items)
        private var mName:TextView = itemView.findViewById(R.id.name_items_profile)

        var list = listOf<String>("Подписчики","Видеозаписи","Аудиозаписи","Посты","Фотографии")

        @SuppressLint("SetTextI18n")
        fun bind(count:Int, items:ProfileModel?){
            mName.text = list[count]
            mCount.text = when (count) {
                0 -> items?.count_followers.toString()
                1 -> items?.count_video
                2 -> items?.count_audio
                3 -> items?.count_wallpost
                4 -> items?.count_photo
                else -> "Пусто"
            }
        }
    }
}