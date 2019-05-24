package com.fxk.android.HataTV.Network;

import com.fxk.android.HataTV.Auth.UidData;
import com.fxk.android.HataTV.MVPInterface;
import com.fxk.android.HataTV.Playlist.Data.CollectData;
import com.fxk.android.HataTV.Playlist.Data.DataChannel;
import com.fxk.android.HataTV.Playlist.Data.DataProgrammes;
import com.fxk.android.HataTV.Playlist.Data.DataTypeChannel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Connection implements MVPInterface.GetConnector{
    private ServerApi api;
    private static  String URL = "";
    private UidData.RequestDataUID uid;

    public Connection(){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        api = retrofit.create(ServerApi.class);
    }

    @Override
    public void getChannelList(final OnGetChannelList onGetChannelList) {
        api.getList().enqueue(new Callback<ArrayList<DataChannel>>() {
            @Override
            public void onResponse(Call<ArrayList<DataChannel>> call, Response<ArrayList<DataChannel>> response) {
                if(response.code() == 200){
                    onGetChannelList.responseChannelList(response.body());
                }else{
                    onGetChannelList.responseErrorConnection("getChannelList", response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DataChannel>> call, Throwable t) {
                onGetChannelList.onFailureConnection("getChannelList", t);
            }
        });
    }

    @Override
    public void getCollectData(final OnGetCollectData onGetCollectData, String hashuid) {
        api.getCollectData(hashuid).enqueue(new Callback<ArrayList<CollectData>>() {
            @Override
            public void onResponse(Call<ArrayList<CollectData>> call, Response<ArrayList<CollectData>> response) {
                if(response.code() == 200)
                    onGetCollectData.responseCollectData(response.body());
                else{
                    onGetCollectData.responseErrorConnection("getCollectData",response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CollectData>> call, Throwable t) {
                    onGetCollectData.onFailureConnection("getCollectData", t);
            }
        });
    }

    @Override
    public void sendException(OnSendException onSendException, DataException data_exception) {
        api.sendException(uid.hashid, data_exception).enqueue(new Callback<DataException>() {
            @Override
            public void onResponse(Call<DataException> call, Response<DataException> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<DataException> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    @Override
    public void getProgrammes(final OnGetChannelProgrammes onGetChannelProgrammes, Integer id) {
        api.getTele(id).enqueue(new Callback<ArrayList<DataProgrammes>>() {
            @Override
            public void onResponse(Call<ArrayList<DataProgrammes>> call, Response<ArrayList<DataProgrammes>> response) {
                if(response.code() == 200){
                    onGetChannelProgrammes.responseChannelProgrammes(response.body());
                }else{
                    onGetChannelProgrammes.responseErrorConnection("getProgrammes",response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DataProgrammes>> call, Throwable t) {
                onGetChannelProgrammes.onFailureConnection("getProgrammes",t);
            }
        });
    }

    @Override
    public void getChannelTypes(final OnGetChannelTypes onGetChannelTypes) {
        api.getChannelTypes().enqueue(new Callback<ArrayList<DataTypeChannel>>() {
            @Override
            public void onResponse(Call<ArrayList<DataTypeChannel>> call, Response<ArrayList<DataTypeChannel>> response) {
                if(response.code() == 200){
                    onGetChannelTypes.responseChannelTypes(response.body());
                }else{
                    onGetChannelTypes.responseErrorConnection("getChannelTypes",response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DataTypeChannel>> call, Throwable t) {
                    onGetChannelTypes.onFailureConnection("getChannelTypes", t);
            }
        });
    }

    @Override
    public void sendUID(final OnAuth onAuth, UidData.RequestDataUID uid) {
        this.uid = uid;
        api.auth(uid).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                onAuth.getAuth(response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                onAuth.onFailureConnection("sendUID",t);
            }
        });
    }
}
