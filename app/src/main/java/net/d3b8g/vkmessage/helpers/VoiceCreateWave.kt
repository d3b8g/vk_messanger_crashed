package net.d3b8g.vkmessage.helpers

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.vm_wave.*
import net.d3b8g.vkmessage.R

class VoiceCreateWave: Fragment() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val voice_code =JsonParser().parse(
            "[0,0,0,0,2,2,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,2,0,0,0,2,1,1,0,0,2,31,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]") as JsonArray
        val amount_of_lt:ArrayList<Int> = ArrayList()
        val inflate_rac = inflater.inflate(R.layout.vm_wave,container,false)
        voice_code.forEachIndexed{index,element->
            val ll_w = LinearLayout(inflater.context)
            val default_params = RelativeLayout.LayoutParams(2,4)
            default_params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE)
            //default_params.addRule(RelativeLayout.RIGHT_OF,amount_of_lt[index])
            default_params.setMargins(10,0,0,0)
            ll_w.background = ColorDrawable(inflater.context.getColor(R.color.vk_share_blue_color))
            ll_w.layoutParams = default_params
            amount_of_lt.add(ll_w.id)
            val finalParent = inflate_rac.findViewById(R.id.vm_wave_rl) as RelativeLayout
            finalParent.addView(ll_w)
        }
        return inflate_rac
    }

}