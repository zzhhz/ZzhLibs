package com.zzh.zlibs.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zzh.zlibs.R;
import com.zzh.zlibs.adapter.FolderAdapter;
import com.zzh.zlibs.image.model.FileItem;
import com.zzh.zlibs.image.runnable.ScanFolderRunnable;
import com.zzh.zlibs.utils.ZUtils;

/**
 * Created by Administrator.
 *
 * @date: 2019/6/17
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs
 * @since 1.0
 */
public class PopImageDialog {
    FolderAdapter adapter;
    PopupWindow window;

    private Activity ctx;

    public PopImageDialog popImageDialog(final Activity ctx, final AdapterView.OnItemClickListener onItemClickListener) {
        this.ctx = ctx;
        View view = LayoutInflater.from(ctx).inflate(R.layout.zzh_pop_image, null);
        window = new PopupWindow(view, ZUtils.getDisplayWidth(ctx), ZUtils.getDisplayHeight(ctx) * 65 / 100);
        ScanFolderRunnable runnable = new ScanFolderRunnable(ctx, null);
        adapter = new FolderAdapter(ctx, runnable.getImageFolder());
        ListView listView = view.findViewById(R.id.zzh_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
                window.dismiss();
            }
        });
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        window.setOutsideTouchable(true);
        return this;
    }

    public void setSelectPosition(int position) {
        adapter.setSelected(position);
    }

    public void showBottom(View parent) {
        window.showAtLocation(parent, Gravity.BOTTOM, 0, ZUtils.dp2px(ctx, 50));
    }

    public FileItem getFileItem(int position) {
        return adapter.getItem(position);
    }
}
