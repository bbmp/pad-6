package com.robam.rokipad.ui.page;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.common.base.Preconditions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.rokipad.R;
import com.robam.rokipad.service.ConvenientCookService;
import com.robam.rokipad.ui.dialog.CommonFullScreenDialog;
import com.robam.rokipad.ui.utils.PropertiesUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ConvenientMenuPage extends BasePage implements View.OnClickListener {
    private static final String TAG = "ConvenientMenuPage";

    private AppCompatButton mBtnLeftStartCook;
    private AppCompatButton mBtnRightStartCook;
    private ImageView mIvArrayUp;
    private ImageView imgFireEeg;
    private ImageView imgHotMilk;
    private TextView tvExit;
    private List<Recipe> converientMenuRecipeList = new ArrayList<Recipe>();
    private Dialog dialog;
    private Stove[] stoveList;
    private ConvenientCookService convenientCookService = ConvenientCookService.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_convenient_menu, container, false);
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View viewRoot) {
        mBtnLeftStartCook = (AppCompatButton) viewRoot.findViewById(R.id.btn_left_start_cook);
        mIvArrayUp = (ImageView) viewRoot.findViewById(R.id.iv_array_up);
        mBtnRightStartCook = (AppCompatButton) viewRoot.findViewById(R.id.btn_right_start_cook);
        imgFireEeg = (ImageView) viewRoot.findViewById(R.id.img_fire_eeg);
        imgHotMilk = (ImageView) viewRoot.findViewById(R.id.img_hot_milk);
        tvExit = (TextView) viewRoot.findViewById(R.id.tv_exit);

        mBtnLeftStartCook.setOnClickListener(this);
        mIvArrayUp.setOnClickListener(this);
        mBtnRightStartCook.setOnClickListener(this);
        imgFireEeg.setOnClickListener(this);
        imgHotMilk.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        stoveList = Utils.getDefaultStove();
        int leftLevel = stoveList[0].leftHead.level;
        int rightLevel = stoveList[0].rightHead.level;
        LogUtils.e(TAG, "leftLevel:" + leftLevel + " rightLevel:" + rightLevel);
        converientMenuRecipeList = getConventientRecipeList();
    }


    private List<Recipe> getConventientRecipeList() {
        String fromRawJsonFile = PropertiesUtil.readJsonFile(getContext(), R.raw.convenient_recipe);
        try {
            JSONObject jsonObject = new JSONObject(fromRawJsonFile);
            converientMenuRecipeList.clear();
            List<Recipe> recipeList = JsonUtils.json2List(jsonObject.getString("cookbooks"), Recipe.class);
            return recipeList;
        } catch (Exception e) {
            LogUtils.e(TAG, "Exception:" + e.toString());
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left_start_cook:
                getOneKeyCookData(getContext(), stoveList[0], converientMenuRecipeList.get(0));
                break;
            case R.id.btn_right_start_cook:
                getOneKeyCookData(getContext(), stoveList[0], converientMenuRecipeList.get(1));
                break;
            case R.id.iv_array_up:
                UIService.getInstance().popBack();
                break;
            case R.id.tv_exit:
                UIService.getInstance().popBack();
                break;
        }
    }

    private void getOneKeyCookData(Context context, final Stove stove, final Recipe recipe) {
        if (checkStove())
            dialog = new CommonFullScreenDialog(getContext(), 1180, 680) {
                @Override
                protected int getViewResId() {
                    return R.layout.dialog_convenient_menu;
                }

                @Override
                protected void initView(View view) {
                    TextView leftHead = (TextView) view.findViewById(R.id.tv_left_head_status);
                    TextView rightHead = (TextView) view.findViewById(R.id.tv_right_head_status);
                    if (stove.leftHead.status == StoveStatus.Working) {
                        leftHead.setTextColor(Color.WHITE);
                        leftHead.setText(getString(R.string.head_open) + stove.leftHead.level + getString(R.string.stove_head_level));
                    } else {
                        leftHead.setText(R.string.head_closed);
                        leftHead.setTextColor(Color.parseColor("#666666"));
                    }

                    if (stove.rightHead.status == StoveStatus.Working) {
                        rightHead.setTextColor(Color.WHITE);
                        rightHead.setText(getString(R.string.head_open) + stove.rightHead.level + getString(R.string.stove_head_level));
                    } else {
                        rightHead.setText(R.string.head_closed);
                        rightHead.setTextColor(Color.parseColor("#666666"));
                    }

                    view.findViewById(R.id.iv_left_head).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectHeadIDCook(getContext(), stove.leftHead, recipe);
                        }

                    });
                    view.findViewById(R.id.iv_right_head).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectHeadIDCook(getContext(), stove.rightHead, recipe);
                        }
                    });
                }
            };
        if (dialog != null) {
            dialog.show();
        }
    }

    private void selectHeadIDCook(Context context, Stove.StoveHead stoveHead, Recipe recipe) {
        if (stoveHead.status == StoveStatus.Working) {
            ToastUtils.show("请选择未开启的炉头");
        } else {
            convenientCookService.start(context, stoveHead, recipe);
            if (dialog != null) {
                dialog.dismiss();
            }
            UIService.getInstance().popBack();
        }
    }

    private boolean checkStove() {
        try {
            Resources r = getResources();
            Preconditions.checkNotNull(stoveList[0], r.getString(R.string.dev_invalid_error));
            Preconditions.checkState(stoveList[0].isConnected(), r.getString(R.string.stove_invalid_error));
            return true;
        } catch (Exception e) {
            ToastUtils.showException(e);
            return false;
        }
    }

}