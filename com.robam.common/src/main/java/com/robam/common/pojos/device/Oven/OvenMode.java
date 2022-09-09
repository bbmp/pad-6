package com.robam.common.pojos.device.Oven;

/**
 * Created by zhoudingjun on 2016/7/23.
 */
public interface OvenMode {
    String DEFAULTMODE = "无模式";
    String FASTHEAT = "快热";
    String WINDBAKED = "风焙烤";
    String BAKED = "焙烤";
    String BOTTOMHEATING = "底加热";
    String UNFREEZE = "解冻";
    String FANBAKED = "风扇烤";
    String BARBECUE = "烤烧";
    String STRONGBARBECUE = "强烤烧";
    String FASTORDERHEAT = "快速预热";
    //自动模式
    short None = 0;
    short BEEF = 1;//牛肉
    short BREAD = 2;//面包
    short BISCUITS = 3;//饼干
    short CHICKENWINGS = 4;//鸡翅
    short CAKE = 5;//蛋糕
    short PIZZA = 6;//披萨
    short GRILLEDSHRIMP = 7;//虾
    short ROASTFISH = 8;//烤鱼
    short SWEETPOTATO = 9;//红薯
    short CORN = 10;//玉米
    short STREAKYPORK = 11;//五花肉
    short VEGETABLES = 12;//蔬菜


    short KUAI_RE = 1;
    short FENG_BEI_KAO = 2;
    short BEI_KAO = 3;
    short DI_JIA_RE = 4;
    short FENG_SHAN_KAO = 6;
    short KAO_SHAO = 7;
    short QIANG_KAO_SHAO = 8;
    short KUAI_SU_YU_RE = 10;
    short JIAN_KAO = 11;
    short GUO_SHU_HONG_GAN = 12;
    short EXP = 9;
    short JIE_DONG = 5;
    short FA_JIAO = 13;
    short SHA_JUN = 14;
    short Keep_Warm = 15;

}
