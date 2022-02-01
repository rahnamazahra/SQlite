package com.rahnama.sqlapplication.model

class ContactModel() {
    var id:Int?=null
    var name:String?=null
    var family:String?=null
    var phone:String?=null

    constructor(id:Int,name:String,family:String,phone:String):this(){
        this.id=id
        this.name=name
        this.family=family
        this.phone=phone
    }

}