package com.robam.rokipad.ui.page;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.common.base.Strings;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiUtils;
import com.robam.common.Utils;
import com.robam.common.events.HomeIcUpdateEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.History;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.common.util.RecipeUtils;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.view.RecipeItemView;
import com.robam.rokipad.utils.IOnClickRecipeItemShow;
import com.robam.rokipad.utils.IRecipeType;
import com.robam.rokipad.utils.OnClickRecipeItemSelect;
import com.robam.rokipad.utils.RoundTransformation;
import com.robam.rokipad.view.PulToLeftViewGroupl;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2019/1/5.
 */

public class RecipeAllPage extends BasePage {

    @InjectView(R.id.list_container)
    FrameLayout listContainer;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.recipe_show)
    RecyclerView recipeShow;
    @InjectView(R.id.pull_group)
    PulToLeftViewGroupl pulToLeftViewGroupl;
    @InjectView(R.id.search_result)
    TextView searchRe;
    int start = 0;
    int num = 10;
    @InjectView(R.id.search_flowlayout)
    TagFlowLayout searchFlowlayout;
    @InjectView(R.id.search)
    EditText search;
    @InjectView(R.id.search_btn)
    ImageView searchBtn;

    @InjectView(R.id.txt_desc)
    TextView txtDesc;//无历史数据

    @InjectView(R.id.recipe_all_show)
    LinearLayout recipeAllShow;
    List<Recipe> recipeList = new ArrayList<>();
    @InjectView(R.id.tv_go_network)
    TextView tvGoNetwork;
    @InjectView(R.id.ll_wifi_iscon)
    LinearLayout llWifiIscon;
    private RecipeItemView recipeItemView;
    private String searchWord = null;

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        fireBaseAnalytics.setCurrentScreen(getActivity(), cx.getString(R.string.google_screen_recipe_home), null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewroot = inflater.inflate(R.layout.search_page, container, false);
        ButterKnife.inject(this, viewroot);
        initView();
        return viewroot;
    }


    RecipeShowAdapter recipeShowAdapter;
    String type = null;
    String cookBookType = "all";

    private void initView() {
        recipeItemView = new RecipeItemView(cx);
        listContainer.addView(recipeItemView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(cx);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recipeShow.setLayoutManager(layoutManager);
        recipeShowAdapter = new RecipeShowAdapter();
        recipeShow.setAdapter(recipeShowAdapter);
        recipeShowAdapter.setOnClickRecipeItemListener(new IOnClickRecipeItemShow() {
            @Override
            public void onItemSelect(String tag, String viewCount, int position) {

                Bundle bundle = new Bundle();
                bundle.putString("id", tag);
                bundle.putString("viewCount", viewCount);
                UIService.getInstance().postPage(PageKey.RecipeDetail, bundle);
            }
        });
        init();
        recipeItemView.setOnClickRecipeItemListener(new OnClickRecipeItemSelect() {
            @Override
            public void onClickDcSelectItem(String tag, int position) {
                searchRe.setVisibility(View.GONE);
                search.setText("");
                LogUtils.e("20200302", "tag:" + tag);
                type = tag;
//                if ("RECOMMAND".equals(type)) {
//                    initData();
//                } else {
                ProgressDialogHelper.setRunning(cx, true);
                start = 0;
                num = 10;
                showTag(tag);
//                }
            }
        });
        if (search != null) {
            search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    Log.e("20201013", "onEditorAction:" + v.getText().toString());
                    String text = v.getText().toString();
                    if (!TextUtils.isEmpty(text)) {
                        searching(text);
                        return true;
                    }
                    return false;
                }
            });
        }

        pulToLeftViewGroupl.setOnPullToLeftListener(new PulToLeftViewGroupl.OnPullToLeftListener() {
            @Override
            public void onReleaseFingerToUpload() {
                LogUtils.e("20190617", "onReleaseFingerToUpload");
                if (TextUtils.isEmpty(search.getText().toString()) && TextUtils.isEmpty(searchWord)) {
                    onLoadMoreHere();
                }

            }

            @Override
            public void onStartToUpload() {
                LogUtils.e("20190617", "onStartToUpload");
            }
        });
    }

    private void init() {
        boolean bool = PreferenceUtils.getBool(PageArgumentKey.IsONE, false);
        if (!WifiUtils.isWifiConnected(cx) && !bool) {
            recipeAllShow.setVisibility(View.GONE);
            llWifiIscon.setVisibility(View.VISIBLE);
        } else if (WifiUtils.isWifiConnected(cx) && bool) {
            recipeAllShow.setVisibility(View.VISIBLE);
            llWifiIscon.setVisibility(View.GONE);
        } else if (WifiUtils.isWifiConnected(cx) && !bool) {
            recipeAllShow.setVisibility(View.VISIBLE);
            llWifiIscon.setVisibility(View.GONE);
        } else {
            recipeAllShow.setVisibility(View.VISIBLE);
            llWifiIscon.setVisibility(View.GONE);
        }
        initDataHead();
        initData();
    }

    private void showTag(String tag) {
        LogUtils.e("20190627", "tag:" + tag);
        cookBookType = "all";
        switch (tag) {
            case IRecipeType.recommand:
                name.setText(R.string.cook_recommend_recipe_text);
                loadingRecommend();
                break;
            case IRecipeType.stove:
                name.setText(R.string.cook_stove_recipe_text);
                getDeviceRecipe();
                break;
            case IRecipeType.pot:
                name.setText(R.string.cook_pot_recipe_text);
                type = "RRQZ";
                cookBookType = "pot";
                getDeviceRecipe();
                break;
            case IRecipeType.oven:
                name.setText(R.string.cook_oven_recipe_text);
                getDeviceRecipe();
                break;
            case IRecipeType.steam:
                name.setText(R.string.cook_steam_recipe_text);
                getDeviceRecipe();
                break;
            case IRecipeType.mic:
                name.setText(R.string.cook_wave_recipe_text);
                getDeviceRecipe();
                break;
            default:
                break;
        }

    }

    @OnClick(R.id.search_btn)
    public void searchBtn() {
        String word = search.getText().toString();
        if (Strings.isNullOrEmpty(word)) {
            ToastUtils.show(R.string.cook_please_enter_keywords_text, Toast.LENGTH_SHORT);
            return;
        }
        searching(word);
    }

    private void searching(final String word) {
        ProgressDialogHelper.setRunning(cx, true);
        String userId = null;
        if (user != null) {
            userId = String.valueOf(user.getID());
        }
        name.setText(R.string.cook_search_recipe_text);
        CookbookManager.getInstance().saveHistoryKeysForCookbook(word);
        StoreService.getInstance().getSearchResult(userId, word, "false", new Callback<Reponses.SearchResult>() {
            @Override
            public void onSuccess(Reponses.SearchResult searchResult) {
                if (searchResult != null) {
                    Log.e("20190111", "search:" + searchResult.toString());
                    if (searchResult.cookBooks.size() == 0) {
                        searchRe.setVisibility(View.VISIBLE);
                        ProgressDialogHelper.setRunning(cx, false);
                    } else {
                        searchRe.setVisibility(View.GONE);
                        recipeShowAdapter.setRecipeList(searchResult.cookBooks);
                        searchWord = word;
                        ProgressDialogHelper.setRunning(cx, false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("20190111", "t::" + t.getMessage());
            }
        });
    }

    public void onLoadMoreHere() {

        if ("RECOMMAND".equals(type)) {

        } else {
            start = 0;
            num += 10;
            getDeviceRecipe();
        }

    }

    User user = Plat.accountService.getCurrentUser();

    private void initDataHead() {
        if (user == null) {
            final List<String> words = CookbookManager.getInstance().getHistoryKeysForCookbook();
            if (words.size() == 0) {
                txtDesc.setVisibility(View.VISIBLE);
                searchFlowlayout.setVisibility(View.INVISIBLE);
                return;
            }
            searchFlowlayout.setAdapter(new TagAdapter<String>(words) {

                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(cx).inflate(R.layout.tv,
                            searchFlowlayout, false);
                    int length = s.length();
                    if (length <= 4) {
                        tv.setText(s);
                    } else {
                        String subText = s.substring(0, 4);
                        tv.setText(subText + "...");
                    }
                    return tv;
                }
            });
            searchFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    Log.e("20190115", "positioin:::" + position);
                    if (words != null || words.size() > 0) {
                        String word = words.get(position);
                        searching(word);
                    }
                    return true;
                }
            });
        } else {
            long userId = user.getID();
            getHistoryKey(userId);
        }

    }

    private void getHistoryKey(long userId) {
        StoreService.getInstance().getCookbookSearchHistory(userId, new Callback<Reponses.HistoryResponse>() {
            @Override
            public void onSuccess(Reponses.HistoryResponse historyResponse) {
                if (historyResponse != null) {
                    initHistoryData(historyResponse.historyList);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    //历史搜索数据
    private void initHistoryData(final List<History> historyList) {
        if (historyList != null && historyList.size() > 0) {
            List<History> histories = null;
            if (historyList.size() > 5) {
                histories = historyList.subList(0, 4);
            } else {
                histories = historyList;
            }
            if (histories.size() == 0) {
                txtDesc.setVisibility(View.VISIBLE);
                searchFlowlayout.setVisibility(View.INVISIBLE);
                return;
            }
            if (null != searchFlowlayout) {
                searchFlowlayout.setAdapter(new TagAdapter<History>(histories) {
                    @Override
                    public View getView(FlowLayout parent, int position, History history) {
                        TextView tv = (TextView) LayoutInflater.from(cx).inflate(R.layout.tv,
                                searchFlowlayout, false);
                        String s = history.value;
                        int length = s.length();
                        if (length <= 4) {
                            tv.setText(s);
                        } else {
                            String subText = s.substring(0, 4);
                            tv.setText(subText + "...");
                        }
                        return tv;
                    }
                });

                searchFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        Log.e("20190115", "hhhhhh:" + position);
                        if (historyList != null && historyList.size() > 0) {
                            String word = historyList.get(position).value;
                            searching(word);
                        }
                        return true;
                    }
                });
            }

        }
    }

    private void initData() {
        ProgressDialogHelper.setRunning(cx, true);
        Pot[] pot = Utils.getDefaultPot();
        if (pot[0] == null) {
            loadingRecommend();
        } else {
            type = "RRQZ";
            cookBookType = "pot";
            getDeviceRecipe();
            recipeItemView.setCurrent(2);
            if (name != null) {
                name.setText(R.string.cook_pot_recipe_text);
            }
        }
    }

    private void loadingRecommend() {
        CookbookManager.getInstance().getRecommendCookbooksForMob(new Callback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                if (recipes != null) {
                    recipeShowAdapter.setRecipeList(recipes);
                    searchWord = null;
                    PreferenceUtils.setBool(PageArgumentKey.IsONE, true);
                    ProgressDialogHelper.setRunning(cx, false);
                    if (name != null) {
                        name.setText(R.string.cook_recommend_recipe_text);
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }

    private void getDeviceRecipe() {
        try {
            CookbookManager.getInstance().getGroundingRecipesByDevice(type, start, num, cookBookType,
                    null, new Callback<List<Recipe>>() {
                        @Override
                        public void onSuccess(List<Recipe> recipes) {
                            if (recipes == null || recipes.size() == 0) {
                                return;
                            }
                            LogUtils.e("20190617", "recipes:" + recipes.size());
                            if (recipeList.size() == recipes.size() && recipeList.size() > 20) {
                                ToastUtils.show(R.string.cook_not_more, 0);
                                ProgressDialogHelper.setRunning(cx, false);
                                return;
                            }
                            if (recipes != null) {
                                recipeList.clear();
                                recipeList.addAll(recipes);
                                recipeShowAdapter.setRecipeList(recipeList);
                                searchWord = null;
                                PreferenceUtils.setBool(PageArgumentKey.IsONE, true);
                                try {
                                    if (null != pulToLeftViewGroupl) {
                                        pulToLeftViewGroupl.completeToUpload();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ProgressDialogHelper.setRunning(cx, false);
                            if (null == t) return;
                            ToastUtils.show(t.getMessage());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventUtils.postEvent(new HomeIcUpdateEvent("HOME"));
        ButterKnife.reset(this);
    }

    @OnClick(R.id.tv_go_network)
    public void onTvGoNetworkClicked() {
        UIService.getInstance().returnHome();
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.IsNotNetwork, "IsNotNetwork");
        UIService.getInstance().postPage(PageKey.HomeSet, bd);
    }

    class RecipeShowAdapter extends RecyclerView.Adapter<RecipeShowAdapter.RecipeShowViewHolder> {

        List<Recipe> recipeList = new ArrayList<>();

        public void setRecipeList(List<Recipe> list) {
            this.recipeList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecipeShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(cx).inflate(R.layout.recipe_show, parent, false);
            return new RecipeShowViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeShowViewHolder holder, final int position) {
            Glide.with(cx).load(recipeList.get(position).imgMedium).asBitmap()
                    .placeholder(R.mipmap.img_default_recipe)
                    .error(R.mipmap.img_default_recipe)
                    .transform(new CenterCrop(cx), new RoundTransformation(cx, 10))
                    .into(holder.img);

//            Glide.with(cx)
//                    .load(recipeList.get(position).imgMedium)
//                    .asBitmap()
//                    .placeholder(R.mipmap.img_default_recipe)
//                    .error(R.mipmap.img_default_recipe)
//                    .transform(new CenterCrop(cx), new RoundTransformation(cx, 10))
//                    .into(new TransformationUtils(holder.img));

            if (getString(R.string.cook_recommend_recipe_text).equals(name.getText())){
                holder.ic_device_tag.setVisibility(View.VISIBLE);
            }else {
                holder.ic_device_tag.setVisibility(View.GONE);
            }
            holder.recipeName.setText(recipeList.get(position).getName());
            holder.viewNum.setText(recipeList.get(position).viewCount);
            Recipe recipe = recipeList.get(position);
            List<Dc> js_dcs = recipe.getJs_dcs();
            String cookbookType = recipe.getCookbookType();
            if (null != js_dcs && js_dcs.size() > 0) {
                Dc dc = js_dcs.get(0);
                if (null != dc) {
                    String dcs = dc.dc;
                    Log.e("20201014", "dcs:" + dcs);
                    switch (dcs) {
                        case "RRQZ":
                        case "RDCZ":

                            if ("pot".equals(cookbookType)) {
                                holder.ic_device_tag.setImageResource(R.mipmap.ic_wrg_recipe_tag);
                            } else {
                                holder.ic_device_tag.setImageResource(R.mipmap.ic_zj_recipe_tag);
                            }
                            break;
                        case "RDKX":
                            holder.ic_device_tag.setImageResource(R.mipmap.ic_dkx_recipe_tag);
                            break;
                        case "RZQL":
                            holder.ic_device_tag.setImageResource(R.mipmap.ic_dzx_recipe_tag);
                            break;
                        case "RWBL":
                            holder.ic_device_tag.setImageResource(R.mipmap.ic_wbl_recipe_tag);
                            break;

                    }
                }
            }
            holder.lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickRecipeItemListener != null) {
                        onClickRecipeItemListener.onItemSelect(String.valueOf(recipeList.get(position).id), recipeList.get(position).viewCount, position);
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return recipeList != null ? recipeList.size() : 0;
        }

        class RecipeShowViewHolder extends RecyclerView.ViewHolder {
            LinearLayout lay;
            ImageView img;
            ImageView ic_device_tag;
            TextView recipeName;
            TextView viewNum;

            public RecipeShowViewHolder(View itemView) {
                super(itemView);
                lay = (LinearLayout) itemView.findViewById(R.id.lay_gg);
                img = (ImageView) itemView.findViewById(R.id.recipe_img);
                ic_device_tag = (ImageView) itemView.findViewById(R.id.ic_device_tag);
                recipeName = (TextView) itemView.findViewById(R.id.recipe_name);
                viewNum = (TextView) itemView.findViewById(R.id.view_num);
            }
        }

        public IOnClickRecipeItemShow onClickRecipeItemListener;

        public void setOnClickRecipeItemListener(IOnClickRecipeItemShow onClickRecipeItem) {
            this.onClickRecipeItemListener = onClickRecipeItem;
        }

    }

    static class ViewHolder {
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
