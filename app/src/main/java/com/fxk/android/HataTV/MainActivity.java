package com.fxk.android.HataTV;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fxk.android.HataTV.Auth.UidData;
import com.fxk.android.HataTV.Network.Connection;
import com.fxk.android.HataTV.Player.SimplePlayer;
import com.fxk.android.HataTV.Playlist.Data.CollectData;
import com.fxk.android.HataTV.Playlist.Adapters.ProgrammesChannelAdapter;
import com.fxk.android.HataTV.Playlist.Adapters.TypeChannelCustomAdapter;
import com.fxk.android.HataTV.Presenter.Presenter;
import com.fxk.android.HataTV.Playlist.Adapters.ChannelCustomAdapter;
import com.fxk.android.HataTV.Scenarist.Scenarist;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MVPInterface.MainView{
    ChannelCustomAdapter channelCustomAdapter;
    TypeChannelCustomAdapter typeChannelCustomAdapter;
    ProgrammesChannelAdapter programmesChannelAdapter;

    ListView typeChannelList;
    ListView channelList;
    ListView programmesList;
    SurfaceView videoBoard;
    LinearLayout infoBoard;
    LinearLayout menuLayout;

    ImageView top_menu_button;
    ImageView channel_logo;
    TextView programme_onair;

    FrameLayout video_frame;
    ImageView btnRatio;
    Integer ratio_flag;

    ImageView btnTypes;
    TextView group_text;

    Presenter present;
    Scenarist scenarist;
    SimplePlayer player;
    private Player.DefaultEventListener playerEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        videoBoard = findViewById(R.id.videoBoard);
        typeChannelList = findViewById(R.id.listTypeChannel);
        channelList = findViewById(R.id.listChannel);
        programmesList = findViewById(R.id.listProgrammes);
        infoBoard = findViewById(R.id.infoBoard);
        menuLayout = findViewById(R.id.menuLayout);
        btnTypes = findViewById(R.id.types_btn);
        group_text = findViewById(R.id.types_text);

        top_menu_button = findViewById(R.id.top_types_btn);
        channel_logo = findViewById(R.id.img);
        programme_onair = findViewById(R.id.programme_air);

        video_frame = findViewById(R.id.video_frame);
        btnRatio = findViewById(R.id.btn_ratio);
        ratio_flag = 0;

        // ИНИЦИАЛИЗАЦИЯ Коннектора, Плеера в Презентере
        player = new SimplePlayer(this);
        present = new Presenter(this,  new Connection(),
                                            player,
                                            new UidData(getApplicationContext()));
        scenarist = new Scenarist(this, present);
        getEventListner();

        top_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scenarist.changeScene(scenarist.list_channels);
            }
        });

        btnRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    player.setRatio();
            }
        });

        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = channelCustomAdapter.channels.get(position).service_uri;
                Uri uri = Uri.parse(url);
                present.channelClick(uri, channelCustomAdapter.channels.get(position).meta.lcn);

                Picasso.get()
                        .load(channelCustomAdapter.channels.get(position).meta.logo)
                        .placeholder(R.drawable.exo_edit_mode_logo)
                        .into(channel_logo);
                programme_onair.setText(channelCustomAdapter.channels.get(position).name);
                channelList.setEnabled(true);
                scenarist.changeScene(scenarist.list_channels);
            }
        });
        // must work
        channelList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(scenarist.current_scene == scenarist.list_programmes)
                    scenarist.changeScene(scenarist.list_channels);
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) { }
        });

        typeChannelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                group_text.setText(typeChannelCustomAdapter.types.get(i));
                present.typeClick(typeChannelCustomAdapter.types.get(i));
                scenarist.changeScene(scenarist.list_channels);
            }
        });

        videoBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scenarist.current_scene != scenarist.videoboard_full)
                    scenarist.changeScene(scenarist.videoboard_full);
                else scenarist.changeScene(scenarist.up_btn_info);
            }
        });

        btnTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scenarist.current_scene != scenarist.list_types)
                    scenarist.changeScene(scenarist.list_types);
                else scenarist.changeScene(scenarist.list_channels);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scenarist.setFullscreen();
        present.onReady();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        present.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        present.onPause();
    }

    @Override
    public void setChannelAdapter(ArrayList<CollectData> array){
        channelCustomAdapter = new ChannelCustomAdapter(getApplicationContext(), array, scenarist);
        if(channelCustomAdapter.getCount() > 0){
            channelList.setAdapter(channelCustomAdapter);
            scenarist.changeScene(scenarist.list_channels);
        }
    }

    @Override
    public void setTypeChannelAdapter(ArrayList<String> array) {
        if(typeChannelCustomAdapter == null){
            typeChannelCustomAdapter = new TypeChannelCustomAdapter(getApplicationContext(), array);
            if(typeChannelCustomAdapter.getCount() > 0)
                typeChannelList.setAdapter(typeChannelCustomAdapter);
        }else refreshDataType(array);
    }

    @Override
    public void setProgrammesAdapter(ArrayList<CollectData.Epg_onair> array) {
        programmesChannelAdapter = new ProgrammesChannelAdapter(getApplicationContext(), array);
        if(programmesChannelAdapter.getCount() > 0){
            programmesList.setAdapter(programmesChannelAdapter);
        }
    }

    @Override
    public void refreshDataChannel(ArrayList<CollectData> channelList) {
        channelCustomAdapter.refreshData(channelList);
    }

    @Override
    public void refreshDataType(ArrayList<String> typesList) {
        typeChannelCustomAdapter.refreshData(typesList);
    }

    @Override
    public void catchError(String error_type) {
        scenarist.errorHandling(error_type);
    }

    private void getEventListner(){
        playerEventListener = new Player.DefaultEventListener(){
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playbackState == Player.STATE_READY) {
                    player.applyAspectRatio();
                } else if(playWhenReady){

                } else {

                }
            }
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                super.onPlayerError(error);
                present.videoError(error);
            }
        };
        player.player.addListener(playerEventListener);
    }
}
