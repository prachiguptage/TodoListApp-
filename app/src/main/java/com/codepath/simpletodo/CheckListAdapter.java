package com.codepath.simpletodo;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//Adapter for populating checklist

public class CheckListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<CheckListItem> addCheckList;
    private Context context;

    public CheckListAdapter(Context context, List<CheckListItem> addCheckList) {
        this.context = context;
        this.addCheckList = addCheckList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return addCheckList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return addCheckList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return addCheckList.get(position).get_id();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.custom_adapter_checklist, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tvCheckCheckItem = (TextView) convertView.findViewById(R.id.tvCheckCheckItem);
            viewHolder.ivCheckDelete = (ImageView) convertView.findViewById(R.id.ivCheckDelete);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TextView tvCheckCheckItem = viewHolder.tvCheckCheckItem;
        ImageView ivDelete = viewHolder.ivCheckDelete;
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(addCheckList.get(position).get_id()>0) {
                    DatabaseHandler.getInstance(context).
                            deleteCheckListItem(addCheckList.get(position).get_id());
                }
                addCheckList.remove(position);
                notifyDataSetChanged();
            }
        });

        CheckListItem content = (CheckListItem) getItem(position);

        tvCheckCheckItem.setText(content.get_item_name());
        if (content.get_checked().equals(Constant.CHECKED)) {
            tvCheckCheckItem.setPaintFlags(tvCheckCheckItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvCheckCheckItem.setPaintFlags(0);
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView tvCheckCheckItem;
        private ImageView ivCheckDelete;
    }
}
