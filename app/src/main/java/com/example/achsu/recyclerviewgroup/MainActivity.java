package com.example.achsu.recyclerviewgroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.achsu.recyclerviewgroup.model.ChildModel;
import com.example.achsu.recyclerviewgroup.model.GroupModel;
import com.example.achsu.recyclerviewgroup.model.Model;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolBar();
        setRecyclerView();

    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Model> list = new ArrayList();
        GroupModel<ChildModel> groupModel = new GroupModel("group1", "000");

        ArrayList<ChildModel> childModels = new ArrayList<>();
        for (int index = 0 ; index < 5; index++) {
            childModels.add(new ChildModel("child " + index, index + ""));
        }
        groupModel.setList(childModels);

        list.add(groupModel);
        list.add(new ChildModel("child 66", "66"));
        list.add(new ChildModel("child 77", "77"));
        list.add(new ChildModel("child 88", "88"));

        GroupModel<ChildModel> groupModel1 = new GroupModel("group2", "111");
        ArrayList<ChildModel> childModels1 = new ArrayList<>();
        for (int index = 0 ; index < 5; index++) {
            childModels1.add(new ChildModel("child " + index, index + ""));
        }
        groupModel1.setList(childModels1);
        list.add(groupModel1);

        list.add(new ChildModel("child 11", "11"));
        list.add(new ChildModel("child 22", "22"));
        list.add(new ChildModel("child 33", "33"));

        SampleAdapter sampleAdapter = new SampleAdapter(this, list);
        recyclerView.setAdapter(sampleAdapter);
    }
}
