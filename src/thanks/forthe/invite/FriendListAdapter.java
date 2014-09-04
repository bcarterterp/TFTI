package thanks.forthe.invite;

import java.util.ArrayList;

import thanks.forthe.text.R;
import android.content.Context;
import android.provider.DocumentsContract.Root;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendListAdapter extends ArrayAdapter<String>{
	
	private Context context;
	private ArrayList<String> names;
	private ArrayList<Integer> name_index;
	
	public FriendListAdapter(Context context, ArrayList<String> user_names,ArrayList<Integer> index) {
		
		super(context, R.layout.friends_list_row,user_names);
		this.context = context;
		names = user_names;
		name_index = index;

	}
	
	public View getView(final int position,View convertView,ViewGroup parent){
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.friends_list_row, parent, false);
		ImageView friend_pic = (ImageView)rowView.findViewById(R.id.friend_pic);
		TextView friend_name = (TextView)rowView.findViewById(R.id.friend_id);
		friend_name.setText(names.get(position));
		final Button invite_friend = (Button)rowView.findViewById(R.id.invite_friend);
		if(name_index.contains(position)){
			invite_friend.setText("Invited");
		}
		invite_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch(v.getId()){
				case R.id.invite_friend:

					if(!name_index.contains(position)){
						name_index.add(position);
						invite_friend.setText("Invited");
					}else{
						name_index.remove(Integer.valueOf(position));
						invite_friend.setText("Invite");
					}
					break;
				}
			}
		});
		
		return rowView;
	}
	

}
