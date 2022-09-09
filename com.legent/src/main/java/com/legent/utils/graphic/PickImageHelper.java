package com.legent.utils.graphic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.events.ActivityResultOnPageEvent;
import com.legent.events.PageChangedEvent;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;

import java.io.File;

public class PickImageHelper {

    public final static String CAMERA_TEXT = "相机";
    public final static String ALBUM_TEXT = "相册";
    private final static boolean RETURN_DATA = false;
    private final static int RequestCode_Photo = 1;
    private final static int RequestCode_Snap = 2;
    private final static int RequestCode_CROP = 3;
    Activity act;
    PickCallback callback;

    boolean isManual = false;
    int width = 200;
    int height = 200;
    Uri uriCrop, uriSnap;
    String strCamera = CAMERA_TEXT;
    String strAlbum = ALBUM_TEXT;

    public PickImageHelper(Activity act, PickCallback callback) {
        this.act = act;
        this.callback = callback;
    }

    public void setText(String cameraText, String albumText) {
        this.strCamera = cameraText;
        this.strAlbum = albumText;
    }

    public PickImageHelper setPickSize(int width, int height) {
        this.width = width;
        this.height = height;

        return this;
    }

    public PickImageHelper setCropIsManual(boolean isManual) {
        this.isManual = isManual;
        return this;
    }

    @Subscribe
    public void onEvent(ActivityResultOnPageEvent event) {

        if (event.resultCode == 0 && event.requestCode == RequestCode_CROP) {
            ToastUtils.show(new String("图片剪裁失败，请重新获取!"));
            EventUtils.unregist(PickImageHelper.this);
            return;
        }

        int requestCode = event.requestCode;
        Intent data = event.intent;

        switch (requestCode) {
            // 如果是直接从相册获取
            case RequestCode_Photo:// 1
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case RequestCode_Snap: // 2
                if (uriSnap != null)
                    startPhotoZoom(uriSnap);
                break;
            // 取得裁剪后的图片
            case RequestCode_CROP:// 3
                if (data != null) {
                    EventUtils.unregist(PickImageHelper.this);
                    setPicToView(data);
                }
                break;
            default:
                break;

        }
    }

    public void showPickDialog(String title) {

        DialogInterface.OnClickListener dlgClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                try {
                    File cropFile = File.createTempFile("crop", ".jpg",
                            Environment.getExternalStorageDirectory());
                    uriCrop = Uri.fromFile(cropFile);
                    File snapFile = File.createTempFile("snap", ".jpg",
                            Environment.getExternalStorageDirectory());
                    uriSnap = Uri.fromFile(snapFile);
                } catch (Exception e) {
                }

                if (which == DialogInterface.BUTTON_POSITIVE) {
                    //拍照
                    Intent intent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
//                    // 下面这句指定调用相机拍照后的照片存储的路径
//                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            uriSnap);
                    act.startActivityForResult(intent,
                            RequestCode_Snap);
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    //相册
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    act.startActivityForResult(intent,
                            RequestCode_Photo);
                }
            }
        };

        AlertDialog dlg = new AlertDialog.Builder(act)
                .setTitle(title)
                .setPositiveButton(strCamera, dlgClickListener)
                .setNegativeButton(strAlbum, dlgClickListener)
                .create();

        dlg.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                EventUtils.regist(PickImageHelper.this);
                EventUtils.postEvent(new PageChangedEvent(new String("dialogshow")));
            }
        });
        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                EventUtils.postEvent(new PageChangedEvent(new String("dialogdismiss")));
            }
        });
        dlg.show();
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");

        if (!isManual) {
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", width);
            intent.putExtra("aspectY", height);

            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
        }

        // 是否返回数据
        intent.putExtra("return-data", RETURN_DATA);
        // 直接输出文件时
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCrop);

        // 是否保留比例
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);

        // JPEG 格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 关闭人脸检测
        intent.putExtra("noFaceDetection", true);

        act.startActivityForResult(intent, RequestCode_CROP);

    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    protected void setPicToView(Intent picdata) {
        // 如果 return-data = true,返回的是Bitmap
        Bitmap photo = null;
        if (RETURN_DATA) {
            photo = picdata.getExtras().getParcelable("data");
        } else {
            photo = BitmapUtils.fromUri(act, uriCrop);
        }

        if (photo != null) {
            if (isManual) {
                width = photo.getWidth();
                height = photo.getHeight();
            }

            photo = BitmapUtils.zoomBySize(photo, width, height);
            callback.onPickComplete(photo);
        }
        if (uriSnap != null) {
//            LogUtils.i("20170508", "uriSnap is  true");
            File file = new File(uriSnap.getPath());
            if (file != null && file.exists()) {
//                LogUtils.i("20170508", "file " + file.getPath() + " uriSnap " + file.getAbsolutePath());
                file.delete();
            }
        }

        if (uriCrop != null) {
//            LogUtils.i("20170508", "uriCrop is  true");
            File file = new File(uriCrop.getPath());
            if (file != null && file.exists()) {
//                LogUtils.i("20170508", "file " + file.getPath() + " uriCrop " + file.getAbsolutePath());
                file.delete();
            }
        }
    }

    public interface PickCallback {
        void onPickComplete(Bitmap bmp);
    }
}
