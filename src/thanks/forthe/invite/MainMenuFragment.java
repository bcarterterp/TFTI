package thanks.forthe.invite;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import thanks.forthe.invite.LoginFragment.UserSelectionCallback;
import thanks.forthe.text.R;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainMenuFragment extends Fragment implements OnClickListener{
	
	private Button created, pending, accepted;
	private OnOptionSelectCallback callback;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
			
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.invite_choice_fragment, container,
				false);
		created = (Button) rootView.findViewById(R.id.invite_created_button);
		created.setOnClickListener(this);
		pending = (Button) rootView.findViewById(R.id.invite_pending_button);
		pending.setOnClickListener(this);
		accepted = (Button) rootView.findViewById(R.id.invites_accepted_button);
		accepted.setOnClickListener(this);

		return rootView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState){
		
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.invite_created_button:
			callback.showCreated();
			break;
		case R.id.invite_pending_button:
			callback.showPending();
			break;
		case R.id.invites_accepted_button:
			callback.showAccepted();
			break;
		}
		
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callback = (OnOptionSelectCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement OnOptionSelectCallback.");
		}
	}
	
	public static interface OnOptionSelectCallback{
		
		public void showCreated();
		public void showPending();
		public void showAccepted();
		
	}

}
