package ru.tkachenko.exam_helper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ru.tkachenko.exam_helper.Helpers.TaskItem;
import ru.tkachenko.exam_helper.R;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 2/16/13
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskListAdapter extends BaseAdapter {
    private ArrayList<TaskItem> data = new ArrayList<TaskItem>();
    private Context context;
    private LayoutInflater inflater;

    public TaskListAdapter(Context context, ArrayList<TaskItem> data) {
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.task, parent, false);
        }
        ((ImageView) view.findViewById(R.id.picture)).setImageBitmap(data.get(position).getBitmap());
        ((TextView) view.findViewById(R.id.comment)).setText(data.get(position).getDescription());
        return view;
    }
}
