package com.robam.common.io.cloud;

import com.legent.plat.pojos.RCReponse;
import com.robam.common.io.cloud.Reponses.AlbumResponse;
import com.robam.common.io.cloud.Reponses.AlbumsResponse;
import com.robam.common.io.cloud.Reponses.CookbookImageReponse;
import com.robam.common.io.cloud.Reponses.CookbookProviderResponse;
import com.robam.common.io.cloud.Reponses.CookbookResponse;
import com.robam.common.io.cloud.Reponses.CookbooksResponse;
import com.robam.common.io.cloud.Reponses.DeiverIfAllowReponse;
import com.robam.common.io.cloud.Reponses.EventStatusReponse;
import com.robam.common.io.cloud.Reponses.GetCrmCustomerReponse;
import com.robam.common.io.cloud.Reponses.GetCustomerInfoReponse;
import com.robam.common.io.cloud.Reponses.GetOrderReponse;
import com.robam.common.io.cloud.Reponses.GetSmartParams360Reponse;
import com.robam.common.io.cloud.Reponses.HomeAdvertsForMobResponse;
import com.robam.common.io.cloud.Reponses.HomeAdvertsForPadResponse;
import com.robam.common.io.cloud.Reponses.HomeTitleForMobResponse;
import com.robam.common.io.cloud.Reponses.HotKeysForCookbookResponse;
import com.robam.common.io.cloud.Reponses.MaterialFrequencyResponse;
import com.robam.common.io.cloud.Reponses.MaterialsResponse;
import com.robam.common.io.cloud.Reponses.OrderIfOpenReponse;
import com.robam.common.io.cloud.Reponses.QueryMaintainReponse;
import com.robam.common.io.cloud.Reponses.QueryOrderReponse;
import com.robam.common.io.cloud.Reponses.SmartParamsReponse;
import com.robam.common.io.cloud.Reponses.StoreCategoryResponse;
import com.robam.common.io.cloud.Reponses.StoreVersionResponse;
import com.robam.common.io.cloud.Reponses.SubmitOrderReponse;
import com.robam.common.io.cloud.Reponses.ThumbCookbookResponse;
import com.robam.common.io.cloud.Requests.ApplyAfterSaleRequest;
import com.robam.common.io.cloud.Requests.CookAlbumRequest;
import com.robam.common.io.cloud.Requests.CookingLogRequest;
import com.robam.common.io.cloud.Requests.GetCookAlbumsRequest;
import com.robam.common.io.cloud.Requests.GetCookbooksByNameRequest;
import com.robam.common.io.cloud.Requests.GetCookbooksByTagRequest;
import com.robam.common.io.cloud.Requests.GetCrmCustomerRequest;
import com.robam.common.io.cloud.Requests.GetGroudingRecipesRequest;
import com.robam.common.io.cloud.Requests.GetOrderRequest;
import com.robam.common.io.cloud.Requests.GetRecommendRecipesByDeviceForPadRequest;
import com.robam.common.io.cloud.Requests.GetSmartParams360Request;
import com.robam.common.io.cloud.Requests.GetSmartParamsRequest;
import com.robam.common.io.cloud.Requests.GetTodayRecipesByDeviceRequest;
import com.robam.common.io.cloud.Requests.QueryMaintainRequest;
import com.robam.common.io.cloud.Requests.QueryOrderRequest;
import com.robam.common.io.cloud.Requests.SaveCustomerInfoRequest;
import com.robam.common.io.cloud.Requests.SetSmartParams360Request;
import com.robam.common.io.cloud.Requests.SetSmartParamsByDailyRequest;
import com.robam.common.io.cloud.Requests.SetSmartParamsByWeeklyRequest;
import com.robam.common.io.cloud.Requests.StoreRequest;
import com.robam.common.io.cloud.Requests.SubmitCookAlbumRequest;
import com.robam.common.io.cloud.Requests.SubmitMaintainRequest;
import com.robam.common.io.cloud.Requests.SubmitOrderRequest;
import com.robam.common.io.cloud.Requests.UpdateOrderContacterRequest;
import com.robam.common.io.cloud.Requests.UserBookRequest;
import com.robam.common.io.cloud.Requests.UserMaterialRequest;
import com.robam.common.io.cloud.Requests.UserOrderRequest;
import com.robam.common.io.cloud.Requests.UserRequest;
import com.robam.common.io.cloud.Requests.getGroundingRecipesByDeviceRequest;
import com.robam.common.io.cloud.Requests.getNotRecommendRecipesByDeviceRequest;
import com.robam.common.io.cloud.Requests.getRecommendRecipesByDeviceForCellphoneRequest;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface IRokiRestService {

    String Url_UserProfile = "http://h5.myroki.com/#/agreement";

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    String getStoreVersion = "/rest/api/store/version/get";
    //获取菜谱分类换成新接口
    //String getStoreCategory = "/rest/api/cookbook/group-tag/get";
    String getStoreCategory = "/rest/cks/api/cookbook/group-tag/get";
    String getCookbookProviders = "/rest/api/cookbook/source/get";
    //将获取某个标签下的菜谱换成新接口
    //String getCookbooksByTag = "/rest/api/cookbook/by-tag/get";
    String getCookbooksByTag = "/rest/api/cookbook/by-tag/get";
    //根据名称搜索菜谱换成新接口
    String getCookbooksByName = "/rest/cks/api/cookbook/by-name/get";
    //获取时令菜
    // String getSeasonCookbooks = "/rest/api/cookbook/season/get";
    String getSeasonCookbooks = "/rest/cks/api/cookbook/seasion/get";
    String getRecommendCookbooksForMob = "/rest/api/cookbook/recommend/app/get";
    String getRecommendCookbooksForPad = "/rest/api/cookbook/recommend/pad/get-en";
    //    String getRecommendCookbooksForPadEn = "/rest/api/cookbook/recommend/pad/get-en";
    String getHotKeysForCookbook = "/rest/api/cookbook/hot/get";
    String getRecipeOfThemeList = "/rest/api/cookbook/get-by-ids";
    String getOldCookbookById = "/rest/api/cookbook/by-id/get";
    String getCookbookById = "/rest/cks/api/cookbook/details/get-by-id";
    String getRecipeStepById = "/rest/cks/api/cookbook/steps";
    String getAccessoryFrequencyForMob = "/rest/api/cookbook/recommend/app/accessory/get";
    String getCookbookSteps = "/rest/cks/api/cookbook/steps";

    String getPersonalizedRecipeBook = "/rest/cks/api/recommender/user/get";        //获取热门个性化菜谱
    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    String getTodayCookbooks = "/rest/api/cookbook/today/get";
    String addTodayCookbook = "/rest/api/cookbook/today/add";
    String deleteTodayCookbook = "/rest/api/cookbook/today/delete";
    String deleteAllTodayCookbook = "/rest/api/cookbook/today/all/delete";
    String exportMaterialsFromToday = "/rest/api/cookbook/today/material/export";
    String addMaterialsToToday = "/rest/api/cookbook/today/material/add";
    String deleteMaterialsFromToday = "/rest/api/cookbook/today/material/delete";
    String getFavorityCookbooks = "/rest/api/cookbook/collect/get";
    String addFavorityCookbooks = "/rest/api/cookbook/collect/add";
    String delteFavorityCookbooks = "/rest/api/cookbook/collect/delete";
    //String delteAllFavorityCookbooks = "/rest/api/cookbook/collect/all/delete";
    String delteAllFavorityCookbooks = "/rest/api/cookbook/collect/delete-all";
    String getGroundingRecipes = "/rest/api/cookbook/grounding/get";

    //厨房知识组
    String getCookingKnowledgeGroup = "/rest/ops/api/cookingKnowledgeGroup/getCookingKnowledgeGroup";
    //获取厨房支持列表
    String  getCookingKnowledge = "/rest/ops/api/cookingKnowledge/getCookingKnowledge";

    //根据设备品类获取所有菜谱(只包括导航菜谱)
    String getGroundingRecipesByDevice = "/rest/cks/api/cookbook/grounding/get-by-dc";
    //根据设备品类获取Pad端推荐菜谱
    String getRecommendRecipesByDeviceForPad = "/rest/api/cookbook/recommend/pad/get-by-dc";
    //根据设备品类获取手机端推荐菜谱
    String getRecommendRecipesByDeviceForCellphone = "/rest/api/cookbook/recommend/app/get-by-dc";
    //根据设备品类获取非推荐的菜谱
    String getNotRecommendRecipesByDevice = "/rest/api/cookbook/not-recommend/get-by-dc";
    //根据设备品类获取今日菜谱
    String getTodayRecipesByDevice = "/rest/api/cookbook/today/get-by-dc";
    //**RENT ADD**/
    //获取主题菜谱列表
    String getThemeRecipeList = "/rest/cks/api/theme/list/get";
    String getThemeRecipeList_new = "/rest/api/theme-manage/theme/list/get";
    //获取当前主题是否被收藏
    //获取动态封面
    String getDynamicCover = "/rest/api/cooking/album/get-dynamics-first";
    //获取菜谱视频列表
    String getRecipeLiveList = "/rest/cks/api/cookbook/video-cookbook/get";
    //获取动态厨艺换成新接口
    // String getRecipeDynamicShow = "/rest/api/cooking/album/get-favorite-dynamics-new";
    String getRecipeDynamicShow = "/rest/cks/api/cooking/albums/newest/get";
    //获取动态厨艺-new
    String getRecipeDynamicShow_new = "/rest/api/cooking/albums/newest/get";
    //获取主题是否收藏
    String isThemeCollected = "/rest/api/user-theme-relation/is-favorite";
    //收藏
    String setCollectOfTheme = "/rest/api/user-theme-relation/add-favorite-theme";
    //取消收藏
    String cancelCollectOfTheme = "/rest/api/user-theme-relation/delete";
    //获取咨询列表换成新接口
    //String getConsultationList = "/rest/api/news-manage/list-all-new";
    String getConsultationList = "/rest/ops/api/news-manage/list-page";
    //获取设备菜谱封面
    String getDeviceRecipeImg = "/rest/api/device/get-detail-desc";
    //获取已收藏主题
    //String getRecipeThemeOfCollect="/rest/api/user-theme-relation/select-by-userId";//老接口
    String getRecipeThemeOfCollect = "/rest/cks/api/user-theme/collection/get";
    String getRecipeThemeOfCollect_new = "/rest/api/user-theme-relation/collection/get";
    //获取首页直播信息
    String getCurrentLiveShow = "/rest/ops/api/liveshow/current/get";

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------
    String addCookingLog = "/rest/api/cooking/record/add";

    String addCookingRecordInfo = "/rest/cks/api/cooking/record/submit"; //烧菜记录提交 -吴四


    // -------------------------------------------------------------------------------
    //album
    // -------------------------------------------------------------------------------
    String getMyCookAlbumByCookbook = "/rest/api/cooking/album/my/get";
    //获取他人分享相册换成新接口
    //String getOtherCookAlbumsByCookbook = "/rest/api/cooking/album/others/get";
    String getOtherCookAlbumsByCookbook = "/rest/cks/api/cooking/album/exclude/get";
    String getOtherCookAlbumsByCookbook_new = "/rest/api/cooking/album/exclude/get";
    String submitCookAlbum = "/rest/api/cooking/album/add";
    String removeCookAlbum = "/rest/api/cooking/album/delete";
    String praiseCookAlbum = "/rest/api/cooking/album/point-praise";
    String unpraiseCookAlbum = "/rest/api/cooking/album/cancel-point-praise";
    //关注动态换成新接口
    //String getMyCookAlbums = "/rest/api/cooking/album/my/get-all";
    String getMyCookAlbums = "/rest/cks/api/cooking/albums/my/get";
    String clearMyCookAlbums = "/rest/api/cooking/album/delete-all";

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------
    String getHomeAdvertsForMob = "/rest/api//app/image/advert/drop-down/get";
    String getHomeTitleForMob = "/rest/api/app/image/title/get";
    String getHomeAdvertsForPad = "/rest/api/pad/image/advert/get";
    String getFavorityImagesForPad = "/rest/api/pad/image/collect/get";
    String getRecommendImagesForPad = "/rest/api/pad/image/recommend/get";
    String getAllBookImagesForPad = "/rest/api/pad/image/cookbook/get";

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    String applyAfterSale = "/rest/api/customer-service/apply";
    String getSmartParams = "/rest/api/device/intelligence/get";
    String setSmartParamsByDaily = "/rest/api/device/intelligence/by-day";
    String setSmartParamsByWeekly = "/rest/api/device/intelligence/weekly";
    String getSmartParams360 = "/rest/api/device/360/get";
    String setSmartParams360 = "/rest/api/device/360/set";

    // -------------------------------------------------------------------------------
    //免费配送
    // -------------------------------------------------------------------------------
    String getCustomerInfo = "/rest/api/shopping/customer-info/get";
    String saveCustomerInfo = "/rest/api/shopping/customer-info/save";
    String submitOrder = "/rest/api/shopping/order/save";
    String getOrder = "/rest/api/shopping/order/get-by-id";
    String updateOrderContacter = "/rest/api/shopping/order/customer-info/update";
    String queryOrder = "/rest/api/shopping/order/get";
    String cancelOrder = "/rest/api/shopping/order/delete";
    String orderIfOpen = "/rest/api/shopping/if-open";
    String deiverIfAllow = "/rest/api/shopping/if-allow-free-send";
    String getEventStatus = "/rest/api/event/status/get";


    // -------------------------------------------------------------------------------
    // 清洁维保
    // -------------------------------------------------------------------------------

    String getCrmCustomerRequest = "/rest/api/maintain/customer/get";
    String submitMaintainRequest = "/rest/api/maintain/save";
    String queryMaintainRequest = "/rest/api/maintain/get";


    // -------------------------------------------------------------------------------
    //获取搜索历史记录
    String getCookbookSearchHistory = "/rest/cks/api/cookbook/search/history/get";
    String postSearchWord = "/rest/cks/api/cookbook/by-name/search";
    // -------------------------------------------------------------------------------
    String setFamilyMember = "/rest/dms/api/device/family-member/set";
    String getSetFamilyMember = "/rest/dms/api/device/family-member/get";
    String getTodayDrinking = "/rest/dms/api/device/drinking-volume/today/get";
    String getHistoryDrinking = "/rest/dms/api/device/drinking-volume/history/get";

    String getYiGuo = "/rest/ops/api/advert/app/image/advert/drop-down/get";

    // String getCookBooksClassify="/rest/cks/api/cookbook/by-name/get";

  /*  @POST(getCookBooksClassify)
    void getCookBooksClassify(@Body GetCookbooksByNameRequest reqBody, Callback<Reponses.CookbooksClassifyResponse> callback);*/

    /**
     * 获取热门菜谱
     */
    @POST(getPersonalizedRecipeBook)
    void getPersonalizedRecipeBooks(@Body Requests.PersonalizedRecipeRequest reqBody,
                                    Callback<Reponses.PersonalizedRecipeResponse> callback);


    @POST(getCookbookSearchHistory)
    void getCookbookSearchHistory(@Body Requests.CookbookSearchHistory reqBody,
                                  Callback<Reponses.HistoryResponse> callback);
    @POST(postSearchWord)
    void getSearchResult(@Body Requests.SearchWord reqBody,Callback<Reponses.SearchResult> callback);

//

    @POST(getYiGuo)
    void getYiGuoUrl(Callback<Reponses.GetYiGuoUrlResponse> callback);
    /**
     * 获取当前饮水量
     */
    @POST(getTodayDrinking)
    void getTodayDrinking(@Body Requests.TodayDrinkingRequest reqBody,
                          Callback<Reponses.TodayDrinkingResponse> callback);



    /**
     * 获取历史饮水量
     */
    @POST(getHistoryDrinking)
    void getHistoryDrinking(@Body Requests.HistoryDrinkingRequest reqBody,
                            Callback<Reponses.HistoryDrinkingResponse> callback
    );

    /**
     * 获取厨房知识组
     */
    @POST(getCookingKnowledgeGroup)
    void  getCookingKnowledgeGroup(Callback<Reponses.CookingKnowledgeGroupResponse> callback);

    /**
     * 获取厨房知识列表
     */
    @POST(getCookingKnowledge)
    void getCookingKnowledge(@Body Requests.CookingKnowledgeRequest rewBody,
                             Callback<Reponses.CookingKnowledgeResponse> callback);
    /**
     * 联网优化
     */
    String getNetworkDeviceInfoRequest = "/rest/dms/api/device/instruction-book/get";

    /**
     * 获取库版本号
     */
    @POST(getStoreVersion)
    void getStoreVersion(@Body StoreRequest reqBody,
                         Callback<StoreVersionResponse> callback);

    /**
     * 获取分类
     */
    @POST(getStoreCategory)
    void getStoreCategory(Callback<StoreCategoryResponse> callback);

    /**
     * 获取所有菜谱供应商
     */
    @POST(getCookbookProviders)
    void getCookbookProviders(Callback<CookbookProviderResponse> callback);

    /**
     * 根据标签获取菜谱列表，包含自有菜谱与第三方菜谱
     */
    @POST(getCookbooksByTag)
    void getCookbooksByTag(@Body GetCookbooksByTagRequest reqBody,
                           Callback<CookbooksResponse> callback);

    /**
     * 根据名称获取菜谱列表，包含自有菜谱与第三方菜谱
     */
    @POST(getCookbooksByName)
    void getCookbooksByName(@Body GetCookbooksByNameRequest reqBody,
                            Callback<CookbooksResponse> callback);

    /**
     * 根据设备种类获取推荐菜谱 for Pad
     * 20160630周定钧
     */
    @POST(getRecommendRecipesByDeviceForPad)
    void getRecommendRecipesByDeviceForPad(@Body GetRecommendRecipesByDeviceForPadRequest reqBody,
                                           Callback<ThumbCookbookResponse> callback);

    /**
     * 根据设备种类获取推荐菜谱 for Cellphone
     * 201600711周定钧
     */
    @POST(getRecommendRecipesByDeviceForCellphone)
    void getRecommendRecipesByDeviceForCellphone(@Body getRecommendRecipesByDeviceForCellphoneRequest reqBody,
                                                 Callback<ThumbCookbookResponse> callback);


    /**
     * 根据设备品类获取非推荐的菜谱
     * 201600711周定钧
     */
    @POST(getNotRecommendRecipesByDevice)
    void getNotRecommendRecipesByDevice(@Body getNotRecommendRecipesByDeviceRequest reqBody,
                                        Callback<ThumbCookbookResponse> callback);


    /**
     * 根据设备种类获取所有菜谱
     * 201600711周定钧
     */
    @POST(getGroundingRecipesByDevice)
    void getGroundingRecipesByDevice(@Body getGroundingRecipesByDeviceRequest reqBody,
                                     Callback<ThumbCookbookResponse> callback);

    /**
     * 根据设备种类获取今日菜谱
     * 201600711周定钧
     */
    @POST(getTodayRecipesByDevice)
    void getTodayRecipesByDevice(@Body GetTodayRecipesByDeviceRequest reqBody,
                                 Callback<CookbooksResponse> callback);

    /**
     * 获取时令菜谱
     */
    @POST(getSeasonCookbooks)
    void getSeasonCookbooks(Callback<CookbooksResponse> callback);

    /**
     * 获取推荐菜谱 for 手机
     */
    @POST(getRecommendCookbooksForMob)
    void getRecommendCookbooksForMob(@Body UserRequest reqBody,
                                     Callback<ThumbCookbookResponse> callback);

    /**
     * 获取推荐菜谱 for Pad
     */
    @POST(getRecommendCookbooksForPad)
    void getRecommendCookbooksForPad(@Body UserRequest reqBody,
                                     Callback<ThumbCookbookResponse> callback);

    /**
     * 获取热门搜索关键字
     */
    @POST(getHotKeysForCookbook)
    void getHotKeysForCookbook(Callback<HotKeysForCookbookResponse> callback);

    @POST(getRecipeOfThemeList)
    void getRecipeOfThemeList(@Body Requests.GetReicpeOfTheme reqBody, Callback<CookbooksResponse> callback);

    /**
     * 获取菜谱详情
     */
    @POST(getCookbookById)
    void getCookbookById(@Body UserBookRequest reqBody,
                         Callback<CookbookResponse> callback);

    //获取烹饪步骤信息
    @POST(getCookbookSteps)
    void getCookbookSteps(@Body Requests.UserCookBookSteps reqBody,
                         Callback<Reponses.CookbookStepResponse> callback);


    /**
     * 获取老菜谱详情
     */
    @POST(getOldCookbookById)
    void getOldCookbookById(@Body UserBookRequest reqBody,
                            Callback<CookbookResponse> callback);

    /**
     * 获取mob端首页调味料排行榜
     */
    @POST(getAccessoryFrequencyForMob)
    void getAccessoryFrequencyForMob(@Body UserRequest reqBody, Callback<MaterialFrequencyResponse> callback);

    // -------------------------------------------------------------------------------
    // 今日菜单
    // -------------------------------------------------------------------------------

    /**
     * 获取今日菜谱
     */
    @POST(getTodayCookbooks)
    void getTodayCookbooks(@Body UserRequest reqBody,
                           Callback<CookbooksResponse> callback);

    /**
     * 添加菜谱到今日菜谱
     */
    @POST(addTodayCookbook)
    void addTodayCookbook(@Body UserBookRequest reqBody,
                          Callback<RCReponse> callback);

    /**
     * 设置家庭人数
     */
    @POST(setFamilyMember)
    void setFamilyMember(@Body Requests.FamilyMember reqBody,
                         Callback<RCReponse> callback);

    /**
     * 获取家庭人数
     */
    @POST(getSetFamilyMember)
    void getFamilyMember(@Body Requests.getFamilytotal reqBody,
                         Callback<Reponses.GetFamilyResponse> callback);

    /**
     * 从今日菜谱中删除菜谱
     */
    @POST(deleteTodayCookbook)
    void deleteTodayCookbook(@Body UserBookRequest reqBody,
                             Callback<RCReponse> callback);

    /**
     * 从今日菜谱中删除全部菜谱
     */
    @POST(deleteAllTodayCookbook)
    void deleteAllTodayCookbook(@Body UserRequest reqBody,
                                Callback<RCReponse> callback);

    /**
     * 从今日菜谱中导出食材
     */
    @POST(exportMaterialsFromToday)
    void exportMaterialsFromToday(@Body UserRequest reqBody,
                                  Callback<MaterialsResponse> callback);

    /**
     * 添加食材到今日菜谱的食材清单
     */
    @POST(addMaterialsToToday)
    void addMaterialsToToday(@Body UserMaterialRequest reqBody,
                             Callback<RCReponse> callback);

    /**
     * 添加食材到今日菜谱的食材清单
     */
    @POST(deleteMaterialsFromToday)
    void deleteMaterialsFromToday(@Body UserMaterialRequest reqBody,
                                  Callback<RCReponse> callback);
/****RENT**/
    /**
     * 获取主题列表
     */
    @POST(getThemeRecipeList)
    void setGetThemeRecipeList(Callback<Reponses.RecipeThemeResponse> callback);

    @POST(getThemeRecipeList_new)
    void setGetThemeRecipeList_new(Callback<Reponses.RecipeThemeResponse> callback);

    /**
     * 获取动态封面
     */
    @GET(getDynamicCover)
    void getDynamicCover(Callback<Reponses.RecipeDynamicCover> callback);

    /**
     * 获取直播视频列表
     */
    @POST(getRecipeLiveList)
    void getRecipeLiveList(@Body Requests.GetPageRequest request, Callback<Reponses.RecipeLiveListResponse> callback);

    /**
     * 获取晒厨艺列表
     */
    @POST(getRecipeDynamicShow)
    void getRecipeShowList(@Body Requests.GetPageUserRequest request, Callback<Reponses.RecipeShowListResponse> callback);

    @POST(getRecipeDynamicShow_new)
    void getRecipeShowList_new(@Body Requests.GetPageUserRequest request, Callback<Reponses.RecipeShowListResponse> callback);

    /**
     * 获取主题是否收藏
     */
    @POST(isThemeCollected)
    void getThemeCollectStatus(@Body Requests.ThemeCollectRequest request, Callback<Reponses.ThemeFavorite> callback);

    /**
     * 设置收藏
     */
    @POST(setCollectOfTheme)
    void setSetCollectOfTheme(@Body Requests.ThemeCollectRequest request, Callback<Reponses.CollectStatusRespone> callback);

    /**
     * 取消收藏
     */
    @POST(cancelCollectOfTheme)
    void setCancelCollectOfTheme(@Body Requests.ThemeCollectRequest request, Callback<RCReponse> callback);

    /**
     * 获取咨询列表
     */
    @POST(getConsultationList)
    void getConsultationList(@Body Requests.ConsultationListRequest request, Callback<Reponses.ConsultationListResponse> callback);

    /**
     * 获取咨询列表手机端
     */
    @POST(getConsultationList)
    void getConsultationList(Callback<Reponses.ConsultationListResponse> callback);

    /**
     * 获取设备菜谱封面
     */
    @POST(getDeviceRecipeImg)
    void getDeviceRecipeImg(@Body Requests.CategoryRecipeImgRequest request, Callback<Reponses.CategoryRecipeImgRespone> callback);

    /**
     * 获取已收藏主题
     */
    @GET(getRecipeThemeOfCollect)
    void getMyFavoriteThemeRecipeList(@Query("userId") String userId, Callback<Reponses.RecipeThemeResponse2> callback);

    @GET(getRecipeThemeOfCollect_new)
    void getMyFavoriteThemeRecipeList_new(@Query("userId") String userId, Callback<Reponses.RecipeThemeResponse3> callback);
    // -------------------------------------------------------------------------------
    // 我的收藏
    // -------------------------------------------------------------------------------

    /**
     * 获取菜谱-我的收藏
     */
    @POST(getFavorityCookbooks)
    void getFavorityCookbooks(@Body UserRequest reqBody,
                              Callback<CookbooksResponse> callback);

    /**
     * 增加菜谱到我的收藏
     */
    @POST(addFavorityCookbooks)
    void addFavorityCookbooks(@Body UserBookRequest reqBody,
                              Callback<RCReponse> callback);

    /**
     * 从我的收藏中删除菜谱
     */
    @POST(delteFavorityCookbooks)
    void deleteFavorityCookbooks(@Body UserBookRequest reqBody,
                                 Callback<RCReponse> callback);

    /**
     * 清空我的收藏
     */
    @POST(delteAllFavorityCookbooks)
    void delteAllFavorityCookbooks(@Body UserRequest reqBody,
                                   Callback<RCReponse> callback);

    @POST(getGroundingRecipes)
    void getGroundingRecipes(@Body GetGroudingRecipesRequest reqBody,
                             Callback<ThumbCookbookResponse> callback);


    // -------------------------------------------------------------------------------
    // 烧菜分享
    // -------------------------------------------------------------------------------

    @POST(addCookingLog)
    void addCookingLog(@Body CookingLogRequest reqBody,
                       Callback<RCReponse> callback);


    @POST(addCookingRecordInfo)
    void addCookingRecordInfo(@Body Requests.CookingRecordRequest reqBody,
                              Callback<RCReponse> callback);

    /**
     */
    @POST(getMyCookAlbumByCookbook)
    void getMyCookAlbumByCookbook(@Body UserBookRequest reqBody,
                                  Callback<AlbumResponse> callback);

    /**
     * 获取烧菜分享列表
     */
    @POST(getOtherCookAlbumsByCookbook)
    void getOtherCookAlbumsByCookbook(@Body GetCookAlbumsRequest reqBody,
                                      Callback<AlbumsResponse> callback);

    @POST(getOtherCookAlbumsByCookbook_new)
    void getOtherCookAlbumsByCookbook_new(@Body GetCookAlbumsRequest reqBody,
                                          Callback<AlbumsResponse> callback);

    /**
     * 提交烧菜分享
     */
    @POST(submitCookAlbum)
    void submitCookAlbum(@Body SubmitCookAlbumRequest reqBody,
                         Callback<RCReponse> callback);

    /**
     * 删除已经烧菜分享
     */
    @POST(removeCookAlbum)
    void removeCookAlbum(@Body CookAlbumRequest reqBody,
                         Callback<RCReponse> callback);

    /**
     * 点赞
     */
    @POST(praiseCookAlbum)
    void praiseCookAlbum(@Body CookAlbumRequest reqBody,
                         Callback<RCReponse> callback);

    /**
     * 取消点赞
     */
    @POST(unpraiseCookAlbum)
    void unpraiseCookAlbum(@Body CookAlbumRequest reqBody,
                           Callback<RCReponse> callback);

    /**
     * 获取厨艺秀列表
     */
    @POST(getMyCookAlbums)
    void getMyCookAlbums(@Body UserRequest reqBody,
                         Callback<AlbumsResponse> callback);

    /**
     * 清空厨艺秀列表
     */
    @POST(clearMyCookAlbums)
    void clearMyCookAlbums(@Body UserRequest reqBody,
                           Callback<RCReponse> callback);

    /**
     * 获取直播视频信息
     */
    @POST(getCurrentLiveShow)
    void getCurrentLiveShow(Callback<Reponses.CurrentLiveResponse> callback);


    // -------------------------------------------------------------------------------
    // 推送广告
    // -------------------------------------------------------------------------------

    /**
     * 获取app首页推广图片
     */
    @POST(getHomeAdvertsForMob)
    void getHomeAdvertsForMob(Callback<HomeAdvertsForMobResponse> callback);

    /**
     * 获取mob端首页推广的title的文字和图片
     */
    @POST(getHomeTitleForMob)
    void getHomeTitleForMob(Callback<HomeTitleForMobResponse> callback);

    /**
     * 获取pad首页推广图片
     */
    @POST(getHomeAdvertsForPad)
    void getHomeAdvertsForPad(Callback<HomeAdvertsForPadResponse> callback);

    /**
     * 获取pad我的收藏菜谱入口图片
     */
    @POST(getFavorityImagesForPad)
    void getFavorityImagesForPad(Callback<CookbookImageReponse> callback);

    /**
     * 获取pad为您推荐菜谱入口图片
     */
    @POST(getRecommendImagesForPad)
    void getRecommendImagesForPad(Callback<CookbookImageReponse> callback);

    /**
     * 获取pad所有菜谱入口图片
     */
    @POST(getAllBookImagesForPad)
    void getAllBookImagesForPad(Callback<CookbookImageReponse> callback);

    /**
     * 申请售后服务
     */
    @POST(applyAfterSale)
    void applyAfterSale(@Body ApplyAfterSaleRequest reqBody,
                        Callback<RCReponse> callback);

    // -------------------------------------------------------------------------------
    // 智能设定
    // -------------------------------------------------------------------------------

    /**
     * 获取智能设定配置（通风换气）
     */
    @POST(getSmartParams)
    void getSmartParams(@Body GetSmartParamsRequest reqBody,
                        Callback<SmartParamsReponse> callback);

    @POST(setSmartParamsByDaily)
    void setSmartParamsByDaily(@Body SetSmartParamsByDailyRequest reqBody,
                               Callback<RCReponse> callback);

    @POST(setSmartParamsByWeekly)
    void setSmartParamsByWeekly(@Body SetSmartParamsByWeeklyRequest reqBody,
                                Callback<RCReponse> callback);

    @POST(getSmartParams360)
    void getSmartParams360(@Body GetSmartParams360Request reqBody,
                           Callback<GetSmartParams360Reponse> callback);

    @POST(setSmartParams360)
    void setSmartParams360(@Body SetSmartParams360Request reqBody,
                           Callback<RCReponse> callback);


    // -------------------------------------------------------------------------------
    // 订单配送
    // -------------------------------------------------------------------------------
    @POST(getCustomerInfo)
    void getCustomerInfo(@Body UserRequest reqBody,
                         Callback<GetCustomerInfoReponse> callback);


    @POST(saveCustomerInfo)
    void saveCustomerInfo(@Body SaveCustomerInfoRequest reqBody,
                          Callback<RCReponse> callback);

    @POST(submitOrder)
    void submitOrder(@Body SubmitOrderRequest reqBody,
                     Callback<SubmitOrderReponse> callback);


    @POST(getOrder)
    void getOrder(@Body GetOrderRequest reqBody,
                  Callback<GetOrderReponse> callback);

    @POST(queryOrder)
    void queryOrder(@Body QueryOrderRequest reqBody,
                    Callback<QueryOrderReponse> callback);

    @POST(cancelOrder)
    void cancelOrder(@Body UserOrderRequest reqBody,
                     Callback<RCReponse> callback);

    @POST(updateOrderContacter)
    void updateOrderContacter(@Body UpdateOrderContacterRequest reqBody, Callback<RCReponse> callback);

    @POST(orderIfOpen)
    void orderIfOpen(Callback<OrderIfOpenReponse> callback);

    @POST(getEventStatus)
    void getEventStatus(Callback<EventStatusReponse> callback);

    @POST(deiverIfAllow)
    void deiverIfAllow(@Body UserRequest reqBody, Callback<DeiverIfAllowReponse> callback);

    // -------------------------------------------------------------------------------
    // 清洁维保
    // -------------------------------------------------------------------------------
    @POST(getCrmCustomerRequest)
    void getCrmCustomer(@Body GetCrmCustomerRequest reqBody,
                        Callback<GetCrmCustomerReponse> callback);

    @POST(submitMaintainRequest)
    void submitMaintain(@Body SubmitMaintainRequest reqBody,
                        Callback<RCReponse> callback);

    @POST(queryMaintainRequest)
    void queryMaintain(@Body QueryMaintainRequest reqBody,
                       Callback<QueryMaintainReponse> callback);


    // -------------------------------------------------------------------------------
    // other
    // -------------------------------------------------------------------------------

    /**
     * 联网优化接口
     * 获取设备联网说明列表
     * 20161212周定钧
     */
    @POST(getNetworkDeviceInfoRequest)
    void getNetworkDeviceInfo(@Body Requests.GetNetworkDeviceInfoRequest reqBody,
                              Callback<Reponses.NetworkDeviceInfoResponse> callback);


}
