package com.asilbek.messenger.models



class ChatMessage(val id:String,val text: String,val fromId:String,val toId:String,val time:String){
    constructor():this("","","","","")



}