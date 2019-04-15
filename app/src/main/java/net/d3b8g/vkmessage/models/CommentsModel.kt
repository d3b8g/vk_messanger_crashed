package net.d3b8g.vkmessage.models

class CommentsModel(
    var name:String,
    var avatar:String,
    var from_id:Long,
    var date:Long,
    var text:String,
    var haveThread:Boolean,
    var count_thread:Int?,
    var comments_id:Int,
    var haveAttach:Boolean,
    var likes:Int,
    var can_like:Boolean,
    var is_online:Boolean
) {
}