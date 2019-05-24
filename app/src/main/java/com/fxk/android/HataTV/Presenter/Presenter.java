package com.fxk.android.HataTV.Presenter;

import android.net.Uri;
import com.fxk.android.HataTV.Auth.UidData;
import com.fxk.android.HataTV.MVPInterface;
import com.fxk.android.HataTV.Network.DataException;
import com.fxk.android.HataTV.Playlist.Data.CollectData;
import com.fxk.android.HataTV.Playlist.Data.DataChannel;
import com.fxk.android.HataTV.Playlist.Data.DataProgrammes;
import com.fxk.android.HataTV.Playlist.Data.DataTypeChannel;
import com.fxk.android.HataTV.Playlist.Parse.ParseChannelsByType;
import com.google.android.exoplayer2.ExoPlaybackException;

import java.util.ArrayList;

public class Presenter implements MVPInterface.Presenter,
                                MVPInterface.GetConnector.OnGetChannelList,
                                MVPInterface.GetConnector.OnGetChannelProgrammes,
                                MVPInterface.GetConnector.OnGetChannelTypes,
                                MVPInterface.GetConnector.OnAuth,
                                MVPInterface.GetConnector.OnGetCollectData,
                                MVPInterface.GetConnector.OnSendException
{
    private MVPInterface.MainView mainView;
    private MVPInterface.GetConnector getConnector;
    private MVPInterface.Player playerBoard;
    private UidData uid;
    private ArrayList<CollectData> collectDataArrayList;
    private ParseChannelsByType parseChannelsByType;
    private String current_id;

    public Presenter(MVPInterface.MainView mw, MVPInterface.GetConnector gc,
                     MVPInterface.Player pb, UidData uid){
        this.mainView = mw;
        this.getConnector = gc;
        this.playerBoard = pb;
        this.uid = uid;
        current_id = "";
    }

    @Override
    public void onReady() {
        if(uid.getHASH_UID() != null){
            getConnector.sendUID(this, uid.uid);
            getConnector.getCollectData(this, uid.uid.hashid);
        }
    }

    @Override
    public void onPause() {
        try{
            playerBoard.playerPause();
        }catch (Exception e){
            DataException data = new DataException();
            data.meta.function_name = "Presenter.onPause";
            data.meta.error_detail = e.getMessage();
            getConnector.sendException(this, new DataException());
        }

    }

    @Override
    public void onDestroy() {
        playerBoard.playerStop();
        mainView = null;
    }

    @Override
    public void responseCollectData(ArrayList<CollectData> collectData) {
        try{
            if(collectData.size() > 0){
                // заполняем листвью каналов
                collectDataArrayList = collectData;
                mainView.setChannelAdapter(collectData);
                // заполняем листвью типов
                DataTypeChannel types = new DataTypeChannel(collectData);
                mainView.setTypeChannelAdapter(types.types);
            }
        } catch (Exception e){
            DataException data = new DataException();
            data.meta.function_name = "Presenter.responseCollectData";
            data.meta.error_detail = e.getMessage();
            getConnector.sendException(this, new DataException());
        }
    }

    @Override
    public void channelClick(Uri uri, String lcn) {
        try{
            if(collectDataArrayList != null && !current_id.equals(lcn)){
                playerBoard.changeChannel(uri);
                current_id = lcn;
            }
        }catch (Exception e){
            DataException data = new DataException();
            data.meta.function_name = "Presenter.channelClick";
            data.meta.error_detail = e.getMessage();
            getConnector.sendException(this, new DataException());
        }
    }

    @Override
    public void typeClick(String type) {
        try{
            parseChannelsByType = new ParseChannelsByType(collectDataArrayList);
            ArrayList<CollectData> result = parseChannelsByType.getChannelByTypeList(type);
            mainView.setChannelAdapter(result);
        } catch (Exception e){
            DataException data = new DataException();
            data.meta.function_name = "Presenter.typeClick";
            data.meta.error_detail = e.getMessage();
            getConnector.sendException(this, new DataException());
        }
    }

    @Override
    public void responseChannelList(ArrayList<DataChannel> channelList) {

    }
    @Override
    public void responseChannelProgrammes(ArrayList<DataProgrammes> programmesList) {

    }
    @Override
    public void responseChannelTypes(ArrayList<DataTypeChannel> typesList) {

    }

    @Override
    public void videoError(ExoPlaybackException error) {
        switch (error.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                mainView.catchError("PlayerTypeSourceException");
                break;
            case ExoPlaybackException.TYPE_RENDERER:
                mainView.catchError("PlayerRenderException");
                break;
            case ExoPlaybackException.TYPE_UNEXPECTED:
                mainView.catchError("PlayerUnexpectedException");
                break;
        }
    }

    @Override
    public void getAuth(Integer code){

    }

    @Override
    public void responseAnswer() {

    }

    @Override
    public void responseErrorConnection(String function_name , int code) {
        DataException data = new DataException();
        data.meta.function_name = function_name;
        data.meta.error_detail = String.valueOf(code);
        getConnector.sendException(this, data);
        mainView.catchError("ConnectException");
    }

    @Override
    public void onFailureConnection(String function_name , Throwable t) {
        DataException data = new DataException();
        data.meta.function_name = function_name;
        data.meta.error_detail =  t.getMessage();
        getConnector.sendException(this, data);
        if(mainView != null){
            mainView.catchError("ConnectException");
        }
    }
}
