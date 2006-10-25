// ----------------------------------------------------------------------------
// SatMonSys_CombinedRealTimeResults15DayArchive - object to hold data from 
//	the combined realtime 15 day archive table.
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2006-02-09	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package DWR.DMI.SatMonSysDMI;

import java.util.Date;

import RTi.DMI.DMIUtil;
import RTi.DMI.DMIDataObject;

public class SatMonSys_CombinedRealTimeResults15DayArchive
extends DMIDataObject {

protected int _div = 			DMIUtil.MISSING_INT;
protected int _wd = 			DMIUtil.MISSING_INT;
protected String _abbrev = 		DMIUtil.MISSING_STRING;
protected String _variable = 		DMIUtil.MISSING_STRING;
protected Date _date_time = 		DMIUtil.MISSING_DATE;
protected double _amt = 		DMIUtil.MISSING_DOUBLE;
protected String _flag = 		DMIUtil.MISSING_STRING;
protected Date _rcvd_date_time = 	DMIUtil.MISSING_DATE;
protected String _data_source = 	DMIUtil.MISSING_STRING;

/**
Constructor.
*/
public SatMonSys_CombinedRealTimeResults15DayArchive() {
	super();
}

/**
Cleans up member variables.
*/
public void finalize() 
throws Throwable {
	_abbrev = null;
	_variable = null;
	_date_time = null;
	_flag = null;
	_rcvd_date_time = null;
	_data_source = null;
	super.finalize();
}

/**
Returns abbrev
@return abbrev
*/
public String getAbbrev() {
	return _abbrev;
}

/**
Returns amt
@return amt
*/
public double getAmt() {
	return _amt;
}

/**
Returns data_source
@return data_source
*/
public String getData_source() {
	return _data_source;
}

/**
Returns date_time
@return date_time
*/
public Date getDate_time() {
	return _date_time;
}

/**
Returns div
@return div
*/
public int getDiv() {
	return _div;
}

/**
Returns flag
@return flag
*/
public String getFlag() {
	return _flag;
}

/**
Returns rcvd_date_time
@return rcvd_date_time
*/
public Date getRcvd_date_time() {
	return _rcvd_date_time;
}

/**
Returns variable
@return variable
*/
public String getVariable() {
	return _variable;
}

/**
Returns wd
@return wd
*/
public int getWD() {
	return _wd;
}

/**
Sets abbrev
@param abbrev value to put into _abbrev
*/
public void setAbbrev(String abbrev) {
	_abbrev = abbrev;
}

/**
Sets amt
@param amt value to put into _amt
*/
public void setAmt(double amt) {
	_amt = amt;
}

/**
Sets data_source
@param data_source value to put into _data_source
*/
public void setData_source(String data_source) {
	_data_source = data_source;
}

/**
Sets date_time
@param date_time value to put into _date_time
*/
public void setDate_time(Date date_time) {
	_date_time = date_time;
}

/**
Sets div
@param div value to put into _div
*/
public void setDiv(int div) {
	_div = div;
}

/**
Sets flag
@param flag value to put into _flag
*/
public void setFlag(String flag) {
	_flag = flag;
}

/**
Sets rcvd_date_time
@param rcvd_date_time value to put into _rcvd_date_time
*/
public void setRcvd_date_time(Date rcvd_date_time) {
	_rcvd_date_time = rcvd_date_time;
}

/**
Sets variable
@param variable value to put into _variable
*/
public void setVariable(String variable) {
	_variable = variable;
}

/**
Sets wd
@param wd value to put into _wd
*/
public void setWD(int wd) {
	_wd = wd;
}

/**
Returns a String representation of this object.
@return a String representation of this object.
*/
public String toString() {
	return "SatMonSys_CombinedRealTimeResults15DayArchive {"
		+ "   div:            " + _div + "\n"
		+ "   wd:             " + _wd + "\n"
		+ "   abbrev:        '" + _abbrev + "'\n"
		+ "   variable:      '" + _variable + "'\n"
		+ "   date_time:      " + _date_time + "\n"
		+ "   amt:            " + _amt + "\n"
		+ "   flag:          '" + _flag + "'\n"
		+ "   rcvd_date_time: " + _rcvd_date_time + "\n"
		+ "   data_source:   '" + _data_source + "'\n"
		+ "}\n";
}

}
