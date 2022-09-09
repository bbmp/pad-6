package com.robam.rokipad.ui.view;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.plat.Plat;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.utils.EventUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.rokipad.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2018/12/13.
 */

public class SettingAccountView extends FrameLayout {

    /*final int DEF_IMG = R.mipmap.img_user_default;
    final DisplayImageOptions opt = ImageUtils.getDefaultBuilder()
            .showImageOnLoading(DEF_IMG)
            .showImageForEmptyUri(DEF_IMG)
            .showImageOnFail(DEF_IMG)
            .displayer(new CircleBitmapDisplayer()).build();*/

    Context cx;
    User owner;
    AbsFan dfan;
    //记录当前删除设备
    String currentDeleteDevice;

    LayoutInflater layoutInflater;
    View viewRoot;

    @InjectView(R.id.user_img)
    ImageView userImg;
    @InjectView(R.id.user_name)
    TextView userName;
    @InjectView(R.id.user_phone)
    TextView userPhone;
    @InjectView(R.id.add_device)
    ImageView addDevice;
    @InjectView(R.id.info_show)
    RecyclerView infoShow;


    private ExpandableListView expandableListView;


    public SettingAccountView(Context context) {
        super(context);
        init(context, null);
    }

    public SettingAccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SettingAccountView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {
        this.cx = cx;
        owner = Plat.accountService.getCurrentUser();
        dfan = Utils.getDefaultFan();
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.view_setting_account, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);
        LinearLayoutManager layoutManager = new LinearLayoutManager(cx);
        infoShow.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
       // infoShow.setAdapter(new recycleAdapter());

    }

    class recycleAdapter extends RecyclerView.Adapter<recycleAdapter.ViewHolder> {
        List<AbsDevice> listDevice = new ArrayList<>();

        recycleAdapter(List<AbsDevice> listDevice) {
            this.listDevice = listDevice;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(cx).inflate(R.layout.account_item_recyc, parent, false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return listDevice.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @InjectView(R.id.device_img)
            ImageView deviceImg;
            @InjectView(R.id.device_name)
            TextView deviceName;
            @InjectView(R.id.device_share)
            TextView deviceShare;
            @InjectView(R.id.device_type)
            TextView deviceType;
            @InjectView(R.id.device_ma)
            TextView deviceMa;
            @InjectView(R.id.device_num)
            TextView deviceNum;
            @InjectView(R.id.user_show)
            RecyclerView userShow;
            public ViewHolder(View itemView) {
                super(itemView);
                deviceImg = (ImageView) itemView.findViewById(R.id.device_img);
                deviceName = (TextView) itemView.findViewById(R.id.device_name);
                deviceShare = (TextView) itemView.findViewById(R.id.device_share);
                deviceType = (TextView) itemView.findViewById(R.id.device_type);
                deviceMa = (TextView) itemView.findViewById(R.id.device_type);
                deviceNum = (TextView) itemView.findViewById(R.id.device_num);
                userShow = (RecyclerView) itemView.findViewById(R.id.user_show);

            }
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);

    }

}
