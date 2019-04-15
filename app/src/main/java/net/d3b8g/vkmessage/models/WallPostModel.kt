package net.d3b8g.vkmessage.models

class WallPostModel(
    var name:String,
    var avatar:String,
    var wall_id:Int,
    var wall_text:String?,
    var count_likes:Int,
    var have_like:Boolean,
    var count_comments:Int,
    var count_share:Int,
    var count_views:Int,
    var post_time:String,
    var have_pinned_post:Boolean,

//attach
    var attachHave:Boolean,
    var photo:String?,
    var photo_width:Int?,
    var photo_height:Int?,

    var poll_ques:String?,
    var poll_answers:ArrayList<PoolAnswersModel>?
) {
}