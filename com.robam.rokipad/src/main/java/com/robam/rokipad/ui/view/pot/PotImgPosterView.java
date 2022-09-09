package com.robam.rokipad.ui.view.pot;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.robam.rokipad.R;
import com.robam.rokipad.listener.OnItemClickListener;

import butterknife.ButterKnife;

/**
 * Created by 14807 on 2019/8/6.
 * PS:温控锅海报View
 */
public class PotImgPosterView extends FrameLayout {

    Context cx;
    View viewRoot;
    private OnItemClickListener onItemClickListener;

    public PotImgPosterView(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public PotImgPosterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public PotImgPosterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context cx, Object o) {
        this.cx = cx;
        viewRoot = LayoutInflater.from(cx).inflate(
                R.layout.pot_img_view, this, true);
        if (!viewRoot.isInEditMode()) {
            ButterKnife.inject(this, viewRoot);
        }
//        fLPotPoster.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onClick();
//                }
//            }
//        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
