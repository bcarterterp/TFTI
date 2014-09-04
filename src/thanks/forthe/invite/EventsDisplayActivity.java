package thanks.forthe.invite;

import java.io.ByteArrayOutputStream;

import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import thanks.forthe.text.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EventsDisplayActivity extends ActionBarActivity implements 
				NavigationDrawerFragment.NavigationDrawerCallbacks, MainMenuFragment.OnOptionSelectCallback,
				EventInfoFragment.EventSelectionCallback,EventDisplayFragment.OnNewEvent{

	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host_activity);
		
		
		final ActionBar bar = getSupportActionBar();
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			
			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				
			}
		};

		bar.addTab(
				bar.newTab()
				.setIcon(R.drawable.camera)
				.setTabListener(tabListener));
		bar.addTab(
				bar.newTab()
				.setIcon(R.drawable.friends_icon)
				.setTabListener(tabListener));
		bar.addTab(
				bar.newTab()
				.setIcon(R.drawable.chat_icon)
				.setTabListener(tabListener));
		
		bar.setDisplayShowTitleEnabled(false);
		bar.show();
		


		
		
		Parse.initialize(this, "YhQVvN059Op1smwAnikVPltmzgJtJxx7yUiw3Dpy", "NIUIst19OzMqY9KyX2SKdFZTzj5pkDMsKBQYTKwS");

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						new MainMenuFragment()).commit();
	}
	
	
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		
	}
	
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("");
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
	public void showCreated() {
		
		EventDisplayFragment  display = new EventDisplayFragment();
		Bundle args = new Bundle();
		args.putString("type", "created");
		display.setArguments(args);
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						display).addToBackStack(null).commit();
	}


	@Override
	public void showPending() {
		
		EventDisplayFragment  display = new EventDisplayFragment();
		Bundle args = new Bundle();
		args.putString("type", "pending");
		display.setArguments(args);
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						display).addToBackStack(null).commit();
		
	}


	@Override
	public void showAccepted() {
		
		EventDisplayFragment  display = new EventDisplayFragment();
		Bundle args = new Bundle();
		args.putString("type", "accepted");
		display.setArguments(args);
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						display).addToBackStack(null).commit();
		
	}


	@Override
	public void createNewEvent() {
		
		Intent intent = new Intent(EventsDisplayActivity.this,CreateEventActivity.class);
		startActivity(intent);
		
	}


	@Override
	public void showEvent(ParseObject event, String eventDate,String eventTime,boolean created) {
		// TODO Auto-generated method stub
		Bundle args = new Bundle();
		EventInfoFragment event_fragment = new EventInfoFragment();
		ParseUser person = event.getParseUser("createdBy");
		try {
			person.fetchIfNeeded();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		args.putString("user_name",person.getUsername());
		args.putString("event_date", eventDate);
		args.putString("event_time", eventTime);
		args.putString("event_name", event.getString("eventName"));
		args.putString("event_desc", event.getString("eventDescription"));
		args.putString("event_location", event.getString("eventLocation"));
		args.putBoolean("privacy", event.getBoolean("privacy"));
		args.putBoolean("created", created);
		event_fragment.setArguments(args);
		event_fragment.passEvent(event);
		
		ParseFile coverPhoto = (ParseFile)event.get("coverPhoto");
		if(coverPhoto != null){
			coverPhoto.getDataInBackground(new GetDataCallback() {

				@Override
				public void done(final byte[] photo, ParseException e) {

					if(e == null){

						MyAsyncTaskBitmap task = new MyAsyncTaskBitmap(photo);
						task.execute();

					}else{

					}

				}

			});
		}
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container, event_fragment).addToBackStack(null).commit();
	}


	@Override
	public void showEventPhotos() {
		
		Intent intent = new Intent(EventsDisplayActivity.this,PhotoViewerActivity.class);
		startActivity(intent);
		
	}


	@Override
	public void inviteFriends(ParseObject event) {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
		.beginTransaction().replace(R.id.container,
				new AddFriendsFragment()).addToBackStack(null).commit();
		
		
	}


	@Override
	public void showEventChat(ParseObject event) {
		
		EventChatFragment fragment = new EventChatFragment();
		fragment.passEvent(event);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
		.beginTransaction().replace(R.id.container,
				fragment).addToBackStack(null).commit();
		
	}
	
}
