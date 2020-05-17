package com.zs.wanandroid.ui.login

import com.example.baselibrary.base.BasePresenter
import com.example.baselibrary.utils.PrefUtils
import com.zs.wanandroid.constants.Constants
import com.zs.wanandroid.event.LoginEvent
import com.zs.wanandroid.http.RetrofitManager
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class LoginPresenter(
    view: LoginContract.View
) : BasePresenter<LoginContract.View>(view), LoginContract.Presenter<LoginContract.View> {

    private val apiService = RetrofitManager.get().coroutineApiService()

    override fun login(username: String, password: String) {

        scope.launch {
            val response = apiService.login(username, password)
            if (response.success()) {
                response.data?.let {
                    //登陆成功保存用户信息，并发送消息
                    PrefUtils.setObject(Constants.USER_INFO, it)
                    EventBus.getDefault().post(LoginEvent())
                    view?.loginSuccess()
                }
            } else {
                view?.onError(response.errorMsg)
            }
        }
        /*RetrofitHelper.getApiService()
            .login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : HttpDefaultObserver<UserEntity>() {
                override fun onSuccess(t: UserEntity) {
                    //登陆成功保存用户信息，并发送消息
                    PrefUtils.setObject(Constants.USER_INFO, t)
                    EventBus.getDefault().post(LoginEvent())
                    view?.loginSuccess()
                }

                override fun onError(errorMsg: String) {
                    view?.onError(errorMsg)
                }

                override fun disposable(d: Disposable) {
                    addSubscribe(d)
                }
            })*/
    }


}
