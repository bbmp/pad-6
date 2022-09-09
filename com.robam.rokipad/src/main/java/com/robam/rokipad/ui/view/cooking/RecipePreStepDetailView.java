package com.robam.rokipad.ui.view.cooking;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.legent.utils.LogUtils;
import com.robam.common.pojos.CookStep;
import com.robam.rokipad.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/28.
 * PS: 菜谱预览步鄹详情页面.
 */
public class RecipePreStepDetailView extends FrameLayout {


    @InjectView(R.id.rv_recipe_step)
    RecyclerView mRvRecipeStep;
    private List<CookStep> mCookSteps;
    private Context mContext;
    private RecipePreStepAdapter mRecipeStepAdapter;


    public RecipePreStepDetailView(Context context, List<CookStep> cookSteps) {
        super(context);
        mCookSteps = cookSteps;
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_recipe_step_detail,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        initData();
    }

    private void initData() {
        mRecipeStepAdapter = new RecipePreStepAdapter();
        mRecipeStepAdapter.setData(mCookSteps);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRvRecipeStep.setLayoutManager(layoutManager);
        mRvRecipeStep.setAdapter(mRecipeStepAdapter);
    }

    class RecipePreStepAdapter extends RecyclerView.Adapter<RecipeStepViewHolder> {

        List<CookStep> preSubSteps;

        public void setData(List<CookStep> preSteps) {
            this.preSubSteps = preSteps;
            notifyDataSetChanged();
            LogUtils.e("20200910", "size:" + preSubSteps.size());
            LogUtils.e("20200910", "preSubSteps:" + preSubSteps);
        }
        @Override
        public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_step_pre_item, parent, false);
            return new RecipeStepViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
            holder.mCurrentStep.setText(String.valueOf(preSubSteps.get(position).order));
            holder.mSumStep.setText(String.valueOf(preSubSteps.size()));
            holder.mStepDesc.setText(preSubSteps.get(position).desc);
        }

        @Override
        public int getItemCount() {
            return preSubSteps == null ? 0 : preSubSteps.size();
        }
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder {
        TextView mCurrentStep;
        TextView mSumStep;
        TextView mStepStatus;
        TextView mStepDesc;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            mCurrentStep = (TextView) itemView.findViewById(R.id.tv_current_step);
            mSumStep = (TextView) itemView.findViewById(R.id.tv_sum_step);
            mStepStatus = (TextView) itemView.findViewById(R.id.tv_step_status);
            mStepDesc = (TextView) itemView.findViewById(R.id.tv_step_desc);
        }
    }


}
