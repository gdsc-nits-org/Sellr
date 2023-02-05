package com.example.sellr.data

data class LostAndFoundData(val objectName:String ?= null  ,
                            val objectLocation:String ?= null  ,
                            val contactNumber:String ?= null,
                            val uid:String ?= null,
                            val imageUrl : String ?= null,
                            val LostOrFound : String ?= null)
{

}