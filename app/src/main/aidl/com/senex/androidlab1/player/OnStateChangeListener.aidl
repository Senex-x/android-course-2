package com.senex.androidlab1.player;

// Declare any non-default types here with import statements

import com.senex.androidlab1.player.PlayerState;

interface OnStateChangeListener {
    void onStateChange(in PlayerState newState);
}