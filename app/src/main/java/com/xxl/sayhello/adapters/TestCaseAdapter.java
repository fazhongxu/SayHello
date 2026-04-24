package com.xxl.sayhello.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.xxl.sayhello.R;
import com.xxl.sayhello.databinding.ItemAiMakeImageCaseBinding;
import com.xxl.sayhello.models.TestCaseEntity;
import com.xxl.sayhello.widget.RoundImageView;

import java.util.List;

public class TestCaseAdapter extends RecyclerView.Adapter<TestCaseAdapter.ViewHolder> {
    private Context mContext;
    private List<TestCaseEntity> mData;
    private OnItemClickListener mItemClickListener;
    private float cornerRadius;

    public TestCaseAdapter(Context context, List<TestCaseEntity> data) {
        this.mContext = context;
        this.mData = data;
        this.cornerRadius = dp2px(10);
    }

    private float dp2px(float dp) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAiMakeImageCaseBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.item_ai_make_image_case,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestCaseEntity item = mData.get(position);
        
        if (item.getWidth() > 0 && item.getHeight() > 0) {
            int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            int itemWidth = (screenWidth - 30) / 2;
            int itemHeight = (int) (itemWidth * (float) item.getHeight() / item.getWidth());
            holder.binding.ivImage.getLayoutParams().height = itemHeight;
            holder.binding.ivImage.requestLayout();
        }
        
        holder.binding.ivImage.setCornersRadius(cornerRadius, cornerRadius, 0, 0);
        
        Glide.with(mContext)
                .load(item.getPreviewUrl())
                .placeholder(R.drawable.resources_ic_app_logo)
                .into(holder.binding.ivImage);
        holder.binding.tvTitle.setText(item.getTitle());

        holder.binding.llItemContainer.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClicked(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setData(List<TestCaseEntity> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClicked(TestCaseEntity item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemAiMakeImageCaseBinding binding;

        public ViewHolder(ItemAiMakeImageCaseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}