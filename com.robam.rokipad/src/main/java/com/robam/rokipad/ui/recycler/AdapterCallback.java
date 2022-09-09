package com.robam.rokipad.ui.recycler;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/25.
 * PS: Not easy to write code, please indicate.
 */
public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
