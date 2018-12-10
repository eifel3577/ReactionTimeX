package com.argo_entertainment.reactiontime;

/*
	Author: ArgoMedia (Vlad Volovik)
 */
import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ReactionTimeFragment extends AndroidFragmentApplication {
	ActionResolver resolver;
	AndroidApplicationConfiguration config;

	public void init(ActionResolver resolver, AndroidApplicationConfiguration configuration){
		this.resolver = resolver;
		this.config = configuration;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return initializeForView(new ReactionTimeClass(resolver), config);
	}
}
