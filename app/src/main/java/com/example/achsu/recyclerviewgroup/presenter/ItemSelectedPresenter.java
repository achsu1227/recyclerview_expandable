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
    public static final int DEFAULT_SIZE = 5;
    private ItemSelectedContractor.View mItemSelectedView;

    private int itemListMaxSize = DEFAULT_SIZE;
    private Map<String, LinkedHashMap<String, String>> selectedDataMap = new HashMap<>();

    private Map<String, String> selectedSingleTypeItems = new HashMap<>();

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

    private boolean checkSelectedOverSize(Model data, int size) {
        if (data instanceof ChildModel) {
            ChildModel childModel = (ChildModel) data;
            // 如果是單選通過條件
            if (ChildModel.SelectType.Single.equals(childModel.getSelectType())) {
                if (selectedDataMap.get(childModel.getParentId()) != null
                        && selectedDataMap.get(childModel.getParentId()).size() > 0) {
                    return false;
                }
            }
            // 如果有單選通過條件
            if (!selectedSingleTypeItems.isEmpty()) {
                if (selectedSingleTypeItems.containsKey(childModel.getParentId())) {
                    return false;
                }
            }
        }

        if (size > itemListMaxSize
                || size < 0) {
            mItemSelectedView.showToast("max");
            return true;
        }
        return false;
    }

    private boolean checkChildItemIsSingleType(Model data) {
        if (data instanceof ChildModel) {
            ChildModel childModel = (ChildModel) data;
            if (ChildModel.SelectType.Single.equals(childModel.getSelectType())) {
                if (selectedDataMap.get(childModel.getParentId()) != null
                        && selectedDataMap.get(childModel.getParentId()).size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void notifyListChange() {
        notifyChangeSubject.onNext(new Object());
    }

    @Override
    public void addData(Model data) {
        if (!checkSelectedOverSize(data, getDataSize() + 1)) {
            if (data instanceof ChildModel) {
                ChildModel childModel = ((ChildModel)data);
                handleAddMapData(childModel);
            }
            notifyListChange();
        }
    }

    @Override
    public void removeData(Model data) {
        if (!checkSelectedOverSize(data, getDataSize() - 1)) {
            if (data instanceof ChildModel) {
                ChildModel childModel = ((ChildModel) data);
                handleRemoveMapData(childModel);
            }
            notifyListChange();
        }
    }

    public void handleData(Model data) {
        if (data instanceof ChildModel) {
            ChildModel childModel = (ChildModel) data;
            if (!isContainMap(childModel)) {
                addData(data);
            } else {
                removeData(data);
            }
        }
    }

    private int getDataSize() {
        int size = 0;
        if (selectedDataMap != null && !selectedDataMap.isEmpty()) {
            for (LinkedHashMap<String, String> dataMap : selectedDataMap.values()) {
                if (dataMap != null) {
                    size += dataMap.size();
                }
            }
        }
        return size;
    }

    /**
     * 是否有包含在資料集內.
     * @param childModel ChildModel
     * @return boolean
     */
    public boolean isContainMap(ChildModel childModel) {
        boolean containResult = false;
        if (selectedDataMap.containsKey(childModel.getParentId())) {
            LinkedHashMap dataSet = selectedDataMap.get(childModel.getParentId());
            if (dataSet != null) {
                containResult = dataSet.containsKey(childModel.getValue());
            }
        }
        return containResult;
    }

    private void handleAddMapData(ChildModel childModel) {
        if (selectedDataMap.containsKey(childModel.getParentId())) {
            handleSingChildItem(childModel);
            selectedDataMap.get(childModel.getParentId()).put(childModel.getValue(), childModel.getName());
        } else {
            selectedDataMap.put(childModel.getParentId(), new LinkedHashMap<String, String>());
            selectedDataMap.get(childModel.getParentId()).put(childModel.getValue(), childModel.getName());
        }
    }

    private void handleSingChildItem(ChildModel childModel) {
        if (ChildModel.SelectType.Single.equals(childModel.getSelectType())) {
            selectedSingleTypeItems.put(childModel.getParentId(), childModel.getValue());
            selectedDataMap.get(childModel.getParentId()).clear();
        } else {
            if (!selectedSingleTypeItems.isEmpty()) {
                selectedDataMap.get(childModel.getParentId()).clear();
                selectedSingleTypeItems.remove(childModel.getParentId());
            }
        }
    }

    private void handleRemoveMapData(ChildModel childModel) {
        if (selectedDataMap.containsKey(childModel.getParentId())) {
            handleRemoveSelectedSingleTyoeItems(childModel);
            selectedDataMap.get(childModel.getParentId()).remove(childModel.getValue());
        } else {
            selectedDataMap.put(childModel.getParentId(), new LinkedHashMap<String, String>());
            selectedDataMap.get(childModel.getParentId()).remove(childModel.getValue());
        }
    }

    private void handleRemoveSelectedSingleTyoeItems(ChildModel childModel) {
        if (ChildModel.SelectType.Single.equals(childModel.getSelectType())) {
            selectedSingleTypeItems.remove(childModel.getParentId());
        }
    }
}
