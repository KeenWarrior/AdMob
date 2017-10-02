package com.mygdx.game;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AndroidLauncher extends AndroidApplication implements AdService {

	private InterstitialAd mInterstitialAd;

	private Handler handler = new Handler();

	AdView adView;
	LinearLayout game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View v = initializeForView(new MyGdxGame(this), config);

		adView = (AdView) findViewById(R.id.adView);
		game = (LinearLayout) findViewById(R.id.game);

		game.addView(v);

		MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

		adView.loadAd(new AdRequest.Builder().build());

		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

		mInterstitialAd.loadAd(new AdRequest.Builder().build());

		mInterstitialAd.setAdListener(new AdListener(){
			@Override public void onAdClosed() {
				mInterstitialAd.loadAd(new AdRequest.Builder().build());
			}
		});
	}

	@Override public void showInterstetial() {

		handler.post(new Runnable() {
			@Override public void run() {
				if(mInterstitialAd.isLoaded()){
					mInterstitialAd.show();
				}
			}
		});



	}

	@Override public void toggleBanner() {
		final int vis = adView.getVisibility();

		handler.post(new Runnable() {
			@Override public void run() {
				switch (vis) {
					case View.VISIBLE :
						adView.setVisibility(View.INVISIBLE);
						break;
					case View.INVISIBLE :
						adView.setVisibility(View.VISIBLE);
						break;
				}
			}
		});

	}
}
