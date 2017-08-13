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
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private final int REQUEST_CODE =100;

   // ArrayList<String> items;
  //  ArrayAdapter<String> itemsAdapter;
    List<TaskEntry> entry;
    ListView lvItems;
    DatabaseHandler handler;
    TaskListAdapter tlAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler =DatabaseHandler.getInstance(this);

        lvItems=(ListView)findViewById(R.id.lvItems);
//        items=new ArrayList<>();
//        readItems();
//        itemsAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1,items);
//        lvItems.setAdapter(itemsAdapter);
//        items.add("First Item");
//        items.add("Second Item");
//        setupListViewListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        entry =handler.taskListTableToList();
        tlAdapter = new TaskListAdapter(this,entry);
        lvItems.setAdapter(tlAdapter);
        setListViewListener();
    }

    private void setListViewListener(){

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Entry")
                        .setMessage("Entry will be deleted. Do you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.deleteEntry(entry.get(position).get_id());
                                entry.remove(position);
                                tlAdapter.notifyDataSetChanged();
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
                return false;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(MainActivity.this,DetailActivity.class);
                detailIntent.putExtra(Constant.ID,entry.get(position).get_id());
                startActivity(detailIntent);
            }
        });
    }

//    private void setupListViewListener(){
//        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                items.remove(position);
//                writeItems();
//                itemsAdapter.notifyDataSetChanged();
//                return true;
//            }
//        });

//        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this,EditItemActivity.class);
//                intent.putExtra("position",position);
//                intent.putExtra("text",items.get(position));
//                startActivityForResult(intent,REQUEST_CODE);
//            }
//        });
//    }

//    private void readItems(){
//        File filesDir=getFilesDir();
//        File todoFile= new File(filesDir,"todo.txt");
//        try {
//            items = new ArrayList<>(FileUtils.readLines(todoFile));
//        }catch (IOException e){
//            items=new ArrayList<>();
//        }
//    }

//    private void writeItems(){
//        File fileDir = getFilesDir();
//        File todoFile =new File(fileDir,"todo.txt");
//        try {
//            FileUtils.writeLines(todoFile,items);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }



    public void onAddItem(View view){
        EditText etNewItem=(EditText)findViewById(R.id.etNewItem);
        String itemText =etNewItem.getText().toString();
        if(itemText.length()>0) {
 //           itemsAdapter.add(itemText);
            Intent intent = new Intent(MainActivity.this,AddActivity.class);
            intent.putExtra(Constant.TITLE,itemText);
            startActivity(intent);
            etNewItem.setText("");
   //         writeItems();
        }else {
            Utils.displayDialog(MainActivity.this,"Item cannot be null.Please enter value");

        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode==RESULT_OK&&requestCode==REQUEST_CODE){
//            String text = data.getExtras().getString("text");
//            int position = data.getExtras().getInt("position");
//
// //           items.remove(position);
//  //          items.add(position,text);
//
//        }
//    }

//    private void displayDialog(){
//        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
//        builder.setMessage("Item cannot be null.Please enter value")
//            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iAdd:
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                intent.putExtra(Constant.TITLE,"");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
