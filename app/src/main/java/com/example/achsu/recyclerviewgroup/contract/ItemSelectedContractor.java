package com.example.achsu.recyclerviewgroup.contract;

import com.example.achsu.recyclerviewgroup.base.BasePresenter;
import com.example.achsu.recyclerviewgroup.base.BaseView;
import com.example.achsu.recyclerviewgroup.model.Model;

public interface ItemSelectedContractor {
    interface View extends BaseView {
        void showToast(String msg);

        void onItemClick(int position, Model data);
    }


    interface Presenter extends BasePresenter {
        void notifyListChange();

        void addData(Model data);

        void removeData(Model data);
    }
}
