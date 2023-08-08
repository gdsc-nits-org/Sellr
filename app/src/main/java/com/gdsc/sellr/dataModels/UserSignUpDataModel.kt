package com.gdsc.sellr.dataModels

data class UserSignUpDataModel(
    val email:String,
    val password:String,
    val phonenum: String?,
    val name:String?,
    val scholarID:String?,
    val otp:String?,
    val infoentered:String
)
