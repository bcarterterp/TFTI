package thanks.forthe.invite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import thanks.forthe.text.R;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class EventDisplayFragment extends Fragment implements OnClickListener{
	
	ImageButton new_event;
	private ListView listview;
	LinearLayout linlaHeaderProgress;
	private ArrayAdapter<String> adaptor;
	private OnNewEvent callback;
	private ArrayList<String> event_names,event_dates;
	private List<ParseObject>events;
	private String eventDate,eventTime;
	private String choice;
	private boolean created,completed;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		event_names = new ArrayList<String>();
		event_dates = new ArrayList<String>();

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.events_list_fragment, container,
				false);
		linlaHeaderProgress = (LinearLayout) rootView.findViewById(R.id.linlaHeaderProgress);
		linlaHeaderProgress.setVisibility(View.VISIBLE);
		new_event = (ImageButton) rootView.findViewById(R.id.new_event_button);
		new_event.setOnClickListener(this);
		listview = (ListView) rootView.findViewById(R.id.event_list);
		adaptor = new EventArrayAdapter(this.getActivity(), event_names, event_dates,this);
		listview.setAdapter(adaptor);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.new_event_button:
			callback.createNewEvent();
			break;

		default:
			break;
		}
		
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callback = (OnNewEvent) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement OnItemSelect.");
		}
	}
	
	@Override
	public void setArguments(Bundle args) {
		
		super.setArguments(args);
		choice = args.getString("type");
		if(choice.equalsIgnoreCase("created")){
			
			getCreated();
			created = true;
			
		}else if(choice.equals("pending")){
			
			getPending();
			created = false;
			
		}else if(choice.equals("accepted")){
			
			
			created = false;
			
		}
		
	}
	
	
	private void getCreated(){
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TFTIEvent");
		ParseUser user = ParseUser.getCurrentUser();
		query.whereEqualTo("createdBy", user);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> returned_events, ParseException e) {
				
				if(e == null){
					
					for(ParseObject single_event: returned_events){
						
						event_names.add(single_event.getString("eventName"));
						event_dates.add(getRealDate(single_event.getDate("eventTime")));
						
						
					}
					
					events = returned_events;
					adaptor.notifyDataSetChanged();
					completed = true;
					
				}else{
					
				}
				
				linlaHeaderProgress.setVisibility(View.GONE);
			}
			
		});
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(completed){
			linlaHeaderProgress.setVisibility(View.GONE);
		}
	}
	
	private void getPending(){
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TFTIActivity");
		ParseUser user = ParseUser.getCurrentUser();
		query.whereEqualTo("to", user);
		query.whereEqualTo("invitationStatus", "pending");
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> returned_events, ParseException e) {
				
				if(e == null){
					
					events = new ArrayList<ParseObject>();
					for(ParseObject single_event: returned_events){
						
						ParseObject event = single_event.getParseObject("forEvent");
						try {
							
							event.fetchIfNeeded();
							event_names.add(event.getString("eventName"));
							event_dates.add(getRealDate(event.getDate("eventTime")));
							events.add(event);
							
						} catch (ParseException e1) {
							
							e1.printStackTrace();
						}						
						
					}
					
					adaptor.notifyDataSetChanged();
					completed = true;
					
				}else{
					
				}
				
				linlaHeaderProgress.setVisibility(View.GONE);
			}
			
		});
		
	}
	
	private String getRealDate(Date event_date){
		
		String return_date = "Error 100";
		Calendar calendar = Calendar.getInstance();
		int month = event_date.getMonth();
		int day = event_date.getDate();
		int year = event_date.getYear();
		int hour = event_date.getHours();
		int minute = event_date.getMinutes();
		
		eventDate = (month+1)+"/"+day+"/"+year;
		
		String ampm;
		
		if(hour == 0){		
			hour = 12;
			ampm = "AM";	
		}else if(hour == 12){		
			ampm = "PM";		
		}else if(hour > 12){		
			hour = hour - 12;
			ampm = "PM";		
		}else{
			ampm = "AM";
		}
		
		if(minute < 10){
			eventTime = hour+":0"+minute+ampm;
			
		}else{
			eventTime = hour+":"+minute+ampm;
		}
		
		if(month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH)
				&& year == calendar.get(Calendar.YEAR)){
			return_date = "Today";
			eventDate = "Today";
		}else if(calendar.getActualMaximum(Calendar.MONTH) == calendar.get(Calendar.MONTH)){
			
			if(day == 1 && year == calendar.get(Calendar.YEAR)){
				return_date = "Tomorrow";
				eventDate = "Tomorrow";
			}
			
		}else if(month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH)+1
				&& year == calendar.get(Calendar.YEAR)){
			return_date = "Tomorrow";
			eventDate = "Tomorrow";
		}else{
			return_date = eventDate+" @ "+eventTime;
		}
		
		return return_date;
		
	}
	
	private String getDate(String date){
		
		String return_date;
		String dateRegex = "\\d{1,2}[/]\\d{1,2}[/]\\d{4}";
		Pattern pattern = Pattern.compile(dateRegex);
		Matcher matcher = pattern.matcher(date);
		if(matcher.find()){
			return_date = matcher.group();
			String[] integers = return_date.split("/");
			Calendar calendar = Calendar.getInstance();
			int month = Integer.valueOf(integers[0]);
			int day = Integer.valueOf(integers[1]);
			int year = Integer.valueOf(integers[2]);
			
			if(month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH)
					&& year == calendar.get(Calendar.YEAR)){
				return_date = "Today";
				eventDate = "Today";
			}else if(calendar.getActualMaximum(Calendar.MONTH) == calendar.get(Calendar.MONTH)){
				
				if(day == 1 && year == calendar.get(Calendar.YEAR)){
					return_date = "Tomorrow";
					eventDate = "Tomorrow";
				}
				
			}else if(month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH)+1
					&& year == calendar.get(Calendar.YEAR)){
				return_date = "Tomorrow";
				eventDate = "Tomorrow";
			}
		}else{
			return_date = date;
		}
		
		return return_date;
		
	}
	
	public void selectEvent(int position){
		
		ParseObject picked_event = events.get(position);
		getRealDate(picked_event.getDate("eventTime"));
		callback.showEvent(picked_event,eventDate,eventTime,created);
		
	}
	
	public interface OnNewEvent{
		
		public void createNewEvent();
		public void showEvent(ParseObject event,String date,String time,boolean created);
		
	}

}
