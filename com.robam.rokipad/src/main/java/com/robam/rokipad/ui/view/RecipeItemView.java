package com.robam.rokipad.ui.view;

import android.content.Context;
import android.graphics.Color;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legent.utils.EventUtils;
import com.robam.rokipad.R;
import com.robam.rokipad.utils.OnClickRecipeItem;
import com.robam.rokipad.utils.OnClickRecipeItemSelect;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2019/1/7.
 */

public class RecipeItemView extends FrameLayout {

    Context cx;
    View viewRoot;
    @InjectView(R.id.recipe_item)
    RecyclerView recipeItem;


    public RecipeItemView(Context context) {
        super(context);
        init(context, null);
    }

    public RecipeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecipeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
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

    private OnClickRecipeItemSelect onClickRecipeItemSelectLister;

    public void setOnClickRecipeItemListener(OnClickRecipeItemSelect onClickRecipeItemSelectLister){
        this.onClickRecipeItemSelectLister = onClickRecipeItemSelectLister;
    }

    private void init(Context cx, AttributeSet attrs) {
        this.cx = cx;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.recipe_item, this, true);
        if (!viewRoot.isInEditMode())
            ButterKnife.inject(this, viewRoot);

        LinearLayoutManager layoutManager = new LinearLayoutManager(cx);
        recipeItem.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        initView();

    }

    RecipeAdapter recipeAdapter;
    List<String> itemName = new ArrayList<>();
    List<String> dcList = new ArrayList<>();
    String[] name = {"推荐","灶具","无人锅","电烤箱","电蒸箱","微波炉"};
    String[] dcTag = {"RECOMMAND","RRQZ","RPOT","RDKX","RZQL","RWBL"};
    private void initView() {
        for (int i = 0; i < name.length; i++) {
            itemName.add(name[i]);
        }
        for (int i = 0; i <dcTag.length ; i++) {
            dcList.add(dcTag[i]);
        }
        recipeAdapter = new RecipeAdapter(itemName,dcList);
        recipeItem.setAdapter(recipeAdapter);
        recipeAdapter.setCurrent(0);
        recipeAdapter.setOnClickRecipeItemListener(new OnClickRecipeItem() {
            @Override
            public void onItemSelect(String tag, int position) {
                recipeAdapter.setCurrent(position);
                if (onClickRecipeItemSelectLister!=null){
                    onClickRecipeItemSelectLister.onClickDcSelectItem(tag,position);
                }
            }
        });
    }

    public void setCurrent(int pos){
        if (recipeAdapter != null){
            recipeAdapter.setCurrent(pos);
        }
    }

    class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ItemViewHolder>{
        List<String> listItem = new ArrayList<>();
        List<String> listTag = new ArrayList<>();
        private int currentItem;
        public RecipeAdapter(List<String> listItem,List<String> tagList){
            this.listItem = listItem;
            this.listTag = tagList;
        }

        public void setCurrent(int currentItem){
            this.currentItem = currentItem;
           notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(cx).inflate(R.layout.recipe_item_list, parent,false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            holder.itemTxt.setText(listItem.get(position));
            if (currentItem == position){
                holder.itemTxt.setTextColor(Color.parseColor("#3468d3"));
                holder.line.setVisibility(VISIBLE);
            }else{
                holder.itemTxt.setTextColor(Color.parseColor("#E6ffffff"));
                holder.line.setVisibility(INVISIBLE);
            }
            final String tag = listTag.get(position);
            holder.itemShow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickRecipeItemListener!=null){
                        onClickRecipeItemListener.onItemSelect(tag,position);
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return listItem.size();
        }

        class ItemViewHolder  extends RecyclerView.ViewHolder{
            RelativeLayout itemShow;
            ImageView line;
            TextView itemTxt;
             public ItemViewHolder(View itemView) {
                 super(itemView);
                 itemShow = (RelativeLayout) itemView.findViewById(R.id.item_show);
                 line = (ImageView) itemView.findViewById(R.id.line);
                 itemTxt = (TextView) itemView.findViewById(R.id.item_txt);
             }
        }


        private OnClickRecipeItem onClickRecipeItemListener;

        protected void setOnClickRecipeItemListener(OnClickRecipeItem onClickRecipeItem){
            this.onClickRecipeItemListener = onClickRecipeItem;
        }
    }


}
