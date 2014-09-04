package thanks.forthe.invite;

import java.util.ArrayList;

import thanks.forthe.text.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddFriendsListAdapter  extends ArrayAdapter<String>{
	
	private Context context;
	private ArrayList<String> names;
	private AddFriendsFragment fragment;
	
	public AddFriendsListAdapter(Context context, ArrayList<String> user_names,AddFriendsFragment fragment) {
		
		super(context, R.layout.friends_list_row,user_names);
		this.context = context;
		names = user_names;
		this.fragment = fragment;

	}
	
	public View getView(final int position,View convertView,ViewGroup parent){
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.friends_list_row, parent, false);
		ImageView friend_pic = (ImageView)rowView.findViewById(R.id.friend_pic);
		TextView friend_name = (TextView)rowView.findViewById(R.id.friend_id);
		friend_name.setText(names.get(position));
		final Button remove_friend = (Button)rowView.findViewById(R.id.invite_friend);
		remove_friend.setText("Remove");
		remove_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				fragment.removeUser(position);
				
			}
		});
		
		return rowView;
	}

}
