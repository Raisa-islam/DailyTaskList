package com.raisa.dailytasklist;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {
    Context context;

    ArrayList<Task>list;
    DatabaseReference databaseReference;


    public TaskListAdapter(Context context, ArrayList<Task> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_task, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = list.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.menuDelete:
                                delete(task.getId());
                                Toast.makeText(context, "task deleted", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menuUpdate:
                                show(task.getId(), task.getTitle(), task.getDescription());
                                Toast.makeText(context, "Tsk Updated", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, more;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.idTVTaskTitle);
            description = itemView.findViewById(R.id.idTVTaskDescription);
            more = itemView.findViewById(R.id.dot);
        }
    }
    public void delete(String personId)
    {
         databaseReference =
                FirebaseDatabase.getInstance().getReference("Persons").child(personId);
        databaseReference.removeValue();
    }

    void show(String key, String t, String des)
    {

        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);


        dialog.setContentView(R.layout.new_task_add);
        TimePicker timePicker = dialog.findViewById(R.id.TimePicker);

        EditText title = dialog.findViewById(R.id.idEdtTask);
        EditText description = dialog.findViewById(R.id.idEdtTaskDescription);
        Button save = dialog.findViewById(R.id.idBtnSave);
        Button cancel = dialog.findViewById(R.id.idBtnCancel);
        title.setText(t);
        description.setText(des);
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

                    update(Title, Description, key);


                }
                else {

                }

                //Toast.makeText(MainActivity.this, "Task Added", Toast.LENGTH_SHORT).show();
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
   public void update(String s, String m, String key)
    {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("tasks");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title",s );
        hashMap.put("description",m );
        Task task = new Task(key, s, m);
        databaseReference.setValue(task);

    }
}
