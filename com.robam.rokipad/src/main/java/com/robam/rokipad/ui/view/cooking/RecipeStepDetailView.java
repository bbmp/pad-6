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

import com.robam.common.pojos.CookStep;
import com.robam.rokipad.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/3/28.
 * PS: 菜谱步鄹详情页面.
 */
public class RecipeStepDetailView extends FrameLayout {


    @InjectView(R.id.rv_recipe_step)
    RecyclerView mRvRecipeStep;
    private ArrayList<CookStep> mCookSteps;
    private Context mContext;
    private RecipeStepAdapter mRecipeStepAdapter;


    public RecipeStepDetailView(Context context, ArrayList<CookStep> cookSteps) {
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
        mRecipeStepAdapter = new RecipeStepAdapter();
        mRecipeStepAdapter.setData(mCookSteps);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRvRecipeStep.setLayoutManager(layoutManager);
        mRvRecipeStep.setAdapter(mRecipeStepAdapter);
    }

    class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepViewHolder> {

        List<CookStep> cookSteps;

        public void setData(ArrayList<CookStep> cookSteps) {
            this.cookSteps = cookSteps;
            notifyDataSetChanged();
        }

        @Override
        public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_step_item, parent, false);
            return new RecipeStepViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
            holder.mCurrentStep.setText(String.valueOf(cookSteps.get(position).order));
            holder.mSumStep.setText(String.valueOf(cookSteps.size()));
            holder.mStepDesc.setText(cookSteps.get(position).desc);
        }

        @Override
        public int getItemCount() {
            return cookSteps.size();
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
