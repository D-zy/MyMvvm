package com.eg.data.model.bean

import java.io.Serializable

data class LoginBean(
        var lastLoginTime: String? = null,
        var initPasswordFlag: String? = "0",
        var userPosition: String? = null,
        var onlySeeSelf: String? = "0",
        var passwordExpireFlag: String? = "0",
        var isAdmin: String? = "0",
        var userName: String? = null,
        var userId: String? = "0",
        var token: String? = null,
        var lastLoginIp: String? = null,
        var menuList: List<LoginBean1.MenuListBean>? = null,
        var bankSources: List<LoginBean1.BankSourcesBean>? = null) : Serializable {
    data class MenuListBean(
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
            var menuId: String? = "0",
            var bpid: String? = "0",
            var menuName: String? = null,
            var type: String? = null,
            var icon: Any? = null,
            var route: String? = null,
            var url: Any? = null,
            var buttonNo: Any? = null,
            var lastMenuId: Any? = null,
            var remark: Any? = null
    ) : Serializable

    data  class BankSourcesBean(
            /**
             * bankSourceId : 100000
             * bankSourceName : ALL
             */
            var bankSourceId: String? = "0",
            var bankSourceName: String? = null
    ) : Serializable
}


