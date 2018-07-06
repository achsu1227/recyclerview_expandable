package com.example.achsu.recyclerviewgroup.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.achsu.recyclerviewgroup.base.BasePresenter;
import com.example.achsu.recyclerviewgroup.contract.ItemSelectedContractor;
import com.example.achsu.recyclerviewgroup.model.ChildModel;
import com.example.achsu.recyclerviewgroup.model.Model;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class ItemSelectedPresenter implements ItemSelectedContractor.Presenter {
    public static final int DEFAULT_SIZE = 3;
    private ItemSelectedContractor.View mItemSelectedView;

    private int itemListMaxSize = DEFAULT_SIZE;
    private Map<String, LinkedHashMap<String, String>> modelMap = new HashMap<>();
    private List<Model> modelList = new ArrayList<>();

    private PublishSubject<Model> insertDataSubject = PublishSubject.create();
    private PublishSubject<Model> removeDataSubject = PublishSubject.create();

    private PublishSubject notifyChangeSubject = PublishSubject.create();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ItemSelectedPresenter(ItemSelectedContractor.View mItemSelectedView,  int size) {
        this.mItemSelectedView = mItemSelectedView;
        this.itemListMaxSize = size;
        registerPublishSubject();
    }

    public void registerPublishSubject() {
        insertDataSubject.subscribe(new Consumer<Model>() {
            @Override
            public void accept(Model model) throws Exception {
                //TODO 調整 modelList 減少.
                addData(model);
            }
        });

        removeDataSubject.subscribe(new Consumer<Model>() {
            @Override
            public void accept(Model model) throws Exception {
                //TODO 調整 modelList 減少.
                removeData(model);
            }
        });
    }

    public void sendInsertDataSubject(Model model) {
        insertDataSubject.onNext(model);
    }

    public void sendRemoveDataSubject(Model model) {
        removeDataSubject.onNext(model);
    }

    public Disposable subscribe(Consumer consumer) {
        Disposable disposable = notifyChangeSubject.subscribe(consumer);
        compositeDisposable.add(disposable);
        return disposable;
    }

    public void unSubscribe(Disposable disposable) {
        compositeDisposable.delete(disposable);
        //compositeDisposable.clear();
    }

    /**
     * 清除全部subscribe訂閱者.
     */
    public void unSubscribeAll() {
        compositeDisposable.clear();
        //compositeDisposable.clear();
    }

    private boolean checkSelectedOverSize(int size) {
        if (size > itemListMaxSize
                || size < 0) {
            mItemSelectedView.showToast("max");
            return true;
        }
        return false;
    }

    @Override
    public void notifyListChange() {
        notifyChangeSubject.onNext(new Object());
    }

    @Override
    public void addData(Model data) {
        if (!checkSelectedOverSize(modelList.size() + 1)) {
            modelList.add(data);
            if (data instanceof ChildModel) {
                ChildModel childModel = ((ChildModel)data);
                handleAddMapData(childModel);
            }

            notifyListChange();
        }
    }

    @Override
    public void removeData(Model data) {

        if (!checkSelectedOverSize(modelList.size() - 1)) {
            modelList.remove(data);
            if (data instanceof ChildModel) {
                ChildModel childModel = ((ChildModel) data);
                handleRemoveMapData(childModel);
            }

            notifyListChange();
        }

    }

    public void handleData(Model data) {
        if (!isContainData(data)) {
            addData(data);
        } else {
            removeData(data);
        }
    }

    private boolean isContainData(Model data) {
        boolean isContainData = false;
        if (modelList != null) {
            for (Model item : modelList) {
                if (item.getValue().equals(data.getValue())) {
                    isContainData = true;
                    break;
                }
            }
        }
        return isContainData;
    }

    public List<Model> getModelList() {
        return modelList;
    }

    public boolean isContainMap(ChildModel childModel) {
        boolean containResult = false;
        if (modelMap.containsKey(childModel.getParentId())) {
            LinkedHashMap dataSet = modelMap.get(childModel.getParentId());
            if (dataSet != null) {
                containResult = dataSet.containsKey(childModel.getValue());
            }
        }
        return containResult;
    }

    private void handleAddMapData(ChildModel childModel) {
        if (modelMap.containsKey(childModel.getParentId())) {
            modelMap.get(childModel.getParentId()).put(childModel.getValue(), childModel.getName());
        } else {
            modelMap.put(childModel.getParentId(), new LinkedHashMap<String, String>());
            modelMap.get(childModel.getParentId()).put(childModel.getValue(), childModel.getName());
        }
    }

    private void handleRemoveMapData(ChildModel childModel) {
        if (modelMap.containsKey(childModel.getParentId())) {
            modelMap.get(childModel.getParentId()).remove(childModel.getValue());
        } else {
            modelMap.put(childModel.getParentId(), new LinkedHashMap<String, String>());
            modelMap.get(childModel.getParentId()).remove(childModel.getValue());
        }
    }
}
