package com.sh1r0.vr2014.pokemon;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import rajawali.Object3D;
import rajawali.lights.DirectionalLight;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.parser.LoaderOBJ;
import rajawali.vuforia.RajawaliVuforiaRenderer;
import android.content.Context;
import android.media.MediaPlayer;

public class Renderer extends RajawaliVuforiaRenderer {
	private DirectionalLight mLight;
	// private SkeletalAnimationObject3D mBob;
	// private Object3D mF22;
	// private Object3D mAndroid;

	private Pokemon pBulbasaur;
	private Pokemon pCharmander;
	private Pokemon pSquirtle;
	private Pokemon pPikachu;
	private Pokemon pRaichu;
	private Pokemon pCaterpie;
	private Pokemon pTransel;
	private Pokemon pButterfree;
	private Pokemon pRiolu;
	private Pokemon pLucario;

	private Vector3 woodPosition;
	private Quaternion woodOrientation;

	private ArrayList<Object3D> pokemonList;

	// private Animation3D caterpieFeedAnim;

	public Renderer(Context context) {
		super(context);
	}

	protected void initScene() {
		mLight = new DirectionalLight(.1f, 0, -1.0f);
		mLight.setColor(1.0f, 1.0f, 0.8f);
		mLight.setPower(1);

		getCurrentScene().addLight(mLight);

		pokemonList = new ArrayList<Object3D>(0);

		pBulbasaur = new Pokemon(getCurrentScene(), loadPokemonObj(R.raw.bulbasaur_obj, 25), null);
		pCharmander = new Pokemon(getCurrentScene(), loadPokemonObj(R.raw.charmander_obj, 50), null);
		pSquirtle = new Pokemon(getCurrentScene(), loadPokemonObj(R.raw.squirtle_obj, 50), null);

		pRaichu = new Pokemon(getCurrentScene(), loadPokemonObj(R.raw.raichu_obj, 50), null,
				MediaPlayer.create(mContext, R.raw.raichu_sound));
		pPikachu = new Pokemon(getCurrentScene(), loadPokemonObj(R.raw.pikachu_obj, 50), pRaichu,
				MediaPlayer.create(mContext, R.raw.pikachu_sound));

		pButterfree = new Pokemon(getCurrentScene(), loadPokemonObj(R.raw.butterfree_obj, 50), null,
				MediaPlayer.create(mContext, R.raw.butterfree_sound));
		pTransel = new Pokemon(getCurrentScene(), loadPokemonObj(R.raw.transel_obj, 50), pButterfree,
				MediaPlayer.create(mContext, R.raw.transel_sound));
		pCaterpie = new Pokemon(getCurrentScene(), loadPokemonObj(R.raw.caterpie_obj, 100), pTransel,
				MediaPlayer.create(mContext, R.raw.caterpie_sound));

		pLucario = new Pokemon(getCurrentScene(), loadPokemonObj(R.raw.lucario_obj, 50), null);
		pRiolu = new Pokemon(getCurrentScene(), loadPokemonObj(R.raw.rioluposed_obj, 50), pLucario);
/*
		try {
			//
			// -- Load Bob (model by Katsbits
			// http://www.katsbits.com/download/models/)
			//
			LoaderMD5Mesh meshParser = new LoaderMD5Mesh(this, R.raw.boblampclean_mesh);
			meshParser.parse();
			mBob = (SkeletalAnimationObject3D) meshParser.getParsedAnimationObject();
			mBob.setScale(2);

			LoaderMD5Anim animParser = new LoaderMD5Anim("dance", this, R.raw.boblampclean_anim);
			animParser.parse();
			mBob.setAnimationSequence((SkeletalAnimationSequence) animParser.getParsedAnimationSequence());

			getCurrentScene().addChild(mBob);

			mBob.play();
			mBob.setVisible(false);

			//
			// -- Load F22 (model by KuhnIndustries
			// http://www.blendswap.com/blends/view/67634)
			
			 GZIPInputStream gzi = new GZIPInputStream(mContext.getResources().openRawResource(R.raw.f22));
			 ObjectInputStream fis = new ObjectInputStream(gzi);
			 SerializedObject3D serializedObj = (SerializedObject3D) fis.readObject();
			 fis.close();
			
			 mF22 = new Object3D(serializedObj);
			 mF22.setScale(30);
			 getCurrentScene().addChild(mF22);
			
			 Material f22Material = new Material();
			 f22Material.enableLighting(true);
			 f22Material.setDiffuseMethod(new DiffuseMethod.Lambert());
			 f22Material.addTexture(new Texture("f22Texture", R.drawable.f22));
			 f22Material.setColorInfluence(0);
			
			 mF22.setMaterial(f22Material);

			//
			// -- Load Android
			//

			 gzi = new GZIPInputStream(mContext.getResources().openRawResource(R.raw.android));
			 fis = new ObjectInputStream(gzi);
			 serializedObj = (SerializedObject3D) fis.readObject();
			 fis.close();
			
			 mAndroid = new Object3D(serializedObj);
			 mAndroid.setScale(14);
			 getCurrentScene().addChild(mAndroid);
			
			 Material androidMaterial = new Material();
			 androidMaterial.enableLighting(true);
			 androidMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
			 androidMaterial.setSpecularMethod(new SpecularMethod.Phong());
			 mAndroid.setColor(0x00dd00);
			 mAndroid.setMaterial(androidMaterial);
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
	}

	@Override
	protected void foundImageMarker(String trackableName, Vector3 position, Quaternion orientation) {
		if (trackableName.equals("wood")) {
			woodPosition = position;
			woodOrientation = orientation;
		}

		if (trackableName.equals("rectangle")) {
			pCaterpie.show(position, orientation, true);
		}

		if (trackableName.equals("circle")) {
			pPikachu.show(position, orientation, true);
		}

		// -- also handle cylinder targets here
		// if (trackableName.equals("CylinderApp")) {
		// }

		// -- also handle multi-targets here
	}

	@Override
	protected void onButtonPressed(String trackableName, String buttonName) {
		if (trackableName.equals("circle")) {
			if (buttonName.equals("feed")) {
				pPikachu.feed();
			} else if (buttonName.equals("reset")) {
				pPikachu.reset();
			}
		} else if (trackableName.equals("rectangle")) {
			if (buttonName.equals("feed")) {
				pCaterpie.feed();
			} else if (buttonName.equals("reset")) {
				pCaterpie.reset();
			}
		} else if (trackableName.equals("wood")) {
			if (buttonName.equals("red")) {
				pCharmander.show(woodPosition, woodOrientation);
			} else if (buttonName.equals("blue")) {
				pSquirtle.show(woodPosition, woodOrientation);
			} else if (buttonName.equals("green")) {
				pBulbasaur.show(woodPosition, woodOrientation);
			} else if (buttonName.equals("yellow")) {
				pPikachu.show(woodPosition, woodOrientation);
			}
		}
	}

	@Override
	protected void foundFrameMarker(int markerId, Vector3 position, Quaternion orientation) {
		switch (markerId) {
			case 0:
				pPikachu.show(position, orientation);
				break;
			case 1:
				pRaichu.show(position, orientation);
				break;
			case 2:
				pRiolu.show(position, orientation);
				break;
			case 3:
				pLucario.show(position, orientation);
				break;
			case 4:
				pCaterpie.show(position, orientation);
				break;
			case 5:
				pTransel.show(position, orientation);
				break;
			case 6:
				pButterfree.show(position, orientation);
				break;
			case 7:
				pBulbasaur.show(position, orientation);
				break;
			case 8:
				pCharmander.show(position, orientation);
				break;
			case 9:
				pSquirtle.show(position, orientation);
				break;
			default:
				break;
		}
	}

	@Override
	public void noFrameMarkersFound() {
	}

	public void onDrawFrame(GL10 glUnused) {
		for (Object3D object : pokemonList) {
			object.setVisible(false);
		}
		// mBob.setVisible(false);
		// mF22.setVisible(false);
		// mAndroid.setVisible(false);
		super.onDrawFrame(glUnused);
	}

	private Object3D loadPokemonObj(int resourceId, double scale) {
		Object3D object = null;
		try {
			LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, resourceId);
			objParser.parse();
			object = objParser.getParsedObject();
			object.setScale(scale);
			getCurrentScene().addChild(object);
			pokemonList.add(object);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return object;
	}
}
