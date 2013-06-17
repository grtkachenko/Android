package ru.tkachenko.exam.task_items;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ru.tkachenko.exam.R;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 1/16/13
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class MyListAdapter extends BaseAdapter {
    private ArrayList<MyTask> data = new ArrayList<MyTask>();
    private Context context;
    private LayoutInflater inflater;

    public MyListAdapter(Context context, ArrayList<MyTask> data) {
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int position) {
        return position;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item, parent, false);
        }
        ((TextView) view.findViewById(R.id.title)).setText(data.get(position).getTitle());
        ((TextView) view.findViewById(R.id.task)).setText(data.get(position).getTask());

        ImageView colorImage = (ImageView) view.findViewById(R.id.imagePriority);
        Bitmap bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(data.get(position).getColorPriority());
        colorImage.setImageBitmap(bitmap);
        return view;
    }
}