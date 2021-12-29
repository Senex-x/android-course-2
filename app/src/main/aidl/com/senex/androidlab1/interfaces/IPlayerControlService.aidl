package com.senex.androidlab1.interfaces;

import com.senex.androidlab1.models.Track;
import com.senex.androidlab1.player.OnStateChangeListener;

interface IPlayerControlService {
     Track getCurrentTrack();

     void resumeOrPauseIfCurrentOrPlayNew(long trackId);

     void resumeOrPause();

     void play(long trackId);

     void previous();

     void next();

     void stop();

     int getTrackElapsedDurationMillis();

     void subscribeForStateChange(OnStateChangeListener listener);

     void unsubscribeFromStateChange(OnStateChangeListener listener);
}
