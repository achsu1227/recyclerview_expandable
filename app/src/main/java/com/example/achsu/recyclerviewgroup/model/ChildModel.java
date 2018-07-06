package com.example.achsu.recyclerviewgroup.model;

public class ChildModel extends Model implements Cloneable{
    private String parentId = "";
    private int index;
    private SelectType selectType = SelectType.Multi;

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

    public SelectType getSelectType() {
        return selectType;
    }

    public void setSelectType(SelectType selectType) {
        this.selectType = selectType;
    }

    public static class Builder {
        private int type;
        private String name;
        private String value;
        private String parentId = "";
        private int index;
        private SelectType selectType = SelectType.Multi;

        public Builder(String name, String value) {
            this.type = Type.CHILD;
            this.name = name;
            this.value = value;
        }

        public Builder addParentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder addIndex(int index) {
            this.index = index;
            return this;
        }

        public Builder addSelectType(SelectType selectType) {
            this.selectType = selectType;
            return this;
        }

        public ChildModel build() {
            ChildModel childModel = new ChildModel(name, value);
            childModel.setType(Type.CHILD);
            if (parentId != null) {
                childModel.setParentId(parentId);
            }
            childModel.setIndex(index);
            childModel.setSelectType(selectType);
            return childModel;
        }
    }

    public enum SelectType {
        Multi, Single
    }
}
