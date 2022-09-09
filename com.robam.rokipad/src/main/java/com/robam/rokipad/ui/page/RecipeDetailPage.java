package com.robam.rokipad.ui.page;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.Callback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.CookBookTag;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Material;
import com.robam.common.pojos.Recipe;
import com.robam.common.services.CookbookManager;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.recycler.CustomLinearLayoutManager;
import com.robam.rokipad.view.RoundImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2019/1/18.
 */

public class RecipeDetailPage extends BasePage {

    String id;
    String viewCount;
    @InjectView(R.id.recipe_title)
    TextView recipeTitle;
    @InjectView(R.id.eye)
    ImageView eye;
    @InjectView(R.id.time)
    ImageView time;
    @InjectView(R.id.simple)
    ImageView simple;
    @InjectView(R.id.desc)
    ImageView desc;
    @InjectView(R.id.list)
    LinearLayout list;
    @InjectView(R.id.main_title)
    TextView mainTitle;
    @InjectView(R.id.line1)
    View line1;
    @InjectView(R.id.main_recyc)
    RecyclerView mainRecyc;
    @InjectView(R.id.two_title)
    TextView twoTitle;
    @InjectView(R.id.line2)
    View line2;
    @InjectView(R.id.second_recyc)
    RecyclerView secondRecyc;
    @InjectView(R.id.rl_main)
    RelativeLayout rl_main;
    @InjectView(R.id.rl_two)
    RelativeLayout rl_two;

    List<Material> mainMaterial = new ArrayList<>();
    List<Material> secondMaterial = new ArrayList<>();
    MaterialAdapter materialAdapter1;
    MaterialAdapter materialAdapter2;
    @InjectView(R.id.img_show)
    RoundImageView imgShow;
    @InjectView(R.id.time_show)
    TextView timeShow;
    @InjectView(R.id.simple_show)
    TextView simpleShow;
    @InjectView(R.id.taste_show)
    TextView tasteShow;
    @InjectView(R.id.desc_txt)
    TextView descTxt;
    @InjectView(R.id.tv_follow)
    TextView tvFollow;
    @InjectView(R.id.recipe_detail)
    RelativeLayout recipeDetail;
    private List<CookStep> mCookSteps;
    private Recipe mRecipe;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bd) {
        id = getArguments().getString("id");
        viewCount = getArguments().getString("viewCount");
        Log.e("20190118", "id:::" + id);
        View viewroot = inflater.inflate(R.layout.recipe_detail_page, container, false);
        ButterKnife.inject(this, viewroot);
        return viewroot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        materialAdapter1 = new MaterialAdapter();
        materialAdapter2 = new MaterialAdapter();
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(cx);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mainRecyc.setLayoutManager(layoutManager);
        mainRecyc.setAdapter(materialAdapter1);
        CustomLinearLayoutManager layoutManager2 = new CustomLinearLayoutManager(cx);
        layoutManager2.setOrientation(OrientationHelper.VERTICAL);
        secondRecyc.setLayoutManager(layoutManager2);
        secondRecyc.setAdapter(materialAdapter2);
    }

    private void initData() {
        ProgressDialogHelper.setRunning(cx, true);
        long recipeId = Long.parseLong(id);
        CookbookManager.getInstance().getCookbookById(recipeId, new Callback<Recipe>() {
            @Override
            public void onSuccess(Recipe recipe) {
                LogUtils.e("20200910", "recipe:" + recipe);
                if (recipe != null) {
                    mRecipe = recipe;
                    getData(recipe);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.e("20200910", "t:" + t.getMessage());

                boolean connect = NetworkUtils.isConnect(cx);
                if (!connect) {
                    ToastUtils.show(R.string.get_recipefail_pleasechecknet);
                    ProgressDialogHelper.setRunning(cx, false);
                }
                if (mRecipe == null) return;
                ProgressDialogHelper.setRunning(cx, false);
                String string = PreferenceUtils.getString(String.valueOf(mRecipe.id), "");
                LogUtils.e("20200910", "string:" + string);
                if (null != tasteShow) {
                    tasteShow.setText(string);
                }
            }
        });

    }

    private void getData(Recipe recipe) {
        if (recipe == null) return;
        if (imgShow != null) {
            Glide.with(cx).load(null != recipe.imgMedium ? recipe.imgMedium : recipe.imgSmall)
                    .error(R.mipmap.img_default_recipe)
                    .into(imgShow);
        }
        String name = recipe.getName();
        if (recipeTitle != null) {
            recipeTitle.setText(name);
        }
        if (timeShow != null) {
            timeShow.setText((recipe.needTime / 60) + "min");
        }
        if (null != simpleShow) {
            int difficulty = recipe.difficulty;
            if (1 == difficulty || 2 == difficulty) {
                simpleShow.setText("简单");
            } else if (3 == difficulty || 4 == difficulty) {
                simpleShow.setText("适中");
            } else {
                if (5 == difficulty) {
                    simpleShow.setText("较难");
                }
            }
        }
        List<CookBookTag> cookbookTags = recipe.getCookbookTags();

        StringBuffer sb = new StringBuffer();
        if (null != cookbookTags && cookbookTags.size() > 0) {
            for (int i = 0; i < cookbookTags.size(); i++) {
                String str = cookbookTags.get(i).name;
                sb.append(str);
            }
            if (null != tasteShow) {
                tasteShow.setText(sb.toString());
                PreferenceUtils.setString(String.valueOf(recipe.id), sb.toString());
            }
        } else {
            String string = PreferenceUtils.getString(String.valueOf(recipe.id), "");
            LogUtils.e("20200310", "string:" + string);
            if (null != tasteShow) {
                tasteShow.setText(string);
            }
        }
        if (null != descTxt) {
            descTxt.setText(recipe.desc);
        }
        if (null != tvFollow) {
            tvFollow.setText(viewCount);
        }
        mainMaterial.addAll(recipe.materials.getMain());
        secondMaterial.addAll(recipe.materials.getAccessory());
        if (mainMaterial == null || mainMaterial.size() == 0) {
            if (null != rl_main) {
                rl_main.setVisibility(View.INVISIBLE);
            }
        } else {
            if (null != rl_main) {
                rl_main.setVisibility(View.VISIBLE);
            }
        }
        materialAdapter1.setData(mainMaterial);
        if (secondMaterial == null || secondMaterial.size() == 0) {
            if (null != rl_two) {
                rl_two.setVisibility(View.INVISIBLE);
            }
        } else {
            if (null != rl_two) {
                rl_two.setVisibility(View.VISIBLE);
            }
        }
        materialAdapter2.setData(secondMaterial);

        CookbookManager.getInstance().getCookBookSteps(mRecipe.id, null,
                null, new Callback<List<CookStep>>() {
                    @Override
                    public void onSuccess(List<CookStep> cookSteps) {

                        if (cookSteps == null || cookSteps.size() == 0) return;
                        mCookSteps = cookSteps;
                        ProgressDialogHelper.setRunning(cx, false);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                    }
                });
    }

    @Override
    public void setRootBg() {
        setRootBgRes(R.mipmap.ic_center_bg);
    }

    @OnClick(R.id.recipe_detail)
    public void onViewClicked() {
        try {

            boolean connect = NetworkUtils.isConnect(getContext());
            if (!connect && mRecipe == null) {
                ToastUtils.show(R.string.get_recipefail_pleasechecknet);
                return;
            }
            Bundle bd = new Bundle();
            bd.putSerializable(PageArgumentKey.CookSteps, (Serializable) mCookSteps);
            bd.putSerializable(PageArgumentKey.Recipe, mRecipe);
            bd.putString(PageArgumentKey.BookId, id);
            UIService.getInstance().postPage(PageKey.StoveAutoCook, bd);

        } catch (Exception e) {

            LogUtils.e("20190626", "e;" + e.toString());
        }
    }


    class MaterialAdapter extends RecyclerView.Adapter<RecipeMaterialHolder> {

        List<Material> listTemp = new ArrayList<>();

        public void setData(List<Material> list) {
            this.listTemp = list;
            notifyDataSetChanged();
        }

        @Override
        public RecipeMaterialHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(cx).inflate(R.layout.recipe_material_item, parent, false);
            return new RecipeMaterialHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeMaterialHolder holder, int position) {
            holder.mName.setText(listTemp.get(position).getName());
            holder.mWeight.setText(listTemp.get(position).standardWeight + listTemp.get(position).standardUnit);
        }

        @Override
        public int getItemCount() {
            return listTemp.size();
        }
    }

    class RecipeMaterialHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mWeight;

        public RecipeMaterialHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.m_name);
            mWeight = (TextView) itemView.findViewById(R.id.m_weight);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
