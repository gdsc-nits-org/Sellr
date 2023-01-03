package com.example.sellr.datahome

data class items_home(var condition: String ?= null,
                      var imagePrimary: String ?=null,
                      var price: String ?= null,
                      var sold: Boolean = false,
                      var productName: String ?= null,
                      val pid:String?=null,
                      var key:String?=null,
                      var addedtofav: Boolean = false)
