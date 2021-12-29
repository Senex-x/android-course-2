package com.senex.androidlab1.player;

import com.senex.androidlab1.player.PlayerState;

interface OnStateChangeListener {
    void onStateChange(in PlayerState newState);
}