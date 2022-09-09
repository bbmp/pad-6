package com.robam.rokipad.ui.page;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ResourcesUtils;
import com.robam.common.pojos.dictionary.TempData;
import com.robam.common.pojos.dictionary.Tips;
import com.robam.rokipad.R;
import com.robam.rokipad.ui.recycler.RecyclerAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 14807 on 2019/8/16.
 * PS:
 */

public class PotCookingTipsPage extends BasePage {


    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    RecyclerAdapter<Tips> mAdapterTips;
    List<Tips> tipsList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_pot_cooking_tips, container, false);
        ButterKnife.inject(this,view);
        tempData();
        initAdapter();
        return view;
    }

    @Override
    public void setRootBg() {
        setRootBgRes(R.mipmap.ic_center_bg);
    }

    private void initAdapter() {

        recyclerView.setAdapter(mAdapterTips = new RecyclerAdapter<Tips>() {
            @Override
            protected int getItemViewType(int position, Tips tips) {
                return R.layout.item_pot_tips;
            }

            @Override
            protected ViewHolder<Tips> onCreateViewHolder(View root, int viewType) {
                return new PotTempTipsViewHolder(root);
            }
        });

        mAdapterTips.replace(tipsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void tempData() {
        TempData tempData = null;
        String pot_temp_tips = ResourcesUtils.raw2String(R.raw.pot_temp_tips);
        try {
            tempData = JsonUtils.json2Pojo(pot_temp_tips, TempData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Preconditions.checkNotNull(tempData, "加载 app oilData 失败");
        tipsList = tempData.getTipsList();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    class PotTempTipsViewHolder extends RecyclerAdapter.ViewHolder<Tips>{

        TextView tv_temp_range;
        TextView tv_statue;
        TextView tc_tips;

        public PotTempTipsViewHolder(View itemView) {
            super(itemView);

            tv_temp_range = (TextView) itemView.findViewById(R.id.tv_temp_range);
            tv_statue = (TextView) itemView.findViewById(R.id.tv_statue);
            tc_tips = (TextView) itemView.findViewById(R.id.tc_tips);
        }

        @Override
        protected void onBind(Tips tips) {
            tv_temp_range.setText(tips.getTempeRange());
            tv_statue.setText(tips.getStatue());
            tc_tips.setText(tips.getTips());

        }
    }

}
