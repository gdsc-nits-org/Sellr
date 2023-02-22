package com.example.sellr.data

data class LostAndFoundData(
    val objectName: String? = null,
    val objectLocation: String? = null,
    val objectDescription: String? = null,
    val uid: String? = null,
    val imagePrimary : String? = null,
    val imageList:ArrayList<String>?=null,
    val lostOrFound: String? = null,
    val pid : String? = null
)
{

}