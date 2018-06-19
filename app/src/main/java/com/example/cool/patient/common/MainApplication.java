package com.example.cool.patient.common;

import android.app.Application;
import android.content.Context;

import  com.example.cool.patient.helper.LocaleHelper;

public class MainApplication extends Application {
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
	}
}
