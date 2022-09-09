package com.robam.common.util;

import com.legent.Callback;
import com.legent.VoidCallback3;
import com.legent.dao.DaoHelper;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ResourcesUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.StoreService;

import java.util.List;

import static com.legent.ContextIniter.cx;

/**
 * Created by as on 2017-02-20.
 */

public class RecipeUtils {
    /**
     * p判断是否温控锅菜谱
     * oneKey---5 一键烹饪
     * pot---4 温控锅
     * origin---1
     * 这个all可能是空也可能不是，反正all返回原先所有菜谱
     *
     * @return
     */
    public static boolean isPlotRecipe(Recipe recipe) {
        if (recipe == null)
            return false;
        if ("4".equals(recipe.getCookbookType()))
            return true;
        return false;
    }

    public static boolean isFastRecipe(Recipe recipe) {
        if (recipe == null)
            return false;
        if ("5".equals(recipe.getCookbookType()))
            return true;
        return false;
    }

    /**
     * 通过Raw索引index反射tclass 对象
     *
     * @return
     * @throws Exception
     */
    public static <T> T getFromeJson(Class<T> tClass, int index) throws Exception {
        String dicContent = ResourcesUtils.raw2String(index);
        T clazz = null;
        try {
            clazz = JsonUtils.json2Pojo(dicContent, tClass);
        } finally {

        }
        return clazz;
    }

    public static <T> T getListFromeJson(Class<T> tClass, int index) throws Exception {
        String dicContent = ResourcesUtils.raw2String(index);
        T clazz = null;
        try {
            clazz = (T) JsonUtils.json2List(dicContent, tClass);
        } finally {

        }
        return clazz;
    }

    /**
     * 判断菜谱是否包含详情数据
     *
     * @return
     */
    public static boolean ifRecipeContainStep(List<Recipe> recipes) {
        if (recipes == null || recipes.size() <= 0)
            return false;
        for (Recipe recipe : recipes) {
            if (!ifRecipeContainStep(recipe))
                return false;
        }
        return true;
    }

    public static boolean ifRecipeContainStep(Recipe recipe) {
        if (recipe == null)
            return false;
        List<CookStep> list = recipe.getJs_cookSteps();
        if (list == null || list.size() <= 0)
            return false;
        return true;
    }

    public static void getRecipeDetailFromDBOrNET(long id, final VoidCallback3 callback3) throws Exception {
        if (id == 0)
            throw new NullPointerException();
        Recipe recipe = null;
        try {
            recipe = DaoHelper.getById(Recipe.class, id);
        } catch (Exception e) {
        } finally {
            if (recipe != null && recipe.hasDetail && ifRecipeContainStep(recipe)) {
                callback3.onCompleted(recipe);
            } else {
                ProgressDialogHelper.setRunning(cx, true);
                StoreService.getInstance().getCookbookById(id, new Callback<Recipe>() {
                    @Override
                    public void onSuccess(Recipe recipe) {
                        callback3.onCompleted(recipe);
                        ProgressDialogHelper.setRunning(cx, false);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        callback3.onCompleted(null);
                        ProgressDialogHelper.setRunning(cx, false);
                    }
                });
            }
        }

    }


}
