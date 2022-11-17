package com.hao.horderfood.model

class Food {
    var idFood:Int? = null
    var nameFood:String? = null
    var priceFood:Int? = null
    var imgFood:ByteArray? = null
    var idType:Int? = null

    constructor(idFood:Int, nameFood:String,priceFood:Int, imgFood:ByteArray? ,idType:Int)
    {
        this.idFood = idFood
        this.nameFood = nameFood
        this.priceFood = priceFood
        this.imgFood = imgFood
        this.idType = idType
    }

    constructor(){}
}