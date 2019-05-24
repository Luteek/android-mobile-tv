package com.fxk.android.HataTV;

import com.google.android.exoplayer2.ExoPlaybackException;

public class ExceptionTypes {

    final static public class PlayerError{
        public ExoPlaybackException player_error;
        public PlayerError(ExoPlaybackException error){
                this.player_error = error;
            }
        }

    public class InternetError{
        public Integer http_code;
        public InternetError(Integer http_code){
            this.http_code = http_code;
        }
    }
}
