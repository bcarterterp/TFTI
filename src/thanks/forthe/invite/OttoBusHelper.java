package thanks.forthe.invite;

import com.squareup.otto.Bus;

public class OttoBusHelper {
	
	public static final Bus BUS = new Bus();
	
	public static Bus getBus(){
		return BUS;
	}

}
