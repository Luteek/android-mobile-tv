package com.fxk.android.HataTV.Playlist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fxk.android.HataTV.R;

import java.util.ArrayList;

public class TypeChannelCustomAdapter extends ArrayAdapter<String> {
    Context context;
    private LayoutInflater lInflater;
    public ArrayList<String> types;

    public TypeChannelCustomAdapter(Context context, ArrayList<String> types){
        super(context, 0 , types);
        this.context = context;
        this.types = types;

        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = lInflater.inflate(R.layout.channel_type_intems, viewGroup, false);
        }

        String type = types.get(i);

        ((TextView) view.findViewById(R.id.type_name)).setText(type);
        return view;
    }

    public void refreshData(ArrayList<String> data){
        this.types.clear();
        this.types.addAll(data);
        notifyDataSetChanged();
    }
}
