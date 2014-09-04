package thanks.forthe.invite;

import java.util.ArrayList;

import com.parse.ParseUser;

import thanks.forthe.text.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class CreateEventActivity extends ActionBarActivity implements
				NavigationDrawerFragment.NavigationDrawerCallbacks,
				FirstCreateEventFragment.NewEventCallback{
	
	private int year,month,day;
	private int hour,minute;
	private String event_name, event_location, desc, ampm;
	private Bitmap event_image;
	private boolean privacy;
	private ArrayList<ParseUser> users;
	
	
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host_activity);
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						new FirstCreateEventFragment()).commit();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void next_screen(int step) {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		switch (step) {
		case 2:
			fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						new SecondNewEventFragment()).addToBackStack(null).commit();
			break;
		case 3:
			LastNewEventFragment last_fragment = new LastNewEventFragment();
			Bundle args = new Bundle();
			args.putInt("year", year);
			args.putInt("month", month);
			args.putInt("day", day);
			args.putInt("hour", hour);
			args.putInt("minute", minute);
			args.putString("event_name", event_name);
			args.putString("event_location", event_location);
			args.putString("event_description", desc);
			args.putString("AMPM", ampm);
			args.putBoolean("private", privacy);
			last_fragment.setArguments(args);
			fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						last_fragment).addToBackStack(null).commit();
			break;
		default:
			break;
		}
		
		
	}

	@Override
	public void screenOneInfo(int year, int month, int day, int hour,
			int minute, String ampm, String name, String loc, String desc,
			Bitmap img,boolean privacy) {
		
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.ampm = ampm;
		event_location = loc;
		event_name = name;
		this.desc = desc;
		event_image = img;
		this.privacy = privacy;
		
	}

	@Override
	public void screenTwoInfo(ArrayList<ParseUser> names) {
		
		users = names;
		
	}

	@Override
	public Bitmap passImage() {
		
		return event_image;
		
	}

	@Override
	public ArrayList<ParseUser> passUsers() {
		// TODO Auto-generated method stub
		return users;
	}

}
