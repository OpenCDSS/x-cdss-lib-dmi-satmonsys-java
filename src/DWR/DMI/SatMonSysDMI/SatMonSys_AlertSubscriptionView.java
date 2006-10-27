// ----------------------------------------------------------------------------
// SatMonSys_AlertSubscriptionView - object to hold data from the 
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

public class SatMonSys_AlertSubscriptionView
extends DMIDataObject {

protected int _alert_num = 			DMIUtil.MISSING_INT;
protected String _alert_description = 		DMIUtil.MISSING_STRING;
protected String _abbrev = 			DMIUtil.MISSING_STRING;
protected String _station_name = 		DMIUtil.MISSING_STRING;
protected String _result_name = 		DMIUtil.MISSING_STRING;
protected int _alert_type = 			DMIUtil.MISSING_INT;
protected String _alert_type_desc = 		DMIUtil.MISSING_STRING;
protected boolean _alert_enabled = 		false;
protected String _notification_type_desc = 	DMIUtil.MISSING_STRING;

/**
Constructor.
*/
public SatMonSys_AlertSubscriptionView() {
	super();
}

/**
Cleans up member variables.
*/
public void finalize() 
throws Throwable {
	_alert_description = null;
	_abbrev = null;
	_station_name = null;
	_result_name = null;
	_alert_type_desc = null;
	_notification_type_desc = null;
	super.finalize();
}

/**
Returns _abbrev
@return _abbrev
*/
public String getAbbrev() {
	return _abbrev;
}

/**
Returns _alert_description
@return _alert_description
*/
public String getAlert_description() {
	return _alert_description;
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
Returns _alert_type
@return _alert_type
*/
public int getAlert_type() {
	return _alert_type;
}

/**
Returns _notification_type_desc
@return _notification_type_desc
*/
public String getNotification_type_desc() {
	return _notification_type_desc;
}

/**
Returns _alert_type_desc
@return _alert_type_desc
*/
public String getAlert_type_desc() {
	return _alert_type_desc;
}

/**
Returns _result_name
@return _result_name
*/
public String getResult_name() {
	return _result_name;
}

/**
Returns _station_name
@return _station_name
*/
public String getStation_name() {
	return _station_name;
}

/**
Sets _abbrev
@param abbrev value to put into _abbrev
*/
public void setAbbrev(String abbrev) {
	_abbrev = abbrev;
}

/**
Sets _alert_description
@param alert_description value to put into _alert_description
*/
public void setAlert_description(String alert_description) {
	_alert_description = alert_description;
}

/**
Sets _alert_enabled
@param alert_enabled value to put into _alert_enabled
*/
public void setAlert_enabled(boolean alert_enabled) {
	_alert_enabled = alert_enabled;
}

/**
Sets _alert_num
@param alert_num value to put into _alert_num
*/
public void setAlert_num(int alert_num) {
	_alert_num = alert_num;
}

/**
Sets _alert_type
@param alert_type value to put into _alert_type
*/
public void setAlert_type(int alert_type) {
	_alert_type = alert_type;
}

/**
Sets _notification_type_desc
@param notification_type_desc value to put into _notification_type_desc
*/
public void setNotification_type_desc(String notification_type_desc) {
	_notification_type_desc = notification_type_desc;
}

/**
Sets _alert_type_desc
@param alert_type_desc value to put into _alert_type_desc
*/
public void setAlert_type_desc(String alert_type_desc) {
	_alert_type_desc = alert_type_desc;
}

/**
Sets _result_name
@param result_name value to put into _result_name
*/
public void setResult_name(String result_name) {
	_result_name = result_name;
}

/**
Sets _station_name
@param station_name value to put into _station_name
*/
public void setStation_name(String station_name) {
	_station_name = station_name;
}

/**
Returns a String representation of this Object.
@return a String representation of this Object.
*/
public String toString() {
	return "SatMonSys_AlertSubscriptionView {\n"
		+ "alert_num:               " + _alert_num + "\n"
		+ "alert_description:      '" + _alert_description + "'\n"
		+ "abbrev:                 '" + _abbrev + "'\n"
		+ "station_name:           '" + _station_name + "'\n"
		+ "result_name:            '" + _result_name + "'\n"
		+ "alert_type:              " + _alert_type + "\n"
		+ "alert_type_desc:        '" + _alert_type_desc + "'\n"
		+ "alert_enabled:           " + _alert_enabled + "\n"
		+ "notification_type_desc: '" + _notification_type_desc 
			+ "'\n}\n";
}

}
