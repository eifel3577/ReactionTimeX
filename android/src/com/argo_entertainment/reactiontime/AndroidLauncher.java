package com.argo_entertainment.reactiontime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

public class AndroidLauncher extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		startActivity(new Intent(AndroidLauncher.this, AndroidActivity.class));
	}
}
