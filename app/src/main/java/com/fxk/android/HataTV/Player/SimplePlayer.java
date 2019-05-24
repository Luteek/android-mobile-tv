package com.fxk.android.HataTV.Player;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaCodec;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.fxk.android.HataTV.MVPInterface;
import com.fxk.android.HataTV.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static android.view.Gravity.CENTER;


public class SimplePlayer implements ExoPlayer.EventListener,
        MVPInterface.Player {

    private Activity activity;
    private BandwidthMeter bandwidthMeter;
    private TrackSelection.Factory videoTrackSelectionFactory;
    private TrackSelector trackSelector;
    public SimpleExoPlayer player;
    private MediaSource videoSource;
    private DataSource.Factory dataSourceFactory;
    private Context context;
    private SurfaceView video;
    private Uri current_channel;

    TextView video_ratio;
    TextView display_ratio;
    TextView custom_ratio;

    FrameLayout video_frame;
    private Point size;
    Integer ratio_code;

    private float ratio_min;
    private float ratio_middle;
    private float ratio_high;
    private float ratio_xhigh;

    private float displayRatio;

    public SimplePlayer(Activity _activity){

        this.activity = _activity;

        ratio_min = (float) 1.1;     // 4:3
        ratio_middle = (float) 1.65;  // 16:10
        ratio_high = (float) 1.76;    // 16:9
        ratio_xhigh = (float) 1.9;      // 18:9
        ratio_code = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        displayRatio = (float) size.x / size.y;


        video_ratio = activity.findViewById(R.id.video_ratio);
        display_ratio =  activity.findViewById(R.id.display_ratio);
        custom_ratio =  activity.findViewById(R.id.custom_ratio);

        context = this.activity.getApplicationContext();
        // 1. Create a default TrackSelector
        bandwidthMeter = new DefaultBandwidthMeter();
        videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        // Bind the player to the view.
        video = activity.findViewById(R.id.videoBoard);
        player.setVideoSurfaceView(video);
        //player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);

        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "MyApplication"), null);

        videoSource = new HlsMediaSource(null, dataSourceFactory,null , null);
        video_frame = activity.findViewById(R.id.video_frame);

    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public boolean playerReadyStatus() {
        return player.getPlayWhenReady();
    }

    @Override
    public void playerPause() {
        if(playerReadyStatus()){
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void playerStop() {
        player.stop();
    }

    @Override
    public void playerPlay() {
        if(!playerReadyStatus()){
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void changeChannel(Uri uri) {
        if(uri != null){
            playerStop();
            videoSource.releaseSource();
            videoSource = new HlsMediaSource(uri, dataSourceFactory, null, null);
            player.prepare(videoSource);
            playerPlay();
            setCurrent_channel(uri);
        }
    }

    public Uri getCurrent_channel() {
        return current_channel;
    }

    private void setCurrent_channel(Uri uri){
        current_channel = uri;
    }

    public void setRatio(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        switch (ratio_code){
            case 0: //4:3
                // if aspect ratio display !4:3
                if(displayRatio >= ratio_middle){
                    params.width =  size.y / 3 * 4;
                    params.height = size.y;
                }
                break;
            case 1: //16:9
                // if aspect ratio display !16:9
                if(displayRatio < ratio_middle){
                    params.width =  size.x;
                    params.height = size.x / 16 * 9;
                }
                break;
            case 2:
                params.width =  size.x;
                params.height = size.y;
                break;
        }
        params.gravity = CENTER;
        video_frame.setLayoutParams(params);
        if(ratio_code < 2)
            ratio_code++;
        else ratio_code = 0;
    }

    public void applyAspectRatio() {
        Integer videoWidth = player.getVideoFormat().width;
        Integer videoHeight = player.getVideoFormat().height;
        float videoRatio = (float)   videoWidth / videoHeight;
        // if video aspect ratio 4:3 - change Ratio
        if(videoRatio >= ratio_min && videoRatio < ratio_high){// video 4:3
            ratio_code = 0;
            setRatio();
        }else{
            ratio_code = 1;
            setRatio();
        }

    }
}
