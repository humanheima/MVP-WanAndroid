package com.zs.wanandroid.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.example.baselibrary.base.IBasePresenter
import com.example.baselibrary.utils.PrefUtils
import com.example.zs_wan_android.R
import com.zs.wanandroid.base.AppBaseActivity
import com.zs.wanandroid.constants.Constants
import com.zs.wanandroid.entity.IntegralEntity
import com.zs.wanandroid.http.BaseResponse
import com.zs.wanandroid.http.RetrofitManager
import com.zs.wanandroid.proxy.IConfirmClickCallBack
import com.zs.wanandroid.ui.main.MainActivity
import com.zs.wanandroid.utils.DialogUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit


/**
 * 开屏页
 * 权限：https://www.cnblogs.com/blosaa/p/9584348.html
 *
 * @author zs
 * @date 2020-03-07
 */
class SplashActivity : AppBaseActivity<IBasePresenter<*>>() {

    private var disposable: Disposable? = null
    private val tips = "玩安卓现在要向您申请存储权限，用于存储历史记录以及保存小姐姐图片，您也可以在设置中手动开启或者取消。"
    private val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val apiService = RetrofitManager.get().coroutineApiService()

    override fun init(savedInstanceState: Bundle?) {
        saveIntegral()
        requestPermission()
    }

    /**
     * 申请权限
     */
    private fun requestPermission() {
        //已申请
        if (EasyPermissions.hasPermissions(this, *perms)) {
            startIntent()
        } else {
            //为申请，显示申请提示语
            DialogUtils.tips(this, tips, object : IConfirmClickCallBack {
                override fun onClick() {
                    requestLocationAndCallPermission()
                }
            })
        }
    }

    /**
     * 开始倒计时跳转
     */
    private fun startIntent() {
        disposable = Observable.timer(2000, TimeUnit.MILLISECONDS)
            .subscribe {
                intent(MainActivity::class.java, false)
                finish()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    @AfterPermissionGranted(WRITE_EXTERNAL_STORAGE)
    private fun requestLocationAndCallPermission() {
        Log.d(TAG, "requestLocationAndCallPermission: ")
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            startIntent()
        } else {
            EasyPermissions.requestPermissions(this, "请求写入权限", WRITE_EXTERNAL_STORAGE, *perms)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 保存积分信息
     */
    private fun saveIntegral() {
        Log.d(TAG, "saveIntegral: ")
        scope.launch {
            val response: BaseResponse<IntegralEntity> = apiService.getIntegral()
            if (response.success()) {
                Log.d(TAG, "saveIntegral: success")
                PrefUtils.setObject(Constants.INTEGRAL_INFO, response.data)
            } else {
                Log.d(TAG, "saveIntegral: failed${response.errorMsg}")
            }
        }

        /*RetrofitHelper.getApiService()
            .getIntegral()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : HttpDefaultObserver<IntegralEntity>() {
                override fun onSuccess(t: IntegralEntity) {
                    PrefUtils.setObject(Constants.INTEGRAL_INFO, t)
                }

                override fun onError(errorMsg: String) {
                }

                override fun disposable(d: Disposable) {
                }
            })*/
    }

    companion object {
        private const val WRITE_EXTERNAL_STORAGE = 100
    }
}
