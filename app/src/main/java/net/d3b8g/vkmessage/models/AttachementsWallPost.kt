package net.d3b8g.vkmessage.models

class AttachementsWallPost(

    var post_id:Long,
    var name_pin:String,
    var avatar:String,
    var time_pin:Long,
    var image_pin:String?,
    var text_pin:String?,
    var attachments_pin:String?,

//Image Prams if have
    var image_width:Int?,
    var image_height:Int?

) {
}