package com.example.achsu.recyclerviewgroup.model;

public class ChildModel extends Model implements Cloneable{
    private String parentId = "";
    private int index;

    public ChildModel(String name, String value) {
        super(Type.CHILD, name, value);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
