package thanks.forthe.invite;

import java.util.ArrayList;
import thanks.forthe.text.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventArrayAdapter extends ArrayAdapter<String>{
	
	private Context context;
	private ArrayList<String> event_names;
	private ArrayList<String> event_dates;
	private EventDisplayFragment parent;
	
	public EventArrayAdapter(Context context,ArrayList<String> names,ArrayList<String> dates,EventDisplayFragment caller) {
		
		super(context,R.layout.event_list_row,names);
		this.context = context;
		event_names = names;
		event_dates = dates;
		parent = caller;
		
	}
	
	public View getView(final int position,View convertView,ViewGroup parent){
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.event_list_row, parent, false);
		LinearLayout layout = (LinearLayout)rowView.findViewById(R.id.event_selection);
		TextView name = (TextView) rowView.findViewById(R.id.event_name_text);
		TextView date = (TextView) rowView.findViewById(R.id.event_time_text);
	
		name.setText(event_names.get(position));
		date.setText(event_dates.get(position));
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				chooseEvent(position);
				
			}
		});
		
		return rowView;
	}
	
	private void chooseEvent(int position){
		
		parent.selectEvent(position);
		
	}

}
