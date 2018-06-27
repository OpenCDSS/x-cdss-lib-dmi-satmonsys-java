// ----------------------------------------------------------------------------
// SatMonSys_Util - Utility methods for the SatMonSys DMI package.
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2005-09-30	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package DWR.DMI.SatMonSysDMI;

import java.io.File;

import RTi.Util.GUI.InputFilter;
import RTi.Util.GUI.InputFilter_JPanel;

import RTi.Util.IO.IOUtil;
import RTi.Util.IO.PropList;

import RTi.Util.Message.Message;

import RTi.Util.String.StringUtil;

public class SatMonSys_Util {

/**
The number of parameter triplets that SPFlex can handle.
*/
private final static int __NUM_TRIPLETS = 6;

/**
Adds a triplet to the parameter list in the first position available.
@param parameter the parameter list that has already been set up.
@param triplet the triplet to put in the parameter list.
*/
public static void addTriplet(String[] parameters, String[] triplet) {
	for (int i = 0; i < __NUM_TRIPLETS; i++) {
		if (parameters[1 + (i * 3)].equals("-999")) {
			parameters[1 + (i * 3)] = triplet[0];
			parameters[2 + (i * 3)] = triplet[1];
			parameters[3 + (i * 3)] = triplet[2];
			return;
		}
	}
}

/**
Returns the path to the configuration file that this DMI uses.
@return the path to the configuration file that this DMI uses.
*/
public static String getConfigurationFile() {
	return IOUtil.getApplicationHomeDir() + File.separator
		+ "system" + File.separator + "CDSS.cfg";
}

/**
Given an array of stored procedure parameters, this will fill in missing 
information, which includes the viewNumber to run and the order by information.
@param parameters the array of stored procedure parameters to fill in.
@param viewNumber the internal number of the SPFlex view to run.
@param orderBys the order by statement that should be taken into account with
the query.  If no ordering is to be done with the query, this should be less
than or equal to 0.
*/
protected static void fillSPParameters(String[] parameters, String viewNumber, 
int orderBy) {
	parameters[0] = viewNumber;
	if (orderBy > 0) {
		parameters[parameters.length -2] = "" + orderBy;
	}
	else {
		parameters[parameters.length - 2] = "-999";
	}
	parameters[parameters.length - 1] = "CDSS";
}

/**
Builds a SPFlex parameter array from an InputFilter_JPanel and the second 
element of the value returned from HydroBaseDMI.getWaterDistrictWhereClause().
@param panel the InputFilter_JPanel that contains the values for which to
run the query.  It can be null, in which case no InputFilter values will 
be included in the parameter list.
@param districtWhere the second element of the array return from 
HydroBaseDMI.getWaterDistrictWhereClause().  It will look like either:<p><pre>
	"DIV X"
or
	"WD X"
</pre>
where X is the Division or Water District for which to restrict the query.<p>
This parameter can currently be null, in which case it will not be included
in the parameter list.
@return a 21-element array containing the parameters for the SPFlex query.
See HydroBaseDMI.runSPFlex() for more information.
@throws Exception if there is an error building the parameter array.
*/
public static String[] getSPFlexParameters(InputFilter_JPanel panel,
String[] districtWhere) 
throws Exception {
	InputFilter filter = null;
	int nfg = 0;
	
	if (panel != null) {
		nfg = panel.getNumFilterGroups();
	}

	int num = (__NUM_TRIPLETS * 3) + 3;

	String[] triplet = null;
	String[] parameters = new String[num];

	int count = 1;

	for (int i = 0; i < num; i++) {
		parameters[i] = "-999";
	}

	// first put the information for limiting the water district or 
	// division in the parameter array.  The comparator is always "EQ".

	/*
	if (districtWhere != null) {	
		if (districtWhere[1].equals(HydroBase_GUI_Util._ALL_DIVISIONS)){
			parameters[count++] = "div";
			parameters[count++] = "NE";
			parameters[count++] = "-1";
		}
		else if (districtWhere[1].equals(
		    HydroBase_GUI_Util._DIVISION_8)) {
		    	parameters[count++] = "div";
			parameters[count++] = "EQ";
			parameters[count++] = "8";
		}
		else {
			int index = districtWhere[1].indexOf(" ");
			String field = districtWhere[1].substring(0, index);
			String value = districtWhere[1].substring(index).trim();
			
			parameters[count++] = field;
			parameters[count++] = "EQ";
			parameters[count++] = value;
		}
	}
	*/

	// loop through all the InputFilters and put their values into 
	// the array.  getSPFlexParametersTriplet() will build an array 
	// with the field to query, the SPFlex comparator, and the value
	// to query against.
	for (int i = 0; i < nfg; i++) {
		filter = panel.getInputFilter(i);	
		triplet = getSPFlexParametersTriplet(filter, 
			panel.getOperator(i));
		if (triplet != null) {
			// non-null triplets contain values and can be put
			// into the array
			parameters[count++] = triplet[0];
			parameters[count++] = triplet[1];
			parameters[count++] = triplet[2];
		}
		else {
			// null triplets mean the empty InputFilter was
			// selected.  "-999" is used as filler in the array.
		}
	}

	return parameters;
}

/**
Builds an array containing the SPFlex information for a query from an
InputFilter.
@param filter the filter for which to build the SPFlex information.
@param op the operator selected for the InputFilter in the panel.
@return a three-element array containing:<p>
<b>0</b> - the field on which to constrain the query<br>
<b>1</b> - the SPFlex operator to apply between the field and the value<br>
<b>2</b> - the value against which to query<br>
<i>null</i> will be returned if no field was selected in the InputFilter.
@throws Exception if an error occurs.
*/
public static String[] getSPFlexParametersTriplet(InputFilter filter, 
String op) 
throws Exception {
	String[] triplet = new String[3];
	// Get the selected filter for the filter group...
	if (filter.getWhereLabel().trim().equals("")) {
		// Blank indicates that the filter should be ignored...
		return null;
	}

	// Get the input type...
	int inputType = filter.getInputType();
	if ( filter.getChoiceTokenType() > 0 ) {
		inputType = filter.getChoiceTokenType();
	}
	
	// Get the internal where.

	// Note:
	// getWhereInternal2() should always be used for stored procedure
	// SPFlex parameter building from InputFilters.  InputFilters can have
	// two where_internal values defined.  Typically, the first one is
	// used for non-stored procedure queries and the second one is used
	// for stored procedure queries.  However, some InputFilters are only
	// used with Stored Procedures, and so they have a where_internal 
	// set, but not a where_internal_2.  getWhereInternal2() handles this
	// by return where_internal_2, unless it is null.  In that case,
	// where_internal will be returned.
	triplet[0] = filter.getWhereInternal2();

	// Get the user input...
	triplet[2] = filter.getInputInternal().trim();

	if (op.equalsIgnoreCase(InputFilter.INPUT_BETWEEN)) {
		// REVISIT - need to enable in InputFilter_JPanel.
	}
	else if (op.equalsIgnoreCase(InputFilter.INPUT_CONTAINS)) {
		triplet[1] = "CN";
	}
	else if (op.equalsIgnoreCase(InputFilter.INPUT_ENDS_WITH)) {
		triplet[1] = "EW";
	}
	else if (op.equalsIgnoreCase(InputFilter.INPUT_EQUALS)){
		if (inputType == StringUtil.TYPE_STRING) {
			triplet[1] = "MA";
		}
		else {	
			triplet[1] = "EQ";
		}
	}
	else if (op.equalsIgnoreCase(InputFilter.INPUT_GREATER_THAN)) {
		triplet[1] = "GT";
	}
	else if (op.equalsIgnoreCase(
	    InputFilter.INPUT_GREATER_THAN_OR_EQUAL_TO)) {
	    	triplet[1] = "GE";
	}
	else if (op.equalsIgnoreCase(InputFilter.INPUT_LESS_THAN)) {
		triplet[1] = "LT";
	}
	else if (op.equalsIgnoreCase(InputFilter.INPUT_LESS_THAN_OR_EQUAL_TO)) {
		triplet[1] = "LE";
	}
	else if (op.equalsIgnoreCase(InputFilter.INPUT_MATCHES)) {
		triplet[1] = "MA";
	}
	else if (op.equalsIgnoreCase(InputFilter.INPUT_ONE_OF)){
		// REVISIT - need to enable in InputFilter_JPanel
	}
	else if (op.equalsIgnoreCase(InputFilter.INPUT_STARTS_WITH)) {
		triplet[1] = "SW";
	}
	else {	
		throw new Exception("Unrecognized operator \"" + op 
			+ "\"...skipping..." );
	}

	return triplet;
}

/**
Reads the configuration properties in the configuration file.
@param configurationFile the path to the config file to read.
@return the PropList read from the config file.
*/
public static PropList readConfiguration(String configurationFile) 
throws Exception {
	String routine = "HydroBase_Util.readConfiguration";

	if (!IOUtil.fileExists(configurationFile)) {
		throw new Exception("Configuration file \"" 
			+ configurationFile + "\" does not exist.");
	}

	PropList configurationProps = new PropList("ConfigurationProps");
	configurationProps.setPersistentName(configurationFile);
	try {
		configurationProps.readPersistent();
	}
	catch (Exception e) {
		Message.printWarning(2, routine, 
			"Configuration Properties could not be read from file: "
			+ "'" + configurationFile + "'.");
		Message.printWarning(3, routine, e);
	}

	return configurationProps;
}

}