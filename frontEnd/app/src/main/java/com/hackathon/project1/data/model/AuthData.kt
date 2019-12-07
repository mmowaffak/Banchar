package com.hackathon.project1.data.model

class AuthData (

    var email: String? = null,
    var name: String? = null,
    var username: String? = null,
    var password: String? = null,
    var type: Int? = null, //1 for winch & 2 for customer
    var carType: String? =  null,
    var licensePlate: String? =  null,
    var usernameOrEmail: String? = null

)



//{

//    fun prepareSignup(email: String,name: String?, username: String?, password: String?): AuthData {
//        val data = AuthData()
//        data.email = email
//        data.name = name
//        data.username = username
//        data.password = password
//
//        return data
//    }
//}