package net.d3b8g.vkmessage.helpers

fun getUnixData(unix:Long,pattern:String):String{
    return java.text.SimpleDateFormat(pattern).format(java.util.Date(unix*1000))
}
fun getChangeInDate(unix:Long,circs:String):String{
    val now = System.currentTimeMillis()/1000L
    return when {
        now - unix in 71..8640 -> "$circs${java.text.SimpleDateFormat("HH:mm").format(java.util.Date(unix))}"
        now-unix in 86400..172800 -> "вчера в ${java.text.SimpleDateFormat("HH:mm").format(java.util.Date(unix))}"
        else -> {
            val nowInMMMMM = getUnixData(now,"MMMM")
            val unixInMMMMM = getUnixData(unix,"MMMM")
            val nowInYYYY = getUnixData(now,"yyyy")
            val unixInYYYY= getUnixData(unix,"yyyy")
            when {
                nowInMMMMM!=unixInMMMMM ->"$circs${getUnixData(unix,"MM.dd HH:mm")}"
                nowInYYYY!=unixInYYYY -> "$circs${getUnixData(unix,"yyyy MM.dd HH:mm")}"
                else -> "$circs${getUnixData(unix,"dd HH:mm")}"
            }
        }
    }
}
fun getUnixDataNow():Long{
    return System.currentTimeMillis()/1000
}