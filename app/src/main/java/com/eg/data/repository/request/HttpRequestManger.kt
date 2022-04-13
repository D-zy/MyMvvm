package com.eg.data.repository.request


/**
 * 作者　: hegaojian
 * 时间　: 2020/5/4
 * 描述　: 处理协程的请求类
 */

val HttpRequestCoroutine: HttpRequestManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpRequestManger()
}

class HttpRequestManger {
    /**
     * 获取首页文章数据
     */
//    suspend fun getHomeData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
//        //同时异步请求2个接口，请求完成后合并数据
//        return withContext(Dispatchers.IO) {
//            val listData = async { apiService.getAritrilList(pageNo) }
//            //如果App配置打开了首页请求置顶文章，且是第一页
//            if (CacheUtil.isNeedTop() && pageNo == 0) {
//                val topData = async { apiService.getTopAritrilList() }
//                listData.await().data.datas.addAll(0, topData.await().data)
//                listData.await()
//            } else {
//                listData.await()
//            }
//        }
//    }

//    /**
//     * 注册并登陆
//     */
//    suspend fun register(username: String, password: String): ApiResponse<UserInfo> {
//        val registerData = apiService.register(username, password, password)
//        //判断注册结果 注册成功，调用登录接口
//        if (registerData.isSucces()) {
//            return apiService.login(username, password)
//        } else {
//            //抛出错误异常
//            throw AppException(registerData.errorCode, registerData.errorMsg)
//        }
//    }
//
//    /**
//     * 获取项目标题数据
//     */
//    suspend fun getProjectData(
//        pageNo: Int,
//        cid: Int = 0,
//        isNew: Boolean = false
//    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
//        return if (isNew) {
//            apiService.getProjecNewData(pageNo)
//        } else {
//            apiService.getProjecDataByType(pageNo, cid)
//        }
//    }
}