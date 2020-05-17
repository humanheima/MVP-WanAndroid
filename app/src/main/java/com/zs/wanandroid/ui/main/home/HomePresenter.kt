package com.zs.wanandroid.ui.main.home

import android.util.Log
import com.example.baselibrary.base.BasePresenter
import com.zs.wanandroid.entity.ArticleEntity
import com.zs.wanandroid.entity.BannerEntity
import com.zs.wanandroid.http.BaseResponse
import com.zs.wanandroid.http.RetrofitManager
import kotlinx.coroutines.launch

/**
 * @author zs
 * @date 2020-03-08
 */
class HomePresenter(view: HomeContract.View) :
    BasePresenter<HomeContract.View>(view),
    HomeContract.Presenter<HomeContract.View> {

    private val apiService = RetrofitManager.get().coroutineApiService()

    /**
     * 加载首页文章列表
     */
    override fun loadData(pageNum: Int) {
        scope.launch {
            val response: BaseResponse<ArticleEntity> = apiService.getHomeList(pageNum)
            if (response.success()) {
                val datas = response.data?.datas
                datas?.forEach {
                    Log.d(TAG, "loadData: success ${it.title}")
                }
                if (pageNum == 0) {
                    datas?.let { loadTopList(it) }
                } else {
                    datas?.let { view?.showList(it) }
                }

            } else {
                Log.d(TAG, "loadData: ${response.errorMsg}")
                view?.onError(response.errorMsg)
            }
        }
    }

    /**
     * 包括置顶文章
     */
    private fun loadTopList(list: MutableList<ArticleEntity.DatasBean>) {

        scope.launch {
            val response = apiService.getTopList()
            if (response.success()) {
                response.data?.let {
                    list.addAll(0, it)
                }
                view?.showList(list)
            } else {
                Log.d(TAG, "loadTopList: ${response.errorMsg}")
                view?.onError(response.errorMsg)
            }
        }
    }

    /**
     * 由于banner和list位于同一界面
     * 所以banner在presenter内部请求
     */
    override fun loadBanner() {
        scope.launch {
            val response: BaseResponse<MutableList<BannerEntity>> = apiService.getBanner()
            if (response.success()) {
                val bannerList = response.data ?: return@launch
                view?.showBanner(bannerList)

            } else {
                Log.d(TAG, "loadBanner: ${response.errorMsg}")
                view?.onError(response.errorMsg)
            }
        }
    }

    /**
     * 取消收藏
     */
    override fun unCollect(id: Int) {
        scope.launch {
            val response = apiService.unCollect(id)
            if (response.success()) {
                view?.unCollectSuccess()
            } else {

                view?.onError(response.errorMsg)
            }
        }
    }

    /**
     * 收藏
     */
    override fun collect(id: Int) {
        scope.launch {
            val response = apiService.collect(id)
            if (response.success()) {
                view?.collectSuccess()
            } else {

                view?.onError(response.errorMsg)
            }
        }
    }
}