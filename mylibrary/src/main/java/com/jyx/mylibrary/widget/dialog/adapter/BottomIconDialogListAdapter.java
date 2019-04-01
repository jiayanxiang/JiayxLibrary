package com.jyx.mylibrary.widget.dialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyx.mylibrary.R;
import com.jyx.mylibrary.bean.BottomIconBean;
import com.jyx.mylibrary.utils.StringUtils;

import java.util.List;

/**
 * @author jyx
 * @CTime 2019/3/6
 * @explain:
 */
public class BottomIconDialogListAdapter extends BaseAdapter {

    private Context context;
    private List<BottomIconBean> bottomIconBeanArrayList;

    public BottomIconDialogListAdapter(Context context, List<BottomIconBean> stringList) {
        this.context = context;
        this.bottomIconBeanArrayList = stringList;
    }

    @Override
    public int getCount() {
        return bottomIconBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return bottomIconBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.bottom_icon_dialog_item_view, null);
            viewHolder.item_txt = convertView.findViewById(R.id.item_txt);
            viewHolder.item_iv = convertView.findViewById(R.id.item_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BottomIconBean bottomIconBean = bottomIconBeanArrayList.get(position);
        if (!StringUtils.isObjectEmpty(bottomIconBean)) {
            viewHolder.item_txt.setText(bottomIconBean.getStrTitle());
            Glide.with(context).load(bottomIconBean.getIntegerIcon()).into(viewHolder.item_iv);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView item_txt;
        ImageView item_iv;
    }
}
