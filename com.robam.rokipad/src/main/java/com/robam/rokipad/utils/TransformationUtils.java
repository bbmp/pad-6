package com.robam.rokipad.utils;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;

/**
 * Created by 14807 on 2019/7/4.
 * PS:等比例缩放网络或本地图片
 */

public class TransformationUtils extends ImageViewTarget<Bitmap> {

    private ImageView mTargetView;

    public TransformationUtils(ImageView view) {
        super(view);
        mTargetView = view;
    }

    @Override
    protected void setResource(Bitmap resource) {
        view.setImageBitmap(resource);

        //获取原图的宽和高
        int prototypeWidth = resource.getWidth();
        int prototypeHeight = resource.getHeight();

        //获取ImageView的宽
        int mTargetViewWidth = mTargetView.getWidth();

        //计算缩放比例
        float scaling = (float) ((mTargetViewWidth * 0.1) / (float) (prototypeWidth * 0.1));

        //计算图片等比例放大后的高
        int imageViewHeight = (int) (prototypeHeight * scaling);
        ViewGroup.LayoutParams params = mTargetView.getLayoutParams();
        params.height = imageViewHeight;
        mTargetView.setLayoutParams(params);
    }
}
