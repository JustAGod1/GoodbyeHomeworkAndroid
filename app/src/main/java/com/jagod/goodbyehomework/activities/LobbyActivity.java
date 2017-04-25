package com.jagod.goodbyehomework.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jagod.goodbyhomework.R;

import java.util.ArrayList;

public class LobbyActivity extends FragmentActivity {

    private ArrayList<String> fragmentsOnBoard = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

    }



}
