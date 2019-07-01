package com.zzh.zlibs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zzh.zlibs.R;
import com.zzh.zlibs.image.model.FileItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class FolderAdapter extends BaseAdapter {
    private List<FileItem> dataList;
    private Context ctx;
    private int selected = -1;

    public FolderAdapter(Context ctx, List<FileItem> dataList) {
        this.ctx = ctx;
        if (ctx == null) {
            throw new NullPointerException("--Context 不能为空--");
        }

        if (dataList == null) {
            this.dataList = new ArrayList<>();
        }
        FileItem item = new FileItem();
        item.setTitle("全部图片");
        this.dataList.add(item);
        this.dataList.addAll(dataList);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public FileItem getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.zzh_item_folder, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.zzh_tv_title.setText(dataList.get(position).getTitle());
        if (selected == position) {
            holder.zzh_rb_folder.setChecked(true);
        } else {
            holder.zzh_rb_folder.setChecked(false);
        }


        return convertView;
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView zzh_tv_title;
        RadioButton zzh_rb_folder;

        public ViewHolder(View item) {
            zzh_rb_folder = item.findViewById(R.id.zzh_rb_folder);
            zzh_tv_title = item.findViewById(R.id.zzh_tv_title);
        }
    }
}
