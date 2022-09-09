package com.robam.rokipad.ui.page.cooking;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceService;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.PreSubStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.recipe.AbsRecipeCookTask;
import com.robam.common.recipe.inter.IRecipe;
import com.robam.common.recipe.step.DeviceStatusCheck;
import com.robam.rokipad.NewPadApp;
import com.robam.rokipad.R;
import com.robam.rokipad.factory.RokiDialogFactory;
import com.robam.rokipad.listener.IRokiDialog;
import com.robam.rokipad.ui.PageArgumentKey;
import com.robam.rokipad.ui.PageKey;
import com.robam.rokipad.ui.dialog.CommonFullScreenDialog;
import com.robam.rokipad.ui.recycler.RecyclerAdapter;
import com.robam.rokipad.ui.view.cooking.RecipePrePareDetailView;
import com.robam.rokipad.ui.view.cooking.RecipePreStepDetailView;
import com.robam.rokipad.utils.DeviceDcToNameUtil;
import com.robam.rokipad.utils.DialogUtil;
import com.robam.rokipad.utils.cooking.CookingRecipeCheckUtils;
import com.robam.rokipad.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/27.
 * PS: 灶具自动烹饪.
 */
public class StoveAutoCookPage extends BasePage {
    private static final String TAG = "StoveAutoCookPage";

    @InjectView(R.id.riv_image_detail)
    RoundImageView mRivImageDetail;
    @InjectView(R.id.fl_prepare_recipe_container)
    FrameLayout mFlPrepareRecipeContainer;
    @InjectView(R.id.fl_recipe_step_container)
    FrameLayout mFlRecipeStepContainer;
    @InjectView(R.id.v_pre_bg)
    View vPreBg;
    @InjectView(R.id.recycler_pre)
    RecyclerView recyclerPre;
    private String recipeId;
    private ArrayList<CookStep> mCookSteps;
    private Recipe mRecipe;
    private RecipePrePareDetailView mRecipePrePareDetailView;
    private RecipePreStepDetailView mRecipePreStepDetailView;
    private final String POT_COOKBOOK_TYPE = "4";
    private boolean isDeviceOccupy;//设备是否被占用
    private String headId;//灶具炉头ID
    private RecyclerAdapter<PreSubStep> preSubStepAdapter;
    private int mPosition = 0;
    private String dc;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRecipe = (Recipe) bundle.getSerializable(PageArgumentKey.Recipe);
            mCookSteps = (ArrayList<CookStep>) bundle.getSerializable(PageArgumentKey.CookSteps);
            recipeId = bundle.getString(PageArgumentKey.BookId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalytics fireBaseAnalytics = NewPadApp.getFireBaseAnalytics();
        if (mRecipe != null) {
            String name = mRecipe.name;
            fireBaseAnalytics.setCurrentScreen(getActivity(), name + cx.getString(R.string.google_screen_recipe_detail), null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_stove_auto_cook, container, false);
        ButterKnife.inject(this, view);
        initView();
        initListener();
        return view;
    }

    private void initView() {
        try {
            List<PreSubStep> preSubSteps = mRecipe.preStep.getPreSubSteps();
            List<CookStep> js_cookSteps = mRecipe.getJs_cookSteps();
            LogUtils.e(TAG, "js_cookSteps:" + js_cookSteps.toString());
            if (preSubSteps != null && preSubSteps.size() > 0) {
                mRecipePrePareDetailView = new RecipePrePareDetailView(cx, preSubSteps.get(0).desc);
                mFlPrepareRecipeContainer.addView(mRecipePrePareDetailView);
                if (preSubSteps.size() == 1) {
                    vPreBg.setVisibility(View.GONE);
                    recyclerPre.setVisibility(View.GONE);
                    Glide.with(cx).load(preSubSteps.get(0).imageUrl)
                            .placeholder(R.mipmap.img_default_recipe).into(mRivImageDetail);
                } else {
                    vPreBg.setVisibility(View.VISIBLE);
                    recyclerPre.setVisibility(View.VISIBLE);
                    Glide.with(cx).load(preSubSteps.get(0).imageUrl)
                            .placeholder(R.mipmap.img_default_recipe).into(mRivImageDetail);
                    recyclerPre.setAdapter(preSubStepAdapter = new RecyclerAdapter<PreSubStep>() {
                        @Override
                        protected int getItemViewType(int position, PreSubStep preSubStep) {
                            return R.layout.item_pre_step;
                        }

                        @Override
                        protected ViewHolder<PreSubStep> onCreateViewHolder(View root, int viewType) {
                            return new PreSubStepViewHolder(root);
                        }
                    });
                    preSubStepAdapter.replace(preSubSteps);
                    recyclerPre.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    preSubStepAdapter.setListener(new RecyclerAdapter.AdapterListener<PreSubStep>() {
                        @Override
                        public void onItemClick(RecyclerAdapter.ViewHolder holder, PreSubStep preSubStep) {
                            PreSubStepViewHolder preSubStepViewHolder = (PreSubStepViewHolder) holder;
                            int position = preSubStepViewHolder.getAdapterPosition();
                            mPosition = position + 1;
                            Glide.with(cx).load(preSubStep.imageUrl)
                                    .placeholder(R.mipmap.img_default_recipe).into(mRivImageDetail);
                            mRecipePrePareDetailView.setPreStepDese(preSubStep.desc);
                            preSubStepAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onItemLongClick(RecyclerAdapter.ViewHolder holder, PreSubStep preSubStep) {

                        }
                    });

                }
            } else {
                mRecipePrePareDetailView = new RecipePrePareDetailView(cx, null);
                mFlPrepareRecipeContainer.addView(mRecipePrePareDetailView);
            }

            mRecipePreStepDetailView = new RecipePreStepDetailView(cx, js_cookSteps);
            mFlRecipeStepContainer.addView(mRecipePreStepDetailView);
        } catch (Exception e) {
            LogUtils.e(TAG, "e:" + e);
            e.printStackTrace();
        }
    }

    private void initListener() {
        if (mRecipePrePareDetailView == null) return;
        mRecipePrePareDetailView.setStartCookingListener(
                new RecipePrePareDetailView.IStartCookingListener() {
                    @Override
                    public void start() {
                        LogUtils.e(TAG, "start mCookSteps:" + mCookSteps);
                        if (mCookSteps == null || mCookSteps.size() == 0)
                            return;
                        dc = mCookSteps.get(0).getDc();
                        if (TextUtils.isEmpty(dc)) {
                            ToastUtils.show(R.string.cook_no_recipe_auto_cook_text);
                            return;
                        }
                        boolean isDevice = CookingRecipeCheckUtils.checkRecipeIsDevice(mRecipe);
                        if (!isDevice) {
                            ToastUtils.show(R.string.canNotAutoCook);
                            return;
                        }
                        //判断温控锅
                        if (POT_COOKBOOK_TYPE.equals(mRecipe.getCookbookType())) {
                            Pot[] pot = Utils.getDefaultPot();
                            if (null == pot[0]) {
                                ToastUtils.show(R.string.cook_no_pot_available_text);
                                return;
                            }
                            if (!pot[0].isConnected()) {
                                ToastUtils.show(R.string.cook_pot_available_off_line_text);
                                return;
                            }
                            Stove[] stove = Utils.getDefaultStove();
                            if (null == stove[0]) {
                                ToastUtils.show(R.string.cook_no_stove_available_text);
                                return;
                            }
                            if (!stove[0].isConnected()) {
                                ToastUtils.showShort(R.string.device_off_line);
                                return;
                            }

                        } else {

                            Stove[] stove = Utils.getDefaultStove();
                            if (null == stove[0]) {
                                ToastUtils.show(R.string.cook_no_stove_available_text);
                                return;
                            }
                            if (!stove[0].isConnected()) {
                                ToastUtils.showShort(R.string.device_off_line);
                                return;
                            }

                        }
                        AbsRecipeCookTask recipeCookTask = new AbsRecipeCookTask();
                        Map<String, Object> prerunMap = recipeCookTask.prerun(mCookSteps.get(0));
                        checkDevice(prerunMap, new AbsRecipeCookTask.RecipeDeviceSelect<String>() {
                                    @Override
                                    public void selectDevice(final String guid, String... head) {
                                        IDevice device = DeviceService.getInstance().lookupChild(guid);
                                        if (device != null) {
                                            boolean deviceIsCon = DeviceStatusCheck.getInstance().getDeviceConnect(device.getDc());
                                            if (deviceIsCon) {
                                                ToastUtils.showShort(DeviceDcToNameUtil.getDcToName(device.getDc()) + getString(R.string.recipe_device_no_connect));
                                                return;
                                            }
                                            //判断设备是否被占用
                                            if (IDeviceType.RRQZ.equals(device.getDc())) {
                                                headId = head[0];
                                                if (IRokiFamily.R9B39.equals(device.getDt()) || IRokiFamily.R9B30C.equals(device.getDt()) || IRokiFamily._9B39E.equals(device.getDt())) {
                                                    Stove stove = (Stove) device;
                                                    if (stove.isLock) {
                                                        ToastUtils.show(R.string.cook_stove_unlocking__text, Toast.LENGTH_SHORT);
                                                        return;
                                                    }
                                                } else if (IRokiFamily.R9B37.equals(device.getDt())) {
                                                    ToastUtils.show(R.string.notSupportAutoCook, Toast.LENGTH_SHORT);
                                                    return;
                                                }
                                                isDeviceOccupy = DeviceStatusCheck.getInstance().getStatus(device.getDc(), head[0]);
                                            }

//                                            LogUtils.e(TAG,"deviceDp"+device.getDp()+mRecipe.getJs_dcs());

                                            if (isDeviceOccupy) {//判断设备是否被占用
                                                String deviceName = DeviceDcToNameUtil.getDcToName(device.getDc());
                                                ToastUtils.showShort(deviceName + getString(R.string.recipe_device_occupy));
                                                return;
                                            }
                                            //  温控锅的单独处理 deviceType==4代表温控锅的菜谱
                                            if (POT_COOKBOOK_TYPE.equals(mRecipe.getCookbookType())) {
                                                Bundle bd = new Bundle();
                                                bd.putSerializable(PageArgumentKey.Recipe, mRecipe);
                                                bd.putSerializable(PageArgumentKey.CookSteps, mCookSteps);
                                                bd.putString(PageArgumentKey.DeviceGuid, guid);
                                                bd.putInt(PageArgumentKey.HeadId, Integer.parseInt(headId));
                                                UIService.getInstance().postPage(PageKey.AutoCookRecipePotStart, bd);
                                            } else {
                                                Bundle bd = new Bundle();
                                                bd.putSerializable(PageArgumentKey.Recipe, mRecipe);
                                                bd.putSerializable(PageArgumentKey.CookSteps, mCookSteps);
                                                bd.putString(PageArgumentKey.DeviceGuid, guid);
                                                bd.putInt(PageArgumentKey.HeadId, Integer.parseInt(headId));
                                                UIService.getInstance().postPage(PageKey.AutoCookRecipeStoveStart, bd);
                                            }
                                        }
                                    }

                                    @Override
                                    public void cancelSelect() {

                                    }
                                }

                        );
                    }
                }

        );
    }

    Dialog dialog;

    private void checkDevice(Map<String, Object> map, final AbsRecipeCookTask.RecipeDeviceSelect<String> callback3) {
        if (!(Boolean) map.get(IRecipe.RECIPE_STEP_DC) || !(Boolean) map.get(IRecipe.DEVICE_IFHAS)) {
//            ToastUtils.showShort("无" + DeviceDcToNameUtil.getDcToName(mCookSteps.get(0).getDc()) + "连接后才可自动烹饪");
            final IRokiDialog notDeviceDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_0);
            Stove[] stove = Utils.getDefaultStove();
            if (stove[0] != null) {
                ToastUtils.show(R.string.no_adapter_device_notic);
                return;
            }
            notDeviceDialog.setContentText(R.string.device_not_device_text);
            notDeviceDialog.setOkBtn(R.string.device_link_text, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notDeviceDialog.dismiss();
                    UIService.getInstance().returnHome();
                    LogUtils.e(TAG, "dc:" + dc);
                    if (IDeviceType.RRQZ.equals(dc) || IDeviceType.RDCZ.equals(dc)) {
                        Bundle bd = new Bundle();
                        bd.putString(PageArgumentKey.deviceCategory, dc);
                        UIService.getInstance().postPage(PageKey.DeviceBluetoothAdd, bd);
                    }
                }
            });
            notDeviceDialog.setCancelBtn(R.string.cancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notDeviceDialog.dismiss();
                }
            });
            notDeviceDialog.show();
        } else {
            List<String> occupy = (List<String>) map.get(IRecipe.DEVICE_OCCUPY);
            final List<String> availb = (List<String>) map.get(IRecipe.DEVICE_AVAILABLE);
            availb.addAll(occupy);
            if (Utils.isStove(availb.get(0))) {
                dialog = new CommonFullScreenDialog(getContext(), 1180, 680) {
                    @Override
                    protected int getViewResId() {
                        return R.layout.dialog_autorecipe_choosestovehead;
                    }

                    @Override
                    protected void initView(View view) {
                        view.findViewById(R.id.choose_dlg_left).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callback3.selectDevice(availb.get(0), "0");
                                if (dialog != null)
                                    dialog.dismiss();
                            }
                        });
                        view.findViewById(R.id.choose_dlg_right).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callback3.selectDevice(availb.get(0), "1");
                                if (dialog != null)
                                    dialog.dismiss();
                            }
                        });
                    }
                };

            } else {

                if (availb.size() == 1) {
                    callback3.selectDevice(availb.get(0));
                    return;
                }

                IDevice device = DeviceService.getInstance().lookupChild(availb.get(0));
                final String name1 = device.getDeviceType().getName();
                final String id1 = device.getDeviceType().getID();
                device = DeviceService.getInstance().lookupChild(availb.get(1));
                final String name2 = device.getDeviceType().getName();
                final String id2 = device.getDeviceType().getID();

                dialog = new CommonFullScreenDialog(getContext(), 1024, 600) {
                    @Override
                    protected int getViewResId() {
                        return R.layout.dialog_autorecipe_choosestovehead;
                    }

                    @Override
                    protected void initView(View view) {
                        LinearLayout left_frame = (LinearLayout) view.findViewById(R.id.choose_dlg_left);
                        LinearLayout right_frame = (LinearLayout) view.findViewById(R.id.choose_dlg_right);
//                        left_frame.setBackgroundResource(R.mipmap.img_auto_kzw);
//                        right_frame.setBackgroundResource(R.mipmap.img_auto_kzw);
                        ((TextView) left_frame.getChildAt(0)).setText(name1);
                        ((TextView) left_frame.getChildAt(1)).setText(id1);
                        ((TextView) right_frame.getChildAt(0)).setText(name2);
                        ((TextView) right_frame.getChildAt(1)).setText(id2);
                        TextView txtDesc = (TextView) view.findViewById(R.id.btn_txt);
                        txtDesc.setText("请选择您要使用的设备");
                        view.findViewById(R.id.choose_dlg_left).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callback3.selectDevice(availb.get(0));
                                if (dialog != null)
                                    dialog.dismiss();
                            }
                        });
                        view.findViewById(R.id.choose_dlg_right).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callback3.selectDevice(availb.get(1));
                                if (dialog != null)
                                    dialog.dismiss();
                            }
                        });
                    }
                };
            }
            dialog.show();
        }

    }

    @Override
    public void setRootBg() {
        setRootBgRes(R.mipmap.ic_center_bg);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    class PreSubStepViewHolder extends RecyclerAdapter.ViewHolder<PreSubStep> {

        RelativeLayout rlBg;
        ImageView ivStep;
        ImageView ivBg;
        TextView tvStepNum;


        public PreSubStepViewHolder(View itemView) {
            super(itemView);
            rlBg = (RelativeLayout) itemView.findViewById(R.id.rl_bg);
            ivStep = (ImageView) itemView.findViewById(R.id.iv_step);
            ivBg = (ImageView) itemView.findViewById(R.id.iv_bg);
            tvStepNum = (TextView) itemView.findViewById(R.id.tv_step_num);
        }

        @Override
        protected void onBind(PreSubStep preSubStep) {
            if (mPosition == preSubStep.index) {
                ivBg.setVisibility(View.GONE);
            } else {
                ivBg.setVisibility(View.VISIBLE);
                if (mPosition == 0 && preSubStep.index == 1) {
                    ivBg.setVisibility(View.GONE);
                }
            }
            Glide.with(cx).load(preSubStep.imageUrl)
                    .placeholder(R.mipmap.img_default_recipe).into(ivStep);
            tvStepNum.setText(String.valueOf(preSubStep.index));
        }
    }

}
