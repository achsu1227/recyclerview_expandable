package com.example.achsu.recyclerviewgroup.model;

import java.util.ArrayList;
import java.util.List;

public class GroupModel<T extends ChildModel> extends Model {

    private List<T> list = new ArrayList();
    private boolean isExpandable = false;

    public GroupModel(String name, String value) {
        super(Type.GROUP, name, value);
    }

    private void setListIndex(List<T> list) {
        if (list != null) {
            for (int index = 0; index < list.size(); index++) {
                T child = list.get(index);
                if (child != null) {
                    child.setIndex(index);
                    child.setParentId(getValue());
                }
            }
        }
    }

    public void setList(List<T> list) {
        setListIndex(list);
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
