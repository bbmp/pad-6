package com.legent.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by robam on 2018/9/11.
 */

public class ListCacheUtils<T>{
    private int mSize;
    private List<T> mDst = new LinkedList<T>();
    public ListCacheUtils(int size){
        mSize = size;
    }

    public void updataDstList(boolean isTail2Add, List<T> addLs){
        if (isTail2Add){
            mDst.addAll(addLs);
            while (mDst.size() > mSize){
                mDst.remove(0);
            }
        }else {
            mDst.addAll(0,addLs);
            while (mDst.size() > mSize){
                mDst.remove(mSize);
            }
        }
    }

    public List<T> getDstList(){
        return mDst;
    }
}
