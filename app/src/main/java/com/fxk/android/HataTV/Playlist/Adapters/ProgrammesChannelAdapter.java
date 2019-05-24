package com.fxk.android.HataTV.Playlist.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fxk.android.HataTV.Playlist.Data.CollectData;
import com.fxk.android.HataTV.R;

import java.util.ArrayList;
import java.util.Date;

public class ProgrammesChannelAdapter extends ArrayAdapter<CollectData.Epg_onair> {
    private Context context;
    private LayoutInflater lInflater;
    public ArrayList<CollectData.Epg_onair> programmes;

    public ProgrammesChannelAdapter(Context context, ArrayList<CollectData.Epg_onair> programmes){
        super(context, 0 , programmes);
        this.context = context;
        this.programmes = programmes;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return programmes.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = lInflater.inflate(R.layout.programmes_item, viewGroup, false);
        }

        CollectData.Epg_onair programme = programmes.get(i);

        Date res = programme.getStart();
        String foramettedTime = programme.reformDateToString(res);
        ((TextView) view.findViewById(R.id.start)).setText(foramettedTime);
        res = programme.getFinish();
//            foramettedTime = programme.reformDateToString(res);
//            ((TextView) view.findViewById(R.id.stop)).setText(foramettedTime);


        ((TextView) view.findViewById(R.id.programme_name)).setText(programme.title);
        return view;
    }


}
