package com.argo_entertainment.reactiontime;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.badlogic.gdx.math.Vector2;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


//лаунчер. Настройка учетки биллинга и Google API
public class AndroidActivity extends AndroidApplication implements ActionResolver, PurchasesUpdatedListener {

	// request codes we use when invoking an external activity
	private static final int RC_UNUSED = 5001;
	private static final int RC_SIGN_IN = 9001;
	private static final int RC_LEADERBOARD_UI = 9004;
	private static final String LEADERBOARD = "CgkIh4zqnIsbEAIQAQ";

	// Client used to sign in with Google APIs
	private GoogleSignInClient signInClient;
	GoogleSignInAccount signedInAccount = null;
	private BillingClient mBillingClient;

	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//задает различные параметры конфигурации
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.useWakelock = true;

		signInClient = GoogleSignIn.getClient(this,
				GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

		mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
		mBillingClient.startConnection(new BillingClientStateListener() {
			@Override
			public void onBillingSetupFinished( int billingResponseCode) {
				if (billingResponseCode == BillingClient.BillingResponse.OK) {
					// The billing client is ready. You can query purchases here.
				}
			}
			@Override
			public void onBillingServiceDisconnected() {
				// Try to restart the connection on the next request to
				// Google Play by calling the startConnection() method.
			}
		});

		List skuList = new ArrayList<>();
		skuList.add("1200_reactcoins_inapp");
		skuList.add("200_reactcoins_inapp");
		skuList.add("400_reactcoins_inapp");
		skuList.add("800_reactcoins_inapp");
		SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
		params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
		mBillingClient.querySkuDetailsAsync(params.build(),
				new SkuDetailsResponseListener() {
					@Override
					public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
						// Process the result.
					}
				});

		initialize(new ReactionTimeClass(this), config);
	}

	private void startSignInIntent() {
		// startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
	}

	@Override
	public void onPause() {
		super.onPause();
		//startSignInIntent();
	}

	@Override
	public boolean getSignedInGPGS() {
		showPop();
		return signedInAccount != null;
	}

	@Override
	public void showPop() {
		if(signedInAccount != null) {
			GamesClient gamesClient = Games.getGamesClient(AndroidActivity.this, signedInAccount);
			gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
			gamesClient.setViewForPopups(((AndroidGraphics) AndroidActivity.this.getGraphics()).getView());
		} else loginGPGS();
	}

	@Override
	public void loginGPGS() {
		signInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							// The signed in account is stored in the task's result.
							signedInAccount = task.getResult();
							showPop();
						} else {
							Intent intent = signInClient.getSignInIntent();
							startActivityForResult(intent, RC_SIGN_IN);
						}
					}
				});
	}

	@Override
	public void silentLoginGPGS() {
		signInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							// The signed in account is stored in the task's result.
							signedInAccount = task.getResult();
							showPop();
						}
					}
				});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				// The signed in account is stored in the result.
				signedInAccount = result.getSignInAccount();
			} else {
				String message = result.getStatus().getStatusMessage();
				if (message == null || message.isEmpty()) {
					message = "Error  - " + result.getStatus();
				}
				new AlertDialog.Builder(this).setMessage(message)
						.setNeutralButton(android.R.string.ok, null).show();
			}
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		if(signedInAccount != null){
			Games.getLeaderboardsClient(this, signedInAccount)
					.submitScore(LEADERBOARD, score);
		}
	}

	@Override
	public void buyCoins(int coins) {
		BillingFlowParams flowParams = BillingFlowParams.newBuilder()
				.setSku(coins + "_reactcoins_inapp")
				.setType(BillingClient.SkuType.INAPP) // SkuType.SUB for subscription
				.build();
		int responseCode = mBillingClient.launchBillingFlow(this, flowParams);
	}

	@Override
	public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
		Gdx.app.log("purchases responseCode", responseCode + " //// ");
		if (responseCode == BillingClient.BillingResponse.OK
				&& purchases != null) {
			for (Purchase purchase : purchases) {
				Gdx.app.log("purchases", purchase.toString());

				String name[]  = purchase.getSku().split("_");
				Gdx.app.log("purchases coins", name[0]);
				Preferences prefs = Gdx.app.getPreferences("gameData");
				Integer old = prefs.getInteger("coins", 0);
				//prefs.putInteger("coins", old + 1000);
				prefs.putInteger("coins", old + Integer.getInteger(name[0]));
				prefs.flush();
			}
		} else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
			// Handle an error caused by a user cancelling the purchase flow.
		} else {
			// Handle any other error codes.
		}
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {

	}
	@Override
	public void showLeaderboard() {
		getSignedInGPGS();

		if(signedInAccount != null){
			Games.getLeaderboardsClient(this, signedInAccount)
					.getLeaderboardIntent(LEADERBOARD)
					.addOnSuccessListener(new OnSuccessListener<Intent>() {
						@Override
						public void onSuccess(Intent intent) {
							startActivityForResult(intent, RC_LEADERBOARD_UI);
						}
					});
		}
	}

	@Override
	public void getLeaderboardGPGS() {

	}

	@Override
	public void getAchievementsGPGS() {

	}

	@Override
	public Vector2 getSceenOffset() {
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			DisplayCutout displayCutout = getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
		}

		WindowManager.LayoutParams.
		WindowInsets.getDisplayCutout();*/
		return new Vector2(0, 0);
	}
}
