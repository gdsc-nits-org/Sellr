package com.gdsc.sellr.model
import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.checkerframework.checker.units.qual.s

@Parcelize
data class Sell(
                    var productName : String? = null,
                    var productDesc : String? = null, var category : String? = null,
                    var additionalCategory:String?=null,
                    var condition : String? = null,
                    var usedForTime: String?= null,
                    var price : String? = null,
                    var imagePrimary : String? = null,
                    var imageList:ArrayList<String>?=null,
                    var userUID : String? = null,
                    var sold:Boolean?=false,
                    var pid : String? = null,
                    var sellingDate : String? = null)
    :Parcelable{
    constructor(productName: String,
                productDesc : String,
                category : String,
                additionalCategory:String,
                condition : String,
                usedForTime: String,
                price : String,
                imagePrimary : String,
                imageList:ArrayList<String>,
                userUID : String,
                sold:Boolean,
                pid : String,
                sellingDate : String) : this() {
        this.pid = pid
        this.category = category
        this.imagePrimary = imagePrimary
        this.additionalCategory = additionalCategory
        this.sold = sold
        this.productName = productName
        this.productDesc = productDesc
        this.condition = condition
        this.userUID = userUID
        this.usedForTime = usedForTime
        this.price = price
        this.imageList = imageList
        this.sellingDate = sellingDate

    }
}