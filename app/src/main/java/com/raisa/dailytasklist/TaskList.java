package com.raisa.dailytasklist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TaskList extends ArrayAdapter<Task> {
    private Activity context;
    private List<Task> taskList;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.rv_task, null, true);

        TextView title = listViewItem.findViewById(R.id.idTVTaskTitle);
        TextView description = listViewItem.findViewById(R.id.idTVTaskDescription);


        Task task = taskList.get(position);
        title.setText(task.getTitle());
        description.setText(task.getDescription());


        return listViewItem;
    }

    public TaskList(Activity context, List<Task>taskList)
    {
        super(context, R.layout.rv_task, taskList);
        this.context = context;
        this.taskList = taskList;
    }
}
