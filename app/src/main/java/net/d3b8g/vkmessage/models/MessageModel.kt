package net.d3b8g.vkmessage.models

class MessageModel (
    var user_id:Long,
    var username:String,
    var last_message_username:String?,
    var last_message_id:Long?,
    var avatar: String?,
    var lastMessage:String,
    var countUnread:Int?,
    var avatar_last_message:String,
    var isOnline: Boolean,
    var last_seen:Long?,
    var typeAttachments:ArrayList<String>,
    var message_date:Long,

    //attach
    var attach_link:ArrayList<String>?
){}