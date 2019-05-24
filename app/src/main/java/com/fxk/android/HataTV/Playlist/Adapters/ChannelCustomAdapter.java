package com.fxk.android.HataTV.Playlist.Adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxk.android.HataTV.Playlist.Data.CollectData;
import com.fxk.android.HataTV.R;
import com.fxk.android.HataTV.Scenarist.Scenarist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChannelCustomAdapter extends ArrayAdapter<CollectData> {
    private Context context;
    private LayoutInflater lInflater;
    public ArrayList<CollectData> channels;
    private Scenarist scenarist;

    public ChannelCustomAdapter(Context context, ArrayList<CollectData> channels, Scenarist scene){
        super(context, 0 , channels);
        this.context = context;
        this.channels = channels;
        this.scenarist = scene;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return channels.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View contentView = view;
        CollectData channel = channels.get(i);
        Date currentTime = Calendar.getInstance().getTime();

        if (contentView == null)
            contentView = lInflater.inflate(R.layout.channel_items, viewGroup, false);


        ImageView channel_img = contentView.findViewById(R.id.img);
        TextView channel_name = contentView.findViewById(R.id.name);
        TextView programme_onair = contentView.findViewById(R.id.programme_air);
        ImageView btnProgramme = contentView.findViewById(R.id.programme_btn);


        /**обработка изображений picasso
         * обрабатывает logo = null, неверный url*/
        if(TextUtils.isEmpty(channel.meta.logo)){
            Picasso.get().cancelRequest(channel_img);
            Picasso.get().load(R.drawable.exo_edit_mode_logo).into(channel_img);
        }else Picasso.get()
                .load(Uri.parse((channel.meta.logo)))
                .placeholder(R.drawable.exo_edit_mode_logo)
                .error(R.drawable.exo_edit_mode_logo)
                .into(channel_img);

        channel_name.setText(channel.name);
        btnProgramme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scenarist.setProgrammesAdapter(channel.epg_onair);
                /** UI BLOCK */
                if(scenarist.current_scene != scenarist.list_programmes)
                    scenarist.changeScene(scenarist.list_programmes);
                else scenarist.changeScene(scenarist.list_channels);

            }
        });
        if(channel.epg_onair.size() > 0){
            btnProgramme.setVisibility(View.VISIBLE);
            for(int y = 0; y < channel.epg_onair.size(); y++){
                CollectData.Epg_onair epg = (CollectData.Epg_onair) channel.epg_onair.get(y);
                Date finish = epg.getFinish();
                if(currentTime.before(finish)){
                    programme_onair.setText(epg.title);
                    break;
                }
            }
        }
        else{ // При прогрузке элемента с заполненым View, очищаем неактуальные данные. Есть ескейпы поэлегантинее
            programme_onair.setText("");
            btnProgramme.setVisibility(View.INVISIBLE);
        }
        return contentView;
    }

    public void refreshData(ArrayList<CollectData> data){
        this.channels.clear();
        this.channels.addAll(data);
        notifyDataSetChanged();
    }
}
