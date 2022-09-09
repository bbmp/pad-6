# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android\androidstudiosdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes *Annotation*
-keepattributes JavascriptInterface
-ignorewarnings

-keep com.robam.common.**{*;}
-keep com.legent.utils.speech.**{*;}
-keep com.legent.utils.sharesdk.**{*;}
-keep com.legent.utils.serialport.**{*;}
-keep com.legent.utils.qrcode.**{*;}
-keep com.mxchip.ftc_service.**{*;}
-keep com.legent.ui.**{*;}
-keep com.legent.plat.**{*;}
-keep com.legent.**{*;}

-libraryjars MobTools.jar
-libraryjars ShareSDK-Core-2.6.0.jar
-libraryjars ShareSDK-Email-2.6.0.jar
-libraryjars ShareSDK-Facebook-2.6.0.jar
-libraryjars ShareSDK-QQ-2.6.0.jar
-libraryjars ShareSDK-QZone-2.6.0.jar
-libraryjars ShareSDK-ShortMessage-2.6.0.jar
-libraryjars ShareSDK-SinaWeibo-2.6.0.jar
-libraryjars ShareSDK-TencentWeibo-2.6.0.jar
-libraryjars ShareSDK-Twitter-2.6.0.jar
-libraryjars ShareSDK-Wechat-2.6.0.jar
-libraryjars ShareSDK-Wechat-Core-2.6.0.jar
-libraryjars ShareSDK-Wechat-Favorite-2.6.0.jar
-libraryjars ShareSDK-Wechat-Moments-2.6.0.jar
-libraryjars Msc.jar



-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.sina.sso.RemoteSSO

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
# UM推送不被混淆
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

# 有赞商城

-keep class **.R$* {*;}
-keep class **.R{*;}

# Youzan SDK
-dontwarn com.youzan.sdk.***
-keep class com.youzan.sdk.**{*;}

# OkHttp
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-keep class okio.**{*;}
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn java.nio.file.*
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Image Loader
-keep class com.squareup.picasso.Picasso
-keep class com.android.volley.toolbox.Volley
-keep class com.bumptech.glide.Glide
-keep class com.nostra13.universalimageloader.core.ImageLoader
-keep class com.facebook.drawee.backends.pipeline.Fresco

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }
