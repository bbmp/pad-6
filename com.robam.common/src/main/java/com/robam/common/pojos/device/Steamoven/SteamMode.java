package com.robam.common.pojos.device.Steamoven;

/**
 * Created by WZTCM on 2015/12/21.
 */
public interface SteamMode {

    short MEAT = 2;

    short SEAFOOD = 3;

    short EGG = 4;

    short CAKE = 5;

    short TIJIN = 6;

    short VEGE = 7;

    short NODDLE = 8;

    short UNFREEZE = 9;

    short CLEAN = 10;

    short STERILIZER = 11;

//新增026
    short STREAKY_PORK  = 10;
    short STEAMED_BREAD  = 11;
    short STRONG_STEAM  = 13;//强力蒸
    short STERILIZERS226  = 14;
    short NO_MODEL = 0;//无模式
    short FISH = 3;//鱼
    short MEAT026 = 10;//五花肉
    short STEAMEDBREAD = 11;//馒头
    short RICE = 12;//米饭

    //新增228
    short UnfreezeMode = 9;        //解冻
    short FermentationMode = 18;   // 发酵
    short SterilizeMode = 14;      //消毒杀菌
    short KeepWarmMode = 19;       //保温

    short FreshMode = 17;         //鲜嫩蒸
    short PabulumMode = 16;       //营养蒸
    short PowerMode = 13;         //强力蒸
    short QuicklyMode = 15;       //快蒸慢炖
    //消毒柜 常量
    short FastSterilizer = 7;     //快速杀菌

    short Descaling = 20;         //除垢
    short Dry = 21;               //干燥
}
