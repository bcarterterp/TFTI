package thanks.forthe.invite;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.FbDialog;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;

import thanks.forthe.text.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;
import android.widget.Toast;

/**This activity will be used to host user log in and account creation**/

public class UserLogin extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, LoginFragment.UserSelectionCallback{

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private ParseUser current_user;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		SharedPreferences preferences = this.getPreferences(0);
		Parse.initialize(this, "YhQVvN059Op1smwAnikVPltmzgJtJxx7yUiw3Dpy", "NIUIst19OzMqY9KyX2SKdFZTzj5pkDMsKBQYTKwS");
		ParseFacebookUtils.initialize("1501644853387300");
		current_user = ParseUser.getCurrentUser();
		//ParseFacebookUtils.getSession().closeAndClearTokenInformation();
		if(current_user != null){
			String name = preferences.getString("Username", "DefaultUserName");
			Toast.makeText(this, "Logged in as "+name, Toast.LENGTH_LONG).show();
			onUserConfirmed();
		}else{

			setContentView(R.layout.host_activity);
			
			mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
					.findFragmentById(R.id.navigation_drawer);
			mTitle = getTitle();

			// Set up the drawer.
			mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
					(DrawerLayout) findViewById(R.id.drawer_layout));

			//display the main login fragment
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					new LoginFragment()).commit();
		}
//		PackageInfo info;
//		try{
//			info = getPackageManager().getPackageInfo("thanks.forthe.text", PackageManager.GET_SIGNATURES);
//			for (Signature signature:info.signatures){
//				MessageDigest md;
//				md = MessageDigest.getInstance("SHA");
//				md.update(signature.toByteArray());
//				String something = new String(Base64.encode(md.digest(), 0));
//				Log.e("hash key", something);
//			}
//		}catch(NameNotFoundException e1){
//			Log.e("name not found", e1.toString());
//		}catch (NoSuchAlgorithmException e) {
//			Log.e("no such algorithm", e.toString());
//		}catch (Exception e) {
//			Log.e("exception", e.toString());
//		}
		
	}

	//Changes the fragment and/or activity on item selected
	@Override
	public void onNavigationDrawerItemSelected(int position) {

	}

	//Will be deleted
	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	//put action bar back when navigation bar is gone
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.user_login, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	//Method will be used to start the next activity after user is verified.
	//User verified in LoginFragment
	@Override
	public void onUserConfirmed() {
		
		//send the username to the next activity
		Intent log_user = new Intent(UserLogin.this,EventsDisplayActivity.class);
		startActivity(log_user);
		finish();
		
	}

	//Will start the fragment that allows the user to find out their user_name and password
	@Override
	public void onUserForgot() {
		// TODO Auto-generated method stub
		
	}

	//Starts the fragment to all for new users.Will call onUserConfirmed
	//after successful login
	@Override
	public void onNewUser() {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						new NewUserFragment()).addToBackStack(null).commit();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
		
	}

	@Override
	public void onFBLogin() {
		
		final ProgressDialog progress;
		
		SharedPreferences preferences = getPreferences(0);
		final SharedPreferences.Editor editor = preferences.edit();
		
		progress = ProgressDialog.show(this, "Logging In",
			    "Please wait while you are logged in!", true);
		
		ParseFacebookUtils.logIn(this, new LogInCallback() {
			
			@Override
			public void done(ParseUser user, ParseException e) {
				
				Request.newMeRequest(ParseFacebookUtils.getSession(), new GraphUserCallback() {
					
					@Override
					public void onCompleted(GraphUser fbuser, Response response) {
						if(fbuser != null){
							editor.putString("Username", fbuser.getName());
							current_user = ParseUser.getCurrentUser();
							current_user.put("username", fbuser.getName());
							editor.putBoolean("verified", true);
							editor.putBoolean("Facebook", true);
							editor.commit();
							current_user.saveInBackground();
							progress.dismiss();
							onUserConfirmed();						
						}else{
							Toast.makeText(getApplicationContext(), "NULL INFO!", Toast.LENGTH_LONG).show();
						}
						
					}
				}).executeAsync();
				
				
				
			}
			
			
		});
		
	}

}
