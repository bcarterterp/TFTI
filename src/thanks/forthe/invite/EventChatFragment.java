package thanks.forthe.invite;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import thanks.forthe.text.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class EventChatFragment extends Fragment implements OnClickListener{
	
	ParseObject event;
	String chat;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.event_chat_fragment, container, false);
		
		return rootView;
		
		
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	public void passEvent(ParseObject event) {
		
		this.event = event;
		
	}
	
	public void getChat(){
		
		
		
	}
	

}
