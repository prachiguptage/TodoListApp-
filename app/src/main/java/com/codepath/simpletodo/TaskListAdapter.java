package com.codepath.simpletodo;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/*Adapter for populating Main Activity List*/

public class TaskListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<TaskEntry> taskEntry;
    private Context context;

    public TaskListAdapter(Context context, List<TaskEntry> taskEntry) {
        this.context = context;
        this.taskEntry = taskEntry;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return taskEntry.size();
    }

    @Override
    public Object getItem(int position) {
        return taskEntry.get(position);
    }

    @Override
    public long getItemId(int position) {
        return taskEntry.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Collections.sort(this.taskEntry,new SortByPriority());

        if (convertView==null){
            convertView=inflater.inflate(R.layout.custom_adapter_task_list,parent,false);

            holder=new ViewHolder();
            holder.tvCaTitle=(TextView)convertView.findViewById(R.id.tvCaTitle);
            holder.tvCaPriority=(TextView)convertView.findViewById(R.id.tvCaPriority);
            holder.tvCaStatus=(TextView)convertView.findViewById(R.id.tvCaStatus);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        TextView tvCaTitle = holder.tvCaTitle;
        TextView tvCaPriority = holder.tvCaPriority;
        TextView tvCaStatus=holder.tvCaStatus;

        TaskEntry entry = (TaskEntry) taskEntry.get(position);

        tvCaTitle.setText(entry.get_title());

        tvCaStatus.setText(entry.get_status());

        tvCaPriority.setText(Utils.getPriority(entry.get_priority()));

        switch (entry.get_priority()){
            case 3:
                tvCaPriority.setTextColor(ContextCompat.getColor(context,R.color.low_priority));
                break;
            case 2:
                tvCaPriority.setTextColor(ContextCompat.getColor(context,R.color.medium_priority));
                break;
            case 1:
                tvCaPriority.setTextColor(ContextCompat.getColor(context,R.color.high_priority));
                break;
        }

        switch (entry.get_status()){
            case Constant.DONE:
                tvCaStatus.setTextColor(ContextCompat.getColor(context,R.color.status_done));
                break;
            case Constant.TODO:
                tvCaStatus.setTextColor(ContextCompat.getColor(context,R.color.status_todo));
                break;
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView tvCaTitle;
        private TextView tvCaPriority;
        private TextView tvCaStatus;
    }
}
