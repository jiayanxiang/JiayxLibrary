package com.jyx.mylibrary.widget.dialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jyx.mylibrary.R;

import java.util.List;

/**
 * @author jyx
 * @CTime 2019/3/6
 * @explain:
 */
public class BottomDialogListAdapter extends BaseAdapter {

    private Context context;
    private List<String> stringList;

    public BottomDialogListAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.bottom_dialog_item_view, null);
            viewHolder.item_txt = convertView.findViewById(R.id.item_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_txt.setText(stringList.get(position));
        return convertView;
    }

    public class ViewHolder {
        TextView item_txt;
    }
}
