package com.hao.horderfood.model

class Staff {
    var idStaff: Int? = null;
    var nameStaff: String? = null;
    var passStaff: String? = null;
    var genderStaff: String? = null;
    var dateStaff: String? = null;
    var idCardStaff: String? = null;

    constructor(idStaff: Int?,nameStaff:String?,passStaff:String?,genderStaff:String?,dateStaff:String?,idCardStaff:String?)
    {
        this.idStaff = idStaff;
        this.nameStaff = nameStaff;
        this.passStaff = passStaff;
        this.genderStaff = genderStaff;
        this.dateStaff = dateStaff;
        this.idCardStaff = idCardStaff;
    }

    constructor(){}


}