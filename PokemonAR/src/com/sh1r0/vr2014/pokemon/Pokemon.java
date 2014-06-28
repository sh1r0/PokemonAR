package com.sh1r0.vr2014.pokemon;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import android.media.MediaPlayer;

import rajawali.Object3D;
import rajawali.animation.Animation;
import rajawali.animation.Animation3D;
import rajawali.animation.AnimationGroup;
import rajawali.animation.IAnimationListener;
import rajawali.animation.RotateOnAxisAnimation;
import rajawali.animation.ScaleAnimation3D;
import rajawali.animation.TranslateAnimation3D;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.math.vector.Vector3.Axis;
import rajawali.scene.RajawaliScene;

public class Pokemon {
	private static final int HAPPINESS_THRESHOLD = 5;

	private RajawaliScene mScene;
	private Object3D mModel;
	private Pokemon mEvolved;
	private AnimationGroup evolveAnim;
	private Animation3D feedAnim;
	private AtomicBoolean isEvolved;
	private AtomicBoolean isEvolving;
	private AtomicBoolean isFeeding;
	private AtomicInteger happiness;
	private TimerTask tmTask;
	private Vector3 origScale;
	private MediaPlayer sound;

	public Pokemon(RajawaliScene scene, Object3D model, Pokemon evolved) {
		this(scene, model, evolved, null);
	}
	
	public Pokemon(RajawaliScene scene, Object3D model, Pokemon evolved, MediaPlayer mp) {
		this.mScene = scene;
		this.mModel = model;
		this.origScale = model.getScale();
		this.sound = mp;
		this.feedAnim = new TranslateAnimation3D(new Vector3(0, 0, 0));
		this.isEvolved = new AtomicBoolean();
		this.isEvolving = new AtomicBoolean();
		this.isFeeding = new AtomicBoolean();

		if (evolved != null) {
			this.mEvolved = evolved;
			createEvolveAnimation();
		}

		this.happiness = new AtomicInteger();
		this.tmTask = new TimerTask() {
			@Override
			public void run() {
				if (happiness.decrementAndGet() < 0) {
					happiness.set(-1);
				}
			}
		};
		Timer tm = new Timer();
		tm.schedule(tmTask, 5000, 10000);
	}
	
	public void show(Vector3 position, Quaternion orientation) {
		this.show(position, orientation, false);
	}

	public void show(Vector3 position, Quaternion orientation, boolean interactive) {
		if (isEvolved.get()) {
			this.mEvolved.show(position, orientation, interactive);
		} else {
			if (!interactive || happiness.get() >= 0) {
				mModel.setScale(origScale);
			} else {
				mModel.setScale(origScale.multiplyAndCreate(0.5));
			}
			this.mModel.setVisible(true);
			this.mModel.setPosition(position);
			this.mModel.setOrientation(orientation);
		}
	}

	public void feed() {
		if (isEvolved.get()) {
			this.mEvolved.feed();
		} else if (!isFeeding.get() && !isEvolving.get()) {
			isFeeding.set(true);
			createFeedAnimation();
			if (sound != null && !sound.isPlaying()) {
				sound.start();
			}
			this.feedAnim.play();
		}
	}

	public void reset() {
		if (this.mEvolved != null) {
			this.mEvolved.reset();
			this.evolveAnim.reset();
		}

		isEvolved.set(false);
		isEvolving.set(false);
		isFeeding.set(false);
		happiness.set(0);
	}

	private void evolve() {
		if (mEvolved == null)
			return;

		isEvolving.set(true);
		this.evolveAnim.play();
	}

	private void createFeedAnimation() {
		mScene.unregisterAnimation(this.feedAnim);
		Animation3D anim = new TranslateAnimation3D(this.mModel.getPosition(), this.mModel.getPosition().addAndCreate(
				0, 0, 200));
		anim.setDurationMilliseconds(200);
		anim.setRepeatCount(3);
		anim.setRepeatMode(Animation3D.RepeatMode.REVERSE);
		anim.setTransformable3D(this.mModel);
		anim.registerListener(new IAnimationListener() {
			@Override
			public void onAnimationUpdate(Animation animation, double interpolatedTime) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (happiness.incrementAndGet() >= HAPPINESS_THRESHOLD) {
					evolve();
				}
				isFeeding.set(false);
			}
		});
		this.feedAnim = anim;
		mScene.registerAnimation(this.feedAnim);
	}

	private void createEvolveAnimation() {
		if (evolveAnim != null) {
			return;
		}

		this.evolveAnim = new AnimationGroup();

		Vector3 fromScale = this.mModel.getScale();
		Vector3 toScale = fromScale.multiplyAndCreate(2.0);
		Animation3D anim = new ScaleAnimation3D(fromScale, toScale);
		anim.setDurationMilliseconds(400);
		anim.setTransformable3D(this.mModel);
		anim.setRepeatCount(3);
		anim.setRepeatMode(Animation3D.RepeatMode.REVERSE);
		this.evolveAnim.addAnimation(anim);

		anim = new RotateOnAxisAnimation(Axis.Y, 0, 360);
		anim.setDurationMilliseconds(800);
		anim.setRepeatCount(1);
		anim.setRepeatMode(Animation3D.RepeatMode.RESTART);
		anim.setTransformable3D(this.mModel);
		this.evolveAnim.addAnimation(anim);

		this.evolveAnim.registerListener(new IAnimationListener() {
			@Override
			public void onAnimationUpdate(Animation animation, double interpolatedTime) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mEvolved.reset();
				isEvolved.set(true);
				isEvolving.set(false);
			}
		});

		this.mScene.registerAnimation(this.evolveAnim);
	}
}
