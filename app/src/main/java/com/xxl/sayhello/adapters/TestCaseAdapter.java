package com.xxl.sayhello.adapters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xxl.sayhello.R;
import com.xxl.sayhello.models.TestCaseEntity;
import com.xxl.sayhello.widget.RoundImageView;

import java.util.List;

public class TestCaseAdapter extends BaseQuickAdapter<TestCaseEntity, BaseViewHolder> {
    private Context mContext;
    private float cornerRadius;

    public TestCaseAdapter(Context context, List<TestCaseEntity> data) {
        super(R.layout.item_ai_make_image_case, data);
        this.mContext = context;
        this.cornerRadius = dp2px(10);
    }

    private float dp2px(float dp) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TestCaseEntity item) {
        RoundImageView ivImage = helper.getView(R.id.iv_image);

        if (item.getWidth() > 0 && item.getHeight() > 0) {
            int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            int itemWidth = (screenWidth - 30) / 2;
            int itemHeight = (int) (itemWidth * (float) item.getHeight() / item.getWidth());
            ivImage.getLayoutParams().height = itemHeight;
            ivImage.requestLayout();
        }

        ivImage.setCornersRadius(cornerRadius, cornerRadius, 0, 0);

        Glide.with(mContext)
                .load(item.getPreviewUrl())
                .placeholder(R.drawable.resources_ic_app_logo)
                .into(ivImage);

        helper.setText(R.id.tv_title, item.getTitle());
    }
}