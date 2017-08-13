package com.codepath.simpletodo;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DetailAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<CheckListItem> checkListItems;
    private Context context;

    public DetailAdapter(Context context, List<CheckListItem> checkListItems) {
        this.context = context;
        this.checkListItems = checkListItems;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return checkListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return checkListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return checkListItems.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView==null){
            convertView=inflater.inflate(R.layout.custom_dialog_detail_list,parent,false);

            holder=new ViewHolder();
            holder.tvDetailCheckList=(TextView)convertView.findViewById(R.id.tvDetailCheckList);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        TextView tvDetailCheckList = holder.tvDetailCheckList;

        CheckListItem entry = (CheckListItem) checkListItems.get(position);
        tvDetailCheckList.setText(entry.get_item_name());
        if (entry.get_checked().equals(Constant.CHECKED)) {
            tvDetailCheckList.setPaintFlags(tvDetailCheckList.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvDetailCheckList.setPaintFlags(0);
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView tvDetailCheckList;
    }
}
