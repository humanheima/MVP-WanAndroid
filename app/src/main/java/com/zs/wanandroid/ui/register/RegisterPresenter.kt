package com.zs.wanandroid.ui.register

import android.util.Log
import com.example.baselibrary.base.BasePresenter
import com.zs.wanandroid.http.RetrofitManager
import kotlinx.coroutines.launch

/**
 * des 注册
 * @author zs
 * @date 2020-03-18
 */
class RegisterPresenter(view: RegisterContract.View) : BasePresenter<RegisterContract.View>(view)
    , RegisterContract.Presenter<RegisterContract.View> {

    private val apiService = RetrofitManager.get().coroutineApiService()

    override fun register(username: String, password: String, repassword: String) {

        scope.launch {
            val response = apiService.register(username, password, repassword)
            if (response.success()) {
                view?.registerSuccess()

            } else {
                val errorMsg = response.errorMsg
                Log.d(TAG, "register: error $errorMsg")
                view?.onError(errorMsg)
            }
        }
    }
}