package net.d3b8g.vkmessage.models

class SavesAccountUsers {
    var id:Int = 0
    var username:String = ""
    var avatar:String = ""
    var last_data:String = ""
    var user_id:Long = 0

    constructor(username:String,avatar:String,user_id:Long,last_data:String){
        this.username = username
        this.last_data = last_data
        this.avatar = avatar
        this.user_id = user_id
    }
}