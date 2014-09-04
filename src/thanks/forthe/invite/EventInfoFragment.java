package thanks.forthe.invite;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.otto.Subscribe;

import thanks.forthe.text.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class EventInfoFragment extends Fragment implements OnClickListener{
	
	OnClickListener this_listener;
	EventSelectionCallback callback;
	ImageView cover_photo;
	ScrollView scroller;
	Button user_action;
	String userName,eventName,eventDate,
	eventTime,eventDesc,eventLocation;
	BitmapDrawable image;
	ParseObject event;
	byte[] coverPhoto;
	boolean privacy,created,attending;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		image = null;
		OttoBusHelper.getBus().register(this);
		attending = false;
			
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.event_info_fragment, container,
				false);
		ImageButton show_friends = (ImageButton) rootView.findViewById(R.id.show_friends_button);
		show_friends.setOnClickListener(this);
		ImageButton show_chat = (ImageButton) rootView.findViewById(R.id.show_chat_button);
		show_chat.setOnClickListener(this);
		Button event_album = (Button) rootView.findViewById(R.id.event_photo_album_button);
		event_album.setOnClickListener(this);
		Button invite_friends = (Button) rootView.findViewById(R.id.invite_friends);
		invite_friends.setOnClickListener(this);
		user_action = (Button) rootView.findViewById(R.id.user_choice);
		user_action.setOnClickListener(this);
		TextView user_name = (TextView) rootView.findViewById(R.id.user_name);
		TextView event_name = (TextView) rootView.findViewById(R.id.event_name);
		TextView event_date = (TextView) rootView.findViewById(R.id.event_date);
		TextView event_time = (TextView) rootView.findViewById(R.id.event_time);
		TextView event_desc = (TextView) rootView.findViewById(R.id.event_description);
		TextView event_location = (TextView)rootView.findViewById(R.id.event_location);
		scroller = (ScrollView) rootView.findViewById(R.id.scroller);
		if(created){
			user_name.setText("You \n Have started the event");
			user_action.setVisibility(View.GONE);
		}else{
			if(attending){
				user_action.setText("Decline Invite");
			}
			user_name.setText(userName+"\n Invites you too...");
		}
		event_name.setText(eventName);
		event_desc.setText(eventDesc);
		event_location.setText(eventLocation);
		event_time.setText(eventTime);
		event_date.setText(eventDate);
		
		return rootView;
		
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callback = (EventSelectionCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement EventSelectionCallback.");
		}
	}
	
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.event_photo_album_button:
			callback.showEventPhotos();
			break;
		case R.id.invite_friends:
			callback.inviteFriends(null);
			break;
		case R.id.user_choice:
			user_action.setOnClickListener(null);
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TFTIActivity");
			ParseUser user = ParseUser.getCurrentUser();
			query.whereEqualTo("forEvent", event);
			query.whereEqualTo("to", user);
			query.getFirstInBackground(new GetCallback<ParseObject>() {
				
				@Override
				public void done(ParseObject invite, ParseException e) {
					if(e == null){
						
						if(!attending){
							invite.put("invitationStatus", "accepted");
							Toast.makeText(getActivity(), "ATTENDING "+event.getObjectId(), Toast.LENGTH_SHORT).show();
							user_action.setText("Decline Invite");
							attending = true;
						}else{
							invite.put("invitationStatus","pending");
							Toast.makeText(getActivity(), "PENDING "+event.getObjectId(), Toast.LENGTH_SHORT).show();
							user_action.setText("Accept Invite");
							attending = false;
						}
						invite.saveInBackground();
						
					}else{
						
					}
					
					resetListener();
					
				}
				
			});
			break;
		case R.id.show_chat_button:
			callback.showEventChat(event);
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void setArguments(Bundle args) {
		
		super.setArguments(args);
		userName = args.getString("user_name");
		eventDate = args.getString("event_date");
		eventTime = args.getString("event_time");
		eventName = args.getString("event_name");
		eventDesc = args.getString("event_desc");
		eventLocation = args.getString("event_location");
		created = args.getBoolean("created");
		privacy = args.getBoolean("privacy");
		
	}
	
	public void passEvent(ParseObject event){
		this.event = event;
	}
	
	@Subscribe public void onImageResult(AsyncTaskBitmapEvent event){
		
		if(scroller == null){
			image = event.getResult();
		}else{
			scroller.setBackgroundDrawable(event.getResult());
		}
		
	}
	
	private void resetListener(){
		user_action.setOnClickListener(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		OttoBusHelper.getBus().unregister(this);
	}
	
	public interface EventSelectionCallback{
		
		public void showEventPhotos();
		public void showEventChat(ParseObject event);
		public void inviteFriends(ParseObject event);
		
	}

}
