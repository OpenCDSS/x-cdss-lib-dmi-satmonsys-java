// ----------------------------------------------------------------------------
// SatMonSys_AlertPackages - object to hold data from the Alert Packages view
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2005-10-03	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package DWR.DMI.SatMonSysDMI;

import RTi.DMI.DMIUtil;
import RTi.DMI.DMIDataObject;

public class SatMonSys_AlertPackages
extends DMIDataObject {

protected int _alert_num = 		DMIUtil.MISSING_INT;
protected String _alert_description = 	DMIUtil.MISSING_STRING;
protected int _station_num = 		DMIUtil.MISSING_INT;
protected String _station_name = 	DMIUtil.MISSING_STRING;
protected String _abbrev = 		DMIUtil.MISSING_STRING;
protected String _data_provider = 	DMIUtil.MISSING_STRING;
protected String _result_name = 	DMIUtil.MISSING_STRING;
protected int _alert_type = 		DMIUtil.MISSING_INT;
protected String _alert_type_desc = 	DMIUtil.MISSING_STRING;
protected boolean _enabled = 		false;

/**
Constructor.
*/
public SatMonSys_AlertPackages() {
	super();
}

/**
Cleans up member variables.
*/
public void finalize() 
throws Throwable {
	_alert_description = null;
	_station_name = null;
	_abbrev = null;
	_data_provider = null;
	_result_name = null;
	_alert_type_desc = null;
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
Returns _data_provider
@return _data_provider
*/
public String getData_provider() {
	return _data_provider;
}

/**
Returns _enabled
@return _enabled
*/
public boolean getEnabled() {
	return _enabled;
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
Sets _data_provider
@param data_provider value to put in _data_provider
*/
public void setData_provider(String data_provider) {
	_data_provider = data_provider;
}

/**
Sets _enabled
@param enabled value to put in _enabled
*/
public void setEnabled(boolean enabled) {
	_enabled = enabled;
}

/**
Sets _result_name
@param result_name value to put in _result_name
*/
public void setResult_name(String result_name) {
	_result_name = result_name;
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
Returns a String representation of this object.
@return a String representation of this object.
*/
public String toString() {
	return "SatMonSys_AlertPackages {\n"
		+ "   alert_num:          " + _alert_num + "\n"
		+ "   alert_description: '" + _alert_description + "'\n"
		+ "   station_num:        " + _station_num + "\n"
		+ "   station_name:      '" + _station_name + "'\n"
		+ "   abbrev:            '" + _abbrev + "'\n"
		+ "   data_provider:     '" + _data_provider + "'\n"
		+ "   result_name:       '" + _result_name + "'\n"
		+ "   alert_type:         " + _alert_type + "\n"
		+ "   enabled:            " + _enabled + "\n}";
}

}
