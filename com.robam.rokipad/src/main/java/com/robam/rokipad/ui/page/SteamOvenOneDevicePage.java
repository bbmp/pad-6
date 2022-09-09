package com.robam.rokipad.ui.page;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legent.ui.ext.BasePage;
import com.robam.rokipad.R;

import butterknife.ButterKnife;

/**
 * Created by Dell on 2019/2/2.
 */

public class SteamOvenOneDevicePage extends BasePage{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewroot = inflater.inflate(R.layout.steamovenone_device_page, container, false);
        ButterKnife.inject(this, viewroot);
        return viewroot;
    }
}
