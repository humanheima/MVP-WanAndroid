package com.zs.wanandroid.http

import com.zs.wanandroid.entity.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by dumingwei on 2020/5/15
 *
 * Desc: 协程版本的Api
 */
interface CoroutineApiService {
    /**
     * 获取首页文章数据
     */
    @GET("/article/list/{page}/json")
    suspend fun getHomeList(@Path("page") pageNo: Int): BaseResponse<ArticleEntity>

    /**
     * 获取首页置顶文章数据
     */
    @GET("/article/top/json")
    suspend fun getTopList(): BaseResponse<MutableList<ArticleEntity.DatasBean>>

    /**
     * banner
     */
    @GET("/banner/json")
    suspend fun getBanner(): BaseResponse<MutableList<BannerEntity>>

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): BaseResponse<UserEntity>

    @GET("/user/logout/json")
    fun logout(): Observable<BaseResponse<Any>>

    /**
     * 获取收藏文章数据
     */
    @GET("/lg/collect/list/{page}/json")
    fun getCollectData(@Path("page") pageNo: Int):
            Observable<BaseResponse<CollectEntity>>

    /**
     * 获取个人积分
     */
    @GET("/lg/coin/userinfo/json")
    suspend fun getIntegral(): BaseResponse<IntegralEntity>

    /**
     * 收藏
     */
    @POST("/lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): BaseResponse<Any>

    /**
     * 取消收藏
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int): BaseResponse<Any>

    /**
     * 获取项目tab
     */
    @GET("/project/tree/json")
    fun getProjectTabList(): Observable<BaseResponse<MutableList<TabEntity>>>

    /**
     * 获取项目tab
     */
    @GET("/wxarticle/chapters/json  ")
    fun getAccountTabList(): Observable<BaseResponse<MutableList<TabEntity>>>

    /**
     * 获取项目列表
     */
    @GET("/project/list/{pageNum}/json")
    fun getProjectList(@Path("pageNum") pageNum: Int, @Query("cid") cid: Int)
            : Observable<BaseResponse<ArticleEntity>>

    /**
     * 获取公众号列表
     */
    @GET("/wxarticle/list/{id}/{pageNum}/json")
    fun getAccountList(@Path("id") cid: Int, @Path("pageNum") pageNum: Int)
            : Observable<BaseResponse<ArticleEntity>>


    /**
     * 获取项目tab
     */
    @POST("/article/query/{pageNum}/json")
    fun search(@Path("pageNum") pageNum: Int, @Query("k") k: String)
            : Observable<BaseResponse<ArticleEntity>>

    /**
     * 体系
     */
    @GET("/tree/json")
    fun getSystemList(): Observable<BaseResponse<MutableList<SystemListEntity>>>


    /**
     * 获取项目tab
     */
    @GET("/article/list/{pageNum}/json")
    fun getSystemArticle(@Path("pageNum") pageNum: Int, @Query("cid") cid: Int)
            : Observable<BaseResponse<ArticleEntity>>

    /**
     * 导航
     */
    @GET("/navi/json")
    fun getNavigation(): Observable<BaseResponse<MutableList<NavigationEntity>>>

    /**
     * 排名
     */
    @GET("/coin/rank/{pageNum}/json")
    fun getRank(@Path("pageNum") pageNum: Int): Observable<BaseResponse<RankEntity>>

    /**
     * 积分记录
     */
    @GET("/lg/coin/list/{pageNum}/json")
    fun getIntegralRecord(@Path("pageNum") pageNum: Int): Observable<BaseResponse<IntegralRecordEntity>>

    /**
     * 我分享的文章
     */
    @GET("/user/lg/private_articles/{pageNum}/json")
    fun getMyArticle(@Path("pageNum") pageNum: Int): Observable<BaseResponse<MyArticleEntity>>

    /**
     * 我分享的文章
     */
    @POST("/lg/user_article/delete/{id}/json")
    fun deleteMyArticle(@Path("id") id: Int): Observable<BaseResponse<Any>>

    /**
     * 分享文章
     */
    @POST("/lg/user_article/add/json")
    fun shareArticle(
        @Query("title") title: String,
        @Query("link") link: String
    ): Observable<BaseResponse<Any>>

    /**
     * 注册
     */
    @POST("/user/register")
    suspend fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    ): BaseResponse<Any>
}