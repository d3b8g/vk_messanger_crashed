package net.d3b8g.vkmessage.helpers

class WordEndings {
    fun wordEndingsEdit(count:Int,def:Array<String>):Array<String>{
        return if(count.toString().substring(count.toString().length-1,count.toString().length)=="1"){
            arrayOf(def[0]+"ное",def[1]+"ние")
        }else{
            arrayOf(def[0]+"ных",def[1]+"ния")
        }
    }
}