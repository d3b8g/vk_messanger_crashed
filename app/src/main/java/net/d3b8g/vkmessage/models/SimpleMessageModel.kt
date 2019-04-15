package net.d3b8g.vkmessage.models

class SimpleMessageModel(
    var user_id:Long,
    var user_online:Boolean,
    var count_unread:Int?,
    var user_name:String,
    var message_body:String,
    var message_id:Int,
    var user_avatar:String?,
    var message_avatar:String
)