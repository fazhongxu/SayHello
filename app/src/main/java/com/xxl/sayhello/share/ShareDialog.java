package com.xxl.sayhello.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xxl.sayhello.R;

import java.util.ArrayList;
import java.util.List;

public class ShareDialog extends BottomSheetDialogFragment {

    private static final String ARG_CONTENT = "content";
    private ShareContent content;
    private OnSharePlatformSelectedListener listener;

    public interface OnSharePlatformSelectedListener {
        void onPlatformSelected(SharePlatform platform);
    }

    public static void show(Activity activity, ShareContent content, OnSharePlatformSelectedListener listener) {
        ShareDialog dialog = new ShareDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTENT, content);
        dialog.setArguments(args);
        dialog.listener = listener;
        if (activity instanceof androidx.fragment.app.FragmentActivity) {
            dialog.show(((androidx.fragment.app.FragmentActivity) activity).getSupportFragmentManager(), "ShareDialog");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.ShareDialogStyle);
        if (getArguments() != null) {
            content = getArguments().getParcelable(ARG_CONTENT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(true);
        
        // 设置底部弹出样式
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_share, container, false);
        
        GridLayout platformGrid = view.findViewById(R.id.platform_grid);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        // 初始化分享平台
        List<SharePlatform> platforms = getAvailablePlatforms();
        for (SharePlatform platform : platforms) {
            View platformView = createPlatformView(requireContext(), platform);
            platformGrid.addView(platformView);
        }

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    private List<SharePlatform> getAvailablePlatforms() {
        List<SharePlatform> platforms = new ArrayList<>();
        // 这里可以根据实际情况过滤可用的平台
        platforms.add(SharePlatform.WECHAT_FRIEND);
        platforms.add(SharePlatform.WECHAT_MOMENTS);
        platforms.add(SharePlatform.SYSTEM);
        return platforms;
    }

    private View createPlatformView(Context context, SharePlatform platform) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_share_platform, null);
        ImageView icon = view.findViewById(R.id.platform_icon);
        TextView name = view.findViewById(R.id.platform_name);

        // 这里可以设置实际的图标
        // icon.setImageResource(platform.getIconRes());
        name.setText(platform.getName());

        view.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlatformSelected(platform);
            }
            dismiss();
        });

        return view;
    }
}