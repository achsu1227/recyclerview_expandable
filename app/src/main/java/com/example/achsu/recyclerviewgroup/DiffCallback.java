package com.example.achsu.recyclerviewgroup;

import java.util.List;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.achsu.recyclerviewgroup.model.Model;

public class DiffCallback extends DiffUtil.Callback{

    List<Model> oldList;
    List<Model> newList;

    public DiffCallback(List<Model> newList, List<Model> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getType() == newList.get(newItemPosition).getType();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (getOldListSize() != getNewListSize()) {
            return false;
        }
        return oldList.get(oldItemPosition).getName().equals(oldList.get(newItemPosition).getName());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}