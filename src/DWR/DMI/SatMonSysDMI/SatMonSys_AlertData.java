// ----------------------------------------------------------------------------
// SatMonSys_AlertData - object to hold data from the Alert Data view
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2005-10-09	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package DWR.DMI.SatMonSysDMI;

import java.util.Date;

import RTi.DMI.DMIUtil;
import RTi.DMI.DMIDataObject;

public class SatMonSys_AlertData
extends DMIDataObject {

protected int _alert_type = 		DMIUtil.MISSING_INT;
protected String _alert_type_desc = 	DMIUtil.MISSING_STRING;
protected String _abbrev = 		DMIUtil.MISSING_STRING;
protected String _variable = 		DMIUtil.MISSING_STRING;
protected Date _date_time = 		DMIUtil.MISSING_DATE;
protected double _amt = 		DMIUtil.MISSING_DOUBLE;
protected int _div = 			DMIUtil.MISSING_INT;
protected int _wd = 			DMIUtil.MISSING_INT;
protected int _alert_num = 		DMIUtil.MISSING_INT;
protected int _alert_status = DMIUtil.MISSING_INT;

/**
Constructor.
*/
public SatMonSys_AlertData() {
	super();
}

/**
Cleans up member variables.
*/
public void finalize() 
throws Throwable {
	_alert_type_desc = null;
	_abbrev = null;
	_variable = null;
	_date_time = null;
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
Returns _alert_num
@return _alert_num
*/
public int getAlert_num() {
	return _alert_num;
}

public int getAlert_status() {
	return _alert_status;
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
Returns _amt
@return _amt
*/
public double getAmt() {
	return _amt;
}

/**
Returns _date_time
@return _date_time
*/
public Date getDate_time() {
	return _date_time;
}

/**
Returns _div
@return _div
*/
public int getDiv() {
	return _div;
}

/**
Returns _variable
@return _variable
*/
public String getVariable() {
	return _variable;
}

/**
Returns _wd
@return _wd
*/
public int getWD() {
	return _wd;
}

/**
Sets _abbrev
@param abbrev value to put into _abbrev
*/
public void setAbbrev(String abbrev) {
	_abbrev = abbrev;
}

/**
Sets _alert_num
@param alert_num value to put into _alert_num
*/
public void setAlert_num(int alert_num) {
	_alert_num = alert_num;
}

public void setAlert_status(int alert_status) {
	_alert_status = alert_status;
}

/**
Sets _alert_type
@param alert_type value to put into _alert_type
*/
public void setAlert_type(int alert_type) {
	_alert_type = alert_type;
}

/**
Sets _alert_type_desc
@param alert_type_desc value to put into _alert_type_desc
*/
public void setAlert_type_desc(String alert_type_desc) {
	_alert_type_desc = alert_type_desc;
}

/**
Sets _amt
@param amt value to put into _amt
*/
public void setAmt(double amt) {
	_amt = amt;
}

/**
Sets _date_time
@param date_time value to put into _date_time
*/
public void setDate_time(Date date_time) {
	_date_time = date_time;
}

/**
Sets _div
@param div value to put into _div
*/
public void setDiv(int div) {
	_div = div;
}

/**
Sets _variable
@param variable value to put into _variable
*/
public void setVariable(String variable) {
	_variable = variable;
}

/**
Sets _wd
@param wd value to put into _wd
*/
public void setWD(int wd) {
	_wd = wd;
}

/**
Returns a String representation of this Object.
@return a String representation of this Object.
*/
public String toString() {
	return "SatMonSys_AlertDate {\n"
		+ "alert_type:       " + _alert_type + "\n"
		+ "alert_type_desc: '" + _alert_type_desc + "'\n"
		+ "abbrev:          '" + _abbrev + "'\n"
		+ "variable:        '" + _variable + "'\n"
		+ "date_time:        " + _date_time + "\n"
		+ "amt:              " + _amt + "\n"
		+ "div:              " + _div + "\n"
		+ "wd:               " + _wd + "\n"
		+ "alert_num:        " + _alert_num + "\n}\n";
}


}
