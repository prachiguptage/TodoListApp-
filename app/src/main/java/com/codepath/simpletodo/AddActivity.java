package com.codepath.simpletodo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    TextView tvAddDate;
    AlertDialog dialog;
    EditText etAddItem;
    TextView tvNext;
    TextView tvCancel;
    TextView tvOk;
    EditText etAddTitle;
    Spinner spAddPriority;
    CheckListAdapter clAdapter;
    RadioGroup rgAddStatus;

    int index;
    List<CheckListItem> checkListItem;
    ListView lvAddCheckList;
    DatabaseHandler database;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString(Constant.TITLE);

        database = DatabaseHandler.getInstance(this);

        checkListItem = new ArrayList<>();

        spAddPriority = (Spinner) findViewById(R.id.spAddPriority);
        setSpinner();

        etAddTitle = (EditText) findViewById(R.id.etAddTitle);
        etAddTitle.setText(title);

        tvAddDate = (TextView) findViewById(R.id.tvAddDate);
        calendar = Calendar.getInstance();
        tvAddDate.setText((calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                calendar.get(Calendar.YEAR));

        lvAddCheckList = (ListView) findViewById(R.id.lvAddCheckList);
        rgAddStatus = (RadioGroup) findViewById(R.id.rgAddStatus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        clAdapter = new CheckListAdapter(this, checkListItem);
        lvAddCheckList.setAdapter(clAdapter);
        index=-1;

        lvAddCheckList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createCheckDialog();
                index = position;
                etAddItem.setText(checkListItem.get(position).get_item_name());
            }
        });
    }

    public void openAddDatePicker(View view){
        Calendar calendar= Calendar.getInstance();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setup(calendar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvAddDate.setText((month+1)+"/"+
                        dayOfMonth+"/"+
                        year);
            }
        });
        datePickerFragment.show(getSupportFragmentManager(),null);
    }

    public void onAddCheckItem(View view){

        createCheckDialog();


    }


    private void setSpinner() {

        String[] priority = new String[]{
                Constant.PRIORITY_LOW,
                Constant.PRIORITY_MEDIUM,
                Constant.PRIORITY_HIGH
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, priority);
        spAddPriority.setAdapter(adapter);

    }

    public void createCheckDialog(){

        dialog = new AlertDialog.Builder(AddActivity.this)
                .setView(R.layout.custom_dialog_add_checklist)
                .create();
        dialog.show();

        tvNext = (TextView) dialog.findViewById(R.id.tvNext);
        tvOk = (TextView) dialog.findViewById(R.id.tvOk);
        tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        etAddItem = (EditText) dialog.findViewById(R.id.etAddItem);

    }



    public void onNext(View view) {

        String value = etAddItem.getText().toString();
        if (value.length() > 0) {
            CheckListItem item;
            if (index >= 0) {

                item = checkListItem.get(index);
                item.set_item_name(value);
                checkListItem.remove(index);
                checkListItem.add(index, item);
                index = -1;
            } else {
                item = new CheckListItem(value, Constant.UNCHECKED);
                checkListItem.add(item);
            }
            dialog.dismiss();
            onResume();
            dialog = null;
            createCheckDialog();
        } else {
            Toast.makeText(this, "Please Enter Text", Toast.LENGTH_LONG).show();
        }
    }

    public void onCancel(View view) {
        dialog.dismiss();
        dialog = null;
    }

    public void onOk(View view) {
        String value = etAddItem.getText().toString();
        if (value.length() >= 0) {
            CheckListItem item;
            if (index >= 0) {

                item = checkListItem.get(index);
                item.set_item_name(value);
                checkListItem.remove(index);
                checkListItem.add(index, item);
                index = -1;
            } else {
                item = new CheckListItem(value, Constant.UNCHECKED);
                checkListItem.add(item);
            }
            dialog.dismiss();
            onResume();
            dialog = null;
        } else {
            Toast.makeText(this, "Please Enter Text", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.iAddSave:
                save();
                break;
            case R.id.iClose:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Close Window")
                        .setMessage("Entry will not be save. Do you want to close?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(){

        String title = etAddTitle.getText().toString();
        String date = tvAddDate.getText().toString();
        int priorityLevel=0;
        String priority = spAddPriority.getSelectedItem().toString();
        priorityLevel= Utils.getPriorityInt(priority);
        String status=null;

        RadioButton rbStatus = (RadioButton)rgAddStatus.findViewById(rgAddStatus.getCheckedRadioButtonId());
        switch(rbStatus.getId()){
            case R.id.rbAddTodo:
                status = Constant.TODO;
                break;
            case R.id.rbAddDone:
                status=Constant.DONE;
                break;
        }

        if(title!=null&&title.length()>0){

            TaskEntry entry = new TaskEntry(title,date,priorityLevel,status);

            long id = database.addEntry(entry);

            if (checkListItem.size()>0){
                for (CheckListItem item:checkListItem){
                    item.set_list_id(id);
                    database.addCheckListItem(item);
                }
            }

            finish();
        }else {
            Utils.displayDialog(AddActivity.this,"Title Cannot be null");
        }


    }
}
