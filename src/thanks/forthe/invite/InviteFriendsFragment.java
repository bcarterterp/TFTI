package thanks.forthe.invite;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import thanks.forthe.text.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class InviteFriendsFragment extends Fragment implements OnClickListener{
	
	private ArrayAdapter<String> adaptor;
	private ArrayList<Integer>friend_index;
	ArrayList<String> names;
	ArrayList<ParseUser>users;
	ParseObject event;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		friend_index = new ArrayList<Integer>();
		names = new ArrayList<String>();
		getFriends();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.second_new_event_fragment, container,
				false);
		Button send_button = (Button) rootView.findViewById(R.id.next_button);
		send_button.setText("Send Invites");
		send_button.setOnClickListener(this);
		
		final ListView listview = (ListView) rootView.findViewById(R.id.friends_list);
		adaptor = new FriendListAdapter(this.getActivity(), names, friend_index);
		listview.setAdapter(adaptor);
	
		return rootView;
			
	}
	
	private void getFriends(){
		
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereExists("username");
		query.findInBackground(new FindCallback<ParseUser>() {
			
			@Override
			public void done(List<ParseUser> all_users, ParseException e) {
				
				if(e == null){
					
					if(all_users == null){
						Toast.makeText(getActivity(), "EMPTY!!!", Toast.LENGTH_LONG).show();
					}else{
						
						for(ParseUser person: all_users){
							
							names.add(person.getString("username"));
							
						}
						users = (ArrayList<ParseUser>) all_users;
						adaptor.notifyDataSetChanged();
					}

				}else{
					
					Log.d("score", "Error: " + e.getMessage());
					
				}
				
			}
		});
		
	}
	
	private ArrayList<ParseUser> getInvited() {
		ArrayList<ParseUser> returned_users = new ArrayList<ParseUser>();
		for(Integer i:friend_index){
			returned_users.add(users.get(i));
		}
		return returned_users;
	}
	
	private void sendInvites(){
		ArrayList<ParseUser> users = getInvited();
		try {
			event.fetchIfNeeded();
			for(ParseUser friend:users){
				
				ParseObject invite = new ParseObject("TFTIActivity");
				invite.put("forEvent", event);
				invite.put("from", event.get("createdBy"));
				invite.put("invitationStatus", "pending");
				invite.put("to", friend);
				invite.put("type", "invite");
				invite.saveInBackground();
				getActivity().getSupportFragmentManager().popBackStack();
				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void getEvent(ParseObject event){
		
		this.event = event;
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.next_button:
			sendInvites();
			break;

		default:
			break;
		}
		
	}
	
	public interface OnSendInvites{
		
		
		
	}

}
