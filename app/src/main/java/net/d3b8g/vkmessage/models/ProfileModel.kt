package net.d3b8g.vkmessage.models

class ProfileModel(
    var user_avatar:String,
    var user_name:String,
    var user_id:Long,
    var user_surname:String,
    var user_status:String,
    var user_bdate:String,
    var user_city:String,
    var user_family_status:Int,
    var relation_partner:String?,
    var sex:Int,

    var count_video:String?,
    var count_audio:String?,
    var count_wallpost:String?,
    var count_followers:Int?,
    var count_photo:String?
) {
}