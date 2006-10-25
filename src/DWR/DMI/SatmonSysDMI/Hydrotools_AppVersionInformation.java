// ----------------------------------------------------------------------------
// Hydrotools_AppVersionInformation - program version for the application
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2005-09-22	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package DWR.DMI.SatmonSysDMI;

import java.util.Date;

import RTi.DMI.DMIUtil;
import RTi.DMI.DMIDataObject;

public class Hydrotools_AppVersionInformation
extends DMIDataObject {

protected String _ver_major = 		DMIUtil.MISSING_STRING;
protected String _ver_minor = 		DMIUtil.MISSING_STRING;
protected String _ver_build_lower = 	DMIUtil.MISSING_STRING;
protected String _ver_build_upper = 	DMIUtil.MISSING_STRING;
protected Date _release_date = 		DMIUtil.MISSING_DATE;
protected Date _service_start_date = 	DMIUtil.MISSING_DATE;
protected Date _service_end_date = 	DMIUtil.MISSING_DATE;
protected String _currently_enabled = 	DMIUtil.MISSING_STRING;
protected String _custom_version_text = DMIUtil.MISSING_STRING;
protected String _custom_message = 	DMIUtil.MISSING_STRING;

/**
Constructor.
*/
public Hydrotools_AppVersionInformation() {
	super();
}

/**
Cleans up member variables.
*/
public void finalize() 
throws Throwable {
	_ver_major = null;
	_ver_minor = null;
	_ver_build_lower = null;
	_ver_build_upper = null;
	_release_date = null;
	_service_start_date = null;
	_service_end_date = null;
	_currently_enabled = null;
	_custom_version_text = null;
	_custom_message = null;
	super.finalize();
}

/**
Returns _currently_enabled
@return _currently_enabled
*/
public String getCurrently_enabled() {
	return _currently_enabled;
}

/**
Returns _custom_message
@return _custom_message
*/
public String getCustom_message() {
	return _custom_message;
}

/**
Returns _custom_version_text
@return _custom_version_text
*/
public String getCustom_version_text() {
	return _custom_version_text;
}

/**
Returns _release_date
@return _release_date
*/
public Date getRelease_date() {
	return _release_date;
}

/**
Returns _service_end_date
@return _service_end_date
*/
public Date getService_end_date() {
	return _service_end_date;
}

/**
Returns _service_start_date
@return _service_start_date
*/
public Date getService_start_date() {
	return _service_start_date;
}

/**
Returns _ver_build_lower
@return _ver_build_lower
*/
public String getVer_build_lower() {
	return _ver_build_lower;
}

/**
Returns _ver_build_upper
@return _ver_build_upper
*/
public String getVer_build_upper() {
	return _ver_build_upper;
}

/**
Returns _ver_major
@return _ver_major
*/
public String getVer_major() {
	return _ver_major;
}

/**
Returns _ver_minor
@return _ver_minor
*/
public String getVer_minor() {
	return _ver_minor;
}

/**
Sets _currently_enabled
@param currently_enabled value to put into _currently_enabled
*/
public void setCurrently_enabled(String currently_enabled) {
	_currently_enabled = currently_enabled;
}

/**
Sets _custom_message
@param custom_message value to put into _custom_message
*/
public void setCustom_message(String custom_message) {
	_custom_message = custom_message;
}

/**
Sets _custom_version_text
@param custom_version_text value to put into _custom_version_text
*/
public void setCustom_version_text(String custom_version_text) {
	_custom_version_text = custom_version_text;
}

/**
Sets _release_date
@param release_date value to put into _release_date
*/
public void setRelease_date(Date release_date) {
	_release_date = release_date;
}

/**
Sets _service_end_date
@param service_end_date value to put into _service_end_date
*/
public void setService_end_date(Date service_end_date) {
	_service_end_date = service_end_date;
}

/**
Sets _service_start_date
@param service_start_date value to put into _service_start_date
*/
public void setService_start_date(Date service_start_date) {
	_service_start_date = service_start_date;
}

/**
Sets _ver_build_lower
@param ver_build_lower value to put into _ver_build_lower
*/
public void setVer_build_lower(String ver_build_lower) {
	_ver_build_lower = ver_build_lower;
}

/**
Sets _ver_build_upper
@param ver_build_upper value to put into _ver_build_upper
*/
public void setVer_build_upper(String ver_build_upper) {
	_ver_build_upper = ver_build_upper;
}

/**
Sets _ver_major
@param ver_major value to put into _ver_major
*/
public void setVer_major(String ver_major) {
	_ver_major = ver_major;
}

/**
Sets _ver_minor
@param ver_minor value to put into _ver_minor
*/
public void setVer_minor(String ver_minor) {
	_ver_minor = ver_minor;
}

/**
Returns a String representation of this object.
@return a String representation of this object.
*/
public String toString() {
	return "Hydrotools_AppVersionInformation {\n"
		+ "   ver_major:          '" + _ver_major + "'\n"
		+ "   ver_minor:          '" + _ver_minor + "'\n"
		+ "   ver_build_lower:    '" + _ver_build_lower + "'\n"
		+ "   ver_build_upper:    '" + _ver_build_upper + "'\n"
		+ "   release_date:        " + _release_date + "\n"
		+ "   service_start_date:  " + _service_start_date + "\n"
		+ "   service_end_date:    " + _service_end_date + "\n"
		+ "   currently_enabled:  '" + _currently_enabled + "'\n"
		+ "   custom_version_txt: '" + _custom_version_text + "'\n"
		+ "   custom_message:     '" + _custom_message + "'\n}";
}

}
