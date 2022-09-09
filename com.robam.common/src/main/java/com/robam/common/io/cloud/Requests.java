package com.robam.common.io.cloud;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legent.plat.Plat;
import com.legent.plat.pojos.AbsPostRequest;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.CrmProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylar on 15/7/31.
 */
public interface Requests {


    class NewCurrentLiveRequests extends AbsPostRequest{

        @JsonProperty("stutas")
        public int stutas;

        @JsonProperty("isTop")
        public int isTop;

        public NewCurrentLiveRequests(int stutas, int isTop) {
            this.stutas = stutas;
            this.isTop = isTop;
        }
    }

    class StoreRequest extends AbsPostRequest {
        @JsonProperty("storeId")
        public String storeId;

        public StoreRequest(String storeId) {
            this.storeId = storeId;
        }
    }

    class UserRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;

        public UserRequest(long userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "UserRequest{" +
                    "userId=" + userId +
                    '}';
        }
    }


    class UserBookRequest extends UserRequest {

        @JsonProperty("cookbookId")
        public long cookbookId;

        @JsonProperty("entranceCode")
        public String entranceCode;

        @JsonProperty("needStepsInfo")
        public String needStepsInfo;

        public UserBookRequest(long userId, long cookbookId) {
            super(userId);
            this.cookbookId = cookbookId;
        }

        public UserBookRequest(long userId, long cookbookId,String entranceCode) {
            super(userId);
            this.cookbookId = cookbookId;
            this.entranceCode = entranceCode;
        }

        public UserBookRequest(long userId, long cookbookId, String entranceCode, String needStepsInfo) {
            super(userId);
            this.cookbookId = cookbookId;
            this.entranceCode = entranceCode;
            this.needStepsInfo = needStepsInfo;
        }
    }

    class UserCookBookSteps extends AbsPostRequest {
        @JsonProperty("cookbookId")
        public long cookbookId;

        @JsonProperty("categoryCode")
        public String categoryCode;

        @JsonProperty("platCode")
        public String platCode;

        public UserCookBookSteps(long cookbookId, String categoryCode, String platCode) {
            this.cookbookId = cookbookId;
            this.categoryCode = categoryCode;
            this.platCode = platCode;
            LogUtils.i("20180328","id::"+cookbookId+" categoryCode::"+categoryCode+" platCode::"+
            platCode);
        }



    }

    class CookbookSearchHistory extends UserRequest {

        public CookbookSearchHistory(long userId) {
            super(userId);
        }
    }

    class SearchWord {
        @JsonProperty("userId")
        public String userId;

        @JsonProperty("name")
        public String name;

        @JsonProperty("contain3rd")
        public String contain3rd;

        public SearchWord(String userId,String name,String contain3rd) {
            this.userId = userId;
            this.name = name;
            this.contain3rd = contain3rd;
        }
    }


    class RecipeStepRequest extends UserRequest {

        @JsonProperty("cookbookId")
        public long cookbookId;

        @JsonProperty("categoryCode")
        public String categoryCode;

        @JsonProperty("platCode")
        public String platCode;

        public RecipeStepRequest(long userId, long cookbookId) {
            super(userId);
            this.cookbookId = cookbookId;
        }
    }

    class GetYouzanRequst extends UserRequest{

        @JsonProperty("type")
        public String type;
        @JsonProperty("telephone")
        public String telephone;

        public GetYouzanRequst(long userId,String type,String telephone) {
            super(userId);
            this.type = type;
            this.telephone = telephone;
        }
    }

    class GetYouzanOrdersRequst extends UserRequest{

        @JsonProperty("statusList")
        public String[] statusList;

        public GetYouzanOrdersRequst(long userId, String[] statusList) {
            super(userId);
            this.statusList = statusList;
        }
    }

    //by yinwei
    //设置家庭总人数
    class FamilyMember extends UserRequest {

        @JsonProperty("memberCount")
        public String memberCount;

        @JsonProperty("guid")
        public String guid;

        @Override
        public String toString() {
            return "FamilyMember{" +
                    "memberCount='" + memberCount + '\'' +
                    ", guid='" + guid + '\'' +
                    '}';
        }

        public FamilyMember(long userId, String memberCount, String guid) {
            super(userId);
            this.memberCount = memberCount;
            this.guid = guid;
        }
    }

    //获取家庭总人数
    class getFamilytotal extends UserRequest {
        @JsonProperty("guid")
        public String guid;

        @Override
        public String toString() {
            return super.toString() + "getFamilytotal{" +
                    "guid='" + guid + '\'' +
                    '}';
        }

        public getFamilytotal(long userId, String guid) {
            super(userId);
            this.guid = guid;
        }
    }

    //获取当前的饮水量
    class TodayDrinkingRequest extends UserRequest {

        @JsonProperty("guid")
        public String guid;

        @JsonProperty("timeType")
        public String timeType;

        public TodayDrinkingRequest(long userId, String guid, String timeType) {
            super(userId);
            this.guid = guid;
            this.timeType = timeType;
        }
    }

    //获取历史饮水量
    class HistoryDrinkingRequest extends UserRequest {

        @JsonProperty("timeType")
        public String timeType;
        @JsonProperty("pageNo")
        public int pageNo;
        @JsonProperty("limit")
        public int limit;
        @JsonProperty("guid")
        public String guid;

        public HistoryDrinkingRequest(long userId, String guid, String timeType, int pageNo, int limit) {
            super(userId);
            this.guid = guid;
            this.timeType = timeType;
            this.pageNo = pageNo;
            this.limit = limit;
        }

        @Override
        public String toString() {
            return "HistoryDrinkingRequest{" +
                    "timeType='" + timeType + '\'' +
                    ", pageNo=" + pageNo +
                    ", limit=" + limit +
                    ", guid='" + guid + '\'' +
                    '}';
        }
    }

    class  CookingKnowledgeRequest extends AbsPostRequest{
        @JsonProperty("typeCode")
        public String  typeCode;

        @JsonProperty("pageNo")
        public int  pageNo;

        @JsonProperty("pageSize")
        public int  pageSize;

        public CookingKnowledgeRequest(String typeCode, int pageSize, int pageNo) {
            this.typeCode = typeCode;
            this.pageSize = pageSize;
            this.pageNo = pageNo;
        }
    }



    class GetCookbooksByTagRequest extends AbsPostRequest {
        @JsonProperty("cookbookTagId")
        public long cookbookTagId;

        public GetCookbooksByTagRequest(long cookbookTagId) {
            this.cookbookTagId = cookbookTagId;
        }
    }

    class GetReicpeOfTheme extends AbsPostRequest {
        @JsonProperty("CookbookIdList")
        public List<String> cookbookIdList;

        public GetReicpeOfTheme(String[] strings) {
            if (strings != null && strings.length > 0) {
                cookbookIdList = new ArrayList<String>();
                for (int i = 0; i < strings.length; i++) {
                    cookbookIdList.add(strings[i]);
                }
            }
        }
    }

    class GetCookbooksByNameRequest extends AbsPostRequest {
        @JsonProperty("name")
        public String name;

        @JsonProperty("contain3rd")
        public String contain3rd;

        public GetCookbooksByNameRequest(String name,String contain3rd) {
            this.name = name;
            this.contain3rd=contain3rd;
        }
    }

    // 周定钧20160630
    class GetRecommendRecipesByDeviceForPadRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;
        @JsonProperty("dc")
        public String dc;

        public GetRecommendRecipesByDeviceForPadRequest(long userId, String dc) {
            this.userId = userId;
            this.dc = dc;
        }
    }

    // 周定钧20161212
    class GetNetworkDeviceInfoRequest extends AbsPostRequest {
        @JsonProperty("vendor")
        public String vendor;
        @JsonProperty("dc")
        public String dc;
        @JsonProperty("dt")
        public String dt;

        public GetNetworkDeviceInfoRequest(String vendor, String dc,String dt) {
            this.vendor =vendor;
            this.dc = dc;
            this.dt = dt;
        }
    }


    // 周定钧20160630
    class getRecommendRecipesByDeviceForCellphoneRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;
        @JsonProperty("dc")
        public String dc;

        public getRecommendRecipesByDeviceForCellphoneRequest(long userId, String dc) {
            this.userId = userId;
            this.dc = dc;
        }
    }


    // 周定钧20160711
    class getNotRecommendRecipesByDeviceRequest extends AbsPostRequest {
        @JsonProperty("dc")
        public String dc;
        @JsonProperty("start")
        public int start;
        @JsonProperty("limit")
        public int limit;

        public getNotRecommendRecipesByDeviceRequest(String dc, int start, int limit) {
            this.dc = dc;
            this.start = start;
            this.limit = limit;
        }
    }


    // 周定钧20160711
    class getGroundingRecipesByDeviceRequest extends AbsPostRequest {
        @JsonProperty("dc")
        public String dc;
        @JsonProperty("start")
        public int start;
        @JsonProperty("limit")
        public int limit;
        @JsonProperty("cookbookType")
        public String cookbookType;
        @JsonProperty("lang")
        public String lang;

        public getGroundingRecipesByDeviceRequest(String dc, String cookbookType, String lang, int start, int limit) {
            this.dc = dc;
            this.cookbookType = cookbookType;
            this.start = start;
            this.limit = limit;
            this.lang = lang;
        }
        public getGroundingRecipesByDeviceRequest(String dc, String cookbookType, int start, int limit) {
            this.dc = dc;
            this.cookbookType = cookbookType;
            this.start = start;
            this.limit = limit;
        }

        public getGroundingRecipesByDeviceRequest(String dc, int start, int limit) {
            this.dc = dc;
            this.start = start;
            this.limit = limit;
        }
    }


    // 周定钧20160711
    class GetTodayRecipesByDeviceRequest extends AbsPostRequest {
        @JsonProperty("userId")
        public long userId;
        @JsonProperty("dc")
        public String dc;

        public GetTodayRecipesByDeviceRequest(long userId, String dc) {
            this.userId = userId;
            this.dc = dc;
        }
    }


    class UserMaterialRequest extends UserRequest {

        @JsonProperty("materialId")
        public long materialId;

        public UserMaterialRequest(long userId, long materialId) {
            super(userId);
            this.materialId = materialId;
        }
    }

    class GetCookAlbumsRequest extends UserBookRequest {

        @JsonProperty("start")
        public int start;

        @JsonProperty("limit")
        public int limit;

        public GetCookAlbumsRequest(long userId, long cookbookId, int start,
                                    int limit) {
            super(userId, cookbookId);
            this.start = start;
            this.limit = limit;
        }

    }

    class SubmitCookAlbumRequest extends UserBookRequest {

        @JsonProperty("image")
        public String image;

        @JsonProperty("desc")
        public String desc;

        public SubmitCookAlbumRequest(long userId, long cookbookId,
                                      String image, String desc) {
            super(userId, cookbookId);
            this.image = image;
            this.desc = desc;
        }

    }

    class CookAlbumRequest extends UserRequest {
        @JsonProperty("albumId")
        public long albumId;

        public CookAlbumRequest(long userId, long albumId) {
            super(userId);
            this.albumId = albumId;
        }
    }

    class ApplyAfterSaleRequest extends UserRequest {

        @JsonProperty("deviceGuid")
        public String guid;

        public ApplyAfterSaleRequest(long userId, String deviceGuid) {
            super(userId);
            this.guid = deviceGuid;
        }
    }

    class GetSmartParamsRequest extends UserRequest {

        @JsonProperty("deviceGuid")
        public String guid;

        public GetSmartParamsRequest(long userId, String deviceGuid) {
            super(userId);
            this.guid = deviceGuid;
        }
    }

    class SetSmartParamsByDailyRequest extends UserRequest {

        @JsonProperty("deviceGuid")
        public String guid;

        @JsonProperty("on")
        public boolean enable;

        @JsonProperty("day")
        public int day;

        public SetSmartParamsByDailyRequest(long userId, String guid,
                                            boolean enable, int day) {
            super(userId);
            this.guid = guid;
            this.enable = enable;
            this.day = day;
        }
    }

    class SetSmartParamsByWeeklyRequest extends
            SetSmartParamsByDailyRequest {

        @JsonProperty("time")
        public String time;

        public SetSmartParamsByWeeklyRequest(long userId, String guid,
                                             boolean enable, int day, String time) {
            super(userId, guid, enable, day);
            this.time = time;
        }

    }


    class GetSmartParams360Request extends UserRequest {

        @JsonProperty("deviceGuid")
        public String guid;

        public GetSmartParams360Request(long userId, String deviceGuid) {
            super(userId);
            this.guid = deviceGuid;
        }
    }


    class SetSmartParams360Request extends GetSmartParams360Request {

        @JsonProperty("switch")
        public boolean switchStatus;

        public SetSmartParams360Request(long userId, String deviceGuid, boolean status) {
            super(userId, deviceGuid);
            this.switchStatus = status;
        }
    }

    class CookingLogRequest extends UserBookRequest {

        @JsonProperty("deviceGuid")
        public String deviceId;

        @JsonProperty("startTime")
        public long start;

        @JsonProperty("endTime")
        public long end;

        @JsonProperty("finishType")
        public int finishType;

        public CookingLogRequest(long userId, long cookbookId, String deviceId,
                                 long start, long end, boolean isBroken) {
            super(userId, cookbookId);
            this.deviceId = deviceId;
            this.start = start;
            this.end = end;
            this.finishType = isBroken ? 2 : 1;
        }
    }




    class CookingRecordRequest extends UserBookRequest {
        @JsonProperty("userId")
        public long userId;

        @JsonProperty("cookbookId")
        public long cookbookId;

        @JsonProperty("stepCount")
        public int stepCount;

        @JsonProperty("deviceGuid")
        public String  deviceGuid;

        @JsonProperty("appType")
        public String  appType;

        @JsonProperty("startTime")
        public long startTime;

        @JsonProperty("endTime")
        public long endTime;

        @JsonProperty("finishType")
        public int finishType;

        @JsonProperty("stepDetails")
        public List<Object> stepDetails;

        public CookingRecordRequest(long userId,long cookbookId,int stepCount,String deviceGuid,
                                 String appType,long startTime,long endTime,int finishType,List<Object> stepDetails) {
            super(userId, cookbookId);
            this.userId = userId;
            this.cookbookId = cookbookId;
            this.stepCount = stepCount;
            this.deviceGuid = deviceGuid;
            this.appType = appType;
            this.startTime = startTime;
            this.endTime = endTime;
            this.finishType = finishType;
            this.stepDetails = stepDetails;
        }


    }







    class UserOrderRequest extends UserRequest {
        @JsonProperty
        public long orderId;

        public UserOrderRequest(long userId, long orderId) {
            super(userId);
            this.orderId = orderId;
        }
    }

    class SaveCustomerInfoRequest extends UserRequest {

        @JsonProperty()
        public String name;
        @JsonProperty()
        public String phone;
        @JsonProperty()
        public String city;
        @JsonProperty()
        public String address;


        public SaveCustomerInfoRequest(long userId, String name, String phone, String city, String address) {
            super(userId);
            this.name = name;
            this.phone = phone;
            this.city = city;
            this.address = address;
        }
    }

    class SubmitOrderRequest extends UserRequest {
        @JsonProperty("cookbooks")
        public List<Long> recipeIds;

        public SubmitOrderRequest(long userId, List<Long> ids) {
            super(userId);
            this.recipeIds = ids;
        }
    }

    class QueryOrderRequest extends UserRequest {
        @JsonProperty()
        public long time;
        @JsonProperty()
        public int limit;

        public QueryOrderRequest(long userId, long time, int limit) {
            super(userId);
            this.time = time;
            this.limit = limit;
        }
    }

    class UpdateOrderContacterRequest extends SaveCustomerInfoRequest {

        @JsonProperty()
        public long orderId;

        public UpdateOrderContacterRequest(long userId, long orderId, String name, String phone, String city, String address) {
            super(userId, name, phone, city, address);
            this.orderId = orderId;
        }
    }

    class GetOrderRequest extends AbsPostRequest {
        @JsonProperty()
        public long orderId;

        public GetOrderRequest(long orderId) {
            this.orderId = orderId;
        }
    }

    class GetGroudingRecipesRequest extends AbsPostRequest {

        @JsonProperty()
        public int start;
        @JsonProperty()
        public int limit;
        @JsonProperty()
        public String lang;

        public GetGroudingRecipesRequest(int start, int limit, String lang) {
            this.start = start;
            this.limit = limit;
            this.lang = lang;
        }

        public GetGroudingRecipesRequest(int start, int limit) {
            this.start = start;
            this.limit = limit;
        }
    }

    class GetCrmCustomerRequest extends AbsPostRequest {
        @JsonProperty()
        public String phone;

        public GetCrmCustomerRequest(String phone) {
            this.phone = phone;
        }
    }

    class GetPageRequest extends AbsPostRequest {
        @JsonProperty("start")
        public int start;

        @JsonProperty("limit")
        public int limit;

        public GetPageRequest(int start, int limit) {
            this.start = start;
            this.limit = limit;
        }
    }

    class GetPageUserRequest extends UserRequest {
        @JsonProperty("start")
        public int start;

        @JsonProperty("limit")
        public int limit;

        public GetPageUserRequest(int start, int limit) {
            super(Plat.accountService.getCurrentUserId());
            this.start = start;
            this.limit = limit;
        }
    }

    class ThemeCollectRequest extends UserRequest {
        @JsonProperty("themeId")
        public long themeId;

        public ThemeCollectRequest(long userId, long themeId) {
            super(userId);
            this.themeId = themeId;
        }
    }

    class ConsultationListRequest extends AbsPostRequest {
        @JsonProperty("page")
        public int page;
        @JsonProperty("size")
        public int size;

        public ConsultationListRequest(int page, int size) {
            this.page = page;
            this.size = size;
        }
    }


    class CategoryRecipeImgRequest extends AbsPostRequest {
        @JsonProperty("dc")
        public String dc;

        public CategoryRecipeImgRequest(String dc) {
            this.dc = dc;
        }
    }


    class PersonalizedRecipeRequest extends  AbsPostRequest{
        @JsonProperty("userId")
        public long  userId;

        @JsonProperty("pageNo")
        public int   pageNo;

        @JsonProperty("pageSize")
        public int   pageSize;

        public PersonalizedRecipeRequest(long userId, int pageNo, int pageSize) {
            this.userId = userId;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
        }
    }


    class SubmitMaintainRequest extends UserRequest {
        @JsonProperty()
        public String customerId;

        @JsonProperty()
        public String customerName;

        @JsonProperty()
        public String phone;

        @JsonProperty()
        public String address;

        @JsonProperty()
        public String category;

        @JsonProperty()
        public String productType;

        @JsonProperty()
        public String productId;

        @JsonProperty()
        public String buyTime;

        @JsonProperty()
        public long bookTime;

        @JsonProperty()
        public String province;

        @JsonProperty()
        public String city;

        @JsonProperty()
        public String county;


        public SubmitMaintainRequest(long userId, CrmProduct product, long bookTime, String customerId, String customerName, String phone, String province, String city, String county, String address) {
            super(userId);

            this.productId = product.id;
            this.category = product.category;
            this.productType = product.type;
            this.productId = product.id;
            this.buyTime = product.buyTime;
            //
            this.customerId = customerId;
            this.customerName = customerName;
            this.bookTime = bookTime;
            this.phone = phone;
            this.province = province;
            this.city = city;
            this.county = county;
            this.address = address;
        }
    }

    class QueryMaintainRequest extends UserRequest {
        public QueryMaintainRequest(long userId) {
            super(userId);
        }
    }
}
