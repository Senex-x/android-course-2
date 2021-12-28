// OnStateChangeListener.aidl
package com.senex.androidlab1.player;

// Declare any non-default types here with import statements

import com.senex.androidlab1.player.State;

interface OnStateChangeListener {
    void onStateChange(in State newState);
}