package com.zzh.zlibs.image.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzh.zlibs.R;
import com.zzh.zlibs.image.model.FileItem;

import java.util.ArrayList;
import java.util.List;

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
public class ImageAdapter extends BaseAdapter {
    private List<FileItem> dataList;
    private Context ctx;
    private List<FileItem> selectList = new ArrayList<>();
    private int maxSelect = 9;

    public ImageAdapter(List<FileItem> dataList, Context ctx) {
        this(dataList, ctx, 9);
    }

    public ImageAdapter(List<FileItem> dataList, Context ctx, int maxSelect) {
        if (ctx == null) {
            throw new NullPointerException("--Context 不能为空--");
        }
        if (dataList == null) {
            this.dataList = new ArrayList<>();
        }
        selectList.clear();
    }

    public List<FileItem> getSelectList() {
        return selectList;
    }

    public void setDataList(List<FileItem> dataList) {
        if (dataList == null) {
            this.dataList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.dataList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.zzh_item_folder, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.zzh_tv_title.setText(dataList.get(position).getTitle());
        if (selectList.contains(getItem(position))) {
            holder.zzh_iv_select.setImageResource(R.drawable.ic_selected);
        } else {
            holder.zzh_iv_select.setImageResource(-1);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectList.contains(getItem(position))) {
                    selectList.remove(getItem(position));
                } else {
                    selectList.add(getItem(position));
                }
                notifyDataSetChanged();
            }
        };
        holder.zzh_iv_image.setOnClickListener(listener);
        holder.zzh_iv_select.setOnClickListener(listener);
        holder.zzh_tv_title.setOnClickListener(listener);
        return convertView;
    }

    class ViewHolder {
        TextView zzh_tv_title;
        ImageView zzh_iv_select;
        ImageView zzh_iv_image;

        public ViewHolder(View item) {
            zzh_iv_select = item.findViewById(R.id.zzh_iv_select);
            zzh_iv_image = item.findViewById(R.id.zzh_iv_image);
            zzh_tv_title = item.findViewById(R.id.zzh_tv_title);
        }
    }
}
