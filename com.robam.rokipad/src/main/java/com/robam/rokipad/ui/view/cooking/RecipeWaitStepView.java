package com.robam.rokipad.ui.view.cooking;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.RecipeNeedTimeEvent;
import com.robam.common.events.RecipeStepEvent;
import com.robam.common.pojos.CookStep;
import com.robam.rokipad.R;
import com.robam.rokipad.listener.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/4/9.
 * PS: 做菜剩余步鄹.
 */
public class RecipeWaitStepView extends FrameLayout {

    @InjectView(R.id.rv_recipe_step_wait)
    RecyclerView mRvRecipeStepWait;
    private Context mContext;
    private ArrayList<CookStep> mCookSteps;
    private RecipeWaitStepAdapter mRecipeWaitStepAdapter;
    private int mNeedTime;
    private int mSize;//集合的大小就是当前的总步鄹
    private OnRecyclerViewItemClickListener mItemClickListener;
    private HashMap<Integer, RecipeWaitStepViewHolder> mapView = new HashMap<>();

    @Subscribe
    public void onEvent(RecipeStepEvent event) {
        int step = event.step;
        LogUtils.e("20190525", "step:" + step);
        if (step == 0) {
            mRecipeWaitStepAdapter.clear(step);
        } else {
            mRecipeWaitStepAdapter.clearList(step);
        }

    }

    @Subscribe
    public void onEvent(RecipeNeedTimeEvent event) {
        mNeedTime = event.needTime;
        mRecipeWaitStepAdapter.notifyDataSetChanged();
    }

    public RecipeWaitStepView(Context context, ArrayList<CookStep> cookSteps,
                              int size) {
        super(context);
        mContext = context;
        mCookSteps = cookSteps;
        mSize = size;
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_wait_step_recipe,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        initData();
    }

    //初始化数据
    private void initData() {
        mRecipeWaitStepAdapter = new RecipeWaitStepAdapter();
        List<CookStep> cookSteps = mCookSteps.subList(1, mCookSteps.size());
        mRecipeWaitStepAdapter.setData(cookSteps);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRvRecipeStepWait.setLayoutManager(layoutManager);
        mRvRecipeStepWait.setAdapter(mRecipeWaitStepAdapter);
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

    public void setItemClickListener(OnRecyclerViewItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;

    }

    class RecipeWaitStepAdapter extends RecyclerView.Adapter<RecipeWaitStepViewHolder> {

        List<CookStep> cookSteps;

        public void setData(List<CookStep> cookSteps) {
            this.cookSteps = cookSteps;
        }


        public void clear(int pos) {
            if (pos >= 0) {
                if (cookSteps.size() > 0) {
                    cookSteps.remove(pos);
                    notifyItemRemoved(pos);
                }
            }
        }

        public void clearList(int step) {
            if (step >= 0) {
                LogUtils.e("20190412", "cookSteps.size:" + cookSteps.size());
                if (cookSteps.size() > 0) {
                    for (int i = 0; i <= step; i++) {
                        //循环删除数据集中数据，每次删除索引为0的数据
                        cookSteps.remove(0);
                        notifyItemRemoved(0);
                    }
                }
            }
        }

        @Override
        public RecipeWaitStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_step_item, parent, false);
            return new RecipeWaitStepViewHolder(view);
        }

        int count;

        @Override
        public void onBindViewHolder(RecipeWaitStepViewHolder holder, final int position) {
            holder.mCurrentStep.setText(String.valueOf(cookSteps.get(position).order));
            holder.mSumStep.setText(String.valueOf(mSize));
            holder.mStepDesc.setText(cookSteps.get(position).desc);
            if (TextUtils.isEmpty(cookSteps.get(position).stepNote)) {
                holder.mLlStepShow.setVisibility(GONE);
            } else {
                holder.mLlStepShow.setVisibility(VISIBLE);
                holder.mStepShow.setText(cookSteps.get(position).stepNote);
            }

            LogUtils.e("20190410", "mNeedTime:" + mNeedTime + " position:" + position);
            mapView.put(position, holder);
            if (mNeedTime <= 10) {
                if (position == 0) {
                    holder.mStepStatus.setText(R.string.cook_upcoming_start_recipe_text);
                    holder.mStepStatus.setTextColor(0xffdd8735);
                    holder.mStepDesc.setTextColor(0xffffffff);
                    holder.mTvSmallTips.setTextColor(0xffffffff);
                    holder.mStepShow.setTextColor(0xffffffff);
                } else {
                    holder.mStepStatus.setText(R.string.cook_recipe_step_wait_text);
                    holder.mStepStatus.setTextColor(0xff888888);
                    holder.mStepDesc.setTextColor(0xff888888);
                    holder.mTvSmallTips.setTextColor(0xff888888);
                    holder.mStepShow.setTextColor(0xff888888);
                }
            } else {
                holder.mStepStatus.setText(R.string.cook_recipe_step_wait_text);
                holder.mStepStatus.setTextColor(0xff888888);
                holder.mStepDesc.setTextColor(0xff888888);
                holder.mTvSmallTips.setTextColor(0xff888888);
                holder.mStepShow.setTextColor(0xff888888);
            }

            holder.mLlStepItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    count++;
                    if (count == 2) {
                        count = 0;
                        if (mItemClickListener != null) {
                            v.setTag(position);
                            v.setTag(R.id.tag_recycler_holder, cookSteps.get(position).order);
                            mItemClickListener.onItemClick(v);
                        }
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return cookSteps.size();
        }
    }

    class RecipeWaitStepViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLlStepItem;
        LinearLayout mLlStepShow;
        TextView mCurrentStep;
        TextView mSumStep;
        TextView mStepStatus;
        TextView mStepDesc;
        TextView mTvSmallTips;
        TextView mStepShow;

        public RecipeWaitStepViewHolder(View itemView) {
            super(itemView);
            mLlStepItem = (LinearLayout) itemView.findViewById(R.id.ll_step_item);
            mLlStepShow = (LinearLayout) itemView.findViewById(R.id.ll_step_show);
            mCurrentStep = (TextView) itemView.findViewById(R.id.tv_current_step);
            mSumStep = (TextView) itemView.findViewById(R.id.tv_sum_step);
            mStepStatus = (TextView) itemView.findViewById(R.id.tv_step_status);
            mStepDesc = (TextView) itemView.findViewById(R.id.tv_step_desc);
            mTvSmallTips = (TextView) itemView.findViewById(R.id.tv_small_tips);
            mStepShow = (TextView) itemView.findViewById(R.id.tv_step_show);
        }
    }
}
