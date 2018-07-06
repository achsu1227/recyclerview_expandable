package com.example.achsu.recyclerviewgroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.achsu.recyclerviewgroup.contract.ItemSelectedContractor;
import com.example.achsu.recyclerviewgroup.model.ChildModel;
import com.example.achsu.recyclerviewgroup.model.GroupModel;
import com.example.achsu.recyclerviewgroup.model.Model;
import com.example.achsu.recyclerviewgroup.presenter.ItemSelectedPresenter;

public class SampleAdapter<T extends Model> extends RecyclerView.Adapter<SampleAdapter.VH>{

    private ItemSelectedContractor.View mItemSelectedView;
    private ItemSelectedPresenter mItemSelectedPresenter;

    private ArrayList<T> list = new ArrayList<>();
    private WeakReference<Activity> activityWeakReference;

    public SampleAdapter(Activity activity, ArrayList<T> list) {
        activityWeakReference = new WeakReference<>(activity);
        this.list = list;
    }

    public SampleAdapter(Activity activity, ArrayList<T> list, ItemSelectedPresenter mItemSelectedPresenter) {
        this(activity, list);
        this.mItemSelectedPresenter = mItemSelectedPresenter;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        Activity activity = null;
        if (activityWeakReference.get() != null) {
            activity = activityWeakReference.get();
        }

        VH vh = null;
        if (viewType == GroupModel.Type.COMMON) {
            vh = new CommonViewHolder(LayoutInflater.from(activity).inflate(R.layout.layout_common, parent, false));
        } else if (viewType == GroupModel.Type.GROUP) {
            vh = new GroupViewHolder(LayoutInflater.from(activity).inflate(R.layout.layout_group, parent, false));
        } else if (viewType == GroupModel.Type.CHILD) {
            vh = new ChildViewHolder(LayoutInflater.from(activity).inflate(R.layout.layout_child, parent, false));
        } else {
            vh = new CommonViewHolder(LayoutInflater.from(activity).inflate(R.layout.layout_common, parent, false));
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (holder instanceof CommonViewHolder) {
            ((CommonViewHolder) holder).setName(list.get(position).getName());
        } else if (holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = ((GroupViewHolder) holder);
            groupViewHolder.setAdapter(this);
            groupViewHolder.bindView(list.get(position), mItemSelectedPresenter);
        } else if (holder instanceof ChildViewHolder) {
            ChildViewHolder childViewHolder = ((ChildViewHolder) holder);
            childViewHolder.bindView(list.get(position), mItemSelectedPresenter);
            childViewHolder.bindItemSelectedView(mItemSelectedView);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activityWeakReference.get() != null) {
                Activity activity = activityWeakReference.get();
                TypedValue outValue = new TypedValue();
                activity.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                holder.itemView.setForeground(activity.getDrawable(outValue.resourceId));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return list.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<T> getList() {
        return list;
    }

    public void setItemSelectedPresenter(ItemSelectedPresenter mItemSelectedPresenter) {
        this.mItemSelectedPresenter = mItemSelectedPresenter;
    }

    public void setItemSelectedView(ItemSelectedContractor.View mItemSelectedView) {
        this.mItemSelectedView = mItemSelectedView;
    }

    public abstract static class VH<T extends Model> extends RecyclerView.ViewHolder{
        public VH(View itemView) {
            super(itemView);
        }

        public abstract void bindView(T t);
    }

    public static class CommonViewHolder extends VH {
        public CommonViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(Model model) {
            setName(model.getName());
        }

        public void setName(String msg) {
            TextView nameTextView = itemView.findViewById(R.id.nameTextView);
            nameTextView.setText(msg);
        }
    }
    
    public static class GroupViewHolder extends VH implements ListUpdateCallback{
        SampleAdapter adapter;
        ItemSelectedPresenter mItemSelectedPresenter;

        public GroupViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(Model model) {
            setName(model.getName());
            setItemClickListener(model);
        }

        public void bindView(Model model, ItemSelectedPresenter mItemSelectedPresenter) {
            bindView(model);
            this.mItemSelectedPresenter = mItemSelectedPresenter;
        }

        public void setName(String msg) {
            TextView nameTextView = itemView.findViewById(R.id.nameTextView);
            nameTextView.setText(msg);
        }

        public void setAdapter(SampleAdapter adapter) {
            this.adapter = adapter;
        }

        public void setItemClickListener (final Model model) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupModel groupModel = ((GroupModel)model);
                    ArrayList<Model> list = (ArrayList<Model>) adapter.getList().clone();

                    if (!groupModel.isExpandable()) {

                        groupModel.setExpandable(true);

                        /*adapter.getList().addAll(getAdapterPosition() + 1, groupModel.getList());
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(adapter.getList(), list));
                        diffResult.dispatchUpdatesTo(GroupViewHolder.this);*/

                        for (int index = 0 ; index < groupModel.getList().size(); index++) {
                            Object childModel = groupModel.getList().get(index);
                            list = (ArrayList<Model>) adapter.getList().clone();
                            int position = getAdapterPosition() + index +1;
                            adapter.getList().add(position, childModel);
                            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(adapter.getList(), list));
                            diffResult.dispatchUpdatesTo(GroupViewHolder.this);
                        }

                    } else {


                        groupModel.setExpandable(false);

                        /*adapter.getList().removeAll(groupModel.getList());
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(adapter.getList(), list));
                        diffResult.dispatchUpdatesTo(GroupViewHolder.this);*/

                        for (int index = groupModel.getList().size() - 1 ; index >= 0; index--) {
                            Object childModel = groupModel.getList().get(index);
                            list = (ArrayList<Model>) adapter.getList().clone();
                            adapter.getList().remove(childModel);
                            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(adapter.getList(), list));
                            diffResult.dispatchUpdatesTo(GroupViewHolder.this);
                        }
                    }


                }
            });


        }

        @Override
        public void onInserted(int position, int count) {
            adapter.notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            adapter.notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            adapter.notifyItemRangeChanged(position, count, payload);
        }
    }

    public static class ChildViewHolder extends VH {
        private ItemSelectedPresenter mItemSelectedPresenter;
        private ItemSelectedContractor.View mItemSelectedView;

        public ChildViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(Model model) {
            setName(model);
            setItemClickListener(model);
        }

        public void bindView(Model model, ItemSelectedPresenter mItemSelectedPresenter) {
            this.mItemSelectedPresenter = mItemSelectedPresenter;
            bindView(model);
        }

        public void setName(Model model) {
            TextView nameTextView = itemView.findViewById(R.id.nameTextView);

            if (model instanceof ChildModel) {
                ChildModel childModel = (ChildModel) model;
                if (mItemSelectedPresenter.isContainMap(childModel)) {
                    nameTextView.setText("selected : " + model.getName());
                } else {
                    nameTextView.setText(model.getName());
                }
            }

        }

        public void bindItemSelectedView(ItemSelectedContractor.View mItemSelectedView) {
            this.mItemSelectedView = mItemSelectedView;
        }

        public void setItemClickListener (final Model model) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChildModel childModel = ((ChildModel)model);
                    childModel.setName(childModel.getName() + "1");

                    if (mItemSelectedView != null) {
                        mItemSelectedView.onItemClick(getAdapterPosition(), model);
                    }

                    setName(childModel);
                }
            });


        }
    }
}

