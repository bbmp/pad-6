package com.robam.common.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.TransactionManager;
import com.legent.Callback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.utils.LogUtils;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.DaoService;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 菜谱
 *
 * @author sylar
 */
public class Recipe extends AbsRecipe implements Serializable {
    public static final String FOREIGN_COLUMNNAME_ID = "PreStep_ID";
    private static final String TAG = "Recipe";

    @DatabaseField
    @JsonProperty("cookbookType")
    String cookbookType;

    public String getCookbookType() {
        return cookbookType;
    }

    public void setCookbookType(String cookbookType) {
        this.cookbookType = new String(cookbookType);
    }

    /**
     * 菜谱描述
     */
    @DatabaseField
    @JsonProperty("introduction")
    public String desc;

    /**
     * 所需时间 （秒）
     */
    @DatabaseField
    @JsonProperty("needTime")
    public int needTime;

    /**
     * 难度系数
     */
    @DatabaseField
    @JsonProperty("difficulty")
    public int difficulty;

    /**
     * 小图
     */
    @DatabaseField
    @JsonProperty("imgSmall")
    public String imgSmall;

    /**
     * 中图
     */
    @DatabaseField
    @JsonProperty("imgMedium")
    public String imgMedium;

    /**
     * 大图
     */
    @DatabaseField
    @JsonProperty("imgLarge")
    public String imgLarge;

    /**
     * 海报图
     */
    @DatabaseField
    @JsonProperty("imgPoster")
    public String imgPoster;

    @DatabaseField
    @JsonProperty("viewCount")
    public String viewCount;


    /**
     * 本地存储的库版本号
     */
    @DatabaseField()
    public int version;

    /**
     * 是否有明细数据
     */
    @DatabaseField()
    public boolean hasDetail;

    /**
     * 最近的明细数据更新时间
     */
    @DatabaseField()
    public long lastUpgradeTime;


    @ForeignCollectionField()
    private ForeignCollection<Dc> db_dcs;

    public List<Dc> getJs_dcs() {
        if (db_dcs != null && db_dcs.size() > 0) {
            js_dcs = Lists.newArrayList(db_dcs);
        }
        if (js_dcs == null)
            js_dcs = Lists.newArrayList();
        return js_dcs;
    }

    /**
     * 获取菜谱用到的设备品类
     */
    @JsonProperty("dcs")
    protected List<Dc> js_dcs;


    public List<CookBookTagGroup> getJs_cookbook() {
        if (js_cookbook == null)
            return Lists.newArrayList();
        return js_cookbook;
    }

    @JsonProperty("cookbookTagGroups")
    protected List<CookBookTagGroup> js_cookbook;


    /**
     * 食材清单
     */
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "materials_id")
    @JsonProperty("materials")
    public Materials materials;

    /**
     * 备菜步骤
     */
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    @JsonProperty("prepareSteps")
    public PreStep preStep;

    public String getGroupIds() {
        StringBuilder stringBuilder = new StringBuilder();
        if (js_cookbook != null && js_cookbook.size() > 0) {
            for (CookBookTagGroup group : js_cookbook) {
                stringBuilder.append(group.getID() + ":");
            }
            if (stringBuilder != null && !"".equals(stringBuilder))
                return stringBuilder.subSequence(0, stringBuilder.length() - 1).toString();
        }
        //数据库
        if (groupIds == null)
            return "";
        return groupIds;
    }

    /**
     * 菜谱对应GroupId
     */
    @DatabaseField()
    protected String groupIds;
    // ------------------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------------------

    @ForeignCollectionField(eager = true)
    private ForeignCollection<CookStep> db_cookSteps;

    public List<CookStep> getJs_cookSteps() {
        if (db_cookSteps != null && db_cookSteps.size() > 0) {
            js_cookSteps = Lists.newArrayList(db_cookSteps);
            if (Plat.DEBUG)
                LogUtils.e(TAG, "js_cookSteps:" + js_cookSteps.toString());
        }
        if (js_cookSteps == null)
            js_cookSteps = Lists.newArrayList();
        return js_cookSteps;
    }

    @JsonProperty("steps")
    public List<CookStep> js_cookSteps;


    public List<CookBookTag> getCookbookTags() {
        if (cookbookTags == null)
            cookbookTags = Lists.newArrayList();
        return cookbookTags;
    }

    @JsonProperty("cookbookTags")
    protected List<CookBookTag> cookbookTags;


    // ----------------------------------------------------------------------------------------------------

    /**
     * 菜谱分享查看 的url链接
     *
     * @return
     */
    public String getViewUrl() {
        String url = String.format("http://h5.myroki.com/#/recipeShare?cookbookId=%s",
                id);
        return url;
    }

    public boolean isNewest() {
        return Calendar.getInstance().getTimeInMillis() - lastUpgradeTime <= CookbookManager.UpdatePeriod;
    }

    public void getDetail(Callback<Recipe> callback) {
        CookbookManager.getInstance().getCookbookById(id, callback);
    }

    @Override
    public void save2db() {
        delete(this.id);
        //super.save2db();

        if (preStep != null) {
            preStep.save2db();
        }

        if (materials != null) {
            materials.save2db();
        }
        if (js_cookbook != null && js_cookbook.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (CookBookTagGroup group : js_cookbook) {
                stringBuilder.append(group.getID() + ":");
            }
            if (stringBuilder != null && !"".equals(stringBuilder))
                groupIds = stringBuilder.subSequence(0, stringBuilder.length() - 1).toString();
        }

        lastUpgradeTime = Calendar.getInstance().getTimeInMillis();
        super.save2db();//此位置不能变

        if (js_cookSteps != null) {
            for (CookStep cs : js_cookSteps) {
                cs.cookbook = this;
                cs.save2db();
            }
        }

//        if (cookbookTags != null) {
//            for (CookBookTag tag : cookbookTags) {
//                tag.cookbook = this;
//                tag.save2db();
//            }
//        }

        if (js_dcs != null) {
            for (Dc dc : js_dcs) {
                dc.cookbook = this;
                dc.save2db();
            }
        }

       /* if (js_cookbook != null) {
            for (CookBookTagGroup group : js_cookbook) {
                group.cookbook = this;
                group.save2db();
            }
        }*/
        lastUpgradeTime = hasDetail ? Calendar.getInstance().getTimeInMillis() : 0;
        DaoHelper.update(this);
        DaoHelper.refresh(this);
    }

    @Override
    public void delete(long id) {
        try {
            Recipe recipe_db = null;
            if (id != 0)
                recipe_db = DaoHelper.getById(Recipe.class, id);
            else {
                if (this.id != 0)
                    recipe_db = DaoHelper.getById(Recipe.class, this.id);
            }
            if (recipe_db == null) return;
            try {
                recipe_db.preStep.delete(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                recipe_db.materials.delete(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (recipe_db.db_cookSteps != null) {
                for (CookStep step : recipe_db.db_cookSteps) {
                    try {
                        step.delete(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (recipe_db.db_dcs != null) {
                for (Dc dc : recipe_db.db_dcs) {
                    try {
                        dc.delete(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
           /* if (recipe_db.db_cookbook != null) {
                for (CookBookTagGroup group : recipe_db.db_cookbook) {
                    try {
                    }
                        group.delete(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }*/
            DaoHelper.delete(recipe_db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 事物提交
     */
    public void tra2Save() {
        try {
            TransactionManager.callInTransaction(DaoService.getInstance().getCurrentDbHelper().getConnectionSource(),
                    new Callable<Boolean>() {
                        public Boolean call() throws Exception {
                            save2db();
                            return true;
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 事物删除
     * 如若删除自己 id 为0
     */
    public void tra2Del(final long id) {
        try {
            TransactionManager.callInTransaction(DaoService.getInstance().getCurrentDbHelper().getConnectionSource(),
                    new Callable<Boolean>() {
                        public Boolean call() throws Exception {
                            delete(id);
                            return true;
                        }
                    });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
