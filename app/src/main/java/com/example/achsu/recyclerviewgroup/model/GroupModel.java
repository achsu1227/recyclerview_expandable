package com.example.achsu.recyclerviewgroup.model;

import java.util.ArrayList;
import java.util.List;

public class GroupModel<T> extends Model {

    private List<T> list = new ArrayList();
    private boolean isExpandable = false;

    public GroupModel(String name, String value) {
        super(Type.GROUP, name, value);
    }


    public void setList(List<T> list) {
        this.list = list;
    }

    public void addItem(T item) {
        list.add(item);
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public List<T> getList() {
        return list;
    }


}
