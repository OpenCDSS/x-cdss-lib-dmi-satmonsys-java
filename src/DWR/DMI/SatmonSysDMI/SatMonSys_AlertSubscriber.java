// ----------------------------------------------------------------------------
// SatMonSys_AlertSubscriber - class to hold data from the Alert Subscribers 
//	table.
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

public class SatMonSys_AlertSubscriber
extends DMIDataObject {

protected int _user_id = 		DMIUtil.MISSING_INT;
protected String _first_name = 		DMIUtil.MISSING_STRING;
protected String _last_name = 		DMIUtil.MISSING_STRING;
protected String _login = 		DMIUtil.MISSING_STRING;
protected String _password = 		DMIUtil.MISSING_STRING;
protected String _phone_number = 	DMIUtil.MISSING_STRING;
protected String _pager_number = 	DMIUtil.MISSING_STRING;
protected String _email_address = 	DMIUtil.MISSING_STRING;
protected String _admin_privileges = 	DMIUtil.MISSING_STRING;

/**
Constructor.
*/
public SatMonSys_AlertSubscriber() {
	super();
}

/**
Cleans up member variables.
*/
public void finalize() 
throws Throwable {
	_first_name = null;
	_last_name = null;
	_login = null;
	_password = null;
	_phone_number = null;
	_pager_number = null;
	_email_address = null;
	_admin_privileges = null;
	super.finalize();
}

/**
Returns _admin_privileges
@return _admin_privileges
*/
public String getAdmin_privileges() {
	return _admin_privileges;
}

/**
Returns _email_address
@return _email_address
*/
public String getEmail_address() {
	return _email_address;
}

/**
Returns _first_name
@return _first_name
*/
public String getFirst_name() {
	return _first_name;
}

/**
Returns _last_name
@return _last_name
*/
public String getLast_name() {
	return _last_name;
}

/**
Returns _login
@return _login
*/
public String getLogin() {
	return _login;
}

/**
Returns _pager_number
@return _pager_number
*/
public String getPager_number() {
	return _pager_number;
}

/**
Returns _password
@return _password
*/
public String getPassword() {
	return _password;
}

/**
Returns _phone_number
@return _phone_number
*/
public String getPhone_number() {
	return _phone_number;
}

/**
Returns _user_id
@return _user_id
*/
public int getUser_id() {
	return _user_id;
}

/**
Sets _admin_privileges
@param admin_privileges value to put in _admin_privileges
*/
public void setAdmin_privileges(String admin_privileges) {
	_admin_privileges = admin_privileges;
}

/**
Sets _email_address
@param email_address value to put in _email_address
*/
public void setEmail_address(String email_address) {
	_email_address = email_address;
}

/**
Sets _first_name
@param first_name value to put in _first_name
*/
public void setFirst_name(String first_name) {
	_first_name = first_name;
}

/**
Sets _last_name
@param last_name value to put in _last_name
*/
public void setLast_name(String last_name) {
	_last_name = last_name;
}

/**
Sets _login
@param login value to put in _login
*/
public void setLogin(String login) {
	_login = login;
}

/**
Sets _pager_number
@param pager_number value to put in _pager_number
*/
public void setPager_number(String pager_number) {
	_pager_number = pager_number;
}

/**
Sets _password
@param password value to put in _password
*/
public void setPassword(String password) {
	_password = password;
}

/**
Sets _phone_number
@param phone_number value to put in _phone_number
*/
public void setPhone_number(String phone_number) {
	_phone_number = phone_number;
}

/**
Sets _user_id
@param user_id value to put in _user_id
*/
public void setUser_id(int user_id) {
	_user_id = user_id;
}

/**
Returns a String representation of this object.
@return a String representation of this object.
*/
public String toString() {
	return "SatMonSys_AlertSubscriber {\n"
		+ "   user_id:           " + _user_id + "\n"
		+ "   first_name:       '" + _first_name + "'\n"
		+ "   last_name:        '" + _last_name + "'\n"
		+ "   login:            '" + _login + "'\n"
		+ "   password:         '" + _password + "'\n"
		+ "   phone_number:     '" + _phone_number + "'\n"
		+ "   pager_number:     '" + _pager_number + "'\n"
		+ "   email_address:    '" + _email_address + "'\n"
		+ "   admin_privileges: '" + _admin_privileges + "'\n}";
}

}
