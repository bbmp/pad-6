package com.robam.common.io.cloud;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.plat.pojos.RCReponse;
import com.legent.pojos.IJsonPojo;
import com.robam.common.pojos.Advert;
import com.robam.common.pojos.AdvertImage;
import com.robam.common.pojos.CookAlbum;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Cookbooks;
import com.robam.common.pojos.CookingKnowledge;
import com.robam.common.pojos.CrmCustomer;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DrinkingItem;
import com.robam.common.pojos.Group;
import com.robam.common.pojos.History;
import com.robam.common.pojos.Images;
import com.robam.common.pojos.KnowledgeGroup;
import com.robam.common.pojos.MaintainInfo;
import com.robam.common.pojos.MaterialFrequency;
import com.robam.common.pojos.Materials;
import com.robam.common.pojos.OrderContacter;
import com.robam.common.pojos.OrderInfo;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.Recipe3rd;
import com.robam.common.pojos.RecipeConsultation;
import com.robam.common.pojos.RecipeLiveList;
import com.robam.common.pojos.RecipeProvider;
import com.robam.common.pojos.RecipeShow;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.TodayDrinking;
import com.robam.common.pojos.liveshow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylar on 15/7/31.
 */
public interface Reponses {

    class StoreVersionResponse extends RCReponse {
        @JsonProperty("version")
        public int version;
    }

    class StoreCategoryResponse extends RCReponse {
        @JsonProperty("cookbookTagGroups")
        public List<Group> groups;
    }

    class GetYiGuoUrlResponse extends RCReponse {
        @JsonProperty("images")
        public ArrayList<Images> images;
    }

    /**
     *
     * 设置家庭人数返回
     *
     */
   class GetFamilyResponse extends RCReponse{
        @JsonProperty("memberCount")
        public int memberCount;
    }

    /**
     * 获取今日饮水量
     */
    class TodayDrinkingResponse extends RCReponse{
     @JsonProperty("item")
        public List<TodayDrinking> item;

        @Override
        public String toString() {
            if(item==null)
                return "TodayDrinkingResponse .item is null";
            return "TodayDrinkingResponse{" +
                    "item=" + item +
                    '}';
        }
    }

    /**
     * 获取历史饮水量
     */
    class HistoryDrinkingResponse extends RCReponse{
        @JsonProperty("item")
        public DrinkingItem item;
     }

    /**
     * 获取厨房支持组
     */
    class CookingKnowledgeGroupResponse extends RCReponse{
        @JsonProperty("knowledgeGroup")
        public List<KnowledgeGroup> groupList;

    }

    class HistoryResponse extends RCReponse{

        @JsonProperty("historyList")
        public List<History> historyList;


        @JsonProperty("msg")
        public String msg;
    }


    class SearchResult extends RCReponse{
        @JsonProperty("msg")
        public String msg;

        @JsonProperty("cookbooks")
        public List<Recipe> cookBooks;

        @JsonProperty("cookbook3rds")
        public List<Recipe3rd> cookBook3rd;
    }

    /**
     * 获取厨房知识列表
     */
    class CookingKnowledgeResponse extends RCReponse{

        @JsonProperty("hasNext")
        public String hasNext;

        @JsonProperty("cookingKnowledges")
        public List<CookingKnowledge> cookingKnowledges;

    }


    class CookbookResponse extends RCReponse {
        @JsonProperty("cookbook")
        public Recipe cookbook;
    }

    class CookbookStepResponse extends RCReponse {
        @JsonProperty("steps")
        public List<CookStep> cookSteps;
    }

    class Cookbook3rdResponse extends RCReponse {
        @JsonProperty("cookbook")
        public Recipe3rd cookbook;
    }

    class PersonalizedRecipeResponse extends  RCReponse{
        @JsonProperty("cookbooks")
        public List<Recipe> cookbooks;
    }

    class ThumbCookbookResponse extends RCReponse {
        @JsonProperty("cookbooks")
        public List<Recipe> cookbooks;
        @JsonProperty("cookbook_3rds")
        public List<Recipe3rd> cookbook_3rds;
    }

    class RecipeThemeResponse extends RCReponse {
        @JsonProperty("items")
        public List<RecipeTheme> recipeThemes;
    }

    class RecipeThemeResponse2 extends RCReponse {
        @JsonProperty("ThemeLists")
        public List<RecipeTheme> recipeThemes;
    }

    class RecipeThemeResponse3 extends RCReponse {
        @JsonProperty("themeLists")
        public List<RecipeTheme> recipeThemes;
    }

    class RecipeDynamicCover extends RCReponse {
        @JsonProperty("id")
        public long id;
        @JsonProperty("imageUrl")
        public String imageUrl;
    }

    class RecipeLiveListResponse extends RCReponse {
        @JsonProperty("videoCookbooks")
        public List<RecipeLiveList> lives;

        @Override
        public String toString() {
            return "RecipeLiveList{" +
                    "lives=" + lives +
                    '}';
        }
    }

    class RecipeShowListResponse extends RCReponse {
        @JsonProperty("albums")
        public List<RecipeShow> items;
    }

    class CollectStatusRespone extends RCReponse {
        @JsonProperty("status")
        public String status;
    }

    class ThemeFavorite extends RCReponse {
        @JsonProperty("isFavorite")
        public String isFavorite;
    }

    class CookbooksResponse extends RCReponse {

        @JsonProperty("cookbooks")
        public List<Recipe> cookbooks;

        @JsonProperty("cookbook_3rds")
        public List<Recipe3rd> cookbooks3rd;

        public int count() {
            int res = 0;
            if (cookbooks != null)
                res += cookbooks.size();
            if (cookbooks3rd != null)
                res += cookbooks3rd.size();

            return res;
        }
    }

    class CookbooksClassifyResponse extends RCReponse{
        @JsonProperty("cookbooks")
        public List<Cookbooks> cookbooks;

        @JsonProperty("cookbook_3rds")
        public List<Recipe3rd> cookbooks3rd;

        public int count() {
            int res = 0;
            if (cookbooks != null)
                res += cookbooks.size();
            if (cookbooks3rd != null)
                res += cookbooks3rd.size();

            return res;
        }

    }

    class CategoryRecipeImgRespone extends RCReponse {
        @JsonProperty("dc")
        public String dc;
        @JsonProperty("name")
        public String name;
        @JsonProperty("imgUrl")
        public String imgUrl;
        @JsonProperty("desc")
        public String desc;
    }

    class ConsultationListResponse extends RCReponse {
        @JsonProperty("items")
        public List<RecipeConsultation> items;
    }

    class MaterialFrequencyResponse extends RCReponse {

        @JsonProperty("accessorys")
        public List<MaterialFrequency> list;

    }

    class MaterialsResponse extends RCReponse {

        @JsonProperty("materials")
        public Materials materials;

    }

    class CookbookProviderResponse extends RCReponse {

        @JsonProperty("sources")
        public List<RecipeProvider> providers;

    }

    class HotKeysForCookbookResponse extends RCReponse {

        @JsonProperty("cookbooks")
        public List<String> hotKeys;

    }

    class AlbumResponse extends RCReponse {

        @JsonProperty("album")
        public CookAlbum album;

    }

    class AlbumsResponse extends RCReponse {

        @JsonProperty("albums")
        public List<CookAlbum> cookAlbums;

    }

    class HomeAdvertsForMobResponse extends RCReponse {

        @JsonProperty("images")
        public List<Advert.MobAdvert> adverts;

    }

    class HomeTitleForMobResponse extends RCReponse {

        @JsonProperty("images")
        public List<Advert.MobAdvert> titles;

    }

    class HomeAdvertsForPadResponse extends RCReponse {

        @JsonProperty("left")
        public List<Advert.PadAdvert> left;

        @JsonProperty("middle")
        public List<Advert.PadAdvert> middle;

    }

    class CookbookImageReponse extends RCReponse {

        @JsonProperty("images")
        public List<AdvertImage> images;

    }

    class SmartParamsReponse extends RCReponse {

        @JsonProperty("daily")
        public Daily daily;

        @JsonProperty("weekly")
        public Weekly weekly;

        class Daily implements IJsonPojo {

            @JsonProperty("on")
            public boolean enable;

            @JsonProperty("day")
            public int day;
        }

        class Weekly extends Daily {

            @JsonProperty("time")
            public String time;
        }
    }

    class GetSmartParams360Reponse extends RCReponse {

        @JsonProperty("switch")
        public boolean switchStatus;

    }

    class GetCustomerInfoReponse extends RCReponse {

        @JsonProperty("customer")
        public OrderContacter customer;

    }

    class QueryOrderReponse extends RCReponse {

        @JsonProperty()
        public List<OrderInfo> orders;

    }

    class OrderIfOpenReponse extends RCReponse {

        @JsonProperty()
        public boolean open;
    }

    class EventStatusReponse extends RCReponse {

        @JsonProperty()
        public String image;
        @JsonProperty()
        public int status;
    }

    class SubmitOrderReponse extends RCReponse {
        @JsonProperty()
        public long orderId;

    }

    class GetOrderReponse extends RCReponse {
        @JsonProperty()
        public OrderInfo order;
    }

    class DeiverIfAllowReponse extends RCReponse {

        @JsonProperty()
        public boolean allow;

    }

    class GetCrmCustomerReponse extends RCReponse {
        @JsonProperty()
        public CrmCustomer customerInfo;
    }

    class QueryMaintainReponse extends RCReponse {
        @JsonProperty()
        public MaintainInfo maintainInfo;
    }

    class CurrentLiveResponse extends RCReponse {
        @JsonProperty()
        public liveshow liveshow;
    }

    //获取联网优化设备列表
    class NetworkDeviceInfoResponse extends RCReponse {
        @JsonProperty("rc")
        public int rc;
        @JsonProperty("items")
        public List<DeviceGroupList> deviceGroupList;
    }
}
