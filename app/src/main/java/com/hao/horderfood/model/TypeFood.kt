package com.hao.horderfood.model

import java.sql.Blob

class TypeFood {
    var idType: Int? = null
    var nameType: String? = null
    var imgType: ByteArray? = null

    constructor(idType:Int?, nameType:String?, imgType:ByteArray?)
    {
        this.idType = idType
        this.nameType = nameType;
        this.imgType = imgType;
    }
    constructor(){}
}