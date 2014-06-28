package com.sh1r0.vr2014.pokemon;

import rajawali.util.RajLog;
import rajawali.vuforia.RajawaliVuforiaActivity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends RajawaliVuforiaActivity {
	private Renderer mRenderer;
//	private RajawaliVuforiaActivity mUILayout;
	private MediaPlayer mp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER);

		ImageView logoView = new ImageView(this);
		logoView.setImageResource(R.drawable.pikachu);
		ll.addView(logoView);
		
		mp = MediaPlayer.create(this, R.raw.pikachu_sound);
		mp.start();

		addContentView(ll, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		startVuforia();
	}

	@Override
	protected void setupTracker() {
		int result = initTracker(TRACKER_TYPE_MARKER);
		if (result == 1) {
			result = initTracker(TRACKER_TYPE_IMAGE);
			if (result == 1) {
				super.setupTracker();
			} else {
				RajLog.e("Couldn't initialize image tracker.");
			}
		} else {
			RajLog.e("Couldn't initialize marker tracker.");
		}
	}

	@Override
	protected void initApplicationAR() {
		super.initApplicationAR();

		createFrameMarker(0, "Marker0", 250, 250);
		createFrameMarker(1, "Marker1", 250, 250);
		createFrameMarker(2, "Marker2", 250, 250);
		createFrameMarker(3, "Marker3", 250, 250);
		createFrameMarker(4, "Marker4", 250, 250);
		createFrameMarker(5, "Marker5", 250, 250);
		createFrameMarker(6, "Marker6", 250, 250);
		createFrameMarker(7, "Marker7", 250, 250);
		createFrameMarker(8, "Marker8", 250, 250);
		createFrameMarker(9, "Marker9", 250, 250);

		createImageMarker("StonesAndChips.xml");
		
		createImageMarker("Wood.xml");
		setVirtualButtonOnDataSet("Wood.xml", 0, "wood", "red", -108.68f, -53.52f, -75.75f, -65.87f);
		setVirtualButtonOnDataSet("Wood.xml", 0, "wood", "blue", -45.28f, -53.52f, -12.35f, -65.87f);
		setVirtualButtonOnDataSet("Wood.xml", 0, "wood", "yellow", 14.82f, -53.52f, 47.75f, -65.87f);
		setVirtualButtonOnDataSet("Wood.xml", 0, "wood", "green", 76.57f, -53.52f, 109.50f, -65.87f);
		
		createImageMarker("pokemon.xml");
		setVirtualButtonOnDataSet("pokemon.xml", 0, "circle", "feed", -50f, 50f, 50f, -50f);
		setVirtualButtonOnDataSet("pokemon.xml", 0, "circle", "reset", 300f, 50f, 400f, -50f);
		setVirtualButtonOnDataSet("pokemon.xml", 1, "rectangle", "feed", -50f, 50f, 50f, -50f);
		setVirtualButtonOnDataSet("pokemon.xml", 1, "rectangle", "reset", -500f, 300f, -400f, 200f);

		// -- this is how you add a cylinder target:
		// https://developer.vuforia.com/resources/dev-guide/cylinder-targets
		// createImageMarker("sodacan.xml");

		// -- this is how you add a multi-target:
		// https://developer.vuforia.com/resources/dev-guide/multi-targets
		// createImageMarker("MyMultiTarget.xml");
	}

	@Override
	protected void initRajawali() {
		super.initRajawali();
		mRenderer = new Renderer(this);
		mRenderer.setSurfaceView(mSurfaceView);
		super.setRenderer(mRenderer);
/*
		ToggleButton extendedTrackingButton = new ToggleButton(this);
		extendedTrackingButton.setTextOn("Extended Tracking On");
		extendedTrackingButton.setTextOff("Extended Tracking Off");
		extendedTrackingButton.setChecked(false);
		extendedTrackingButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (((ToggleButton) v).isChecked()) {
					if (!startExtendedTracking())
						RajLog.e("Could not start extended tracking");
				} else {
					if (!stopExtendedTracking())
						RajLog.e("Could not stop extended tracking");
				}
			}
		});

		mUILayout = this;
		LinearLayout ll = new LinearLayout(this);
		ll.addView(extendedTrackingButton);
		mUILayout.addContentView(ll, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
*/
	}
}
