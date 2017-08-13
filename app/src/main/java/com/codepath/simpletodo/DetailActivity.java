package com.codepath.simpletodo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    DatabaseHandler handler;
    long id;

    TextView tvDetailTitle;
    TextView tvDetailDate;
    TextView tvDetailPriority;
    TextView tvDetailStatus;
    ListView lvDetailCheckList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        id = extras.getLong(Constant.ID, 0);
        handler=DatabaseHandler.getInstance(this);

        tvDetailTitle = (TextView)findViewById(R.id.tvDetailTitle);
        tvDetailDate = (TextView)findViewById(R.id.tvDetailDate);
        tvDetailPriority = (TextView)findViewById(R.id.tvDetailPriority);
        tvDetailStatus = (TextView)findViewById(R.id.tvDetailStatus);
        lvDetailCheckList = (ListView) findViewById(R.id.lvDetailCheckList);
    }

    @Override
    protected void onResume() {
        super.onResume();

        TaskEntry entry = handler.getEntry(id);
        final List<CheckListItem> checkItem = handler.checklistToList(id);

        tvDetailTitle.setText(entry.get_title());
        tvDetailDate.setText(entry.get_date());
        tvDetailPriority.setText(Utils.getPriority(entry.get_priority()));

        tvDetailStatus.setText(entry.get_status());

        TextView tvViewItem =(TextView)findViewById(R.id.tvDetailItem);

        if (checkItem.size()>0){

            tvViewItem.setVisibility(View.VISIBLE);

            final DetailAdapter adapter = new DetailAdapter(this,checkItem);
            lvDetailCheckList.setAdapter(adapter);
            lvDetailCheckList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String check = checkItem.get(position).get_checked();
                    switch (check) {
                        case Constant.CHECKED:
                            checkItem.get(position).set_checked(Constant.UNCHECKED);
                            break;
                        case Constant.UNCHECKED:
                            checkItem.get(position).set_checked(Constant.CHECKED);
                            break;
                    }
                    handler.updateCheckListItemChecked(checkItem.get(position));

                    adapter.notifyDataSetChanged();
                }
            });

        }else {

            tvViewItem.setVisibility(View.GONE);
            DetailAdapter adapter = new DetailAdapter(this,checkItem);
            lvDetailCheckList.setAdapter(adapter);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.iDetailEdit:
                Intent editIntent = new Intent(DetailActivity.this,EditActivity.class);
                editIntent.putExtra(Constant.ID,id);
                startActivity(editIntent);
                break;
            case R.id.iDetailDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Entry")
                        .setMessage("Entry will be deleted. Do you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                handler.deleteEntry(id);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            case R.id.iDetailClose:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
