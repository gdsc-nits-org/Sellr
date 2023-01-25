package com.example.sellr.data

data class SellData(val productName : String? = null,
                    val productDesc : String? = null, val category : String? = null,
                    val additionalCategory:String?=null,
                    val condition : String? = null,
                    val usedForTime: String?= null,
                    val price : String? = null,
                    val imagePrimary : String? = null,
                    val imageList:ArrayList<String>?=null,
                    val userUID : String? = null,val sold:Boolean?=false,val pid : String? = null,val sellingDate : String? = null)
{

}



        
