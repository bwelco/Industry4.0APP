package com.interfaces;

import com.logisticsView.ObservableScrollView;

/**
 * Created by bwelco on 2016/7/1.
 */
public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
