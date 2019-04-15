package net.d3b8g.vkmessage.helpers

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

inline fun compil_avatar(num:Int):GradientDrawable{

    var color_component:IntArray = when(num){
        in 0..5 -> intArrayOf(Color.parseColor("#84fab0"),Color.parseColor("#8fd3f4"))
        in 5..10 -> intArrayOf(Color.parseColor("#f093fb"),Color.parseColor("#f5576c"))
        in 10..15-> intArrayOf(Color.parseColor("#30cfd0"),Color.parseColor("#330867"))
        in 15..20->intArrayOf(Color.parseColor("#4facfe"),Color.parseColor("#00f2fe"))
        in 20..25 ->intArrayOf(Color.parseColor("#88d3ce"),Color.parseColor("#6e45e2"))
        else -> intArrayOf(Color.parseColor("#fa709a"),Color.parseColor("#fee140"))
    }
    val gd:GradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,color_component)
    gd.cornerRadius = 0f

    return gd
}

class unlim_gavatar(var key:String,var num:Int)

inline fun correctNumber(key:String,lang:Int):unlim_gavatar{

    // 0 - rus
    // 1 - en
    // 2 - another lang, symbol

    val rus:Array<String> = arrayOf("а","б","в","г","д","е","ё","ж","з","и","й","к","л","м","н","о","п","р","с","т","у","ф","х","ц","ч","ш","щ","ъ","ы","ъ","э","ю","я")

    val en:Array<String> = arrayOf("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z")

    when(lang){
        0->{
            for((number, i) in rus.withIndex()){
                if(key.toLowerCase()  == i) return unlim_gavatar(i,number)
            }
        }
        1->{
            for((number, i) in en.withIndex()){
                if(key.toLowerCase()  == i) return unlim_gavatar(i,number)
            }
        }
        else->{
            return unlim_gavatar(key,9)
        }
    }
    return unlim_gavatar(key.toLowerCase(),27)
}