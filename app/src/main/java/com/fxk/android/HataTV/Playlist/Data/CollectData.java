package com.fxk.android.HataTV.Playlist.Data;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CollectData {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("service_uri")
    public String service_uri;
    @SerializedName("meta")
    public Meta meta;
    @SerializedName("epg_onair")
    public ArrayList<Epg_onair> epg_onair= new ArrayList<Epg_onair>();

    public class Meta{
        @SerializedName("lcn")
        public String lcn;
        @SerializedName("group")
        public String group;
        @SerializedName("logo")
        public String logo;
        @SerializedName("site")
        public String site;
        @SerializedName("description")
        public String description;
        @SerializedName("status")
        public String status;
        @SerializedName("bitrate")
        public String bitrate;
    }

    public class Epg_onair{
        @SerializedName("title")
        public String title;
        @SerializedName("descriprion")
        public String description;
        @SerializedName("tags")
        public List<String> tags;
        @SerializedName("start")
        private String start;
        @SerializedName("finish")
        private String finish;

        public Date getStart(){
            SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            try {
                return data.parse(start);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        public Date getFinish(){
            SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            try {
                return data.parse(finish);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        public String reformDateToString(Date str_time){
            SimpleDateFormat out = new SimpleDateFormat("HH:mm");
            return out.format(str_time);
        }
    }

}
