package com.example.javaprojectgrocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    static ListView listview;
    static ArrayList<String> items;

    //static ArrayAdapter<String> adapter;

    static ListViewAdapter adapter;
    EditText input;
    ImageView enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = findViewById(R.id.list);
        items = new ArrayList<>();
        input = findViewById(R.id.get_item);
        enter = findViewById(R.id.add_pic);
        items.add("Apple");
        items.add("Banana");
        items.add("Orange");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item_name = items.get(i);
                makeToast(item_name);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                makeToast("Removed " + items.get(i));
                removeItem(i);
                return false;
            }
        });

//        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
        adapter = new ListViewAdapter(getApplicationContext(), items);
        listview.setAdapter(adapter);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entered_text = input.getText().toString();
                if(entered_text == null || entered_text.length() == 0){
                    makeToast("Please enter text");
                }
                else{
                    addItem(entered_text);
                    input.setText("");
                    makeToast("Added " + entered_text + " successfully");
                }
            }
        });
        loadcontent();
    }

    public void loadcontent(){
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, "list.txt");
        byte[] content = new byte[(int) readFrom.length()];

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(readFrom);
            stream.read(content);

            String info = new String(content);
            info = info.substring(1, info.length() - 1);
            String split[] = info.split(", ");
            items = new ArrayList<>(Arrays.asList(split));
            adapter = new ListViewAdapter(this,items);
            listview.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, "list.txt"));
            writer.write(items.toString().getBytes());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public static void addItem(String user_input){
        items.add(user_input);
        listview.setAdapter(adapter);
    }

    public static void removeItem(int gone){
        items.remove(gone);
        listview.setAdapter(adapter);
    }
    Toast box;



    public void makeToast(String words) {
        if (box != null) box.cancel();
        box = Toast.makeText(getApplicationContext(),words, Toast.LENGTH_SHORT);
        box.show();
    }

    private static boolean quantity(String item_name){ //not used
        String trimmedItemName = item_name.trim().toLowerCase();
        for(String item : items){
            String trimmedItem = item.trim().toLowerCase();
            if(trimmedItemName.equals(trimmedItem)){
                return true;
            }
        }
        return false;
    }


}

// app:srcCompat="@android:drawable/ic_menu_add"