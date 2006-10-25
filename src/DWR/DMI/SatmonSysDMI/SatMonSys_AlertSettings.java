// ----------------------------------------------------------------------------
// SatMonSys_AlertSettings - object to hold data from the Alert Settings view
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

import RTi.Util.String.StringUtil;

public class SatMonSys_AlertSettings
extends DMIDataObject {

protected int _alert_num = 		DMIUtil.MISSING_INT;
protected String _alert_description = 	DMIUtil.MISSING_STRING;
protected int _station_num = 		DMIUtil.MISSING_INT;
protected String _abbrev = 		DMIUtil.MISSING_STRING;
protected String _station_name = 	DMIUtil.MISSING_STRING;
protected String _result_name = 	DMIUtil.MISSING_STRING;
protected int _alert_type = 		DMIUtil.MISSING_INT;
protected String _alert_type_desc = 	DMIUtil.MISSING_STRING;
protected boolean _enabled = 		false;
protected int _setting_num = 		DMIUtil.MISSING_INT;
protected String _start_date = 		DMIUtil.MISSING_STRING;
protected String _end_date = 		DMIUtil.MISSING_STRING;
protected double _check_value = 	DMIUtil.MISSING_DOUBLE;
protected int _check_time_period = 	DMIUtil.MISSING_INT;
protected boolean _alert_status = 	false;
protected String _data_source = 	DMIUtil.MISSING_STRING;
protected String _http_linkage = 	DMIUtil.MISSING_STRING;
protected boolean _has_randoms = 	false;

/**
Constructor.
*/
public SatMonSys_AlertSettings() {
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
	_start_date = null;
	_end_date = null;
	_data_source = null;
	_http_linkage = null;
	super.finalize();
}


/**
Parses out the day from an AlertSettings date string (MM/DD, M/DD, MM/D, M/D).
@param date the date String to parse for the day.  
Possible formats are: MM/DD, M/DD, MM/D, M/D.
@return the number of the day, or -1 if it could not be determined.
*/
public int getDay(String date) {
	int index = date.indexOf("/");
	String s = date.substring(index + 1);
	return StringUtil.atoi(s);
}

/**
Parses out the month from an AlertSettings date string (MM/DD, M/DD, MM/D, M/D).
@param date the date String to parse for the month.  
Possible formats are: MM/DD, M/DD, MM/D, M/D.
@return the number of the month, or -1 if it could not be determined.
*/
public int getMonth(String date) {
	int index = date.indexOf("/");
	String s = date.substring(0, index);
	return StringUtil.atoi(s);
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
Returns _alert_status
@return _alert_status
*/
public boolean getAlert_status() {
	return _alert_status;
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
Returns _alert_type_desc
@return _alert_type_desc
*/
public String getAlert_type_desc() {
	return _alert_type_desc;
}

/**
Returns _check_time_period
@return _check_time_period
*/
public int getCheck_time_period() {
	return _check_time_period;
}

/**
Returns _check_value
@return _check_value
*/
public double getCheck_value() {
	return _check_value;
}

/**
Returns _data_source
@return _data_source
*/
public String getData_source() {
	return _data_source;
}

/**
Returns _enabled
@return _enabled
*/
public boolean getEnabled() {
	return _enabled;
}

/**
Returns _end_date
@return _end_date
*/
public String getEnd_date() {
	return _end_date;
}

/**
Returns _has_randoms
@return _has_randoms
*/
public boolean getHas_randoms() {
	return _has_randoms;
}

/**
Returns _http_linkage
@return _http_linkage
*/
public String getHttp_linkage() {
	return _http_linkage;
}

/**
Returns _result_name
@return _result_name
*/
public String getResult_name() {
	return _result_name;
}

/**
Returns _setting_num
@return _setting_num
*/
public int getSetting_num() {
	return _setting_num;
}

/**
Returns _start_date
@return _start_date
*/
public String getStart_date() {
	return _start_date;
}

/**
Returns _station_name
@return _station_name
*/
public String getStation_name() {
	return _station_name;
}

/**
Returns _station_num
@return _station_num
*/
public int getStation_num() {
	return _station_num;
}

/**
Sets _abbrev
@param abbrev value to put in _abbrev
*/
public void setAbbrev(String abbrev) {
	_abbrev = abbrev;
}

/**
Sets _alert_description
@param alert_description value to put in _alert_description
*/
public void setAlert_description(String alert_description) {
	_alert_description = alert_description;
}

/**
Sets _alert_status
@param alert_status value to put in _alert_status
*/
public void setAlert_status(boolean alert_status) {
	_alert_status = alert_status;
}

/**
Sets _alert_num
@param alert_num value to put in _alert_num
*/
public void setAlert_num(int alert_num) {
	_alert_num = alert_num;
}

/**
Sets _alert_type
@param alert_type value to put in _alert_type
*/
public void setAlert_type(int alert_type) {
	_alert_type = alert_type;
}

/**
Sets _alert_type_desc
@param alert_type_desc value to put in _alert_type_desc
*/
public void setAlert_type_desc(String alert_type_desc) {
	_alert_type_desc = alert_type_desc;
}

/**
Sets _check_time_period
@param check_time_period value to put in _check_time_period
*/
public void setCheck_time_period(int check_time_period) {
	_check_time_period = check_time_period;
}

/**
Sets _check_value
@param check_value value to put in _check_value
*/
public void setCheck_value(double check_value) {
	_check_value = check_value;
}

/**
Sets _data_source
@param data_source value to put in _data_source
*/
public void setData_source(String data_source) {
	_data_source = data_source;
}

/**
Sets _enabled
@param enabled value to put in _enabled
*/
public void setEnabled(boolean enabled) {
	_enabled = enabled;
}

/**
Sets _end_date
@param end_date value to put in _end_date
*/
public void setEnd_date(String end_date) {
	_end_date = end_date;
}

/**
Sets _has_randoms
@param has_randoms value to put in _has_randoms
*/
public void setHas_randoms(boolean has_randoms) {
	_has_randoms = has_randoms;
}

/**
Sets _http_linkage
@param http_linkage value to put in _http_linkage
*/
public void sethttp_linkage(String http_linkage) {
	_http_linkage = http_linkage;
}

/**
Sets _result_name
@param result_name value to put in _result_name
*/
public void setResult_name(String result_name) {
	_result_name = result_name;
}

/**
Sets _setting_num
@param setting_num value to put in _setting_num
*/
public void setSetting_num(int setting_num) {
	_setting_num = setting_num;
}

/**
Sets _start_date
@param start_date value to put in _start_date
*/
public void setStart_date(String start_date) {
	_start_date = start_date;
}

/**
Sets _station_name
@param station_name value to put in _station_name
*/
public void setStation_name(String station_name) {
	_station_name = station_name;
}

/**
Sets _station_num
@param station_num value to put in _station_num
*/
public void setStation_num(int station_num) {
	_station_num = station_num;
}

/**
Returns a String representation of this Object.
@return a String representation of this Object.
*/
public String toString() {
	return "SatMonSys_AlertSettings {\n"
		+ "   alert_num:          " + _alert_num + "\n"
		+ "   alert_description: '" + _alert_description + "'\n"
		+ "   station_num:        " + _station_num + "\n"
		+ "   abbrev:            '" + _abbrev + "'\n"
		+ "   station_name:      '" + _station_name + "'\n"
		+ "   result_name        '" + _result_name + "'\n"
		+ "   alert_type:         " + _alert_type + "\n"
		+ "   alert_type_desc:   '" + _alert_type_desc + "'\n"
		+ "   enabled:            " + _enabled + "\n"
		+ "   setting_num:        " + _setting_num + "\n"
		+ "   start_date:        '" + _start_date + "'\n"
		+ "   end_date:          '" + _end_date + "'\n"
		+ "   check_value:        " + _check_value + "\n"
		+ "   check_time_period:  " + _check_time_period + "\n"
		+ "   alert_status:       " + _alert_status + "\n"
		+ "   data_source:       '" + _data_source + "'\n"
		+ "   http_linkage:      '" + _http_linkage + "'\n"
		+ "   has_randoms:        " + _has_randoms + "\n}";
}

}
