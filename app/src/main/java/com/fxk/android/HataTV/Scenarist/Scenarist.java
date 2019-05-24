package com.fxk.android.HataTV.Scenarist;

import android.app.Activity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fxk.android.HataTV.MVPInterface;
import com.fxk.android.HataTV.Playlist.Adapters.ChannelCustomAdapter;
import com.fxk.android.HataTV.Playlist.Adapters.ProgrammesChannelAdapter;
import com.fxk.android.HataTV.Playlist.Adapters.TypeChannelCustomAdapter;
import com.fxk.android.HataTV.Playlist.Data.CollectData;
import com.fxk.android.HataTV.R;

import java.util.ArrayList;

public class Scenarist {
    private ProgrammesChannelAdapter programmesChannelAdapter;
    private TypeChannelCustomAdapter typeChannelAdapter;
    private ChannelCustomAdapter channelAdapter;

    private Activity activity;

    private ListView typeChannelList;
    private ListView channelList;
    private ListView programmesList;
    private SurfaceView videoBoard;
    private LinearLayout menuLayout;
    private FrameLayout topInfo;

    private boolean mMenuLayout;
    private boolean mProgrammeList;

    private LinearLayout infoBoard;
    private TextView group_text;
    private TextView exceptionText;
    private Button exceptionBtn;
    private ImageView btnTypes;
    private MVPInterface.Presenter present;

    public Scene list_channels;
    public Scene videoboard_full;
    public Scene list_types;
    public Scene list_programmes;
    public Scene info_board;
    public Scene current_scene;
    public Scene up_btn_info;

    public Scenarist(Activity activity, MVPInterface.Presenter present){
        this.activity = activity;
        this.present = present;

        mMenuLayout = false;
        mProgrammeList = false;

        topInfo =       activity.findViewById(R.id.topInfo);
        videoBoard =    activity.findViewById(R.id.videoBoard);
        typeChannelList = activity.findViewById(R.id.listTypeChannel);
        channelList = activity.findViewById(R.id.listChannel);
        programmesList = activity.findViewById(R.id.listProgrammes);
        menuLayout = activity.findViewById(R.id.menuLayout);
        group_text = activity.findViewById(R.id.types_text);

        infoBoard = activity.findViewById(R.id.infoBoard);
        exceptionText = activity.findViewById(R.id.exception_text);
        exceptionBtn = activity.findViewById(R.id.exception_button);
        btnTypes = activity.findViewById(R.id.types_btn);

        current_scene = list_channels;
        initialScens();
    }

    public void setFullscreen() {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void errorHandling(String type_error){
        switch (type_error) {
            case "PlayerTypeSourceException":
                exceptionText.setText(activity.getString(R.string.exceptNetwork));
                exceptionBtn.setText(activity.getString(R.string.exceptBtnRetry));
                setExceptionBtn(true);
                break;
            case "PlayerRenderException":
                exceptionText.setText(activity.getString(R.string.exceptPlayer));
                exceptionBtn.setText(activity.getString(R.string.exceptBtnOK));
                setExceptionBtn(false);
                break;
            case "PlayerUnexpectedException":
                exceptionText.setText(activity.getString(R.string.exceptPlayer));
                exceptionBtn.setText(activity.getString(R.string.exceptBtnOK));
                setExceptionBtn(false);
                break;
            case "SocketTimeoutException":
                exceptionText.setText(activity.getString(R.string.exceptNetwork));
                exceptionBtn.setText(activity.getString(R.string.exceptBtnRetry));
                setExceptionBtn(true);
                break;
            case "ConnectException":
                exceptionText.setText(activity.getString(R.string.exceptNetwork));
                exceptionBtn.setText(activity.getString(R.string.exceptBtnRetry));
                setExceptionBtn(true);
                break;
        }
        changeScene(info_board);
    }

    private void setExceptionBtn(Boolean reconnect){
        if(reconnect){
            exceptionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    group_text.setText("Все");
                    present.onReady();
                    changeScene(list_channels);
                }
            });
        }else{
            exceptionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeScene(list_channels);
                }
            });
        }
    }

    public void setChannelAdapter(ArrayList<CollectData> array){
        channelAdapter = new ChannelCustomAdapter(activity.getApplicationContext(), array, this);
        if(channelAdapter.getCount() > 0)
            channelList.setAdapter(channelAdapter);
    }

    public void setProgrammesAdapter(ArrayList<CollectData.Epg_onair> array) {
        programmesChannelAdapter = new ProgrammesChannelAdapter(activity.getApplicationContext(), array);
        if(programmesChannelAdapter.getCount() > 0)
            programmesList.setAdapter(programmesChannelAdapter);
    }

    public void setTypeChannelAdapter(ArrayList<String> array) {
        typeChannelAdapter = new TypeChannelCustomAdapter(activity.getApplicationContext(), array);
        if(typeChannelAdapter.getCount() > 0)
            typeChannelList.setAdapter(typeChannelAdapter);
    }

    public void changeScene(Scene future_scene){
        topInfo.setVisibility(future_scene.getTop_info());
        videoBoard.setVisibility(future_scene.getVideo_board());
        menuLayout.setVisibility(future_scene.getMenu_layout());
        btnTypes.setVisibility(future_scene.getBtn_types());
        channelList.setVisibility(future_scene.getList_channel());
        typeChannelList.setVisibility(future_scene.getList_types());
        programmesList.setVisibility(future_scene.getList_programmes());
        infoBoard.setVisibility(future_scene.getInfo_board());
        current_scene = future_scene;
    }

    public void initialScens(){
        list_channels = new Scene();
        list_channels.setVideo_board(View.VISIBLE);
        list_channels.setTop_info(View.VISIBLE);
        list_channels.setMenu_layout(View.VISIBLE);
        list_channels.setBtn_types(View.VISIBLE);
        list_channels.setList_channel(View.VISIBLE);
        list_channels.setList_types(View.INVISIBLE);
        list_channels.setList_programmes(View.INVISIBLE);
        list_channels.setInfo_board(View.INVISIBLE);
        list_channels.setBtn_exception(View.INVISIBLE);

        videoboard_full = new Scene();
        videoboard_full.setVideo_board(View.VISIBLE);
        videoboard_full.setTop_info(View.GONE);
        videoboard_full.setMenu_layout(View.INVISIBLE);
        videoboard_full.setBtn_types(View.INVISIBLE);
        videoboard_full.setList_channel(View.INVISIBLE);
        videoboard_full.setList_types(View.INVISIBLE);
        videoboard_full.setList_programmes(View.INVISIBLE);
        videoboard_full.setInfo_board(View.INVISIBLE);
        videoboard_full.setBtn_exception(View.INVISIBLE);

        list_types = new Scene();
        list_types.setVideo_board(View.VISIBLE);
        list_types.setTop_info(View.VISIBLE);
        list_types.setMenu_layout(View.VISIBLE);
        list_types.setBtn_types(View.VISIBLE);
        list_types.setList_channel(View.INVISIBLE);
        list_types.setList_types(View.VISIBLE);
        list_types.setList_programmes(View.INVISIBLE);
        list_types.setInfo_board(View.INVISIBLE);
        list_types.setBtn_exception(View.INVISIBLE);

        list_programmes = new Scene();
        list_programmes.setVideo_board(View.VISIBLE);
        list_programmes.setTop_info(View.GONE);
        list_programmes.setMenu_layout(View.VISIBLE);
        list_programmes.setBtn_types(View.VISIBLE);
        list_programmes.setList_channel(View.VISIBLE);
        list_programmes.setList_types(View.INVISIBLE);
        list_programmes.setList_programmes(View.VISIBLE);
        list_programmes.setInfo_board(View.INVISIBLE);
        list_programmes.setBtn_exception(View.INVISIBLE);

        info_board = new Scene();
        info_board.setVideo_board(View.VISIBLE);
        info_board.setTop_info(View.GONE);
        info_board.setMenu_layout(View.INVISIBLE);
        info_board.setBtn_types(View.VISIBLE);
        info_board.setList_channel(View.VISIBLE);
        info_board.setList_types(View.INVISIBLE);
        info_board.setList_programmes(View.INVISIBLE);
        info_board.setInfo_board(View.VISIBLE);
        info_board.setBtn_exception(View.VISIBLE);

        up_btn_info = new Scene();
        up_btn_info.setTop_info(View.VISIBLE);
        up_btn_info.setVideo_board(View.VISIBLE);
        up_btn_info.setMenu_layout(View.INVISIBLE);
        up_btn_info.setBtn_types(View.INVISIBLE);
        up_btn_info.setList_channel(View.INVISIBLE);
        up_btn_info.setList_types(View.INVISIBLE);
        up_btn_info.setList_programmes(View.INVISIBLE);
        up_btn_info.setInfo_board(View.INVISIBLE);
        up_btn_info.setBtn_exception(View.INVISIBLE);
    }
}
