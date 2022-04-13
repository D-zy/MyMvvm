package com.eg.data.model.bean

import java.io.Serializable

class LoginBean1 : Serializable {
    var lastLoginTime: String? = null
    var initPasswordFlag = 0
    var userPosition: String? = null
    var onlySeeSelf = 0
    var passwordExpireFlag = 0
    var isAdmin = 0
    var userName: String? = null
    var userId = 0
    var token: String? = null
    var lastLoginIp: String? = null
    var menuList: List<MenuListBean>? = null
    var bankSources: List<BankSourcesBean>? = null

    class MenuListBean : Serializable {
        /**
         * menuId : 100322
         * bpid : 100321
         * menuName : 划付时间审核详情
         * type : page
         * icon : null
         * route : /settlement-timeRDetail/:id
         * url : null
         * buttonNo : null
         * lastMenuId : null
         * remark : null
         */
        var menuId = 0
        var bpid = 0
        var menuName: String? = null
        var type: String? = null
        var icon: Any? = null
        var route: String? = null
        var url: Any? = null
        var buttonNo: Any? = null
        var lastMenuId: Any? = null
        var remark: Any? = null
    }

    class BankSourcesBean : Serializable {
        /**
         * bankSourceId : 100000
         * bankSourceName : ALL
         */
        var bankSourceId = 0
        var bankSourceName: String? = null
    }
}