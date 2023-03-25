package com.gdsc.sellr

data class UserModel(
    val email:String,
    val password:String,
    val phonenum: String?,
    val name:String?,
    val scholarID:String?,
    val otp:String?,
    val infoentered:String
)
