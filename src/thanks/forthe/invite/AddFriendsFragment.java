package thanks.forthe.invite;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import thanks.forthe.text.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddFriendsFragment extends Fragment implements OnClickListener{
	
	private ArrayAdapter<String> friend_adapter;
	private ArrayList<String> friends_list;
	private ParseRelation<ParseObject> relation;
	private ParseUser found_user,user;
	private Button modify_friends_list;
	private TextView name_text;
	private EditText search_user;
	private LinearLayout search_result;
	private ImageView found_profile_pic;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		friends_list = new ArrayList<String>();
		getFriendsList();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.friends_list_fragment, container,
				false);
		Button search = (Button) rootView.findViewById(R.id.search_button);
		search.setOnClickListener(this);
		Button home = (Button) rootView.findViewById(R.id.home_button);
		home.setOnClickListener(this); 
		modify_friends_list = (Button) rootView.findViewById(R.id.add_and_remove_button);
		modify_friends_list.setOnClickListener(this);
		
		search_user = (EditText) rootView.findViewById(R.id.find_names);
		found_profile_pic = (ImageView) rootView.findViewById(R.id.friend_pic);
		search_result = (LinearLayout)rootView.findViewById(R.id.search_result);
		name_text = (TextView)	rootView.findViewById(R.id.friend_id);
		
		final ListView friends_listview = (ListView) rootView.findViewById(R.id.friends);
		friend_adapter = new AddFriendsListAdapter(this.getActivity(), friends_list,this);
		friends_listview.setAdapter(friend_adapter);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_button:
			
			String search_name = search_user.getText().toString();
			findUsers(search_name);
			break;

		case R.id.home_button:
			user.saveInBackground();
			Toast.makeText(getActivity(), "SAVED!!!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.add_and_remove_button:
			if(modify_friends_list.getText() == "Add"){
				modify_friends_list.setText("Remove");
				addUser();
			}else{
				modify_friends_list.setText("Add");
				removeUser(-1);
			}
			break;
		default:
			break;
		}
	
	}
	
	private void getFriendsList(){
		
		user = ParseUser.getCurrentUser();
		relation = user.getRelation("friendsList");
		if(relation != null){
			ParseQuery<ParseObject> query = relation.getQuery();
			query.whereExists("username");
			query.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> returned_list, ParseException e) {
					
					for(ParseObject person: returned_list){
						
						friends_list.add(person.getString("username"));
						
					}
					friend_adapter.notifyDataSetChanged();
				}
				
			});
		}
		
	}
	
	private void findUsers(String name){

		ParseQuery<ParseUser> valid_users = ParseUser.getQuery();
		valid_users.whereEqualTo("username",name);
		valid_users.getFirstInBackground(new GetCallback<ParseUser>() {
			
			@Override
			public void done(ParseUser user, ParseException e) {
				
				if(e == null){
					if(user != null){
						found_user = user;
						name_text.setText(user.getUsername());
						if(namePresent(user.getUsername()) > -1){
							modify_friends_list.setText("Remove");
						}
						search_result.setVisibility(View.VISIBLE);
						modify_friends_list.setVisibility(View.VISIBLE);
						found_profile_pic.setVisibility(View.VISIBLE);
						Toast.makeText(getActivity(), "Found USER!!!", Toast.LENGTH_SHORT).show();
					}
				}else{
					name_text.setText("No user found");
					modify_friends_list.setVisibility(View.GONE);
					found_profile_pic.setVisibility(View.GONE);
					Toast.makeText(getActivity(), "No Found USER!!!", Toast.LENGTH_SHORT).show();
				}
			}
			
		});	
		
	}
	
	public void addUser(){
		String uID = found_user.getString("username");
		boolean found = false;
		for(String friend: friends_list){
			if(uID.equalsIgnoreCase(friend)){
				found = true;
			}
		}
		if(!found){
			friends_list.add(found_user.getUsername());
			friend_adapter.notifyDataSetChanged();
			relation.add(found_user);
			user.saveInBackground();
		}
		
	}
	
	private int namePresent(String name){
		
		int index = -1;
		int counter = 0;
		for(String friend: friends_list){
			if(name.equalsIgnoreCase(friend)){
				index = counter;
			}
			counter++;
		}
		
		return index;
		
	}
	
	public void removeUser(final int position){
		
		if (position == -1) {
			int index = namePresent(found_user.getUsername());
			if (index != -1) {
				friends_list.remove(index);
				friend_adapter.notifyDataSetChanged();
				relation.remove(found_user);
				user.saveInBackground();
			}
		}else{
			ParseQuery<ParseObject>friendsQuery = relation.getQuery();
			friendsQuery.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> all_users, ParseException e) {
					
					if(e == null){
						
						for(ParseObject person: all_users){
							
							if(person.getString("username").equalsIgnoreCase(friends_list.get(position))){
								relation.remove(person);
								friends_list.remove(position);
								user.saveInBackground();
							}
							
						}
						
					}else{
						
					}
					
					friend_adapter.notifyDataSetChanged();
					
				}
			});
		}
		
	}

}
