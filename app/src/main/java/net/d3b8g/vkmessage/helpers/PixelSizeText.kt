package net.d3b8g.vkmessage.helpers

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect

class PixelSizeText {
    val text:String
    val context:Context

    constructor(text:String,context: Context){
        this.text = text
        this.context = context
    }

    fun getWidth(textSize:Float):Int{
        var paint = Paint()
        paint.textSize = textSize
        paint.style = Paint.Style.FILL
        var result_width = Rect()
        text.let { text -> paint.getTextBounds(text,0,text.length,result_width) }
        return result_width.width()
    }

    fun getHeight(textSize: Float):Int{
        var paint = Paint()
        paint.textSize = textSize
        paint.style = Paint.Style.FILL
        var result_height = Rect()
        text.let { text -> paint.getTextBounds(text,0,text.length,result_height) }
        return result_height.height()
    }

    fun getRationalLenght(str_lenght:Int,display_width:Int):String{
        val quotient = display_width.div(str_lenght)
        val result  = Math.pow(quotient.toDouble(), quotient.toDouble())
        return text
    }

}