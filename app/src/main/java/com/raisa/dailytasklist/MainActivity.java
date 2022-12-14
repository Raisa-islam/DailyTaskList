package com.raisa.dailytasklist;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addTaskFab;
    RecyclerView recyclerView;
    TaskListAdapter adapter;
    ArrayList<Task> list;
    DatabaseReference databaseReference;
    private TextView noTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Persons");
        addTaskFab = findViewById(R.id.idFABAddTask);
        noTasks = findViewById(R.id.NoTasks);
        recyclerView = findViewById(R.id.idRVCourses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new TaskListAdapter(this, list);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Task task = dataSnapshot.getValue(Task.class);
                    list.add(task);
                }
                adapter.notifyDataSetChanged();
                if(list.size()==0){
                    noTasks.setText("No Tasks to show");
                }
                else{
                    noTasks.setText("");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(); ;
            }
        });
    }



    void showDialog()
    {

        final Dialog dialog = new Dialog(MainActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);


        dialog.setContentView(R.layout.new_task_add);
        TimePicker timePicker = dialog.findViewById(R.id.TimePicker);

        EditText title = dialog.findViewById(R.id.idEdtTask);
        EditText description = dialog.findViewById(R.id.idEdtTaskDescription);
        Button save = dialog.findViewById(R.id.idBtnSave);
        Button cancel = dialog.findViewById(R.id.idBtnCancel);
        final int[] hour = new int[1];
        final int[] min = new int[1];

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {

            hour[0] = i;
            min[0] = i1;


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = title.getText().toString().trim();
                String Description = description.getText().toString().trim();
                int n = hour[0];
                int m = min[0];
                String hrs = Integer.toString(n);
                String min = Integer.toString(m);

                if(!TextUtils.isEmpty(Title))
                {

                    add_info(Title, Description, hrs, min);


                }
                else {

                }

                Toast.makeText(MainActivity.this, "Task Added", Toast.LENGTH_SHORT).show();
               dialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    void add_info(String title, String description, String hour, String min)
    {
        String id = databaseReference.push().getKey();
        Task task = new Task(id, title, description);
        databaseReference.child(id).setValue(task);
    }
}