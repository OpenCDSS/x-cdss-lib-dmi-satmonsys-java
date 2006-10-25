// ----------------------------------------------------------------------------
// SatMonSys_AlertSubscription - object to hold data from the 
// 	SatMonSys_AlertSubscriptions table.
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2005-09-22	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package DWR.DMI.SatMonSysDMI;

import RTi.DMI.DMIUtil;
import RTi.DMI.DMIDataObject;

public class SatMonSys_AlertSubscription
extends DMIDataObject {

protected int _alert_num = 		DMIUtil.MISSING_INT;
protected int _user_id = 		DMIUtil.MISSING_INT;
protected boolean _alert_enabled = 	false;
protected int _notification_type = 	DMIUtil.MISSING_INT;
protected int _max_attempts = 		DMIUtil.MISSING_INT;

/**
Constructor.
*/
public SatMonSys_AlertSubscription() {
	super();
}

/**
Cleans up member variables.
*/
public void finalize() 
throws Throwable {
	super.finalize();
}

/**
Returns _alert_enabled
@return _alert_enabled
*/
public boolean getAlert_enabled() {
	return _alert_enabled;
}

/**
Returns _alert_num
@return _alert_num
*/
public int getAlert_num() {
	return _alert_num;
}

/**
Returns _max_attempts
@return _max_attempts
*/
public int getMax_attempts() {
	return _max_attempts;
}

/**
Returns _notification_type
@return _notification_type
*/
public int getNotification_type() {
	return _notification_type;
}

/**
Returns _user_id
@return _user_id
*/
public int getUser_id() {
	return _user_id;
}

/**
Sets _alert_enabled
@param alert_enabled value to put in _alert_enabled
*/
public void setAlert_enabled(boolean alert_enabled) {
	_alert_enabled = alert_enabled;
}

/**
Sets _alert_num
@param alert_num value to put in _alert_num
*/
public void setAlert_num(int alert_num) {
	_alert_num = alert_num;
}

/**
Sets _max_attempts
@param max_attempts value to put in _max_attempts
*/
public void setMax_attempts(int max_attempts) {
	_max_attempts = max_attempts;
}

/**
Sets _notification_type
@param notification_type value to put in _notification_type
*/
public void setNotification_type(int notification_type) {
	_notification_type = notification_type;
}

/**
Sets _user_id
@param user_id value to put in _user_id
*/
public void setUser_id(int user_id) {
	_user_id = user_id;
}

/**
Returns a string representation of this object.
@return a string representation of this object.
*/
public String toString() {
	return "SatMonSys_AlertSubscription {\n"
		+ "   alert_num:          " + _alert_num + "\n"
		+ "   user_id:            " + _user_id + "\n"
		+ "   alert_enabled:      " + _alert_enabled + "\n"
		+ "   notification_type:  " + _notification_type + "\n"
		+ "   max_attempts:       " + _max_attempts + "\n}";
}

}
