package com.xxl.sayhello.adapters;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xxl.sayhello.R;
import com.xxl.sayhello.databinding.ItemFeatureBinding;
import com.xxl.sayhello.models.FeatureItem;

import java.util.List;

public class FeatureAdapter extends BaseQuickAdapter<FeatureItem, BaseViewHolder> {

    public FeatureAdapter(List<FeatureItem> data) {
        super(R.layout.item_feature, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FeatureItem item) {
        ItemFeatureBinding binding = DataBindingUtil.bind(helper.itemView);
        if (binding != null) {
            binding.setItem(item);
            binding.executePendingBindings();
        }
    }
}