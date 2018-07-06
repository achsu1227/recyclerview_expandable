package com.example.achsu.recyclerviewgroup.view;

import com.example.achsu.recyclerviewgroup.base.BaseView;

public interface ItemSelectedView<T> extends BaseView {
    void onItemClick(int position, T data);
}
