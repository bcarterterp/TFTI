package thanks.forthe.invite;

import java.util.List;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import thanks.forthe.invite.NavigationDrawerFragment.NavigationDrawerCallbacks;
import thanks.forthe.text.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment implements OnClickListener{
	
	private EditText name,password;
	private Button sign_in,forgot,new_user;
	private UserSelectionCallback mConfirmed;
	private Activity main_activity;
	
	String user_name;
	String user_pw;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		main_activity = this.getActivity();
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.user_login_fragment, container,
				false);
		
		name = (EditText) rootView.findViewById(R.id.user_name_field);
		password = (EditText) rootView.findViewById(R.id.user_name_password);
		sign_in = (Button) rootView.findViewById(R.id.submit_button);
		sign_in.setOnClickListener(this);
		forgot = (Button) rootView.findViewById(R.id.forgot_password);
		forgot.setOnClickListener(this);
		new_user = (Button) rootView.findViewById(R.id.new_user);
		new_user.setOnClickListener(this);
		Button fbButton = (Button) rootView.findViewById(R.id.authButton);
		fbButton.setOnClickListener(this);
		
		
		return rootView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState){
		
		super.onActivityCreated(savedInstanceState);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.submit_button:
			
			submitID();
			
			break;
		case R.id.forgot_password:
			
			Toast.makeText(this.getActivity(), "FORGOT CLICKED!!!",	Toast.LENGTH_LONG).show();
			
			break;
		case R.id.new_user:
			
			mConfirmed.onNewUser();
			
			break;
		case R.id.authButton:
			mConfirmed.onFBLogin();
			break;
		default:
			break;
		}
		
	}

	private void submitID(){
		
		user_name = name.getText().toString();
		user_pw = password.getText().toString();
		
		checkID(user_name, user_pw);
		
		
	}
	
	private void checkID(final String name,String password){
		
		
		ParseUser.logInInBackground(name, password, new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			    	SharedPreferences preferences = main_activity.getPreferences(0);
					SharedPreferences.Editor editor = preferences.edit();
					editor.putBoolean("verified", true);
					editor.putString("Username", name);
					editor.commit();
			    	mConfirmed.onUserConfirmed();
			    } else {
			      
			    	String error_message;
					int error_code = e.getCode();
					if(error_code == e.OBJECT_NOT_FOUND){
						error_message = "Wrong UserName/Password";
					}else if(error_code == e.PASSWORD_MISSING){
						error_message = "Enter Password";
					}else{
						error_message = ""+e.getCode();
					}
					
					Toast.makeText(main_activity, error_message, Toast.LENGTH_LONG).show();
			    	
			    }
			  }
			});
		
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mConfirmed = (UserSelectionCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement OnSubmitConfirmed.");
		}
	}
	
	public static interface UserSelectionCallback{
		void onFBLogin();
		void onUserConfirmed();
		void onUserForgot();
		void onNewUser();
		
	}

}
