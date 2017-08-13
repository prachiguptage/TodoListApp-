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

import java.util.Calendar;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    long id;
    DatabaseHandler handler;
    TaskEntry entry;
    List<CheckListItem> checkList;

    EditText etEditTitle;
    TextView tvEditDate;
    Spinner spEditPriority;
    RadioGroup rgEditStatus;
    RadioButton rbEditTodo;
    RadioButton rbEditDone;
    ListView lvEditCheckList;
    AlertDialog dialog;
    EditText etAddItem;
    TextView tvNext;
    TextView tvCancel;
    TextView tvOk;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        handler = DatabaseHandler.getInstance(this);

        Bundle extras = getIntent().getExtras();
        id = extras.getLong(Constant.ID);

        etEditTitle = (EditText) findViewById(R.id.etEditTitle);
        tvEditDate = (TextView) findViewById(R.id.tvEditDate);
        spEditPriority = (Spinner) findViewById(R.id.spEditPriority);
        rgEditStatus = (RadioGroup) findViewById(R.id.rgEditStatus);
        rbEditTodo = (RadioButton) findViewById(R.id.rbEditTodo);
        rbEditDone = (RadioButton) findViewById(R.id.rbEditDone);
        lvEditCheckList = (ListView) findViewById(R.id.lvEditCheckList);

        spEditPriority = (Spinner) findViewById(R.id.spEditPriority);
        String[] priority = new String[]{
                Constant.PRIORITY_LOW,
                Constant.PRIORITY_MEDIUM,
                Constant.PRIORITY_HIGH
        };

        ArrayAdapter<String > aadapter= new ArrayAdapter<String>(this,
                R.layout.spinner_item,priority);
        spEditPriority.setAdapter(aadapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        entry = handler.getEntry(id);
        checkList = handler.checklistToList(id);

        etEditTitle.setText(entry.get_title());
        tvEditDate.setText(entry.get_date());

        spEditPriority.setSelection(3-entry.get_priority());

        switch (entry.get_status()) {
            case Constant.TODO:
                rbEditTodo.setChecked(true);
                break;
            case Constant.DONE:
                rbEditDone.setChecked(true);
                break;
        }

        if (checkList.size() > 0) {
            CheckListAdapter adapter = new CheckListAdapter(this, checkList);
            lvEditCheckList.setAdapter(adapter);

            lvEditCheckList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    createCheckDialog();
                    index = position;
                    etAddItem.setText(checkList.get(position).get_item_name());
                }
            });

        }else {
            index=-1;
        }
    }

    public void editOpenDatePicker(View view){
        Calendar calendar= Calendar.getInstance();
        String[] date=entry.get_date().split("/");
        calendar.set(Calendar.YEAR,Integer.parseInt(date[2]));
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(date[1]));
        calendar.set(Calendar.MONTH,Integer.parseInt(date[0])-1);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setup(calendar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvEditDate.setText((month+1)+"/"+
                        dayOfMonth+"/"+
                        year);
            }
        });
        datePickerFragment.show(getSupportFragmentManager(),null);
    }

    public void onEditCheckItem(View view){

        createCheckDialog();


    }

    public void createCheckDialog(){

        dialog = new AlertDialog.Builder(EditActivity.this)
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

                item = checkList.get(index);
                item.set_item_name(value);
                checkList.remove(index);
                checkList.add(index, item);
                index = -1;
            } else {
                item = new CheckListItem(value, Constant.UNCHECKED);
                item.set_id(-1);
                item.set_list_id(id);
                checkList.add(item);
            }
            handler.updateCheckListItem(item);
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

                item = checkList.get(index);
                item.set_item_name(value);
                checkList.remove(index);
                checkList.add(index, item);
                index = -1;
            } else {
                item = new CheckListItem(value, Constant.UNCHECKED);
                item.set_id(-1);
                item.set_list_id(id);
                checkList.add(item);
            }
            handler.updateCheckListItem(item);
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
                        .setMessage("Changes will not be save. Do you want to close?")
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

        String title = etEditTitle.getText().toString();
        String date = tvEditDate.getText().toString();
        int priorityLevel=0;
        String priority = spEditPriority.getSelectedItem().toString();
        priorityLevel = Utils.getPriorityInt(priority);
        String status=null;

        RadioButton rbStatus = (RadioButton)rgEditStatus.findViewById(rgEditStatus.getCheckedRadioButtonId());
        switch(rbStatus.getId()){
            case R.id.rbEditTodo:
                status = Constant.TODO;
                break;
            case R.id.rbEditDone:
                status=Constant.DONE;
                break;
        }

        if(title!=null){

            TaskEntry entry = new TaskEntry(title,date,priorityLevel,status);
            entry.set_id(id);

            int id = handler.updateEntry(entry);


            finish();
        }else {
            Utils.displayDialog(EditActivity.this,"Title Cannot be null");
        }


    }
}
