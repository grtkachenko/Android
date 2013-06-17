package ru.ifmo.rain.tkachenko.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 12/25/12
 * Time: 3:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class MyListAdapter extends BaseAdapter {
    private ArrayList<Link> data = new ArrayList<Link>();
    private Context context;
    private LayoutInflater inflater;

    public MyListAdapter(Context context, ArrayList<Link> data) {
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
            view = inflater.inflate(R.layout.link, parent, false);
        }

        ((ImageView) view.findViewById(R.id.favoriteStar)).setVisibility(data.get(position).isFavorite() ? View.VISIBLE : View.INVISIBLE);
        ((TextView) view.findViewById(R.id.title)).setText(data.get(position).getTitle());
        ((TextView) view.findViewById(R.id.site)).setText(data.get(position).getSite());
        return view;
    }
}
