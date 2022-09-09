package com.robam.rokipad.ui.page.setting;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legent.ui.ext.BasePage;
import com.robam.rokipad.R;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/1/11.
 * PS: 使用条款页面.
 */
public class TermSheetPage extends BasePage {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_term_shett, container, false);
        return view;
    }
}
