package com.fxk.android.HataTV;

import android.net.Uri;

import com.fxk.android.HataTV.Auth.UidData;
import com.fxk.android.HataTV.Network.DataException;
import com.fxk.android.HataTV.Playlist.Data.CollectData;
import com.fxk.android.HataTV.Playlist.Data.DataChannel;
import com.fxk.android.HataTV.Playlist.Data.DataProgrammes;
import com.fxk.android.HataTV.Playlist.Data.DataTypeChannel;
import com.google.android.exoplayer2.ExoPlaybackException;

import java.util.ArrayList;

public interface MVPInterface {

    interface Presenter{
        void onReady();
        void onDestroy();
        void onPause();

        void channelClick(Uri uri, String lcn);
        void typeClick(String group);

        void videoError(ExoPlaybackException error);
    }

    interface MainView{
        /** Создает адптер для ChannelListView только в том случае, если не был создан до этого.
         * В противном случае использует функцию refreshDataChannel()*/
        void setChannelAdapter(ArrayList<CollectData> channelList);
        /** Создает адаптер для ChannelTypeListView, если уже был создан ранее
         * вызывает функцию refreshDataType()*/
        void setTypeChannelAdapter(ArrayList<String> typeChannelList);
        void setProgrammesAdapter(ArrayList<CollectData.Epg_onair> programmesList);
        void refreshDataChannel(ArrayList<CollectData> channelList);
        void refreshDataType(ArrayList<String> typesList);

        void catchError(String error_type);
    }

    interface Player {
        boolean playerReadyStatus();
        void playerPause();
        void playerStop();
        void playerPlay();
        void changeChannel(Uri uri);
        Uri getCurrent_channel();
    }

    interface GetConnector{

        interface OnGetChannelList{
            void responseChannelList(ArrayList<DataChannel> channelList);
            void responseErrorConnection(String function_name, int code);
            void onFailureConnection(String function_name, Throwable t);
        }
        void getChannelList(OnGetChannelList onGetChannelList);

        interface OnGetCollectData{
            void responseCollectData(ArrayList<CollectData> collectData);
            void responseErrorConnection(String function_name, int code);
            void onFailureConnection(String function_name, Throwable t);
        }
        void getCollectData(OnGetCollectData onGetCollectData, String hashuid);

        interface OnSendException{
            void responseAnswer();
            void responseErrorConnection(String function_name, int code);
            void onFailureConnection(String function_name, Throwable t);
        }
        void sendException(OnSendException onSendException, DataException data_exception);

        interface OnGetChannelProgrammes{
            void responseChannelProgrammes(ArrayList<DataProgrammes> programmesList);
            void responseErrorConnection(String function_name, int code);
            void onFailureConnection(String function_name, Throwable t);
        }
        void getProgrammes(OnGetChannelProgrammes onGetChannelProgrammes, Integer id_channel);

        interface OnGetChannelTypes{
            void responseChannelTypes(ArrayList<DataTypeChannel> typesList);
            void responseErrorConnection(String function_name, int code);
            void onFailureConnection(String function_name, Throwable t);
        }
        void getChannelTypes(OnGetChannelTypes onGetChannelTypes);

        interface OnAuth{
            void getAuth(Integer http_code);
            void responseErrorConnection(String function_name, int code);
            void onFailureConnection(String function_name, Throwable t);
        }
        void sendUID(OnAuth onAuth,
                     UidData.RequestDataUID uid);
    }
}
