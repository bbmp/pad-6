package com.robam.common.io.cloud;

import android.graphics.Bitmap;

import com.legent.Callback;
import com.legent.Helper;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.exceptions.ExceptionHelper;
import com.legent.plat.io.RCRetrofitCallback;
import com.legent.plat.io.RCRetrofitCallbackWithVoid;
import com.legent.plat.pojos.RCReponse;
import com.legent.utils.LogUtils;
import com.legent.utils.graphic.BitmapUtils;
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
import com.robam.common.io.cloud.Requests.GetSmartParams360Request;
import com.robam.common.io.cloud.Requests.GetSmartParamsRequest;
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
import com.robam.common.io.cloud.Requests.UserBookRequest;
import com.robam.common.io.cloud.Requests.UserMaterialRequest;
import com.robam.common.io.cloud.Requests.UserOrderRequest;
import com.robam.common.io.cloud.Requests.UserRequest;
import com.robam.common.pojos.Advert.MobAdvert;
import com.robam.common.pojos.AdvertImage;
import com.robam.common.pojos.CookAlbum;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.CrmCustomer;
import com.robam.common.pojos.CrmProduct;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.Group;
import com.robam.common.pojos.MaintainInfo;
import com.robam.common.pojos.MaterialFrequency;
import com.robam.common.pojos.Materials;
import com.robam.common.pojos.OrderContacter;
import com.robam.common.pojos.OrderInfo;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.RecipeProvider;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class RokiRestHelper {

    final static String TAG = "rest";
    static IRokiRestService svr = Plat.getCustomApi(IRokiRestService.class);

    static public void getStoreVersion(final Callback<Integer> callback) {
        svr.getStoreVersion(new StoreRequest("roki"),
                new RCRetrofitCallback<StoreVersionResponse>(callback) {
                    @Override
                    protected void afterSuccess(StoreVersionResponse result) {
                        callback.onSuccess(result.version);
                    }
                });
    }

    //用户登陆时会触发
    static public void getStoreCategory(final Callback<List<Group>> callback) {

        svr.getStoreCategory(new RCRetrofitCallback<StoreCategoryResponse>(
                callback) {
            @Override
            protected void afterSuccess(StoreCategoryResponse result) {
//                for (int i=0;i<result.groups.size();i++){
//                    result.groups.get(i).save2db();
//                }
                callback.onSuccess(result.groups);
            }
        });
    }

    //用户登陆时会触发
    static public void getCookbookProviders(
            final Callback<List<RecipeProvider>> callback) {
        svr.getCookbookProviders(new RCRetrofitCallback<CookbookProviderResponse>(
                callback) {
            @Override
            protected void afterSuccess(CookbookProviderResponse result) {
                for (int i = 0; i < result.providers.size(); i++) {
                    result.providers.get(i).save2db();
                }
                callback.onSuccess(result.providers);
            }
        });
    }

    static public void getCookbooksByTag(long tagId,
                                         final Callback<CookbooksResponse> callback) {
        svr.getCookbooksByTag(new GetCookbooksByTagRequest(tagId),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    static public void getCookbooksByName(String name, Boolean contain3rd,
                                          final Callback<CookbooksResponse> callback) {
        svr.getCookbooksByName(new GetCookbooksByNameRequest(name, contain3rd ? "true" : null),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }


    /**
     * 根据设备种类获取推荐菜谱 for Pad
     * 20160630周定钧
     */
    static public void getRecommendRecipesByDeviceForPad(long userId, String dc,
                                                         final Callback<List<Recipe>> callback) {
        svr.getRecommendRecipesByDeviceForPad(new Requests.GetRecommendRecipesByDeviceForPadRequest(userId, dc),
                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        callback.onSuccess(result.cookbooks);
                    }
                });
    }

    /**
     * 根据设备种类获取推荐菜谱ForCellphone
     * 20160630周定钧
     */
    static public void getRecommendRecipesByDeviceForCellphone(String dc,
                                                               final Callback<List<Recipe>> callback) {
        svr.getRecommendRecipesByDeviceForCellphone(new Requests.getRecommendRecipesByDeviceForCellphoneRequest(getUserId(), dc),

                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        Helper.onSuccess(callback, result.cookbooks);
                    }
                });
    }

    /**
     * 根据设备种类获取非推荐菜谱
     * 20160630周定钧
     */
    static public void getNotRecommendRecipesByDevice(String dc, int start, int limit,
                                                      final Callback<List<Recipe>> callback) {
        svr.getNotRecommendRecipesByDevice(new Requests.getNotRecommendRecipesByDeviceRequest(dc, start, limit),
                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        Helper.onSuccess(callback, result.cookbooks);
                    }
                });
    }


    /**
     * 根据设备种类获取所有菜谱
     * 20160630周定钧
     */
    static public void getGroundingRecipesByDevice(String dc, String recipeType, String language, int start, int limit,
                                                   final Callback<List<Recipe>> callback) {

        svr.getGroundingRecipesByDevice(new Requests.getGroundingRecipesByDeviceRequest(dc, recipeType, language, start, limit),

                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        Helper.onSuccess(callback, result.cookbooks);
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
//                        LogUtils.i("failure: "+e.toString());
                    }
                });
    }

    //wusi 获取个性话菜谱
    static public void getPersonalizeRecipes(long userId, int pageNo, int pageSize, final Callback<List<Recipe>> callback) {
        svr.getPersonalizedRecipeBooks(new Requests.PersonalizedRecipeRequest(userId, pageNo, pageSize), new RCRetrofitCallback<Reponses.PersonalizedRecipeResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.PersonalizedRecipeResponse result) {
                Helper.onSuccess(callback, result.cookbooks);
            }

            @Override
            public void failure(RetrofitError e) {
                super.failure(e);
                if (Plat.DEBUG)
                    LogUtils.i("Personalize", "failure: " + e.toString());
            }
        });
    }

    //获取搜索记录
    static public void getCookbookSearchHistory(long userId, final Callback<Reponses.HistoryResponse> callback) {
        svr.getCookbookSearchHistory(new Requests.CookbookSearchHistory(userId), new RCRetrofitCallback<Reponses.HistoryResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.HistoryResponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }


    static public void getSearchHistory(String userId, String word, String contain3rd, final Callback<Reponses.SearchResult> callback) {
        svr.getSearchResult(new Requests.SearchWord(userId, word, contain3rd), new RCRetrofitCallback<Reponses.SearchResult>(callback) {
            @Override
            protected void afterSuccess(Reponses.SearchResult result) {
                Helper.onSuccess(callback, result);
            }
        });
    }

    static public void getGroundingRecipesByDevice(String dc, int start, int limit,
                                                   final Callback<List<Recipe>> callback) {
        svr.getGroundingRecipesByDevice(new Requests.getGroundingRecipesByDeviceRequest(dc, start, limit),
                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        Helper.onSuccess(callback, result.cookbooks);
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        super.failure(e);
//                        LogUtils.i("failure: "+e.toString());
                    }
                });
    }
    ////////

    /**
     * 根据设备种类获取今日菜谱
     * 20160630周定钧
     */
    static public void getTodayRecipesByDevice(String dc,
                                               final Callback<CookbooksResponse> callback) {
        svr.getTodayRecipesByDevice(new Requests.GetTodayRecipesByDeviceRequest(getUserId(), dc),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }
//RENT ADD//

    /**
     * 获取主题菜谱列表
     */
    static public void getThemeRecipeList(final Callback<Reponses.RecipeThemeResponse> callback) {
        svr.setGetThemeRecipeList(new RCRetrofitCallback<Reponses.RecipeThemeResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeThemeResponse result) {
                callback.onSuccess(result);
            }
        });
    }


    static public void getThemeRecipeList_new(final Callback<Reponses.RecipeThemeResponse> callback) {
        svr.setGetThemeRecipeList_new(new RCRetrofitCallback<Reponses.RecipeThemeResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeThemeResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取已收藏主题菜谱列表
     */
    static public void getMyFavoriteThemeRecipeList(final Callback<Reponses.RecipeThemeResponse2> callback) {
        svr.getMyFavoriteThemeRecipeList(getUserId() + "", new RCRetrofitCallback<Reponses.RecipeThemeResponse2>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeThemeResponse2 result) {
                callback.onSuccess(result);
            }
        });
    }

    static public void getMyFavoriteThemeRecipeList_new(final Callback<Reponses.RecipeThemeResponse3> callback) {
        if (Plat.DEBUG)
            LogUtils.i("20161021", getUserId() + "");
        svr.getMyFavoriteThemeRecipeList_new(getUserId() + "", new RCRetrofitCallback<Reponses.RecipeThemeResponse3>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeThemeResponse3 result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取动态封面
     */
    static public void getDynamicCover(final Callback<Reponses.RecipeDynamicCover> callback) {
        svr.getDynamicCover(new RCRetrofitCallback<Reponses.RecipeDynamicCover>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeDynamicCover result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取直播视频列表
     */
    static public void getRecipeLiveList(final int start, final int num, final Callback<Reponses.RecipeLiveListResponse> callback) {
        svr.getRecipeLiveList(new Requests.GetPageRequest(start, num), new RCRetrofitCallback<Reponses.RecipeLiveListResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeLiveListResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取动态厨艺
     */
    static public void getRecipeShowList(final int start, final int num, final Callback<Reponses.RecipeShowListResponse> callback) {
        svr.getRecipeShowList(new Requests.GetPageUserRequest(start, num), new RCRetrofitCallback<Reponses.RecipeShowListResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.RecipeShowListResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取这道菜是否收藏过
     */
    static public void getThemeCollectStatus(final long themeId, final Callback<Reponses.ThemeFavorite> callback) {
        svr.getThemeCollectStatus(new Requests.ThemeCollectRequest(getUserId(), themeId), new RCRetrofitCallback<Reponses.ThemeFavorite>(callback) {
            @Override
            protected void afterSuccess(Reponses.ThemeFavorite result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 主题收藏
     */
    static public void setCollectOfTheme(final long themeId, final Callback<Reponses.CollectStatusRespone> callback) {
        svr.setSetCollectOfTheme(new Requests.ThemeCollectRequest(getUserId(), themeId), new RCRetrofitCallback<Reponses.CollectStatusRespone>(callback) {
            @Override
            protected void afterSuccess(Reponses.CollectStatusRespone result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 主题取消收藏
     */
    static public void cancelCollectOfTheme(final long themeId, final Callback<RCReponse> callback) {
        svr.setCancelCollectOfTheme(new Requests.ThemeCollectRequest(getUserId(), themeId), new RCRetrofitCallback<RCReponse>(callback) {
            @Override
            protected void afterSuccess(RCReponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取咨询列表
     */
    static public void getConsultationList(int page, int size, final Callback<Reponses.ConsultationListResponse> callback) {
        svr.getConsultationList(new Requests.ConsultationListRequest(page, size), new RCRetrofitCallback<Reponses.ConsultationListResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.ConsultationListResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取咨询列表
     */
    static public void getConsultationList(final Callback<Reponses.ConsultationListResponse> callback) {
        svr.getConsultationList(new RCRetrofitCallback<Reponses.ConsultationListResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.ConsultationListResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取设备菜谱封面
     */
    static public void getDeviceRecipeImg(String dc, final Callback<Reponses.CategoryRecipeImgRespone> callback) {
        svr.getDeviceRecipeImg(new Requests.CategoryRecipeImgRequest(dc), new RCRetrofitCallback<Reponses.CategoryRecipeImgRespone>(callback) {
            @Override
            protected void afterSuccess(Reponses.CategoryRecipeImgRespone result) {
                callback.onSuccess(result);
            }
        });
    }

    /**
     * 获取直播视频信息
     */
    static public void getCurrentLiveShow(final Callback<Reponses.CurrentLiveResponse> callback) {
        svr.getCurrentLiveShow(new RCRetrofitCallback<Reponses.CurrentLiveResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.CurrentLiveResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    static public void getSeasonCookbooks(
            final Callback<CookbooksResponse> callback) {
        svr.getSeasonCookbooks(new RCRetrofitCallback<CookbooksResponse>(
                callback) {
            @Override
            protected void afterSuccess(CookbooksResponse result) {
                callback.onSuccess(result);
            }
        });
    }


    static public void getRecommendCookbooksForMob(
            final Callback<ThumbCookbookResponse> callback) {
        svr.getRecommendCookbooksForMob(new UserRequest(getUserId()),
                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    public void success(ThumbCookbookResponse result, Response response) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        LogUtils.e("20190108", "e:" + e.toString());
                        callback.onFailure(e);
                    }
                });
    }


    static public void getRecommendCookbooksForPad(
            final Callback<List<Recipe>> callback) {
        svr.getRecommendCookbooksForPad(new UserRequest(getUserId()),
                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        callback.onSuccess(result.cookbooks);

                    }
                });
    }

    static public void getHotKeysForCookbook(
            final Callback<List<String>> callback) {
        svr.getHotKeysForCookbook(new RCRetrofitCallback<HotKeysForCookbookResponse>(
                callback) {
            @Override
            protected void afterSuccess(HotKeysForCookbookResponse result) {
                if (Plat.DEBUG)
                    LogUtils.i("RecipeBook", "result:" + result.toString());
                callback.onSuccess(result.hotKeys);
            }
        });
    }

    static public void getCookbookById(long cookbookId,
                                       final Callback<Recipe> callback) {
        svr.getCookbookById(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallback<CookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbookResponse result) {
                        if (Plat.DEBUG)
                            LogUtils.i("20170415", "result15:" + result.toString());
                        callback.onSuccess(result.cookbook);
                    }
                });
    }

    static public void getCookbookSteps(long cookbookId, String categoryCode, String platCode, final Callback<List<CookStep>> callback) {
        svr.getCookbookSteps(new Requests.UserCookBookSteps(cookbookId, categoryCode, platCode),
                new RCRetrofitCallback<Reponses.CookbookStepResponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.CookbookStepResponse result) {
                        LogUtils.i("20180328", "result:" + result);
                        callback.onSuccess(result.cookSteps);
                    }
                });
    }


    //增加统计
    static public void getCookbookById(long cookbookId, String entranceCode,
                                       final Callback<Recipe> callback) {
        svr.getCookbookById(new UserBookRequest(getUserId(), cookbookId, entranceCode),
                new RCRetrofitCallback<CookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbookResponse result) {
                        if (Plat.DEBUG)
                            LogUtils.i("20170415", "result15:" + result.toString());
                        callback.onSuccess(result.cookbook);
                    }
                });
    }


    static public void getRecipeOfThmem(String[] strings, final Callback<List<Recipe>> callback) {
        svr.getRecipeOfThemeList(new Requests.GetReicpeOfTheme(strings),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result.cookbooks);
                    }
                });
    }


    static public void getOldCookbookById(long cookbookId,
                                          final Callback<Recipe> callback) {
        svr.getOldCookbookById(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallback<CookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbookResponse result) {
                        if (Plat.DEBUG)
                            LogUtils.i("20170415", "result15:" + result.toString());
                        callback.onSuccess(result.cookbook);
                    }
                });
    }

    static public void getAccessoryFrequencyForMob(final Callback<List<MaterialFrequency>> callback) {
        svr.getAccessoryFrequencyForMob(new UserRequest(0), new RCRetrofitCallback<MaterialFrequencyResponse>(callback) {
            @Override
            protected void afterSuccess(MaterialFrequencyResponse result) {
                Helper.onSuccess(callback, result.list);
            }
        });
    }

    // -------------------------------------------------------------------------------

    static public void getTodayCookbooks(
            final Callback<CookbooksResponse> callback) {
        svr.getTodayCookbooks(new UserRequest(getUserId()),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 设置家庭人数
     */
    static public void setFamilyMember(String memberCount, String guid, final VoidCallback callback) {
        svr.setFamilyMember(new Requests.FamilyMember(getUserId(), memberCount, guid),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getFamilyMember(String guid, final Callback<Reponses.GetFamilyResponse> callback) {
        svr.getFamilyMember(new Requests.getFamilytotal(getUserId(), guid),
                new RCRetrofitCallback<Reponses.GetFamilyResponse>(callback) {
                    protected void afterSuccess(Reponses.GetFamilyResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 获取今日饮水量
     *
     * @param guid
     * @param timeType
     * @param callback
     */
    static public void getTodayDrinking(String guid, String timeType, final Callback<Reponses.TodayDrinkingResponse> callback) {
        svr.getTodayDrinking(new Requests.TodayDrinkingRequest(getUserId(), guid, timeType),
                new RCRetrofitCallback<Reponses.TodayDrinkingResponse>(callback) {
                    protected void afterSuccess(Reponses.TodayDrinkingResponse result) {
                        callback.onSuccess(result);
                    }
                });

    }

    /**
     * 获取历史饮水量
     *
     * @param guid
     * @param timeType
     * @param pageNo
     * @param limit
     * @param callback
     */

    static public void getHistoryDrinking(String guid, String timeType, int pageNo, int limit,
                                          final Callback<Reponses.HistoryDrinkingResponse> callback) {
        svr.getHistoryDrinking(new Requests.HistoryDrinkingRequest(getUserId(), guid, timeType, pageNo, limit), new RCRetrofitCallback<Reponses.HistoryDrinkingResponse>(callback) {
            protected void afterSuccess(Reponses.HistoryDrinkingResponse result) {
                callback.onSuccess(result);
                if (Plat.DEBUG)
                    LogUtils.i("rc", "rc:" + result);
            }
        });

    }

    //获取厨房知识列表
    static public void getCookingKnowledge(String typeCode, int pageNo, int pageSize,
                                           final Callback<Reponses.CookingKnowledgeResponse> callback) {
        svr.getCookingKnowledge(new Requests.CookingKnowledgeRequest(typeCode, pageNo, pageSize),
                new RCRetrofitCallback<Reponses.CookingKnowledgeResponse>(callback) {
                    @Override
                    protected void afterSuccess(Reponses.CookingKnowledgeResponse result) {
                        if (Plat.DEBUG)
                            LogUtils.i("20170607", "result:" + result);
                        callback.onSuccess(result);
                    }
                });


    }

    //获取厨房知识组
    static public void getCookingKnowledgeGroup(final retrofit.Callback callback) {
        svr.getCookingKnowledgeGroup(new retrofit.Callback<Reponses.CookingKnowledgeGroupResponse>() {
            @Override
            public void success(Reponses.CookingKnowledgeGroupResponse cookingKnowledgeGroupResponse, Response response) {
                callback.success(cookingKnowledgeGroupResponse, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });

    }


    static public void addTodayCookbook(long cookbookId,
                                        final VoidCallback callback) {
        svr.addTodayCookbook(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void deleteTodayCookbook(long cookbookId,
                                           VoidCallback callback) {
        svr.deleteTodayCookbook(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void deleteAllTodayCookbook(VoidCallback callback) {
        svr.deleteAllTodayCookbook(new UserRequest(getUserId()),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void exportMaterialsFromToday(
            final Callback<Materials> callback) {
        svr.exportMaterialsFromToday(new UserRequest(getUserId()),
                new RCRetrofitCallback<MaterialsResponse>(callback) {
                    @Override
                    protected void afterSuccess(MaterialsResponse result) {
                        callback.onSuccess(result.materials);
                    }
                });
    }

    static public void addMaterialsToToday(long materialId,
                                           VoidCallback callback) {
        svr.addMaterialsToToday(
                new UserMaterialRequest(getUserId(), materialId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void deleteMaterialsFromToday(long materialId,
                                                VoidCallback callback) {
        svr.deleteMaterialsFromToday(new UserMaterialRequest(getUserId(),
                        materialId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    // -------------------------------------------------------------------------------

    static public void getFavorityCookbooks(
            final Callback<CookbooksResponse> callback) {
        svr.getFavorityCookbooks(new UserRequest(getUserId()),
                new RCRetrofitCallback<CookbooksResponse>(callback) {
                    @Override
                    protected void afterSuccess(CookbooksResponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    static public void addFavorityCookbooks(long cookbookId,
                                            VoidCallback callback) {
        svr.addFavorityCookbooks(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void deleteFavorityCookbooks(long cookbookId,
                                               VoidCallback callback) {
        svr.deleteFavorityCookbooks(
                new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void delteAllFavorityCookbooks(VoidCallback callback) {
        svr.delteAllFavorityCookbooks(new UserRequest(getUserId()),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getGroundingRecipes(int start, int limit, String lang,
                                           final Callback<List<Recipe>> callback) {
        svr.getGroundingRecipes(new GetGroudingRecipesRequest(start, limit, lang), new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
            @Override
            protected void afterSuccess(ThumbCookbookResponse result) {
                Helper.onSuccess(callback, result.cookbooks);
            }
        });
    }

    //rent新增
    static public void getGroundingRecipes_new(int start, int limit, String lang,
                                               final Callback<ThumbCookbookResponse> callback) {
        svr.getGroundingRecipes(new GetGroudingRecipesRequest(start, limit, lang), new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
            @Override
            protected void afterSuccess(ThumbCookbookResponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }

    static public void getGroundingRecipes(int start, int limit,
                                           final Callback<List<Recipe>> callback) {
        svr.getGroundingRecipes(new GetGroudingRecipesRequest(start, limit), new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
            @Override
            protected void afterSuccess(ThumbCookbookResponse result) {
                Helper.onSuccess(callback, result.cookbooks);
            }
        });
    }

    //rent新增
    static public void getGroundingRecipes_new(int start, int limit,
                                               final Callback<ThumbCookbookResponse> callback) {
        svr.getGroundingRecipes(new GetGroudingRecipesRequest(start, limit), new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
            @Override
            protected void afterSuccess(ThumbCookbookResponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }

    /**
     * 根据设备种类获取所有菜谱
     * 20160630周定钧
     */
    static public void getGroundingRecipesByDevice(String dc, String recipeType, int start, int limit,
                                                   final Callback<List<Recipe>> callback) {
        svr.getGroundingRecipesByDevice(new Requests.getGroundingRecipesByDeviceRequest(dc, recipeType, start, limit),
                new RCRetrofitCallback<ThumbCookbookResponse>(callback) {
                    @Override
                    protected void afterSuccess(ThumbCookbookResponse result) {
                        Helper.onSuccess(callback, result.cookbooks);
                    }
                });
    }

    // -------------------------------------------------------------------------------

    static public void addCookingLog(String deviceId, long cookbookId,
                                     long start, long end, boolean isBroken, final VoidCallback callback) {
        svr.addCookingLog(new CookingLogRequest(getUserId(), cookbookId,
                        deviceId, start, end, isBroken),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }


    /**
     * @author 2017 11.1 吴四添加
     * @descirde 烧菜记录提交 接口
     */
    static public void addCookingRecordLog(long userId, long cookbookId, int stepCount, String deviceGuid,
                                           String appType, long startTime, long endTime, int finishType, List<Object> stepDetails,
                                           final VoidCallback callback) {

        svr.addCookingRecordInfo(new Requests.CookingRecordRequest(userId, cookbookId, stepCount, deviceGuid, appType, startTime, endTime, finishType, stepDetails),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback) {
                    @Override
                    public void success(RCReponse result, Response response) {
                        callback.onSuccess();
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        callback.onFailure(e);
                    }
                });
    }


    static public void getMyCookAlbumByCookbook(long cookbookId,
                                                final Callback<CookAlbum> callback) {

        svr.getMyCookAlbumByCookbook(new UserBookRequest(getUserId(), cookbookId),
                new RCRetrofitCallback<AlbumResponse>(callback) {
                    @Override
                    protected void afterSuccess(AlbumResponse result) {
                        callback.onSuccess(result.album);
                    }
                });

    }

    static public void getOtherCookAlbumsByCookbook(long cookbookId, int start, int limit,
                                                    final Callback<List<CookAlbum>> callback) {
        svr.getOtherCookAlbumsByCookbook(new GetCookAlbumsRequest(getUserId(), cookbookId,
                        start, limit),
                new RCRetrofitCallback<AlbumsResponse>(callback) {
                    @Override
                    protected void afterSuccess(AlbumsResponse result) {
                        callback.onSuccess(result.cookAlbums);
                    }
                });
    }

    static public void getOtherCookAlbumsByCookbook_new(long cookbookId, int start, int limit,
                                                        final Callback<List<CookAlbum>> callback) {
        svr.getOtherCookAlbumsByCookbook_new(new GetCookAlbumsRequest(getUserId(), cookbookId,
                        start, limit),
                new RCRetrofitCallback<AlbumsResponse>(callback) {
                    @Override
                    protected void afterSuccess(AlbumsResponse result) {
                        callback.onSuccess(result.cookAlbums);
                    }
                });
    }

    static public void submitCookAlbum(long cookbookId, Bitmap image,
                                       String desc, VoidCallback callback) {
        String strImg = BitmapUtils.toBase64(image);
        svr.submitCookAlbum(new SubmitCookAlbumRequest(getUserId(), cookbookId,
                strImg, desc), new RCRetrofitCallbackWithVoid<RCReponse>(
                callback));
    }

    static public void removeCookAlbum(long albumId, VoidCallback callback) {
        svr.removeCookAlbum(new CookAlbumRequest(getUserId(), albumId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void praiseCookAlbum(long albumId, VoidCallback callback) {
        svr.praiseCookAlbum(new CookAlbumRequest(getUserId(), albumId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void unpraiseCookAlbum(long albumId, VoidCallback callback) {
        svr.unpraiseCookAlbum(new CookAlbumRequest(getUserId(), albumId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getMyCookAlbums(final Callback<List<CookAlbum>> callback) {
        svr.getMyCookAlbums(new UserRequest(getUserId()), new RCRetrofitCallback<AlbumsResponse>(callback) {
            @Override
            protected void afterSuccess(AlbumsResponse result) {
                Helper.onSuccess(callback, result.cookAlbums);
            }
        });
    }

    static public void clearMyCookAlbums(final VoidCallback callback) {
        svr.clearMyCookAlbums(new UserRequest(getUserId()), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    // -------------------------------------------------------------------------------

    static public void getHomeAdvertsForMob(
            final Callback<List<MobAdvert>> callback) {
        svr.getHomeAdvertsForMob(new RCRetrofitCallback<HomeAdvertsForMobResponse>(
                callback) {
            @Override
            protected void afterSuccess(HomeAdvertsForMobResponse result) {
                callback.onSuccess(result.adverts);
            }
        });
    }

    static public void getHomeTitleForMob(final Callback<List<MobAdvert>> callback) {
        svr.getHomeTitleForMob(new RCRetrofitCallback<Reponses.HomeTitleForMobResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.HomeTitleForMobResponse result) {
                callback.onSuccess(result.titles);
            }
        });
    }

    static public void getHomeAdvertsForPad(
            final Callback<HomeAdvertsForPadResponse> callback) {
        svr.getHomeAdvertsForPad(new RCRetrofitCallback<HomeAdvertsForPadResponse>(
                callback) {
            @Override
            protected void afterSuccess(HomeAdvertsForPadResponse result) {
                callback.onSuccess(result);
            }
        });
    }

    static public void getYiGuoUrl(final Callback<Reponses.GetYiGuoUrlResponse> callback) {
        svr.getYiGuoUrl(new RCRetrofitCallback<Reponses.GetYiGuoUrlResponse>(callback) {
            @Override
            protected void afterSuccess(Reponses.GetYiGuoUrlResponse result) {
                if (Plat.DEBUG)
                    LogUtils.i("20170222", "result:" + result.toString());
                callback.onSuccess(result);
            }
        });

    }

    static public void getFavorityImagesForPad(
            final Callback<List<AdvertImage>> callback) {
        svr.getFavorityImagesForPad(new RCRetrofitCallback<CookbookImageReponse>(
                callback) {
            @Override
            protected void afterSuccess(CookbookImageReponse result) {
                callback.onSuccess(result.images);
            }
        });
    }

    static public void getRecommendImagesForPad(
            final Callback<List<AdvertImage>> callback) {
        svr.getRecommendImagesForPad(new RCRetrofitCallback<CookbookImageReponse>(
                callback) {
            @Override
            protected void afterSuccess(CookbookImageReponse result) {
                callback.onSuccess(result.images);
            }
        });

    }

    static public void getAllBookImagesForPad(
            final Callback<List<AdvertImage>> callback) {
        svr.getAllBookImagesForPad(new RCRetrofitCallback<CookbookImageReponse>(
                callback) {
            @Override
            protected void afterSuccess(CookbookImageReponse result) {
                callback.onSuccess(result.images);
            }
        });
    }

    // -------------------------------------------------------------------------------

    static public void applyAfterSale(String deviceId,
                                      final VoidCallback callback) {
        svr.applyAfterSale(new ApplyAfterSaleRequest(getUserId(), deviceId),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getSmartParams(String deviceGuid,
                                      final Callback<SmartParamsReponse> callback) {
        svr.getSmartParams(new GetSmartParamsRequest(getUserId(), deviceGuid),
                new RCRetrofitCallback<SmartParamsReponse>(callback) {
                    @Override
                    protected void afterSuccess(SmartParamsReponse result) {
                        callback.onSuccess(result);
                    }
                });
    }

    static public void setSmartParamsByDaily(String guid, boolean enable,
                                             int day, VoidCallback callback) {
        svr.setSmartParamsByDaily(new SetSmartParamsByDailyRequest(getUserId(),
                guid, enable, day), new RCRetrofitCallbackWithVoid<RCReponse>(
                callback));
    }

    static public void setSmartParamsByWeekly(String guid, boolean enable,
                                              int day, String time, VoidCallback callback) {
        svr.setSmartParamsByWeekly(new SetSmartParamsByWeeklyRequest(
                        getUserId(), guid, enable, day, time),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void getSmartParams360(String guid, final Callback<Boolean> callback) {
        svr.getSmartParams360(new GetSmartParams360Request(getUserId(), guid),
                new RCRetrofitCallback<GetSmartParams360Reponse>(callback) {
                    @Override
                    protected void afterSuccess(GetSmartParams360Reponse result) {
                        Helper.onSuccess(callback, result.switchStatus);
                    }
                });
    }

    static public void setSmartParams360(String guid, boolean switchStatus, final VoidCallback callback) {
        svr.setSmartParams360(new SetSmartParams360Request(getUserId(), guid, switchStatus),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    // -------------------------------------------------------------------------------
    // 订单配送
    // -------------------------------------------------------------------------------

    static public void getCustomerInfo(final Callback<OrderContacter> callback) {
        svr.getCustomerInfo(new UserRequest(getUserId()), new RCRetrofitCallback<GetCustomerInfoReponse>(callback) {
            @Override
            protected void afterSuccess(GetCustomerInfoReponse result) {
                Helper.onSuccess(callback, result.customer);
            }
        });
    }

    static public void saveCustomerInfo(String name, String phone, String city, String address, final VoidCallback callback) {
        svr.saveCustomerInfo(new SaveCustomerInfoRequest(getUserId(), name, phone, city, address), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void submitOrder(List<Long> ids, final Callback<Long> callback) {
        svr.submitOrder(new SubmitOrderRequest(getUserId(), ids), new RCRetrofitCallback<SubmitOrderReponse>(callback) {
            @Override
            protected void afterSuccess(SubmitOrderReponse result) {
                Helper.onSuccess(callback, result.orderId);
            }
        });
    }

    static public void getOrder(long orderId, final Callback<OrderInfo> callback) {
        svr.getOrder(new GetOrderRequest(orderId), new RCRetrofitCallback<GetOrderReponse>(callback) {
            @Override
            protected void afterSuccess(GetOrderReponse result) {
                Helper.onSuccess(callback, result.order);
            }
        });
    }

    static public void queryOrder(long time, int limit, final Callback<List<OrderInfo>> callback) {
        svr.queryOrder(new QueryOrderRequest(getUserId(), time, limit), new RCRetrofitCallback<QueryOrderReponse>(callback) {
            @Override
            protected void afterSuccess(QueryOrderReponse result) {
                Helper.onSuccess(callback, result.orders);
            }
        });
    }

    static public void cancelOrder(long orderId, final VoidCallback callback) {
        svr.cancelOrder(new UserOrderRequest(getUserId(), orderId), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void updateOrderContacter(long orderId, String name, String phone, String city, String address, final VoidCallback callback) {
        svr.updateOrderContacter(new Requests.UpdateOrderContacterRequest(getUserId(), orderId, name, phone, city, address),
                new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void orderIfOpen(final Callback<Boolean> callback) {
        svr.orderIfOpen(new RCRetrofitCallback<OrderIfOpenReponse>(callback) {
            @Override
            protected void afterSuccess(OrderIfOpenReponse result) {
                Helper.onSuccess(callback, result.open);
            }
        });
    }

    static public void getEventStatus(final Callback<EventStatusReponse> callback) {
        svr.getEventStatus(new RCRetrofitCallback<EventStatusReponse>(callback) {
            @Override
            protected void afterSuccess(EventStatusReponse result) {
                Helper.onSuccess(callback, result);
            }
        });
    }

    static public void deiverIfAllow(final Callback<Integer> callback) {
        svr.deiverIfAllow(new UserRequest(getUserId()), new retrofit.Callback<DeiverIfAllowReponse>() {
            @Override
            public void success(DeiverIfAllowReponse result, Response response) {
                Helper.onSuccess(callback, result.rc);
            }

            @Override
            public void failure(RetrofitError e) {
                Helper.onFailure(callback, ExceptionHelper.newRestfulException(e.getMessage()));
            }
        });
    }


    // -------------------------------------------------------------------------------
    // 清洁维保
    // -------------------------------------------------------------------------------

    static public void getCrmCustomer(String phone, final Callback<CrmCustomer> callback) {
        svr.getCrmCustomer(new GetCrmCustomerRequest(phone), new RCRetrofitCallback<GetCrmCustomerReponse>(callback) {
            @Override
            protected void afterSuccess(GetCrmCustomerReponse result) {
                Helper.onSuccess(callback, result.customerInfo);
            }
        });
    }

    static public void submitMaintain(CrmProduct product, long bookTime, String customerId, String customerName, String phone, String province, String city, String county, String address, VoidCallback callback) {
        svr.submitMaintain(new SubmitMaintainRequest(getUserId(), product, bookTime, customerId, customerName, phone, province, city, county, address), new RCRetrofitCallbackWithVoid<RCReponse>(callback));
    }

    static public void queryMaintain(final Callback<MaintainInfo> callback) {

        svr.queryMaintain(new QueryMaintainRequest(getUserId()), new RCRetrofitCallback<QueryMaintainReponse>(callback) {

            @Override
            protected void afterSuccess(QueryMaintainReponse result) {
                Helper.onSuccess(callback, result.maintainInfo);
            }
        });
    }

    // -------------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------------

    static private long getUserId() {
        return Plat.accountService.getCurrentUserId();
    }


    //联网优化接口
    static public void getNetworkDeviceInfoRequest(String vendor, String dc, String dt,
                                                   final Callback<List<DeviceGroupList>> callback) {
        svr.getNetworkDeviceInfo(new Requests.GetNetworkDeviceInfoRequest(vendor, dc, dt), new retrofit.Callback<Reponses.NetworkDeviceInfoResponse>() {
            @Override
            public void success(Reponses.NetworkDeviceInfoResponse networkDeviceInfoResponse, Response response) {
                callback.onSuccess(networkDeviceInfoResponse.deviceGroupList);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure(error);
            }
        });

    }


}
