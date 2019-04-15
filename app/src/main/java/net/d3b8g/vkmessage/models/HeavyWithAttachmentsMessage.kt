package net.d3b8g.vkmessage.models

class HeavyWithAttachmentsMessage(
    var user_id:Long,
    var username_lm:String,
    var date_lm:Long,
    var avatar_lm:String,
    var last_seen:Long?,
    var typeAttachments:ArrayList<String>,
    var attach_link:ArrayList<String>?
) {
}