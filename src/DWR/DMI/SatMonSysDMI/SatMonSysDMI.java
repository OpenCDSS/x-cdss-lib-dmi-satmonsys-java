// ----------------------------------------------------------------------------
// SatMonSysDMI.java
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// Notes:
// (1) This class must override determineDatabaseVersion() and readGlobalData()
// in DMI.java
// ----------------------------------------------------------------------------
// History:
//
// 2005-09-22	J. Thomas Sapienza, RTi	Initial version.
// 2005-10-12	JTS, RTi		Implements TSProductAnnotationProvider.
// 2005-10-18	JTS, RTi		Added constructor that takes a 
//					PropList.
// 2005-11-02	JTS, RTi		Doug Stenzel provided us with the 
//					official views and stored procedures to
//					use.  Updated the DMI to use these.
// 2005-12-14	JTS, RTi		* "local" is now recognized as a valid
//					  server name in the constructor that 
//					  takes a proplist.  In addition, if 
//					  local is specified to this constructor
//					  it will attempt to try the two known
//					  ports (one after the other) in the
//					  open() method.
//					* Added an open() method.
// 2006-02-08	JTS, RTi		Changed the colors and styles of alert
//					symbols.
// 2006-02-09	JTS, RTi		* Changed the title for real-time 
//					  streamflow with alarms graphs to say
//					  "Alerts" instead of "Alarms".
//					* Graphs now show all the possible
// 2006-03-27	JTS, RTi		Changed the TSP title "... Climate 
//					Precipiation" to just 
//					"... Precipitation"
// 2006-04-05	JTS, RTi		Changed the TSP title "... Reservoirs"
//					to "... Reservoir Releases"
// ----------------------------------------------------------------------------

package DWR.DMI.SatMonSysDMI;

import java.sql.ResultSet;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import RTi.DMI.DMI;
import RTi.DMI.DMIUtil;
import RTi.DMI.DMISelectStatement;
import RTi.DMI.DMIStatement;
import RTi.DMI.DMIStoredProcedureData;

import RTi.GRTS.TSProductAnnotationProvider;
import RTi.GRTS.TSProduct;

import RTi.TS.TSIdent;
import RTi.TS.TSLimits;
import RTi.TS.TSUtil;

import RTi.Util.IO.IOUtil;
import RTi.Util.IO.Prop;
import RTi.Util.IO.PropList;

import RTi.Util.Message.Message;

import RTi.Util.String.StringUtil;

import RTi.Util.Time.DateTime;
import RTi.Util.Time.DateTimeFormat;
/**
The SatMonSysDMI provides an interface to the Division of Water Resources
Satellite Monitoring System Database.
Most functionality is related to reading information from the database 
in order to support ISF DSS software.<p>

A database connection is made either by specifying a pre-defined ODBC DSN or by 
specifying a database server name, in which case SQL Server is assumed and the 
database name is assumed to be "RealTimeStreamflow".  In either case, a login 
can be specified using a SelectSatMonSysJDialog instance (to be developed).<p>
Once logged-in, SatMonSysDMI will determine the database version and will
read global data (none read yet) in order to allow faster lookups from memory.  
<p>

<b>SQL Method Naming Conventions</b><p>

Most methods in this class follow the naming conventions described below, with
exceptions being utility or look-up methods that do not actually execute SQL.
The first word in the method name is one of the following:<br>
<ol>
<li>read</li>
<li>write</li>
<li>delete</li>
<li>count</li>
</ol>

The second part of the method name is the data object being operated on.
If a list is returned, then "List" is included in the method name.
Finally, if a select based on a where clause is used, the method includes the
field for the Where.  Examples are:

<ol>
<li>	readMeasTypeList</li>
<li>	readMeasTypeForMeasType_num</li>
</ol>
<p>

The SatMonSysDMI code has migrated over time and will continue to change.  <p>
<b>Notes on versioning:</b><br>
Version changes require changes throughout the code base.  In general, the
SatMonSys data objects include data members for the most recent database design,
but place-holders for additional data from older designs may be retained.  New
database and software will ignore old values but the values will be filled in
if a software feature supports backward-compatibility and an old SatMonSys 
database happens to be used.  The following
example tells all the changes that need to be made when a new field is
added to an existing table:<p>
<ul>
<li>in buildSQL(), add the new field to all the select and write statement
sections for the appropriate table.  Do not forget to wrap this new code
with tests for the proper version (DMI.isDatabaseVersionAtLeast())</li>
<li>if, for the table XXXX, a method exists like:<br>
<code>private Vector toXXXXList</code><br>
then add the field to the Vector-filling code in this method</li>
<li>go to the SatMonSys_XXXX.java object that represents the table for
which this field was added.  Add the data member for the field, 
get/set statements, and then add the field (with brief information on the
version in which it was added) to the toString()</li>
<li>add the field, and the appropriate version-checking code, to the 
writeXXXX() method</li>
<li>update determineDatabaseVersion()</li>
</ul>
*/
public class SatMonSysDMI
extends DMI 
implements TSProductAnnotationProvider {

/**
The database version number.
*/
public final static long VERSION_20050922 = 2005092220050922L,
			 VERSION_LATEST   = VERSION_20050922;

/**
Reference numbers for particular queries.
*/
private final int
	// ALERT ARCHIVE
	__S_ALERT_ARCHIVE = 700,

	// ALERT CURRENT CONDITIONS
	__S_ALERT_CURRENT_CONDITIONS = 800,

	// ALERT PACKAGES
	__S_ALERT_PACKAGES = 900,

	// ALERT SETTINGS
	__S_ALERT_SETTINGS = 1000,

	// ALERT SUBSCRIBERS
	__S_ALERT_SUBSCRIBERS = 1100,
	
	// ALERT SUBSCRIPTIONS
	__S_ALERT_SUBSCRIPTIONS_VIEW_BY_USER_ID = 1200,

	__S_ARCHIVE_15_DAY = 1300,
	
	// STATION
	__S_STATION_BY_ABBREV = 2000,
	__S_STATION_BY_STATION_ID = 2010;

/**
Alert types supported.
*/
private final int 
	//__ALERT_UPPER_LIMIT = 0,
	__ALERT_LOWER_LIMIT = 1,
	//__ALERT_RISING_RATE = 2,
	__ALERT_FALLING_RATE = 3;

/**
The types of legend data that should be shown (one of the __ALERT_* values).
*/
private boolean[] __alertLegendTypes = null;

/**
Whether to show the alert legend annotation levels or symbols on the product 
or not.
*/
private boolean 
	__showAlertLegendLevels = false,
	__showAlertLegendSymbols = false;

/**
The last DMISelectStatement executed by the DMI.  This is used for stored
procedure connections in order that the stored procedures can be closed 
properly.
*/
//private DMISelectStatement __lastStatement = null;

/**
The hashtable that caches stored procedure information.
*/
private Hashtable __storedProcedureHashtable = null;

/**
The currently-logged in subscriber.
*/
private SatMonSys_AlertSubscriber __subscriber = null;

/**
The annotation provider name of the DMI.
*/
//private String __annotationProviderName = null;

/**
The login name of the user to log into the database.
*/
private String __userLogin = null;

/**
List of TSProductAnnotationProviders.
*/
private List __providers = new Vector();

private List __subtitles = new Vector();

/** 
Constructor for a connection that uses an ODBC connection.
@param database_engine the database_engine running the database.
@param odbc_name the name of the odbc_connection to which to connect.
@param system_login the login for the connection.
@param system_password the password for the password.
@throws Exception if an error occurs.
*/
public SatMonSysDMI(String database_engine, String odbc_name,
String system_login, String system_password )
throws Exception {
	super(database_engine, odbc_name, system_login, system_password);

	__storedProcedureHashtable = new Hashtable();

	if (odbc_name == null) {
		// Use the default...
		setODBCName("RealTimeStreamflow");
	}

	if (system_login == null) {
		// Use the default...
		setSystemLogin("CWCBISF");
	}
	if (system_password == null) {
		// Use the default...
		setSystemPassword("ISF%CWCB1");
	}
	
	setEditable(true);
	setSecure(true);
	setCapitalize(true);

	if (IOUtil.testing()) {
		dumpSQLOnError(true);
		dumpSQLOnExecution(true);
	}
}

/** 
Constructor for a database server and database name, to use an automatically created URL.
@param database_engine The database engine to use (see the DMI constructor).
If null, default to "SQLServer".
@param database_server The IP address or DSN-resolvable database server machine name.
@param database_name The database name on the server.  If null, default to "RealTimeStreamflow".
@param port Port number used by the database.  If negative, default to that for the database engine.
@param system_login If not null, this is used as the system login to make the
connection.  If null, the default system login is used.
@param system_password If not null, this is used as the system password to make
the connection.  If null, the default system password is used.
*/
public SatMonSysDMI(String database_engine, String database_server,
String database_name, int port, String system_login, String system_password)
throws Exception {
	// Use the default system login and password
	super(database_engine, database_server, database_name, port, system_login, system_password);
	
	__storedProcedureHashtable = new Hashtable();
	
	if (database_engine == null) {
		// Use the default...
		setDatabaseEngine("SQLServer");
	}

	if (database_name == null) {
		// Use the default...
		setDatabaseName("RealTimeStreamflow");
	}

	if (system_login == null) {
		// Use the default...
		setSystemLogin("CWCBISF");
	}

	if (system_password == null) {
		// Use the default...
		setSystemPassword("ISF%CWCB1");
	}
	
	setEditable(true);
	setSecure(true);
	setCapitalize(true);
	if (IOUtil.testing()) {
		dumpSQLOnError(true);
		dumpSQLOnExecution(true);
	}
}

/** 
Constructor for a database server and database name, to use an automatically created URL.
@param props a PropList containing properties controlling how to connect to the database.  
TODO (JTS - 2005-10-12) fill this out explaining the properties in use
*/
public SatMonSysDMI(PropList props) 
throws Exception {
	// Use the default system login and password
	super();

	String database_engine = null;
	String database_server = null;
	String database_name = null;
	String system_login = null;
	String system_password = null;
	String portS = null;

	String defaultDatabase_name = null;
	String defaultServer_name = null;

	String userLogin = null;

	if (props != null) {
		database_engine = props.getValue("ColoradoSMS.DatabaseEngine");
		database_server = props.getValue("ColoradoSMS.DatabaseServer");
		database_name = props.getValue("ColoradoSMS.DatabaseName");
		system_login = props.getValue("ColoradoSMS.SystemLogin");
		system_password = props.getValue("ColoradoSMS.SystemPassword");
		portS = props.getValue("ColoradoSMS.Port");

		defaultDatabase_name = props.getValue("ColoradoSMS.DefaultDatabaseName");
		defaultServer_name = props.getValue("ColoradoSMS.DefaultServerName");

		userLogin = props.getValue("ColoradoSMS.UserLogin");
	}
	
	if (database_engine == null) {
		database_engine = "SQLServer";
	}

	if (database_server == null) {
		if (defaultServer_name == null) {
			throw new Exception(
				"No ColoradoSMS.DefaultServerName or ColoradoSMS.DatabaseServer property set in "
				+ "configuration file.");
		}
		else {
			database_server = defaultServer_name;
		}
	}

	if (database_name == null) {
		if (defaultDatabase_name == null) {
			database_name = "RealTimeStreamflow";
		}
		else {
			database_name = defaultDatabase_name;
		}
	}

	if (system_login == null) {
		system_login = "CWCBISF";
	}

	if (system_password == null) {
		system_password = "ISF%CWCB1";
	}
	
	int port = -1;
	if (portS != null) {
		port = StringUtil.atoi(portS);
	}
	else {
		// TODO (JTS - 2005-12-14) change "local" to a static final string in HydroBase_Util so 
		// that it can be referenced everywhere by a variable.
		if (database_server.equalsIgnoreCase("local")
		    || IOUtil.getProgramHost().equalsIgnoreCase(database_server)) {
		   	// Connecting to the local machine.  Try the MSDE port first.
			database_server = IOUtil.getProgramHost();
			__localPorts = new int[3];
			__localPorts[0] = 5758;
			__localPorts[1] = 21784;
			__localPorts[2] = 1433;
			port = __localPorts[0];
		}
		else {
		    // Connecting to a remote machine.  Try the SQL Server port first.
			__localPorts = new int[3];
			__localPorts[0] = 1433;
			__localPorts[1] = 5758;
			__localPorts[2] = 21784;
			port = __localPorts[0];
		}
	}

	if (userLogin != null) {
		__userLogin = userLogin;
	}

	__storedProcedureHashtable = new Hashtable();

	initialize(database_engine, database_server, database_name, port,
		system_login, system_password, null, true);
}

private static int[] __localPorts = null;

/**
Adds an alert legend to a product from a SatMonSys Annotation Provider.  
This is a helper method for addAnnotation().
@param product the TSProduct to which to add alert annotations.
@param subProduct the subProduct (base-0) under which to add the annotations.
@param annotationNum the number (base-0) of the first alert annotation number
to add.
@return the number (base-0) of the first alert annotation number to add, after
any annotation alerts have been added in this method.  This value will be
passed into any further calls to addAnnotationHelper() as the annotationNum
parameter.  This is to ensure that all annotations are numbered sequentially.<p>
This value is not returned as the number of annotations added because the 
current number of the last annotation added is already internally tracked in 
this method, and if more than one set of annotations is added a TSProduct this
value will be the value required again as the input to this method 
(annotationNum).  Since this code is never called directly by a developer and
only by addAnnotation, this is the best way to do it, though perhaps not the
clearest.
*/
private int addAlertLegend(TSProduct product, int subProduct,
int annotationNum) {
	// If any threshold level annotations were added, add their legend
	// information.
	if (__showAlertLegendLevels) {
		product.setPropValue("ShapeType", "Line", 
			subProduct, annotationNum, true);
		product.setPropValue("Color", "black",
			subProduct, annotationNum, true);
		product.setPropValue("AnnotationID", "Threshold Key",
			subProduct, annotationNum, true);
		product.setPropValue("Order", "OnTopOfData",
			subProduct, annotationNum, true);
		product.setPropValue("LineStyle", "Solid",
			subProduct, annotationNum, true);
		product.setPropValue("XAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("YAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("LineWidth", "2",
			subProduct, annotationNum, true);
		product.setPropValue("Points", "5,92,7,92",
			subProduct, annotationNum, true);
		annotationNum++;
	
		product.setPropValue("ShapeType", "Text", 
			subProduct, annotationNum, true);
		product.setPropValue("Color", "black",
			subProduct, annotationNum, true);
		product.setPropValue("AnnotationID", "Threshold Key Text",
			subProduct, annotationNum, true);
		product.setPropValue("Order", "OnTopOfData",
			subProduct, annotationNum, true);
		product.setPropValue("XAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("YAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("FontSize", "12",
			subProduct, annotationNum, true);
		product.setPropValue("Point", "8,92",
			subProduct, annotationNum, true);
		product.setPropValue("FontStyle", "Plain",
			subProduct, annotationNum, true);
		product.setPropValue("FontName", "Arial",
			subProduct, annotationNum, true);
		product.setPropValue("Text", "Alert Threshold",
			subProduct, annotationNum, true);
		product.setPropValue("TextPosition", "Right",
			subProduct, annotationNum, true);
		annotationNum++;
	}

	if (!__showAlertLegendSymbols) {	
		// if false, then none were added to the product.
		return annotationNum;
	}

	// Finally, for every type of annotation that was added to the 
	// TSProduct, add a line to the legend explaining the symbol and
	// what it represents.

	int startPercent = 86;

	String s = null;

	for (int i = 0; i < __alertLegendTypes.length; i++) {
		__alertLegendTypes[i] = true;
		/*
		Commented out by JTS 2006-02-09
		This is not needed by the ISF group, so disabled.
		if (i == __ALERT_UPPER_LIMIT && __alertLegendTypes[i]) {
			product.setPropValue("SymbolStyle", 
				"Triangle-Up-Hollow-Topline",
				subProduct, annotationNum, true);
			s = "Upper Limit";
		}
		*/
		if (i == __ALERT_LOWER_LIMIT && __alertLegendTypes[i]) {
			product.setPropValue("SymbolStyle", 
				"Triangle-Down-Filled-Botline", 
				subProduct, annotationNum, true);
			product.setPropValue("OutlineColor", "Black",
				subProduct, annotationNum, true);
			s = "Lower Limit (Current)";
		}
		/*
		Commented out by JTS 2006-02-09
		This is not needed by the ISF group, so disabled.
		else if (i == __ALERT_RISING_RATE && __alertLegendTypes[i]) {
			product.setPropValue("SymbolStyle", 
				"Triangle-Up-Hollow",
				subProduct, annotationNum, true);
			s = "Rising Rate";
		}
		*/
		else if (i == __ALERT_FALLING_RATE && __alertLegendTypes[i]) {
			product.setPropValue("SymbolStyle", 
				"Triangle-Down-Filled",
				subProduct, annotationNum, true);
			product.setPropValue("OutlineColor", "Black",
				subProduct, annotationNum, true);
			s = "Falling Rate (Current)";
		}
		else {
			s = "Unknown";
			continue;
		}
		
		product.setPropValue("ShapeType", "Symbol", 
			subProduct, annotationNum, true);
		product.setPropValue("SymbolSize", "10", 
			subProduct, annotationNum, true);
		product.setPropValue("Color", "Red", 
			subProduct, annotationNum, true);
		product.setPropValue("AnnotationID", s + " Symbol",
			subProduct, annotationNum, true);
		product.setPropValue("SymbolPosition", "Center",
			subProduct, annotationNum, true);
		product.setPropValue("Order", "OnTopOfData",
			subProduct, annotationNum, true);
		product.setPropValue("XAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("YAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("Point", "6," + startPercent,
			subProduct, annotationNum, true);
		annotationNum++;		

		product.setPropValue("ShapeType", "Text", 
			subProduct, annotationNum, true);
		product.setPropValue("AnnotationID", s + " Text",
			subProduct, annotationNum, true);
		product.setPropValue("Order", "OnTopOfData",
			subProduct, annotationNum, true);
		product.setPropValue("XAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("YAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("FontSize", "12",
			subProduct, annotationNum, true);
		product.setPropValue("Point", "8," + startPercent,
			subProduct, annotationNum, true);
		product.setPropValue("FontStyle", "Plain",
			subProduct, annotationNum, true);
		product.setPropValue("FontName", "Arial",
			subProduct, annotationNum, true);
		product.setPropValue("Text", s,
			subProduct, annotationNum, true);
		product.setPropValue("TextPosition", "Right",
			subProduct, annotationNum, true);
		annotationNum++;

		startPercent -= 6;
	}

	startPercent = 86;

	for (int i = 0; i < __alertLegendTypes.length; i++) {
		__alertLegendTypes[i] = true;
		/*
		Commented out by JTS 2006-02-09
		This is not needed by the ISF group, so disabled.
		if (i == __ALERT_UPPER_LIMIT && __alertLegendTypes[i]) {
			product.setPropValue("SymbolStyle", 
				"Triangle-Up-Hollow-Topline",
				subProduct, annotationNum, true);
			s = "Upper Limit";
		}
		*/
		if (i == __ALERT_LOWER_LIMIT && __alertLegendTypes[i]) {
			product.setPropValue("SymbolStyle", 
				"Triangle-Down-Hollow-Botline", 
				subProduct, annotationNum, true);
			s = "Lower Limit (Archive)";
		}
		/*
		Commented out by JTS 2006-02-09
		This is not needed by the ISF group, so disabled.
		else if (i == __ALERT_RISING_RATE && __alertLegendTypes[i]) {
			product.setPropValue("SymbolStyle", 
				"Triangle-Up-Hollow",
				subProduct, annotationNum, true);
			s = "Rising Rate";
		}
		*/
		else if (i == __ALERT_FALLING_RATE && __alertLegendTypes[i]) {
			product.setPropValue("SymbolStyle", 
				"Triangle-Down-Hollow",
				subProduct, annotationNum, true);
			s = "Falling Rate (Archive)";
		}
		else {
			s = "Unknown";
			continue;
		}
		
		product.setPropValue("ShapeType", "Symbol", 
			subProduct, annotationNum, true);
		product.setPropValue("SymbolSize", "10", 
			subProduct, annotationNum, true);
		product.setPropValue("Color", "Red", 
			subProduct, annotationNum, true);
		product.setPropValue("AnnotationID", s + " Symbol",
			subProduct, annotationNum, true);
		product.setPropValue("SymbolPosition", "Center",
			subProduct, annotationNum, true);
		product.setPropValue("Order", "OnTopOfData",
			subProduct, annotationNum, true);
		product.setPropValue("XAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("YAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("Point", "30," + startPercent,
			subProduct, annotationNum, true);
		annotationNum++;		

		product.setPropValue("ShapeType", "Text", 
			subProduct, annotationNum, true);
		product.setPropValue("AnnotationID", s + " Text",
			subProduct, annotationNum, true);
		product.setPropValue("Order", "OnTopOfData",
			subProduct, annotationNum, true);
		product.setPropValue("XAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("YAxisSystem", "Percent",
			subProduct, annotationNum, true);
		product.setPropValue("FontSize", "12",
			subProduct, annotationNum, true);
		product.setPropValue("Point", "32," + startPercent,
			subProduct, annotationNum, true);
		product.setPropValue("FontStyle", "Plain",
			subProduct, annotationNum, true);
		product.setPropValue("FontName", "Arial",
			subProduct, annotationNum, true);
		product.setPropValue("Text", s,
			subProduct, annotationNum, true);
		product.setPropValue("TextPosition", "Right",
			subProduct, annotationNum, true);
		annotationNum++;

		startPercent -= 6;
	}

	product.stopAddingHiddenProps();

	return annotationNum;
}

/**
Adds alert annotations to a product from a SatMonSys Annotation Provider.  
This is a helper method for addAnnotation().
@param product the TSProduct to which to add alert annotations.
@param alertNum the alert_num for which to read alert settings from the 
database.
@param subProduct the subProduct (base-0) under which to add the annotations.
@param annotationNum the number (base-0) of the first alert annotation number
to add.
@return the number (base-0) of the first alert annotation number to add, after
any annotation alerts have been added in this method.  This value will be
passed into any further calls to addAnnotationHelper() as the annotationNum
parameter.  This is to ensure that all annotations are numbered sequentially.<p>
This value is not returned as the number of annotations added because the 
current number of the last annotation added is already internally tracked in 
this method, and if more than one set of annotations is added a TSProduct this
value will be the value required again as the input to this method 
(annotationNum).  Since this code is never called directly by a developer and
only by addAnnotation, this is the best way to do it, though perhaps not the
clearest.
*/
private int addAlertLevels(TSProduct product, int alertNum, int subProduct, 
int annotationNum) {
	String routine = "addAlertLevels()";

	product.startAddingHiddenProps();

	// Read the settings for the given alert.  These define the lines that
	// are drawn on the time series showing threshold levels for alerts.

	List settingsV = null;
	try {
		settingsV = readAlertSettingsForAlert_num(alertNum);
	}
	catch (Exception e) {
		Message.printWarning(2, routine,
			"An error occurred reading alert settings for "
			+ "alert_num: " + alertNum + ".");
		Message.printWarning(3, routine, e);
		product.stopAddingHiddenProps();
		return annotationNum;
	}

	// Get the period of the time series on which the alerts will be
	// plotted.  This is done so that alert thresholds that are repeated
	// year-to-year can be started in a year prior to the time series and
	// ended in a year after the time series, so that they repeat for the
	// entire duration of the time series graph.

	// This ensures that the edges of the graph will include the levels as
	// annotations.  Graph clipping ensures that annotations are shown
	// only within the graph boundaries.

	List tslist = product.getTSList();
	TSLimits limits = null;
	try {
		limits = TSUtil.getPeriodFromTS(tslist, TSUtil.MAX_POR);
	}
	catch (Exception e) {
		Message.printWarning(2, routine,
			"Could not get maximum period of record from time "
			+ "series list.");
		Message.printWarning(3, routine, e);
		product.stopAddingHiddenProps();
		return annotationNum;
	}
	
	int firstYear = limits.getDate1().getYear();
	int lastYear = limits.getDate2().getYear();

	// make sure the threshold lines start before any time series and end
	// after every time series, so that they loop throughout the duration
	// of the graph

	firstYear--;
	lastYear++;

	// loop through all the alert thresholds and draw them as lines on 
	// the graph.

	DateTime start = null;
	DateTime end = null;
	double checkValue = 0;
	SatMonSys_AlertSettings settings = null;
	int size = settingsV.size();

	// The DateTimeFormat used to format dates for output
	DateTimeFormat format = new DateTimeFormat("mm/dd");
	// The DateTimeFormat used to parse database dates for input
	DateTimeFormat parse = new DateTimeFormat("m/d");

	// Different formats on input and output are used for verbosity's
	// sake.  The input strings may have 1 or 2-digit months and days
	// and are inconsistent.  The output format ensures that all dates 
	// look consistent, with two-digit month and day values.

	// size defines the number of alert settings set on a station.  A 
	// station can have multiple alert threshold levels set on it, and
	// this sets up annotations for them all.
	for (int i = 0; i < size; i++) { 
		settings = (SatMonSys_AlertSettings)settingsV.get(i);

		try {
			start = parse.parse(settings.getStart_date());
			end = parse.parse(settings.getEnd_date());
		}
		catch (Exception e) {
			Message.printWarning(2, routine,
				"Could not generate start and end dates for "
				+ "alert threshold with start date: '" 
				+ settings.getStart_date() + "', end date: '"
				+ settings.getEnd_date() + "'.");
			Message.printWarning(3, routine, e);

			// try drawing the next threshold level
			continue;
		}
	
		start.setYear(firstYear);
		end.setYear(firstYear);
	
		checkValue = settings.getCheck_value();

		// Both dates have had their year set to the same value (above).
		// However, if the date/time in 'end' is less than the date/time
		// in 'start', 'end' needs to have its year incremented, as the 
		// threshold loops around the end of one year into the next.
	
		if (start.greaterThan(end)) {
			end.addYear(1);
		}

		// when drawing from one date to another, the line normally
		// draws only to the beginning of the end date instead of 
		// drawing a line through the entire end day.  Thus the 
		// following piece of (end.addDay(1)), which insures that 
		// the line will draw for the entire end day, instead of 
		// only to the first hour of the day.
		end.addDay(1);

		__subtitles.add(settings.getStart_date() + "-" 
			+ settings.getEnd_date() + ": " 
			+ settings.getCheck_value());

		// Loop through all the years and add the alert threshold
		// annotation lines.
	
		product.setPropValue("XFormat", "DateTime,mm/dd",
			subProduct, annotationNum, true);
		product.setPropValue("ShapeType", "Line", 
			subProduct, annotationNum, true);
		product.setPropValue("Order", "BehindData",
			subProduct, annotationNum, true);
		product.setPropValue("LineStyle", "Solid",
			subProduct, annotationNum, true);
		product.setPropValue("XAxisSystem", "Data",
			subProduct, annotationNum, true);
		product.setPropValue("YAxisSystem", "Data",
			subProduct, annotationNum, true);
		product.setPropValue("LineWidth", "2",
			subProduct, annotationNum, true);
		product.setPropValue("Color", "black",
			subProduct, annotationNum, true);
		product.setPropValue("AnnotationID", 
			"Alert Line (" + checkValue + ")",
			subProduct, annotationNum, true);
		product.setPropValue("Points", 
			"" + format.format(start) + "," + checkValue
			+ "," + format.format(end) + "," + checkValue,
			subProduct, annotationNum, true);

		annotationNum++;
	}

	// size is the number of alert thresholds set on the graph.  If any 
	// have been defined, the legend is drawn as well.
	if (size > 0) {
		__showAlertLegendLevels = true;
	}

	product.stopAddingHiddenProps();
	return annotationNum;
}

/**
Adds alert annotations to a product from a SatMonSys Annotation Provider.  
This is a helper method for addAnnotation().
@param product the TSProduct to which to add alert annotations.
@param abbrev the station abbreviation of the station for which to add 
annotations.
@param alertNum the alert_num for which to read alert settings from the 
database.
@param subProduct the subProduct (base-0) under which to add the annotations.
@param annotationNum the number (base-0) of the first alert annotation number
to add.
@param showCurrentData if true, then current condition data annotations will
be added.
@param showArchiveData if true, then archive condition data annotations will
be added.
@return the number (base-0) of the first alert annotation number to add, after
any annotation alerts have been added in this method.  This value will be
passed into any further calls to addAnnotationHelper() as the annotationNum
parameter.  This is to ensure that all annotations are numbered sequentially.<p>
This value is not returned as the number of annotations added because the 
current number of the last annotation added is already internally tracked in 
this method, and if more than one set of annotations is added a TSProduct this
value will be the value required again as the input to this method 
(annotationNum).  Since this code is never called directly by a developer and
only by addAnnotation, this is the best way to do it, though perhaps not the
clearest.
*/
private int addAlertSettings(TSProduct product, String abbrev, int alertNum, 
int subProduct, int annotationNum, boolean showCurrentData,
boolean showArchiveData) {
	String routine = "addAnnotationHelper";

	product.startAddingHiddenProps();

	// read archived alert data points

	List v1 = new Vector();
	if (showArchiveData) {
		try {
			v1 = readAlertArchiveForAlert_num(alertNum);
		}
		catch (Exception e) {
			Message.printWarning(2, routine,
				"Error reading alert archive data for alert #: "
				+ alertNum);
			Message.printWarning(3, routine, e);
			v1 = new Vector();
		}
	}

	// read current alert data points

	List v2 = new Vector();
	if (showCurrentData) {
		try {
			v2 = readAlertCurrentConditionsForAlert_num(alertNum);
		}
		catch (Exception e) {
			Message.printWarning(2, routine,
				"Error reading alert current conditions "
				+ "for alert #: " + alertNum);
			Message.printWarning(3, routine, e);
			v2 = new Vector();
		}
	}

	// the data are all SatMonSys_AlertData, so combine them into a single
	// Vector for easier processing.

	// REVISIT (JTS - 2005-11-01)
	// currently do not support different symbols for different stations.
	// May change in the future.

	List dataV = new Vector();
	// Make sure to add the current value first (important for later)
	for (int i = 0; i < v2.size(); i++) {
		dataV.add(v2.get(i));
	}
	for (int i = 0; i < v1.size(); i++) {
		dataV.add(v1.get(i));
	}

	boolean current = true;
	DateTime dt = null;
	int size = dataV.size();

	if (size == 0) {
		// no data to add
		return annotationNum;
	}
	
	SatMonSys_AlertData data = null;
	String s = null;

	// Loop through all the alert points and add them as annotation symbols.

	// Threshold periods are defined with precision of day, but 
	// the actual alert settings are defined with the precision 
	// of minute.  So even though the threshold levels are drawn
	// only with day precision, alert points are drawn with 
	// precision to the minute.

	String fullFormat = "yyyy-mm-dd hh:MM";
	DateTimeFormat format = new DateTimeFormat(fullFormat);

	String xFormat = "DateTime," + fullFormat;

	boolean delta = false;
	double amt = 0;
	SatMonSys_CombinedRealTimeResults15DayArchive archive = null;

	// switch to true when it's time to add this capability to the software.
	boolean ALLOW_UPPER_AND_RISING = false;

	Date currentDate = null;
	String symbolType = null;

	for (int i = 0; i < size; i++) {
		data = (SatMonSys_AlertData)dataV.get(i);
		delta = false;
		
		current = true;
		if (!DMIUtil.isMissing(data.getDiv())) {
			current = false;
		}

		if (current) {
			// the following is OK because current data is added
			// first to the Vector (see above)
			currentDate = data.getDate_time();
		}
		else {
			if (currentDate != null) {	
				if (currentDate.equals(data.getDate_time())) {
					continue;
				}
			}
		}

		s = data.getAlert_type_desc();

		if (s.equals("Upper Limit") || s.equals("Rising Rate")) {
			if (!ALLOW_UPPER_AND_RISING) 
			continue;
		}

		// Make sure, for delta values, that data exists in the
		// 15 day archive.  If not, continue to the next annotation
		// to be added.
		if (!current 
		    && (s.equals("Rising Rate") || s.equals("Falling Rate"))) {
		    	delta = true;
			try {
				archive = 
	       readCombinedRealTimeResults15DayArchiveForAbbrevVariableDatetime(
		       			abbrev,"GAGE_HT", data.getDate_time());
				if (archive == null) {
					continue;
				}
				else {
					amt = archive.getAmt();	
				}
			}
			catch (Exception e) {
				amt = 0;
				Message.printWarning(2, routine, 
					"Unable to read data amount for delta "
					+ "at date: '" + data.getDate_time()
					+ "'.");
				Message.printWarning(3, routine, e);
			}			
		}
		else {
			amt = data.getAmt();
		}

		Message.printDebug(1, "", "Amt: " + amt + "\t"
			+ data.getDate_time() + "\t[" + delta + "]");

		if (ALLOW_UPPER_AND_RISING) {
		if (s.equals("Upper Limit")) {
			if (current) {
				symbolType = "Triangle-Up-Filled-Topline";
			}
			else {
				symbolType = "Triangle-Up-Hollow-Topline";
			}
			product.setPropValue("SymbolStyle", symbolType,
				subProduct, annotationNum, true);
			product.setPropValue("SymbolPosition", "Below", 
				subProduct, annotationNum, true);
		}
		else if (s.equals("Rising Rate")) {
			if (current) {
				symbolType = "Triangle-Up-Filled";
			}
			else {
				symbolType = "Triangle-Up-Hollow";
			}
			product.setPropValue("SymbolStyle", symbolType,
				subProduct, annotationNum, true);
			product.setPropValue("SymbolPosition", "Below", 
				subProduct, annotationNum, true);
		}
		}
		else if (s.equals("Lower Limit")) {
			if (current) {
				symbolType = "Triangle-Down-Filled-BotLine";
			}
			else {
				symbolType = "Triangle-Down-Hollow-BotLine";
			}
			product.setPropValue("SymbolStyle", symbolType,
				subProduct, annotationNum, true);
			product.setPropValue("SymbolPosition", "Above", 
				subProduct, annotationNum, true);
		}
		else if (s.equals("Falling Rate")) {
			if (current) {
				symbolType = "Triangle-Down-Filled";
			}
			else {
				symbolType = "Triangle-Down-Hollow";
			}
			product.setPropValue("SymbolStyle", symbolType,
				subProduct, annotationNum, true);
			product.setPropValue("SymbolPosition", "Above", 
				subProduct, annotationNum, true);
		}
		else {
			product.setPropValue("SymbolStyle", "Circle-Filled", 
				subProduct, annotationNum, true);
			product.setPropValue("SymbolPosition", 
				"Center", subProduct, annotationNum, true);
		}

		if (current) {
			product.setPropValue("Color", "Red", 
				subProduct, annotationNum, true);
			product.setPropValue("OutlineColor", "Black", 
				subProduct, annotationNum, true);
		}
		else {
			product.setPropValue("Color", "Red", 
				subProduct, annotationNum, true);
		}

		dt = new DateTime(data.getDate_time());

		product.setPropValue("ShapeType", "Symbol", 
			subProduct, annotationNum, true);
		product.setPropValue("XFormat", xFormat,
			subProduct, annotationNum, true);
		product.setPropValue("SymbolSize", "10", 
			subProduct, annotationNum, true);
		product.setPropValue("Order", "OnTopOfData",
			subProduct, annotationNum, true);
		product.setPropValue("XAxisSystem", "Data",
			subProduct, annotationNum, true);
		product.setPropValue("YAxisSystem", "Data",
			subProduct, annotationNum, true);
		product.setPropValue("AnnotationID", 
			"Alert (" + format.format(dt) + ", " + amt 
			+ ")", subProduct, annotationNum, true);
		product.setPropValue("Point", 
			"" + format.format(dt) + "," + amt,
			subProduct, annotationNum, true);
		annotationNum++;

		__showAlertLegendSymbols = true;
	}

	product.stopAddingHiddenProps();
	return annotationNum;
}
/**
Adds annotations and adjusts other properties for a TSProduct.  <p>
The TSProduct is updated in the following ways:<p>
<ul>
<li>The title is set.</li>
<li>The subtitle is set.</li>
<li>If any alert levels are read from the database, an annotation for each one
is added so that they are drawn as lines on the graph.</li>
<li>If any current condition alerts exist, they are drawn on the graph.</li>
<li>If any archive alerts exist, they are drawn on the map.</li>
<li>If alert levels or current/archive alerts exist, a legend to accompany them
is drawn in the upper-left of the graph.</li>
</ul>
@param product the TSProduct to which to add annotations.  Cannot be null.
@param controlProps properties to add additional information.  Currently 
unused.
*/
public void addAnnotations(TSProduct product, PropList controlProps) 
throws Exception {
	String routine = "addAnnotations";

	__showAlertLegendLevels = false;
	__showAlertLegendSymbols = false;
	__alertLegendTypes = new boolean[4];
	for (int i = 0; i < 4; i++) {
		__alertLegendTypes[i] = false;
	}
	__subtitles = new Vector();

	int numSubProducts = product.getNumSubProducts();
	int numData = 0;
	String abbrev = null;
	String propVal = null;
	TSIdent ident = null;

	// loop through all the subproducts and the subproduct data looking for
	// the property "AnnotationProvider".  When an annotation provider is 
	// found to be defined for a data section, get the appropriate alerts
	// from the provider for the data.

	if (product == null) {
		throw new Exception("Cannot add annotations to a null "
			+ "TSProduct.");
	}

	int how = product.getPropsHowSet();
	product.setPropsHowSet(Prop.SET_AT_RUNTIME_FOR_USER, false);

	// determine the SMS id number of the user for whom to query alerts
	if (__subscriber == null && !__readSubscriber) {
		readUserData(__userLogin);
	}
	
	if (__subscriber == null && __readSubscriber) {
		Message.printWarning(2, routine,
			"No user has been logged in.  A user must be logged "
			+ "in before alert annotations can be added to a "
			+ "product.  Processing will not continue.");
		product.setPropsHowSet(how, false);
		return;
	}		
	
	int userID = __subscriber.getUser_id();
		// the userID is the number identifying this user in the 
		// database, and is used to determine the alerts to which they
		// are subscribed.

	List alertSubscriptions = readAlertSubscriptionsViewForUser_id(userID);

	Message.printDebug(1, "", "Processing TSProduct for annotations");

	List idents = new Vector();

	int annotationNum = 0;
	String type = null;
	SatMonSys_AlertSubscriptionView view = null;
	int size = 0;

	boolean showArchiveAlerts = true;
	boolean showCurrentAlerts = true;

	String annoType = null;
	String s = null;
	String source = null;
	boolean alarms = false;

	for (int i = 0; i < numSubProducts; i++) {
		propVal = product.getLayeredPropValue(
			"AnnotationProvider", i, -1, false);
		Message.printDebug(1, "", "SP #" + i + ": '" + propVal + "'");
		if (propVal == null) {
			continue;
		}

		if (!provides(propVal)) {
			continue;
		}

		if (annoType == null) {
			annoType = propVal;
		}

		if (propVal.equalsIgnoreCase(__PROVIDER_HISTORICAL) 
		    || propVal.equalsIgnoreCase(__PROVIDER_REALTIME)) {
			s = addNonAlertAnnotations(propVal, product, i);	
			if (s != null) {
				source = s;
			}
			continue;
		}

		alarms = true;

		// Determine the number of the first annotation to add to 
		// the product
		annotationNum = product.getNumAnnotations(i);
		numData = product.getNumData(i);
		for (int j = 0; j < numData; j++) {
			propVal = product.getLayeredPropValue(
				"TSID", i, j, false);
			try {
				ident = new TSIdent(propVal);
			}
			catch (Exception e) {
				Message.printWarning(2, routine,
					"Error in TSID: \"" + propVal + "\".");
				Message.printWarning(3, routine, e);
				continue;
			}

			idents.add(ident);
			s = ident.getMainType();
			if (s != null) {
				source = s;
			}
			// Get the station abbreviation, to know which alerts 
			// to query.
			abbrev = lookupStationAbbrev(ident);
			type = ident.getSubType();
			// type is used to match a time series against a station
			// in the database and get that station's alert settings
	
			size = alertSubscriptions.size();
			for (int k = 0; k < size; k++) {
				view = (SatMonSys_AlertSubscriptionView)
					alertSubscriptions.get(k);
/*
		Message.printDebug(1, "", "Abbrev: '" + abbrev + "'");
		Message.printDebug(1, "", "Type:   '" + type + "'");
		Message.printDebug(1, "", "View: \n" + view);
*/
				// check to see that the alert is for the 
				// specified station
				if (view.getAbbrev().equalsIgnoreCase(abbrev)
				    && view.getResult_name().equalsIgnoreCase(
				    type)) {
				    	__subtitles.add("ABBREV: " + abbrev);
				    	annotationNum = addAlertLevels(
						product, view.getAlert_num(), 
						i, annotationNum);
				    	annotationNum = addAlertSettings(
						product, abbrev, 
						view.getAlert_num(), 
						i, annotationNum,
						showCurrentAlerts,
						showArchiveAlerts);
				}
			}
		}
	}

	if (__showAlertLegendLevels || __showAlertLegendSymbols) {
		addAlertLegend(product, 0, product.getNumAnnotations(0));
	}

	String subtitle = "";

	int count1 = 0;
	int count2 = 0;
	size = __subtitles.size();

	if (alarms) {
		for (int i = 0; i < size; i++) {
			s = (String)__subtitles.get(i);
			if (s.startsWith("ABBREV: ")) {
				s = s.substring(8);
				count1++;
	
				if (count1 > 1) {
					subtitle += "), ";
				}
				subtitle += s + " (";
				count2 = 0;
			}
			else {
				count2++;
	
				if (count2 > 1) {
					subtitle += ", ";
				}
				subtitle += s;
			}
		}
	
		if (size > 0) {
			subtitle += ")";
		}
	}
	else {
		for (int i = 0; i < size; i++) {
			s = (String)__subtitles.get(i);
			if (s.startsWith("ABBREV: ")) {
				s = s.substring(8);
				count1++;
	
				if (count1 > 1) {
					subtitle += ", ";
				}
				subtitle += s;
			}
		}
	}

	if (source == null) {
		source = "";
	}
	else if (source.equalsIgnoreCase("Precip")) {
		source = "Precipitation";
	}
	else if (source.equalsIgnoreCase("Streamflow")) {
		source = "Streamflow";
	}
	else if (source.equalsIgnoreCase("DivTotal")) {
		source = "Diversions";
	}
	else if (source.equalsIgnoreCase("RelTotal")) {	
		source = "Reservoir Releases";
	}

	if (annoType == null) {}
	else if (annoType.equalsIgnoreCase(__PROVIDER_REALTIME_WITH_ALARMS)) {
		product.setPropValue("MainTitleString", 
			"Real-time " + source + " with Alerts", -1, -1);
	}
	else if (annoType.equalsIgnoreCase(__PROVIDER_HISTORICAL)) {
		product.setPropValue("MainTitleString", "Historical " + source,
			-1, -1);
	}
	else if (annoType.equalsIgnoreCase(__PROVIDER_REALTIME)) {
		product.setPropValue("MainTitleString", "Real-time " + source, 
			-1, -1);
	}

	product.setPropValue("SubTitleString", subtitle, -1, -1);

	product.setPropsHowSet(how, false);
}

/**
For historical and realtime time series, adds non-alert based annotations
to the graph.  Basically, adds a subtitle that displays the abbreviation
for the time series.
*/
private String addNonAlertAnnotations(String providerName, TSProduct product,
int numSubProduct) {
	String routine = "addNonAlertAnnotations";

	// Determine the number of the first annotation to add to 
	// the product
	//int annotationNum = product.getNumAnnotations(numSubProduct);
	int numData = product.getNumData(numSubProduct);
	String abbrev;
	TSIdent ident = null;
	String propVal = null;
	String source = null;
	for (int i = 0; i < numData; i++) {
		propVal = product.getLayeredPropValue("TSID", numSubProduct, i,
			false);

		try {
			ident = new TSIdent(propVal);
		}
		catch (Exception e) {
			Message.printWarning(2, routine,
				"Error in TSID: \"" + propVal + "\".");
			Message.printWarning(3, routine, e);
			continue;
		}

		abbrev = lookupStationAbbrev(ident);

		if (abbrev == null) {
			// default to a value in the TSIdent
			abbrev = ident.getMainLocation();
		}

		__subtitles.add("ABBREV: " + abbrev);

		source = ident.getMainType();
	}

	return source;
}

/**
Adds an annotation provider to be supported by this dmi.
@param name of the annotation provider to add.
*/
public void addAnnotationProvider(String name) {
	__providers.add(name);
}

/** 
Build an SQL string based on a requested SQL statement code.  This defines 
the basic statement and allows overloaded methods to avoid redundant code.
This method is used to eliminate redundant code where methods use the same
basic statement but with different where clauses.
@param statement Statement to set values in.
@param sqlNumber the number of the SQL statement to build.  Usually defined
as a private constant as a mnemonic aid.
@return a string containing the full SQL.
*/
private void buildSQL (DMIStatement statement, int sqlNumber)
throws Exception {

	if (canSetUpStoredProcedure(statement, sqlNumber)) {
		// where, distinct and order by clauses can be set
		// as usual for other types of queries.
		return;
	}
}

/**
Checks to see if a stored procedure can be used for the given statement.  If
so, the statement object is modified with the definition of the stored procedure
to be executed, and true is return.  If a stored procedure cannot be used
for this statement, false is returned and no changes are made to the 
statement.
@param statement the statement that might be set up as a stored procedure.
@param sqlNumber the int value that refers to the specific statement being
executed, the same value as used by buildSQL().
@return true if the statement can be set up as a stored procedure.  Otherwise, 
return false.
@throws Exception if there is any error in reading data from the database
while querying stored procedure information.
*/
private boolean canSetUpStoredProcedure(DMIStatement statement, int sqlNumber) 
throws Exception {
	boolean debug = false;
	if (IOUtil.testing() && debug) {
		Message.printStatus(2, "", "Checking to see if SQLNumber " 
			+ sqlNumber + " has a stored procedure.");
	}
			
	String name = null;

	// Look up the name of the stored procedure that maps to the specified
	// sqlNumber value.  

	switch (sqlNumber) {
		case __S_ALERT_ARCHIVE:
			name = "usp_CWCBISF_ALERT_AlertArchive_Sel_By_AlertNum";
			break;
		case __S_ALERT_CURRENT_CONDITIONS:
			name = 
	     "usp_CWCBISF_ALERT_AlertCurrentConditions_Sel_By_AlertNum";
			break;
		case __S_ALERT_PACKAGES:
			name = "usp_CWCBISF_ALERT_AlertPackages_Sel";
			break;
		case __S_ALERT_SETTINGS:
			name ="usp_CWCBISF_ALERT_AlertSettings_Sel_By_AlertNum";
			break;
		case __S_ALERT_SUBSCRIBERS:
			name = "usp_CWCBISF_Alert_Subscribers_Sel";
			break;
		case __S_ALERT_SUBSCRIPTIONS_VIEW_BY_USER_ID:
			name = "usp_CWCBISF_ALERT_Subscriptions_Sel_By_UserID";
			break;
		case __S_ARCHIVE_15_DAY:
			name = "usp_CWCBISF_ALERT_vwALERTCombinedRealTimeResults15DayArchive_Sel_By_abbrev_variable_datetime";
			break;
		case __S_STATION_BY_ABBREV:
			name = "usp_CWCBISF_ALERT_Station_Sel_By_Abbrev";
			break;			
		case __S_STATION_BY_STATION_ID:
			name = "usp_CWCBISF_ALERT_Station_Sel_By_StationID";
			break;
		default:
			return false;
	}

	if (IOUtil.testing() && debug) {
		Message.printStatus(2, "", "Trying to create stored procedure '"
			+ name + "'");
	}

	// Look up the definition of the stored procedure (stored in a
	// DMIStoredProcedureData object) in the hashtable.  This allows
	// repeated calls to the same stored procedure to re-used stored
	// procedure meta data without requerying the database.

	DMIStoredProcedureData data = 
		(DMIStoredProcedureData)__storedProcedureHashtable.get(name);
	
	if (data != null) {
		// If a data object was found, set up the data in the statement
		// below and return true
	}
	else if (data == null 
		&& DMIUtil.databaseHasStoredProcedure(this, name)) {
		// If no data object was found, but the stored procedure is
		// defined in the database then build the data object for the
		// stored procedure and then store it in the hashtable.
		data = new DMIStoredProcedureData(this, name);
		__storedProcedureHashtable.put(name, data);
	}
	else {
		// If no data object was found and the stored procedure is not
		// defined in the database, then use the original DMI code.
		// Return false to buildSQL() so it knows to continue executing.
		if (IOUtil.testing()) {
			Message.printStatus(2, 
				"SatMonSysDMI.canSetUpStoredProcedure",
				"No stored procedure defined in database "
				+ "for SQL#: " + sqlNumber + "  (Name: "
				+ name + ")");
		}

		if (Message.isDebugOn) {
			Message.printDebug(30, 
				"SatMonSysDMI.canSetUpStoredProcedure",
				"No stored procedure defined in database "
				+ "for SQL#: " + sqlNumber);
		}		
		return false;
	}

	if (Message.isDebugOn) {
		Message.printDebug(30, 
			"SatMonSysDMI.canSetUpStoredProcedure",
			"Stored procedure '" + name + "' found and will "
			+ "be used.");
	}	

	if (IOUtil.testing() && debug) {
		DMIUtil.dumpProcedureInfo(this, name);
	}

	// Set the data object in the statement.  Doing so will set up the
	// statement as a stored procedure statement.

	statement.setStoredProcedureData(data);
	return true;
}

/**
Checks the control properties passed to addAnnotations() to verify that all 
are defined properly.
@param controlProps the control PropList to check.
@throws Exception if any of the properties were not defined.
*/
/* FIXME SAM 2008-04-15 Enable if needed
private void checkAddAnnotationControlProps(PropList controlProps) 
throws Exception {
	String routine = "checkAddAnnotationControlProps";

	if (controlProps == null) {
		throw new Exception(
			"Annotation control props cannot be null.");
	}

	String tsid = controlProps.getValue("TSID");
	String subProductNumS = controlProps.getValue("SubProductNum");
	String userIDS = controlProps.getValue("UserID");
	String abbrev = controlProps.getValue("StationAbbrev");

	if (tsid == null) {
		throw new Exception(
			"addAnnotation() control Property 'TSID' must be set.");
	}

	if (tsid == null) {
		throw new Exception(
			"addAnnotation() control Property 'SubProductNum' "
			+ "must be set.");
	}

	if (tsid == null) {
		throw new Exception(
			"addAnnotation() control Property 'UserID' "
			+ "must be set.");
	}

	if (tsid == null) {
		throw new Exception(
			"addAnnotation() control Property 'StationAbbrev' "
			+ "must be set.");
	}

	try {
		new TSIdent(tsid);
	}
	catch (Exception e) {
		throw new Exception(
			"Could not parse TS Identifier: '" + tsid + "'.");
	}

	int productNum = StringUtil.atoi(subProductNumS);
	if (productNum == -1) {
		throw new Exception(
			"Invalid SubProductNum value: '" + subProductNumS 
			+ "'.");
	}

	int userID = StringUtil.atoi(userIDS);
	if (userID == -1) {
		throw new Exception(
			"Invalid 'UserID' value: '" + userIDS + "'.");
	}
}
*/

/**
Closes a ResultSet from a stored procedure query.  Note that in the base DMI 
class there is another version of closeResultSet without the 'select' parameter
passed in.  This is only done for non-SP connections.  This version ensures that
the SP's Statement object is not closed, and thus the SP can be re-used
efficiently.
@param rs the ResultSet to be closed.
@param select the DMIStatement that was executed.
*/
public static void closeResultSet(ResultSet rs, DMIStatement select) 
throws java.sql.SQLException {
	if (rs != null) {
		rs.close();
		rs = null;
	}
}

/**
Determines the version of the database to which this DMI is connected.
*/
public void determineDatabaseVersion() {
	// REVISIT (JTS - 2005-10-09)
	// According to Doug Stenzel, everyone will always have an up-to-date
	// version of the database.  I remain skeptical, but he thinks that
	// stored procedures and views will solve all our problems and that
	// the DMI will change 1:1 as the database changes.  I remain 
	// skeptical.
}

/**
Executes a query and returns the result.  This overrides the normal dmiSelect()
in order that for stored procedures when testing the string that can be
pasted into the Query Analyzer can be printed.
@param q the DMISelectStatement to execute.
@return a ResultSet of records.
*/
public ResultSet dmiSelect(DMISelectStatement q)
throws java.sql.SQLException {
	//__lastStatement = q;
	if (IOUtil.testing()) {
		try {
			Message.printStatus(2, "", "" 
				+ q.createStoredProcedureString());
		}
		catch (Exception e) {
			Message.printStatus(2, "", "" + q.toString());
		
		}
	}
	return super.dmiSelect(q);
}

/**
Returns this DMI's annotation providers.
@return this DMI's annotation providers.
*/
public List getAnnotationProviderChoices() {	
	return __providers;
}

/**
Returns the data for the currently-logged in alert subscriber.
@return the data for the currently-logged in alert subscriber.
*/
public SatMonSys_AlertSubscriber getCurrentSubscriber() {
	try {
		if (__subscriber == null && !__readSubscriber) {
			readUserData(__userLogin);
		}
	}
	catch (Exception e) {
		Message.printWarning(2, "",
			"Could not read the default subscriber.");
		Message.printWarning(3, "", e);
	}
	return __subscriber;
}

/**
Returns information about the properties of the current database.
@return a Vector of Strings with information about the current database.
*/
public List getDatabaseProperties () {
	List v = new Vector();
	v.add("SatMonSys Database Host: " + getDatabaseServer());
	if (getODBCName() != null) {
		v.add("ODBC Data Source Name:  " +
		getODBCName());	
	}
	else {
		v.add (
		"ODBC Data Source Name:  SatMonSys (internally defined)");
	}
	v.add("Database design version appears to be: " +
		getDatabaseVersion());
	v.add("Database does use stored procedures.");
	v.add("");

	if (__providers.size() == 0) {
		v.add("No annotation provider choices defined.");
	}
	else {
		v.add("Annotation provider choices:");
		for (int i = 0; i < __providers.size(); i++) {
			v.add("   " + __providers.get(i));
		}
	}

	return v;
}

/**
Returns comments about the database version.
@return a String array with information about the version of the database.
*/
public String[] getVersionComments() {
	String [] v = new String[5];
	v[0] = "";
	v[1] = "-------------------------------------------------------" +
		"----------------------";
	if (getJDBCODBC()) {
		// Database server...
		v[2] = "SatMonSys database is: " +
			getDatabaseName() + " on " + getDatabaseServer();
	}
	else {
		// Connection is ODBC...
		v[2] = "SatMonSys ODBC DSN is: " + getODBCName();
	}
	v[3] = "------------------------------------------------------" +
		"-----------------------";
	v[4] = "";
	return v;
}

/**
Looks up a view with a given name in the view name to view number hashtable.
If the view name cannot be found, an exception will be thrown.  Otherwise,
the internal number of the view used by SPFlex will be returned.
@param viewName the name of the view to look up in the view name to view number
hashtable.
@return the internal number of the view used by the SPFlex stored procedure.
@throws Exception if the view name cannot be found in the hash table.
*/
/* FIXME SAM 2008-04-15 Enable if needed
private String getViewNumber(String viewName) 
throws Exception {
	String s = (String)(__viewNumbers.get(viewName));
	if (s == null) {
		throw new Exception("View '" + viewName + "' could not be "
			+ "found in the view-to-number lookup.");
	}
	return s;
}
*/

/**
Cleans up member variables.
*/
public void finalize()
throws Throwable {
	__alertLegendTypes = null;
	__storedProcedureHashtable = null;
	__subscriber = null;
	__userLogin = null;
	__providers = null;
	super.finalize();
}

/**
Reads the station abbrev that corresponds to the time series in the given 
TSID.
@param tsid the TSIdent for which to find the corresponding station 
abbreviation.
@return the station abbreviation that corresponds to the given TSIdent.
*/
private String lookupStationAbbrev(TSIdent tsid) {

	if (tsid.getInputType().equalsIgnoreCase("HydroBase")) {
		SatMonSys_Station sta = readStationForTSIdent(tsid);
		if (sta == null) {
			return null;
		}
		else {
			return sta.getAbbrev();
		}
	}
	else {
		return null;
	}
}

/**
Called when the DMI is opened.  This overrides the superclass DMI so that for
stored procedure databases the view numbers hashtable can be set up.  
super.open() is called first in this method, prior to any other setup.
*/
public void open() 
throws Exception, java.sql.SQLException {
	if (__localPorts == null) {
		// Use default port numbers
		super.open();
	}
	else {
		for ( int i = 0; i < __localPorts.length; i++ ) {
			// Try each port number that could be used.
			try {
				setPort(__localPorts[i]);
				super.open();
				break;
			}
			catch (Throwable t) {
			}
		}
	}
	setupViewNumbersHashtable();
}

/**
Checks to see if this TSProductAnnotationProvider interface provides the given provider.
@param providerName the name of the provider to check for.
@return true if the given provider is provided, false if not.
*/
public boolean provides(String providerName) {
	String choice = null;
	List choices = getAnnotationProviderChoices();
	int size = choices.size();
	for (int j = 0; j < size; j++) {
		choice = (String)choices.get(j);
		if (providerName.equals(choice)) {
			return true;
		}
	}
	
	return false;
}

private final String
	__PROVIDER_HISTORICAL = "ColoradoSMS~Historical",
	__PROVIDER_REALTIME = "ColoradoSMS~RealtimeStreamflow",
	__PROVIDER_REALTIME_WITH_ALARMS = "ColoradoSMS~RealtimeStreamflowWithAlarms";

/**
Reads global data from the database.
*/
public void readGlobalData() {
	__providers.add(__PROVIDER_HISTORICAL);
	__providers.add(__PROVIDER_REALTIME);
	__providers.add(__PROVIDER_REALTIME_WITH_ALARMS);

/*
	try {
		readUserData(__userLogin);
	}
	catch (Exception e) {
		Message.printWarning(2, routine, 
			"Could not read the default subscriber.  Database "
			+ "connection will not proceed.");
		Message.printWarning(3, routine, e);
		__subscriber = null;
		try {
			close();
		}
		catch (Exception e2) {
			Message.printWarning(2, routine,
				"An error occurred trying to close the "
				+ "database connection.");
			Message.printWarning(3, routine, e);
		}
	}
*/
}

/**
Reads records from the alert packages table.
@return a Vector of SatMonSys_AlertPackages objects.
@throws Exception if an error occurs.
*/
public List readAlertArchiveForAlert_num(int alert_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, __S_ALERT_ARCHIVE);
	q.addWhereClause("alert_num = " + alert_num);
	ResultSet rs = dmiSelect(q);
	List v = toAlertDataList(rs, __S_ALERT_ARCHIVE);
	closeResultSet(rs, q);
	return v;
}

/**
Reads all records from the alert conditions for the given alert_num.
@return a Vector of SatMonSys_AlertData objects.
@throws Exception if an error occurs.
*/
public List readAlertCurrentConditionsForAlert_num(int alert_num) 
throws Exception {
	return readAlertCurrentConditionsForAlert_num(alert_num, false);
}

/**
Reads all active alert records from the alert conditions for the given 
alert_num.
@return a Vector of SatMonSys_AlertData objects.
@throws Exception if an error occurs.
*/
public List readAlertCurrentConditionsForAlert_num(int alert_num,
boolean activeOnly) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, __S_ALERT_CURRENT_CONDITIONS);
	q.addWhereClause("alert_num = " + alert_num);
	ResultSet rs = dmiSelect(q);
	List v = toAlertDataList(rs, __S_ALERT_CURRENT_CONDITIONS);
	closeResultSet(rs, q);

	if (!activeOnly) {
		return v;
	}

	int size = v.size();

	List v2 = new Vector();
	SatMonSys_AlertData data = null;
	for (int i = 0; i < size; i++) {
		data = (SatMonSys_AlertData)v.get(i);
		if (data.getAlert_status() == 0) {
			// skip
		}
		else {
			v2.add(data);
		}
	}
		
	return v2;
}

/**
Reads records from the alert packages table.
@return a Vector of SatMonSys_AlertPackages objects.
@throws Exception if an error occurs.
*/
public List readAlertPackages() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, __S_ALERT_PACKAGES);
	ResultSet rs = dmiSelect(q);
	List v = toAlertPackagesList(rs);
	closeResultSet(rs, q);
	return v;
}	

/**
Reads the records from the alert settings table with the given alert_num.
@param alert_num the number of the alert to match.
@return a Vector.
@throws Exception if an error occurs.
*/
public List readAlertSettingsForAlert_num(int alert_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, __S_ALERT_SETTINGS);
	q.addWhereClause("alert_num = " + alert_num);
	ResultSet rs = dmiSelect(q);
	// checked stored procedure
	List v = toAlertSettingsList(rs);
	closeResultSet(rs, q);
	return v;
}	

/**
Reads records from the alert packages table.
@return a Vector of SatMonSys_AlertPackages objects.
@throws Exception if an error occurs.
*/
public List readAlertSubscribers() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, __S_ALERT_SUBSCRIBERS);
	ResultSet rs = dmiSelect(q);
	List v = toAlertSubscribersList(rs);
	closeResultSet(rs, q);
	return v;
}	

/**
Reads records from the alert subscriptions table for the given user.
@return a Vector of SatMonSys_AlertSubscriptionView objects.
@throws Exception if an error occurs.
*/
public List readAlertSubscriptionsViewForUser_id(int user_id) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, __S_ALERT_SUBSCRIPTIONS_VIEW_BY_USER_ID);
	q.addWhereClause("user_id = " + user_id);
	ResultSet rs = dmiSelect(q);
	List v = toAlertSubscriptionsViewList(rs);
	closeResultSet(rs, q);
	return v;
}

public SatMonSys_CombinedRealTimeResults15DayArchive 
readCombinedRealTimeResults15DayArchiveForAbbrevVariableDatetime(
String abbrev, String variable, Date datetime) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, __S_ARCHIVE_15_DAY);
	q.addWhereClause("abbrev = '" + abbrev + "'");
	q.addWhereClause("variable = '" + variable + "'");
	q.addWhereClause("date_time = " + datetime);
	ResultSet rs = dmiSelect(q);
	List v = toCombinedRealTimeResults15DayArchive(rs);
	closeResultSet(rs, q);
	if (v.size() == 0) {
		return null;
	}
	else {
		return (SatMonSys_CombinedRealTimeResults15DayArchive)
			v.get(0);
	}
}

/**
Returns the Station record that has the given abbrev.
@param abbrev the abbrev value for which to return a Station record.
@return the Station record that has the given abbrev, or null if none could be
found.
@throws Exception if an error occurs while reading from the database.
*/
public SatMonSys_Station readStationForAbbrev(String abbrev) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, __S_STATION_BY_ABBREV);
	q.addWhereClause("abbrev like '" + abbrev + "'");
	ResultSet rs = dmiSelect(q);
	List v = toStationList(rs);
	closeResultSet(rs, q);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (SatMonSys_Station)v.get(0);
}
/**
Returns the Station record that has the given station_id.
@param station_id the station_id value for which to return a Station record.
@return Station record that has the given station_id, or null if none could be
found.
@throws Exception if an error occurs while reading from the database.
*/
public SatMonSys_Station readStationForStation_id(String station_id) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, __S_STATION_BY_STATION_ID);
	q.addWhereClause("station_id like '" + station_id + "'");
	ResultSet rs = dmiSelect(q);
	List v = toStationList(rs);
	closeResultSet(rs, q);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (SatMonSys_Station)v.get(0);
}

/**
Returns the Station record that corresponds to the given TSIdent.
@param tsid the TSIdent for which to return a given station.
@return Station record that corresponds to the given TSIdent, or null if none 
could be found.
@throws Exception if an error occurs while reading from the database.
*/
private SatMonSys_Station readStationForTSIdent(TSIdent tsid) {
	String routine = "readStationForTSIdent";

	// First pull the station_id out of the TSIdent.

	String stationID = tsid.getLocation();
	if (stationID == null) {
		Message.printWarning(2, routine,	
			"No station ID could be found in the TSID: " 
			+ tsid + ".");
		return null;
	}
	
	// Read the station that matches the given Station_id.
	
	SatMonSys_Station sta = null;
	try {
		sta = readStationForStation_id(stationID);
	}
	catch (Exception e) {
		Message.printWarning(2, routine,
			"An error occurred reading the station "
			+ "for station_id: '" + stationID
			+ "'.");
		Message.printWarning(3, routine, e);
		return null;
	}

	// If no station was read, treat the station_id as an abbrev value,
	// instead, and see if any stations can be read.

	if (sta == null) {
		Message.printWarning(2, routine, 
			"No station could be read for "
			+ "station_id: '" + stationID + "'.  "
			+ "Will try abbrev.");

		try {
			sta = readStationForAbbrev(stationID);
		}
		catch (Exception e) {
			Message.printWarning(2, routine,
				"An error occurred reading the station "
				+ "for station_abbrev: '" + stationID
				+ "'.");
			Message.printWarning(3, routine, e);
			return null;
		}

		if (sta == null) {
			Message.printWarning(2, routine, 
				"No station could be read for "
				+ "station_abbrev: '" + stationID 
				+ "'.");
			return null;
		}
		else {
			return sta;
		}
	}
	else {
		return sta;
	}
}

/**
Reads user data from the database for the user with the given login.
@param userLogin the login value for which to read user data.
REVISIT (JTS - 2005-11-01)
this will be changed/expanded/removed in the future when the DB has a 
proper login screen.
*/
private void readUserData(String userLogin) 
throws Exception {
	__readSubscriber = true;

	if (userLogin == null) {
		// in the future, change this so that it is the default guest
		// login.  None exist now, so __subscriber is set to null.
		__subscriber = null;
		return;
	}

	List v = readAlertSubscribers();
	SatMonSys_AlertSubscriber s = null;
	for (int i = 0; i < v.size(); i++) {
		s = (SatMonSys_AlertSubscriber)v.get(i);

		if (userLogin != null) {
			if (s.getLogin().equals(userLogin)) {
//			if (("" + s.getUser_id()).equals(userLogin)) {
//			if (s.getUser_id() == 1630) {
				__subscriber = s;
				__readSubscriber = true;
				return;
			}
		}
	}

	__subscriber = null;
	throw new Exception("No user could be found with the "
		+ "login value of \"" + userLogin + "\".");
}

boolean __readSubscriber = false;

/**
Runs an SPFlex query using the given parameters.<p>
The parameters are as follows:<p>
<b>0</b> - the internal number of the view for which to run SPFLex<br>
<b>1</b> - the first field on which to query, or -999 if no fields are being
	queried.<br>
<b>2</b> - the first comparator (or -999 if no fields are being queried)<br>
<b>3</b> - the first query value (or -999 if no fields are being queried)<br>
<b>4</b> - the second field on which to query, or -999 if no fields are being
	queried.<br>
<b>5</b> - the second comparator (or -999 if one field is being queried)<br>
<b>6</b> - the second query value (or -999 if one field is being queried)<br>
<b>7</b> - the third field on which to query, or -999 if two fields are being
	queried.<br>
<b>8</b> - the third comparator (or -999 if two fields are being queried)<br>
<b>9</b> - the third query value (or -999 if two fields are being queried)<br>
<b>10</b> - the fourth field on which to query, or -999 if three fields are 
	being queried.<br>
<b>11</b> - the fourth comparator (or -999 if three fields are being queried)
	<br>
<b>12</b> - the fourth query value (or -999 if three fields are being queried)
	<br>
<b>13</b> - the fifth field on which to query, or -999 if three fields are 
	being queried.<br>
<b>14</b> - the fifth comparator (or -999 if three fields are being queried)
	<br>
<b>15</b> - the fifth query value (or -999 if three fields are being queried)
	<br>
<b>16</b> - the sixth field on which to query, or -999 if three fields are 
	being queried.<br>
<b>17</b> - the sixth comparator (or -999 if three fields are being queried)
	<br>
<b>18</b> - the sixth query value (or -999 if three fields are being queried)
	<br>	
<b>19</b> - the order by code<br>
<b>20</b> - the application code<br>
@param parameters the parameters for the SPFlex run.
@return the ResultSet generated by the query.
@throws Exception if there is an error running the stored procedure.
*/
/* FIXME SAM 2008-04-15 Enable if needed
private ResultSet runSPFlex(String[] parameters) 
throws Exception {
	if (__uspFlexStoredProcedureData == null) {
		__uspFlexStoredProcedureData = new DMIStoredProcedureData(this, 
			"usp_Flex");
		__uspFlexSelectStatement = new DMISelectStatement(this);
		__uspFlexSelectStatement.setStoredProcedureData(
			__uspFlexStoredProcedureData);
	}

	//__lastStatement = __uspFlexSelectStatement;

	String s = "\nexec usp_Flex ";
	for (int i = 0; i < parameters.length; i++) {
		__uspFlexSelectStatement.setValue(parameters[i], i + 2);
		s += "'" + parameters[i] + "'";
		if (i < parameters.length - 1) {
			s += ", ";
		}
	}
	return dmiSelect(__uspFlexSelectStatement);
}
*/

/**
Sets the annotation provider name of this DMI.
@param name this DMI's annotation provider name.
*/
public void setAnnotationProviderName(String name) {
	//__annotationProviderName = name;
}

/**
Creates the hashtable that will stored the relationship of view names to the
view numbers used internally by SPFlex, and populates the hashtable as well.
*/
private void setupViewNumbersHashtable() {
	//long version = getDatabaseVersion();

	//__viewNumbers = new Hashtable();
}

/**
Translates data in a ResultSet into a Vector of SatMonSys_AlertData.
@param rs the ResultSet created by an alert package query.
@return a Vector of SatMonSys_AlertData objects.
@throws Exception if an error occurs.
*/
private List toAlertDataList(ResultSet rs, int sqlNumber) 
throws Exception {
	SatMonSys_AlertData data = null;
	List v = new Vector();
	int index = 1;
	
	int i;
	String s;
	float f;
	Date D;

	if (sqlNumber == __S_ALERT_ARCHIVE) {
		while (rs.next()) {
			index = 1;
			data = new SatMonSys_AlertData();
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setAlert_type_desc(s.trim());
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setAlert_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDiv(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setWD(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setAbbrev(s.trim());
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setVariable(s.trim());
			}
			D = rs.getTimestamp(index++);
			if (!rs.wasNull()) {
				data.setDate_time(D);
			}
			f = rs.getFloat(index++);
			if (!rs.wasNull()) {
				data.setAmt((double)f);
			}

			v.add(data);
		}
	}
	else if (sqlNumber == __S_ALERT_CURRENT_CONDITIONS) {
		while (rs.next()) {
			index = 1;
			data = new SatMonSys_AlertData();
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setAlert_type(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setAlert_type_desc(s.trim());
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setAlert_type(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setAbbrev(s.trim());
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setVariable(s.trim());
			}
			D = rs.getTimestamp(index++);
			if (!rs.wasNull()) {
				data.setDate_time(D);
			}
			f = rs.getFloat(index++);
			if (!rs.wasNull()) {
				data.setAmt((double)f);
			}

			v.add(data);
		}
	}

	return v;
}

/**
Translates data in a ResultSet into a Vector of SatMonSys_AlertPackages.
@param rs the ResultSet created by an alert package query.
@return a Vector of SatMonSys_AlertPackages objects.
@throws Exception if an error occurs.
*/
private List toAlertPackagesList(ResultSet rs) 
throws Exception {
	SatMonSys_AlertPackages data = null;
	List v = new Vector();
	int index = 1;
	
	int i;
	String s;
	boolean b;

	while (rs.next()) {
		index = 1;
		data = new SatMonSys_AlertPackages();
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setAlert_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setAlert_description(s.trim());
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setStation_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setStation_name(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setAbbrev(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setData_provider(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setResult_name(s.trim());
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setAlert_type(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setAlert_type_desc(s.trim());
		}
		b = rs.getBoolean(index++);
		if (!rs.wasNull()) {
			data.setEnabled(b);
		}
		v.add(data);
	}
	return v;
}

/**
Translates data in a ResultSet into a Vector of SatMonSys_TSData.
@param rs the ResultSet created by a realtime query.
@return a Vector of SatMonSys_TSData objects.
@throws Exception if an error occurs.
*/
/* FIXME SAM 2008-04-15 Enable if needed
private Vector toAlertRealTimeResultsList(ResultSet rs) 
throws Exception {
	SatMonSys_TSData data = null;
	Vector v = new Vector();
	int index = 1;
	
	int i;
	String s;
	Date D = null;
	float f;

	while (rs.next()) {
		index = 1;
		data = new SatMonSys_TSData();
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDiv(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setWD(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setAbbrev(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setVariable(s.trim());
		}
		D = rs.getTimestamp(index++);
		if (!rs.wasNull()) {
			data.setDate_time(D);
		}
		f = rs.getFloat(index++);
		if (!rs.wasNull()) {
			data.setAmt(f);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setFlag(s.trim());
		}
		D = rs.getTimestamp(index++);
		if (!rs.wasNull()) {
			data.setRcvd_date_time(D);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setData_source(s.trim());
		}
		
		v.add(data);
	}

	return v;
}
*/

/**
Translates data in a ResultSet into a Vector of SatMonSys_AlertSettings.
@param rs the ResultSet created by an alert settings query.
@return a Vector of SatMonSys_AlertSettings objects.
@throws Exception if an error occurs.
*/
private List toAlertSettingsList(ResultSet rs) 
throws Exception {
	SatMonSys_AlertSettings data = null;
	List v = new Vector();
	int index = 1;
	
	int i;
	String s;
	double d;
	boolean b;

	while (rs.next()) {
		index = 1;
		data = new SatMonSys_AlertSettings();
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setSetting_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setStart_date(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setEnd_date(s.trim());
		}
		d = rs.getDouble(index++);
		if (!rs.wasNull()) {
			data.setCheck_value(d);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setCheck_time_period(i);
		}
		b = rs.getBoolean(index++);
		if (!rs.wasNull()) {
			data.setAlert_status(b);
		}

		v.add(data);
	}
	return v;
}

/**
Translates data in a ResultSet into a Vector of SatMonSys_AlertSubscriber.
@param rs the ResultSet created by an alert settings query.
@return a Vector of SatMonSys_AlertSubscriber objects.
@throws Exception if an error occurs.
*/
private List toAlertSubscribersList(ResultSet rs) 
throws Exception {
	SatMonSys_AlertSubscriber data = null;
	List v = new Vector();
	int index = 1;
	
	int i;
	String s;

	while (rs.next()) {
		index = 1;
		data = new SatMonSys_AlertSubscriber();
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setUser_id(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setFirst_name(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setLast_name(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setLogin(s.trim());
		}		
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setPassword(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setPhone_number(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setPager_number(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setEmail_address(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setAdmin_privileges(s.trim());
		}
		
		v.add(data);
	}

	return v;
}

/**
Translates data in a ResultSet into a Vector of SatMonSys_AlertSubscriptionView.
@param rs the ResultSet created by an alert settings query.
@return a Vector of SatMonSys_AlertSubscriptionView objects.
@throws Exception if an error occurs.
*/
private List toAlertSubscriptionsViewList(ResultSet rs) 
throws Exception {
	SatMonSys_AlertSubscriptionView data = null;
	List v = new Vector();
	int index = 1;
	
	int i;
	String s;
	boolean b;

	while (rs.next()) {
		index = 1;
		data = new SatMonSys_AlertSubscriptionView();
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setAlert_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setAlert_description(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setAbbrev(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setStation_name(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setResult_name(s.trim());
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setAlert_type(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setAlert_type_desc(s.trim());
		}
		b = rs.getBoolean(index++);
		if (!rs.wasNull()) {
			data.setAlert_enabled(b);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setNotification_type_desc(s.trim());
		}

		v.add(data);
	}

	return v;
}

/**
Translates data in a ResultSet into a Vector of 
SatMonSys_CombinedRealTimeResults15DayArchive objects.
@param rs the ResultSet created by a 15 day archive query.
@return a Vector of SatMonSys_CombinedRealTimeResults15DayArchive objects.
@throws Exception if an error occurs.
*/
private List toCombinedRealTimeResults15DayArchive(ResultSet rs) 
throws Exception {
	SatMonSys_CombinedRealTimeResults15DayArchive data = null;
	List v = new Vector();
	int index = 1;
	
	Date D;
	double d;
	int i;
	String s;

	while (rs.next()) {
		index = 1;
		data = new SatMonSys_CombinedRealTimeResults15DayArchive();
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDiv(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setWD(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setAbbrev(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setVariable(s.trim());
		}
		D = rs.getTimestamp(index++);
		if (!rs.wasNull()) {
			data.setDate_time(D);
		}
		d = rs.getDouble(index++);
		if (!rs.wasNull()) {
			data.setAmt(d);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setFlag(s.trim());
		}

		v.add(data);
	}

	return v;
}

/**
Translates data in a ResultSet into a Vector of SatMonSys_Station objects.
@param rs the ResultSet created by an alert settings query.
@return a Vector of SatMonSys_Station objects.
@throws Exception if an error occurs.
*/
private List toStationList(ResultSet rs) 
throws Exception {
	SatMonSys_Station data = null;
	List v = new Vector();
	int index = 1;
	
	int i;
	String s;

	while (rs.next()) {
		index = 1;
		data = new SatMonSys_Station();
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setStation_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setAbbrev(s.trim());
		}		
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setStation_id(s.trim());
		}		
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setStation_name(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setSource(s.trim());
		}

		v.add(data);
	}
	
	return v;
}

}
