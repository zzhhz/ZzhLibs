package com.zzh.zlibs.image.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zzh.zlibs.R;
import com.zzh.zlibs.image.model.FileItem;
import com.zzh.zlibs.view.PhotoView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by ZZH on 2018/9/4.
 *
 * @Date: 2018/9/4
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 一张图片的详情
 */
public class ImageDetailAdapter extends PagerAdapter {

    private List<FileItem> mDataList;
    private Context mContext;
    private View.OnLongClickListener mOnLongClickListener;

    public void setOnLongClickListener(View.OnLongClickListener mOnLongClickListener) {
        this.mOnLongClickListener = mOnLongClickListener;
    }

    /**
     *
     */
    public ImageDetailAdapter(Context context, List<FileItem> dataList) {
        if (dataList == null) {
            mDataList = new ArrayList<>();
        } else {
            mDataList = dataList;
        }
        mContext = context;
    }

    /**
     * @param list
     */
    public void addAll(List<FileItem> list) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        if (list == null || list.isEmpty()) {
            return;
        }
        mDataList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.zzh_item_image_detail, container, false);
        PhotoView zpv = inflate.findViewById(R.id.zzh_pv);
        Glide.with(zpv).load(get(position).getPath()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(zpv);
        container.addView(inflate);
        return inflate;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public FileItem get(int position) {
        return mDataList.get(position);
    }
}
