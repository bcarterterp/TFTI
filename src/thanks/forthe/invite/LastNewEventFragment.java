package thanks.forthe.invite;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import thanks.forthe.invite.FirstCreateEventFragment.NewEventCallback;
import thanks.forthe.text.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LastNewEventFragment extends Fragment implements OnClickListener{
	
	NewEventCallback onCallback;
	private int year,month,day;
	private int hour,minute;
	private String event_name, event_location, desc, ampm;
	private Bitmap event_image;
	private boolean privacy;
	private ArrayList<ParseUser> users;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
		year = args.getInt("year");
		month = args.getInt("month");
		day = args.getInt("day");
		hour = args.getInt("hour");
		minute = args.getInt("minute");
		event_name = args.getString("event_name");
		event_location = args.getString("event_location");
		desc = args.getString("event_description");
		ampm = args.getString("AMPM");
		privacy = args.getBoolean("private");
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		event_image = onCallback.passImage();
		users = onCallback.passUsers();
		if(event_image == null){

		}
		View rootView = inflater.inflate(R.layout.last_new_event_fragment, container,
				false);
		((TextView)rootView.findViewById(R.id.event_name)).setText(event_name);
		((ImageView)rootView.findViewById(R.id.event_picture)).setImageBitmap(event_image);
		if(event_image == null){
			((ImageView)rootView.findViewById(R.id.event_picture)).getLayoutParams().height = 50;
			
			Toast.makeText(getActivity(), "NO IMAGE", Toast.LENGTH_LONG).show();
		}
		((TextView)rootView.findViewById(R.id.event_time)).setText(month+"/"+day+"/"+year+" @ "+hour+":"+minute+ampm);
		((TextView)rootView.findViewById(R.id.event_desc)).setText(desc);
		((TextView)rootView.findViewById(R.id.location_name)).setText(event_location);
		((TextView)rootView.findViewById(R.id.number_invites)).setText(users.size()+" Invited");
		((Button)rootView.findViewById(R.id.send_invites)).setOnClickListener(this);
		return rootView;
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.send_invites:
			sendInvite();
			Toast.makeText(getActivity(), "SENT!!!", Toast.LENGTH_LONG).show();
			getActivity().finish();
			break;

		default:
			break;
		}
		
	}
	
	public ParseObject createEvent(){
		
		Parse.initialize(getActivity(), "YhQVvN059Op1smwAnikVPltmzgJtJxx7yUiw3Dpy", "NIUIst19OzMqY9KyX2SKdFZTzj5pkDMsKBQYTKwS");
		ParseUser curr_user = ParseUser.getCurrentUser();
		ParseObject new_event = new ParseObject("TFTIEvent");
		ParseObject event_chat = new ParseObject("TFTIEventChat");
		new_event.put("createdBy", curr_user);
		new_event.put("eventChat", event_chat);
		
		if(event_image != null){
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			event_image.compress(Bitmap.CompressFormat.PNG, 0, os);
			byte[] array = os.toByteArray();
			ParseFile file = new ParseFile("cover_photo.png",array);
			file.saveInBackground();
			new_event.put("coverPhoto", file);
		}
		
		Date date = new Date();
		date.setYear(year);
		date.setMonth(month);
		date.setDate(day);
		date.setHours(hour);
		date.setMinutes(minute);
		
		new_event.put("eventDate", month+"/"+day+"/"+year+"-"+hour+":"+minute+ampm);
		new_event.put("eventDescription", desc);
		new_event.put("eventLocation", event_location);
		new_event.put("eventName", event_name);
		new_event.put("private", privacy);
		new_event.put("eventTime", date);
		new_event.saveInBackground();
		
		return new_event;
		
	}
	
	public void sendInvite(){
		
		ParseUser curr_user = ParseUser.getCurrentUser();
		ParseObject event = createEvent();
		ParseObject event_chat = new ParseObject("TFTIEventChat");
		event.put("eventChat", event_chat);
		
		for(ParseUser user: users){

			ParseObject new_activity = new ParseObject("TFTIActivity");
			new_activity.put("forEvent", event);
			new_activity.put("from", curr_user);
			new_activity.put("to", user);
			new_activity.put("invitationStatus","pending");
			new_activity.put("type", "invite");
			new_activity.saveInBackground();
		
		}
		
		event.saveInBackground();
		
		
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onCallback = (NewEventCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NewEventCallback.");
		}
	}

}
