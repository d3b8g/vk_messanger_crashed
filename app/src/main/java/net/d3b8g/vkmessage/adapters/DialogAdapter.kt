package net.d3b8g.vkmessage.adapters

import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.vk.sdk.VKSdk
import net.d3b8g.vkmessage.R
import net.d3b8g.vkmessage.helpers.VoiceCreateWave
import net.d3b8g.vkmessage.helpers.getUnixData
import net.d3b8g.vkmessage.models.DialogModel


class DialogAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    var dialogList : ArrayList<DialogModel> = ArrayList()
    var dialog_type:Int = -1

    fun source_adpater(dialogs:ArrayList<DialogModel>,type:Int){
        dialogList.clear()
        dialogList.addAll(dialogs)
        dialog_type = type
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_message,parent,false)
        return DialogViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return dialogList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is DialogViewHolder){
            when(dialog_type){
                -1->holder.bind_all(dialogList[position])
                0->holder.only_text(dialogList[position])
                1->holder.only_my(dialogList[position])
                2->holder.only_them(dialogList[position])
            }

        }
    }

    class DialogViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val them_bckg = itemView.findViewById<FrameLayout>(R.id.they_message_frame_bckg)
        val mine_bckg = itemView.findViewById<FrameLayout>(R.id.mine_message_frame_bckg)

        val them_textbox = itemView.findViewById<TextView>(R.id.they_text_box)
        val mine_textbox = itemView.findViewById<TextView>(R.id.mine_text_box)

        val them_timebox = itemView.findViewById<TextView>(R.id.they_time_box)
        val mine_timebox = itemView.findViewById<TextView>(R.id.mine_time_box)

        val them_imgbox = itemView.findViewById<ImageView>(R.id.they_imageview_box)
        val mine_imgbox = itemView.findViewById<ImageView>(R.id.mine_imageview_box)

        val windowManager = itemView.context.resources.displayMetrics

        fun bind_all(item:DialogModel){
            if(item.from_id==VKSdk.getAccessToken().userId.toLong()){
                mine_bckg.visibility = View.VISIBLE
                for(atch in item.attachments){
                    when {
                        atch.type=="text" -> {
                            mine_textbox.maxWidth = windowManager.widthPixels/2
                            mine_textbox.text = atch.url
                        }
                        atch.type=="sticker" ->{
                            mine_imgbox.visibility = View.VISIBLE
                            Picasso.with(itemView.context).load(atch.url).into(mine_imgbox)
                        }
                        atch.type=="photo" -> {
                            mine_imgbox.visibility = View.VISIBLE
                            mine_imgbox.minimumWidth=300
                            mine_imgbox.minimumHeight = 250
                            Picasso.with(itemView.context).load(atch.url).into(mine_imgbox)
                        }
                        atch.type=="audio_message"->{
                            itemView.findViewById<FrameLayout>(R.id.mine_fragment_voice).visibility=View.VISIBLE
                            val transaction = (itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.mine_fragment_voice,VoiceCreateWave())
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }
                    }
                }
                mine_timebox.text = getUnixData(item.date,"HH:mm")
            }else{
                them_bckg.visibility = View.VISIBLE
                for(atch in item.attachments){
                    when {
                        atch.type=="text" -> {
                            them_textbox.maxWidth = windowManager.widthPixels/2
                            them_timebox.text = atch.url
                        }
                        atch.type=="sticker" -> {
                            them_imgbox.visibility = View.VISIBLE
                            Picasso.with(itemView.context).load(atch.url).into(them_imgbox)
                        }
                        atch.type=="photo" -> {
                            them_imgbox.visibility = View.VISIBLE
                            Picasso.with(itemView.context).load(atch.url).into(them_imgbox)
                        }
                        atch.type=="audio_message"->{
                            itemView.findViewById<FrameLayout>(R.id.they_fragment_voice).visibility=View.VISIBLE
                            val transaction = (itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.they_fragment_voice,VoiceCreateWave())
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }
                    }
                }
                them_timebox.text = getUnixData(item.date,"HH:mm")
            }
        }
        fun only_my(item:DialogModel){

        }
        fun only_them(item:DialogModel){

        }
        fun only_text(item:DialogModel){

        }

    }
}