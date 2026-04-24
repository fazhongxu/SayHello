package com.xxl.sayhello.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxl.sayhello.R;
import com.xxl.sayhello.adapters.TestCaseAdapter;
import com.xxl.sayhello.models.TestCaseEntity;
import com.xxl.sayhello.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class TestCaseFragment extends BaseFragment {
    private TestCaseAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_case, container, false);
        initViews(view);
        initData();
        return view;
    }

    private void initViews(View view) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new TestCaseAdapter(requireContext(), new ArrayList<>());
        mAdapter.setOnItemClickListener((adapter, v, position) -> {
            TestCaseEntity item = (TestCaseEntity) adapter.getItem(position);
            showToast("Clicked: " + item.getTitle());
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        List<TestCaseEntity> data = new ArrayList<>();
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260424180508.JPG", "爆款详情图", 800, 800));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260424175612.JPG", "商品视觉大片", 800, 800));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260424175356.JPG", "手绘风", 800, 800));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260424174002.JPG", "图片注释", 800, 800));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260424173655.JPG", "商品详情", 800, 800));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260424163327.JPG", "低价冲击", 800, 800));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260424163110.JPG", "创意品宣", 800, 800));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260424163016.JPG", "配方海报", 800, 800));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260424110825.jpg", "巨型文字", 800, 800));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260423162154.jpg", "产品发布会实景", 800, 800));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260423161950.jpg", "极简中式美学", 675, 1199));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260423161214.jpg", "街头少女感", 800, 1200));
        data.add(new TestCaseEntity("http://imgcuts.lingtuo.cn/88888817_20260423155643.jpg", "手绘城市美食地图", 1080, 1080));
        mAdapter.setNewData(data);
    }
}