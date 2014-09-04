package thanks.forthe.invite;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import thanks.forthe.invite.LoginFragment.UserSelectionCallback;
import thanks.forthe.text.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class NewUserFragment extends Fragment implements OnClickListener{
	
	private EditText new_name,new_password,new_email,new_phone;
	private Button user_done;
	private UserSelectionCallback mConfirmed;
	private Activity main_activity;
	
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PASSWORD_PATTERN = 
			"[A-Za-z0-9-]{6,}+";
	private static final String NO_SPACES = 
			"\\s";
	private static final String USERNAME_PATTERN = 
			"[_A-Za-z0-9-\\+]+";
	
	private Pattern pattern;
	private Matcher matcher;
	ParseUser userObject;
	
	private String name;
	private String password;
	private String email;
	private String phone;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		Parse.initialize(this.getActivity(), "YhQVvN059Op1smwAnikVPltmzgJtJxx7yUiw3Dpy", "NIUIst19OzMqY9KyX2SKdFZTzj5pkDMsKBQYTKwS");
		userObject = new ParseUser();
		main_activity = this.getActivity();

	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.new_user_fragment, container,
				false);
		new_name = (EditText) rootView.findViewById(R.id.new_user_name);
		new_password = (EditText) rootView.findViewById(R.id.new_user_password);
		new_email = (EditText) rootView.findViewById(R.id.new_user_email);
		new_phone = (EditText) rootView.findViewById(R.id.new_user_phone);
		user_done = (Button) rootView.findViewById(R.id.new_user_done);
		user_done.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.new_user_done:
			setUpNewUser();
			break;
		default:
			break;
		}
		
	}
	
	private void setUpNewUser(){
		
		name = new_name.getText().toString();
		password = new_password.getText().toString();
		email = new_email.getText().toString();
		phone = new_phone.getText().toString();
		
		if(validInfo(name, password, email, phone)){

			userObject.setUsername(name);
			userObject.setPassword(password);
			userObject.setEmail(email);	
			userObject.put("phone", phone);
	
			userObject.signUpInBackground(new SignUpCallback() {
				
				@Override
				public void done(ParseException e) {
					
					if (e == null) {
						
						SharedPreferences preferences = main_activity.getPreferences(0);
						SharedPreferences.Editor editor = preferences.edit();
						editor.putBoolean("verified", true);
						editor.putString("Username", name);
						editor.commit();
						mConfirmed.onUserConfirmed();
						
					}else{
						
						String error_message;
						int error_code = e.getCode();
						if(error_code == e.USERNAME_TAKEN){
							error_message = "Username taken!";
						}else if(error_code == e.EMAIL_TAKEN){
							error_message = "Email Taken";
						}else{
							error_message = ""+e.getCode();
						}
						
						Toast.makeText(main_activity, error_message, Toast.LENGTH_LONG).show();
								
						
					}
					
				}
			});

		}

		
	}
	
	private boolean validInfo(String name,String password,String email,String phone){
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		
		if(!matcher.matches()){
			Toast.makeText(getActivity(), "INVALID EMAIL!!!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		pattern = Pattern.compile(NO_SPACES);
		matcher = pattern.matcher(password);
		
		if(!matcher.matches()){
			pattern = Pattern.compile(PASSWORD_PATTERN);
			matcher = pattern.matcher(password);
			
			if(!matcher.matches()){
				
				Toast.makeText(getActivity(), "INVALID PASSWORD!!!", Toast.LENGTH_SHORT).show();
				return false;
				
			}
		}
		
		pattern = Pattern.compile(USERNAME_PATTERN);
		matcher = pattern.matcher(name);
		
		if(!matcher.matches()){
			Toast.makeText(getActivity(), "INVALID USERNAME!!!", Toast.LENGTH_SHORT).show();
		}
		
		return matcher.matches();
		
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
	

}
