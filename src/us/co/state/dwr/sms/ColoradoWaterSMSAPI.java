package us.co.state.dwr.sms;

import java.util.List;
import java.util.Vector;

import javax.xml.ws.Holder;

import DWR.DMI.HydroBaseDMI.HydroBase_StationGeolocMeasType;
import RTi.TS.DayTS;
import RTi.TS.HourTS;
import RTi.TS.IrregularTS;
import RTi.TS.TS;
import RTi.TS.TSIdent;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;
import RTi.Util.Time.DateTime;
import RTi.Util.Time.TimeInterval;

/**
API methods to simplify interaction with the ColoradoWaterSMS.
These methods (and private data) are currently static because only one server is available for
ColoradoWaterSMS.  If multiple servers become available and support multiple instances of web service
sessions, make this a class that needs to be instantiated, or put the cache in a hashtable or other data
structure that is keyed to the server.
*/
public class ColoradoWaterSMSAPI
{
    
/**
Name of this class, for messaging.
*/
private static String __class = "ColoradoWaterSMSAPI";

/**
Cache of distinct list of station variable types.
*/
private static List<String> __distinctStationVariableList = new Vector();

/**
Return the cached list of distinct station variables, for all real-time stations.
*/
private static List<String> getDistinctStationVariableListCache ()
{
    return __distinctStationVariableList;
}

// TODO SAM 2010-03-03 Need to get units from web service when available
/**
Lookup the data units for a time series given the variable.  This is needed because the web service
does not return the units.
@param variable data variable (data type) for which to determine units.
*/
private static String lookupDataUnitsForVariable(String variable)
{
    if ( variable.equalsIgnoreCase("AIRTEMP") || variable.equalsIgnoreCase("WATTEMP") ) {
        return "DEGF";
    }
    else if ( StringUtil.indexOfIgnoreCase(variable,"DISCHRG",0) >= 0 ) {
        return "CFS";
    }
    else if ( variable.equalsIgnoreCase("ELEV") ||
        (StringUtil.indexOfIgnoreCase(variable,"GAGE_HT",0) >= 0) ) {
        return "FT";
    }
    else {
        return "";
    }
}

/**
Parse a transmission date/time, of the format m/d/yyyy h:mm:ss am/pm
where month, day and hour may be one or two digits.
Also, the sequence late in the day may look like:
<pre>
6/7/2011 11:00:00 PM
6/7/2011 11:15:00 PM
6/7/2011 11:30:00 PM
6/7/2011 11:45:00 PM
6/8/2011 12:00:00 AM
6/8/2011 12:15:00 AM
6/8/2011 12:30:00 AM
6/8/2011 12:45:00 AM
6/8/2011 1:00:00 AM
...
6/8/2011 11:45:00 AM
6/8/2011 12:00:00 PM
6/8/2011 12:15:00 PM
6/8/2011 12:30:00 PM
6/8/2011 12:45:00 PM
6/8/2011 1:00:00 PM
</pre>
@param transDateTime transmission date/time string
@param datePrecision precision to create the date, matching the time series.
@param providedDateTime If null, a new DateTime will be created.  If not null the instance will
be reused (this saves memory).
@return a DateTime instance parsed from the string.
*/
public static DateTime parseTransmissionDateTime ( String transDateTime, int datePrecision, DateTime providedDateTime )
{   // First parse by spaces
    List<String> tokens = StringUtil.breakStringList(transDateTime, " ", StringUtil.DELIM_SKIP_BLANKS);
    // Get the date information
    String dateToken = tokens.get(0);
    int month = 0;
    int day = 0;
    int year = 0;
    int hour = 0;
    int minute = 0;
    int second = 0;
    if ( dateToken.indexOf("/") > 0 ) {
        // Format is m/d/yyyy
        List<String> tokens2 = StringUtil.breakStringList(dateToken, "/", 0);
        month = Integer.parseInt(tokens2.get(0));
        day = Integer.parseInt(tokens2.get(1));
        year = Integer.parseInt(tokens2.get(2));
    }
    else if ( dateToken.indexOf("-") > 0 ) {
        // Format is yyyy-mm-dd
        List<String> tokens2 = StringUtil.breakStringList(dateToken, "-", 0);
        year = Integer.parseInt(tokens2.get(0));
        month = Integer.parseInt(tokens2.get(1));
        day = Integer.parseInt(tokens2.get(2));
    }

    // Get the time information (parts may be irrelevant if aggregation is to hour or day)
    // Format is always h:mm
    if ( tokens.size() >= 2 ) {
        List<String> tokens2 = StringUtil.breakStringList(tokens.get(1), ":", 0);
        hour = Integer.parseInt(tokens2.get(0));
        if ( tokens2.size() > 1 ) {
            minute = Integer.parseInt(tokens2.get(1));
        }
        if ( tokens.size() > 2 ) {
            second = Integer.parseInt(tokens2.get(2));
        }
    }
    // Get the offset for am/pm
    int offset = 0;
    if ( tokens.size() >= 3 ) {
        if ( tokens.get(2).equalsIgnoreCase("am") && (hour == 12)) {
            // Input data uses hour 12 AM when it should be 12 in normalized time
            // Only used for real-time and hourly data, otherwise irrelevant
            hour = 0;
        }
        else if ( tokens.get(2).equalsIgnoreCase("pm") && (hour < 12) ) {
            // Hours to add because 1-12 clock is used - only need to adjust 1PM+
            // Only used for real-time and hourly data, otherwise irrelevant
            offset = 12;
        }
    }
    // Now instantiate the date/time and set the information
    if ( providedDateTime == null ) {
        // Create a new DateTime
        DateTime dt = new DateTime(datePrecision);
        dt.setYear(year);
        dt.setMonth(month);
        dt.setDay(day);
        dt.setHour(hour + offset);
        dt.setMinute(minute);
        dt.setSecond(second);
        return dt;
    }
    else {
        // Reuse the provided DateTime instance
        providedDateTime.setPrecision(datePrecision);
        providedDateTime.setYear(year);
        providedDateTime.setMonth(month);
        providedDateTime.setDay(day);
        providedDateTime.setHour(hour + offset);
        providedDateTime.setMinute(minute);
        providedDateTime.setSecond(second);
        return providedDateTime;
    }
}

/**
Read a list of variables (data types).
@param service the web service instance.
@param useCache Indicate whether the cache should be used if available.
*/
public static List<String> readDistinctStationVariableList ( ColoradoWaterSMS service, boolean useCache )
{   String routine = __class + "readStationVariableList";
    // Check to see if the cache is available
    if ( useCache ) {
        List<String> dataTypesCached = getDistinctStationVariableListCache();
        if ( dataTypesCached.size() > 0 ) {
            // Have a cache so use it
            Message.printStatus(2, routine, "Getting distinct list of station variable types from cache.");
            return dataTypesCached;
        }
    }
    // If here need to request the data
    Message.printStatus(2, routine, "Getting distinct list of station variable types from web service request.");
    List<String> dataTypes = new Vector();
    // This is a list, each item which is a list of variables for a station
    Holder<SmsStatusHeader> status = new Holder<SmsStatusHeader>();
    ArrayOfStationVariables array =
        service.getColoradoWaterSMSSoap12().getSMSTransmittingStationVariables(0, 0, null, status);
    // Check for error
    if ( (status.value != null) && (status.value.getError() != null) ) {
        throw new RuntimeException ( "Error getting transmitting station variables (" +
            status.value.getError().getErrorCode() + ": " + status.value.getError().getExceptionDescription() + ")." );
    }
    for ( StationVariables stationVariables : array.getStationVariables() ) {
        // Each variables list has the variables for a station
        String variable = stationVariables.getVariable();
        //Message.printStatus(2,routine,"Abbrev=\"" + stationVariables.getAbbrev() + "\" variable=\"" + variable );
        // Add to the list if unique...
        boolean found = false;
        for ( String dataType : dataTypes ) {
            if ( dataType.equals(variable) ) {
                found = true;
                break;
            }
        }
        if ( !found ) {
            // Add to the list
            dataTypes.add(variable);
        }
    }
    dataTypes = StringUtil.sortStringList(dataTypes);
    setDistinctStationVariableListCache(dataTypes);
    return dataTypes;
}

/**
Read a list of time series, as objects suitable for the TSTool time series list.
@return list of HydroBase_StationGeolocMeasType, in order to represent the station attributes available
from the web service.
*/
public static List<HydroBase_StationGeolocMeasType> readTimeSeriesHeaderObjects ( ColoradoWaterSMS service,
    int wdReq, int divReq,
    String abbrevReq, String stationNameReq, String dataProviderReq, String dataTypeReq,
    String timestepReq, DateTime readStart, DateTime readEnd, boolean readData )
throws Exception
{   List<HydroBase_StationGeolocMeasType> tslist = new Vector();
    // Used below...
    String readStartString = null;
    if ( readStart != null ) {
        readStartString = readStart.toString();
    }
    String readEndString = null;
    if ( readEnd != null ) {
        readEndString = readEnd.toString();
    }
    // Get the list of matching transmitting stations...
    Holder<SmsStatusHeader> status = new Holder<SmsStatusHeader>();
    ArrayOfStation stationArray =
        service.getColoradoWaterSMSSoap12().getSMSTransmittingStations(divReq, wdReq, abbrevReq, status);
    // Check for error
    if ( (status.value != null) && (status.value.getError() != null) ) {
        throw new RuntimeException ( "Error getting transmitting stations for div=" + divReq +
            " wdReq=" + wdReq + " abbrevReq=\"" + abbrevReq + "\" (" +
            status.value.getError().getErrorCode() + ": " + status.value.getError().getExceptionDescription() + ")." );
    }
    // Loop through the stations (a bit odd that the method to return the list is singular)
    for ( Station station : stationArray.getStation() ) {
        // Get the list of variables that match the request
        // This is a list, each item which is a list of variables for a station
        String abbrev = station.getAbbrev();
        String stationName = station.getStationName();
        String dataProvider = station.getDataProviderAbbrev();
        int wd = station.getWd();
        int div = station.getDiv();
        String utmXs = station.getUTMX();
        String utmYs = station.getUTMY();
        String longitude = ""; // TODO SAM 2012-05-17 Not in object
        String latitude = "";
        Holder<SmsStatusHeader> status2 = new Holder<SmsStatusHeader>();
        ArrayOfStationVariables array =
            service.getColoradoWaterSMSSoap12().getSMSTransmittingStationVariables(divReq, wdReq, abbrev, status2 );
        // Check for error
        if ( (status2.value != null) && (status2.value.getError() != null) ) {
            throw new RuntimeException ( "Error getting transmitting station variables (" +
                status2.value.getError().getErrorCode() + ": " + status2.value.getError().getExceptionDescription() + ")." );
        }
        // Not sure how to check for error (is an exception thrown?)
        for ( StationVariables stationVariables : array.getStationVariables() ) {
            // Each variables list has the variables for a station
            String variable = stationVariables.getVariable();
            if ( dataTypeReq.equals("*") || variable.equalsIgnoreCase(dataTypeReq) ) {
                // Have a matching variable (data type)
                // Get the provisional time series for the station and data type (variable).
                // Specify the aggregation interval if specified (otherwise get the raw data).
                /* Don't get the data because this is a performance hit
                ArrayOfStreamflowTransmission dataRecords =
                    service.getColoradoWaterSMSSoap12().getSMSProvisionalData(abbrev, variable,
                    readStartString, readEndString, aggregation );
                int dataRecordsSize = 0;
                if ( dataRecords != null ) {
                    dataRecordsSize = dataRecords.getStreamflowTransmission().size();
                }
                */
                // Define the time series and add to the list
                HydroBase_StationGeolocMeasType mt = new HydroBase_StationGeolocMeasType();
                // FIXME SAM 2009-11-20 What are the data units?
                mt.setAbbrev(abbrev);
                mt.setMeas_type(variable);
                mt.setStation_name(stationName);
                mt.setTime_step(timestepReq);
                mt.setData_source(dataProvider);
                mt.setWD ( wd );
                mt.setDiv ( div );
                mt.setData_units( lookupDataUnitsForVariable(variable));
                if ( StringUtil.isDouble(utmXs) ) {
                    mt.setUtm_x(Double.parseDouble(utmXs));
                }
                if ( StringUtil.isDouble(utmYs) ) {
                    mt.setUtm_y(Double.parseDouble(utmYs));
                }
                if ( StringUtil.isDouble(longitude) ) {
                    mt.setLongdecdeg(Double.parseDouble(longitude));
                }
                if ( StringUtil.isDouble(latitude) ) {
                    mt.setLatdecdeg(Double.parseDouble(latitude));
                }
                tslist.add ( mt );
             }
        }
    }
    return tslist;
}

/**
Read a single time series.
*/
public static TS readTimeSeries ( ColoradoWaterSMS service, String tsidentString,
    DateTime readStart, DateTime readEnd, boolean readData )
throws Exception
{   String routine = "ColoradoWaterSMS.readTimeSeries";
    TS ts = null;
    
    // If the date/times are not specified, default to the last 2 weeks
    if ( readStart == null ) {
        readStart = new DateTime(DateTime.DATE_CURRENT);
        readStart.addDay(-14);
    }
    if ( readEnd == null ) {
        readEnd = new DateTime(DateTime.DATE_CURRENT);
    }
    
    TSIdent tsident = new TSIdent ( tsidentString );
    String aggregation = tsident.getInterval();
    int intervalBase = tsident.getIntervalBase();
    if ( intervalBase == TimeInterval.IRREGULAR ) {
        aggregation = null; // raw data
    }
    String abbrevReq = tsident.getLocation();
    String dataType = tsident.getType();
    int div = -1; // Relying on abbrev to match the station
    int wd = -1; // Relying on abbrev to match the station
    // Get the list of matching transmitting stations...
    Holder<SmsStatusHeader> status = new Holder<SmsStatusHeader>();
    ArrayOfStation stationArray =
        service.getColoradoWaterSMSSoap12().getSMSTransmittingStations(div,wd, abbrevReq,status);
    // Check for error
    if ( (status.value != null) && (status.value.getError() != null) ) {
        throw new RuntimeException ( "Error getting transmitting stations (" +
            status.value.getError().getErrorCode() + ": " + status.value.getError().getExceptionDescription() + ")." );
    }
    // Leave this in to allow wild-carding of abbreviation parts, etc., but it will result in
    // a performance hit.
    // Loop through the stations (a bit odd that the method to return the list is singular)
    for ( Station station : stationArray.getStation() ) {
        // Get the list of variables that match the request
        // This is a list, each item which is a list of variables for a station
        String abbrev = station.getAbbrev();
        String dataProvider = station.getDataProviderAbbrev();
        Holder<SmsStatusHeader> status2 = new Holder<SmsStatusHeader>();
        ArrayOfStationVariables array =
            service.getColoradoWaterSMSSoap12().getSMSTransmittingStationVariables(div, wd, abbrev, status2 );
        // Check for error
        if ( (status2.value != null) && (status2.value.getError() != null) ) {
            throw new RuntimeException ( "Error getting transmitting station variables (" +
                status2.value.getError().getErrorCode() + ": " + status2.value.getError().getExceptionDescription() + ")." );
        }
        // Not sure how to check for error (is an exception thrown?)
        boolean isIrregular = false;
        for ( StationVariables stationVariables : array.getStationVariables() ) {
            // Each variables list has the variables for a station
            String variable = stationVariables.getVariable();
            if ( variable.equalsIgnoreCase(dataType) ) {
                // Have a matching variable (data type)
                // Get the provisional time series for the station and data type (variable).
                // Define the time series and add to the list
                // FIXME SAM 2009-11-20 What are the data units?
                if ( intervalBase == TimeInterval.IRREGULAR ) {
                    ts = new IrregularTS();
                    isIrregular = true;
                }
                else if ( intervalBase == TimeInterval.DAY ) {
                    ts = new DayTS();
                }
                else if ( intervalBase == TimeInterval.HOUR ) {
                    ts = new HourTS();
                }
                else {
                    throw new IllegalArgumentException ( "The interval for \"" + tsidentString + "\" is not supported.");
                }
                //tslist.add ( ts );
                // Set the identifier information
                ts.setIdentifier(tsident);
                ts.setDataUnits( lookupDataUnitsForVariable(variable));
                ts.setDataUnitsOriginal( lookupDataUnitsForVariable(variable));
                if ( readData ) {
                    // Dates are used below.  Don't require for !readData because this may break some
                    // discovery reads that depend on run-time date/times.
                    if ( readStart == null ) {
                        throw new IllegalArgumentException ( "Start date/time has not been specified." );
                    }
                    // Remove time zone if set
                    readStart = new DateTime(readStart);
                    if ( intervalBase == TimeInterval.HOUR ) {
                        // The date string actually needs to have minutes
                        // TODO SAM 2010-03-03 Remove this code and similar for readEnd if the
                        // State corrects the handling of dates to allow YYYY-MM-DD HH
                        readStart.setPrecision(DateTime.PRECISION_MINUTE );
                        readStart.setMinute(0);
                    }
                    readStart.setTimeZone("");
                    String readStartString = readStart.toString();
                    if ( readEnd == null ) {
                        throw new IllegalArgumentException ( "End date/time has not been specified." );
                    }
                    readEnd = new DateTime(readEnd);
                    if ( intervalBase == TimeInterval.HOUR ) {
                        // The date string actually needs to have minutes
                        readEnd.setPrecision(DateTime.PRECISION_MINUTE );
                        readEnd.setMinute(59);
                    }
                    readEnd.setTimeZone("");
                    String readEndString = readEnd.toString();
                    Message.printStatus (2, routine, "Reading time series \"" + tsidentString + "\" for readStart=" +
                        readStartString + " readEnd=" + readEndString );
                    // Specify the aggregation interval if specified (otherwise get the raw data).
                    Holder<SmsStatusHeader> status3 = new Holder<SmsStatusHeader>();
                    Holder<SmsDisclaimerHeader> disclaimer = new Holder<SmsDisclaimerHeader>();
                    ArrayOfStreamflowTransmission dataRecords =
                        service.getColoradoWaterSMSSoap12().getSMSProvisionalData(abbrev, variable,
                        readStartString, readEndString, aggregation, disclaimer, status3 );
                    // Check for error
                    if ( (status3.value != null) && (status3.value.getError() != null) ) {
                        throw new RuntimeException ( "Error getting provisional data (" +
                            status3.value.getError().getErrorCode() + ": " + status3.value.getError().getExceptionDescription() + ")." );
                    }
                    int dataRecordsSize = 0;
                    if ( dataRecords != null ) {
                        dataRecordsSize = dataRecords.getStreamflowTransmission().size();
                    }
                    Message.printStatus(2, routine, "Number of data records = " + dataRecordsSize );
                    // Set the period from the first and last data records
                    if ( dataRecordsSize > 0 ) {
                        // Have some data records to process...
                        StreamflowTransmission dataRecord1 = dataRecords.getStreamflowTransmission().get(0);
                        //Message.printStatus(2, routine, "First record transDateTime=" + dataRecord1.getTransDateTime());
                        ts.setDate1(parseTransmissionDateTime(dataRecord1.getTransDateTime(),0,null));
                        ts.setDate1Original(ts.getDate1());
                        StreamflowTransmission dataRecord2 = dataRecords.getStreamflowTransmission().get(
                            dataRecords.getStreamflowTransmission().size() - 1);
                        ts.setDate2(parseTransmissionDateTime(dataRecord2.getTransDateTime(),0,null));
                        ts.setDate2Original(ts.getDate2());
                        // Transfer the data from the data records to the time series.
                        ts.allocateDataSpace();
                        String transDateTime;
                        double amount;
                        String transFlag;
                        int resultCount; // Number of values aggregated (use if limiting missing?)
                        DateTime date = null;
                        int datePrecision = ts.getDate1().getPrecision();
                        for ( StreamflowTransmission dataRecord : dataRecords.getStreamflowTransmission() ) {
                            // Date format is m/d/yyyy hh:mm:ss am/pm
                            transDateTime = dataRecord.getTransDateTime();
                            amount = dataRecord.getAmount();
                            transFlag = dataRecord.getTransFlag();
                            resultCount = dataRecord.getResultCount();
                            if ( Message.isDebugOn ) {
                                Message.printDebug(10, routine, "transDateTime=" + transDateTime +
                                " amount=" + amount + " transFlag=" + transFlag + " resultCount=" + resultCount);
                            }
                            if ( isIrregular || (date == null) ) {
                                date = parseTransmissionDateTime ( transDateTime, datePrecision, null );
                            }
                            else {
                                // Reuse the date/time
                                date = parseTransmissionDateTime ( transDateTime, datePrecision, date );
                            }
                            ts.setDataValue(date, amount, transFlag, 0);
                        }
                    }
                }
            }
        }
    }
    return ts;
}

/**
TODO SAM 2009-11-23 May not be used since readTimeSeriesHeaderObjects is used for browsing and
single ReadTimeSeries is used for single time series/
Read a list of time series.
*/
/*
public static List<TS> readTimeSeriesList ( ColoradoWaterSMS service, int wd, int div,
    String abbrevReq, String stationNameReq, String dataProviderReq, String dataType,
    String timestep, DateTime readStart, DateTime readEnd, boolean readData )
throws Exception
{   List<TS> tslist = new Vector();
    // Used below...
    String readStartString = null;
    if ( readStart != null ) {
        readStartString = readStart.toString();
    }
    String readEndString = null;
    if ( readEnd != null ) {
        readEndString = readEnd.toString();
    }
    String aggregation = timestep;
    // Get the list of matching transmitting stations...
    Holder<SmsStatusHeader> status = new Holder<SmsStatusHeader>();
    ArrayOfStation stationArray =
        service.getColoradoWaterSMSSoap12().getSMSTransmittingStations(div, wd, abbrevReq, status);
    // Check for error
    if ( status.value.getError() != null ) {
        throw new RuntimeException ( "Error getting transmitting stations (" +
            status.value.getError().getErrorCode() + ": " + status.value.getError().getExceptionDescription() + ")." );
    }
    // Loop through the stations (a bit odd that the method to return the list is singular)
    for ( Station station : stationArray.getStation() ) {
        // Get the list of variables that match the request
        // This is a list, each item which is a list of variables for a station
        String abbrev = station.getAbbrev();
        String dataProvider = station.getDataProviderAbbrev();
        Holder<SmsStatusHeader> status2 = new Holder<SmsStatusHeader>();
        ArrayOfStationVariables array =
            service.getColoradoWaterSMSSoap12().getSMSTransmittingStationVariables(div, wd, abbrev, status2 );
        // Check for error
        if ( status2.value.getError() != null ) {
            throw new RuntimeException ( "Error getting transmitting station variables (" +
                status2.value.getError().getErrorCode() + ": " + status2.value.getError().getExceptionDescription() + ")." );
        }
        // Not sure how to check for error (is an exception thrown?)
        for ( StationVariables stationVariables : array.getStationVariables() ) {
            // Each variables list has the variables for a station
            String variable = stationVariables.getVariable();
            if ( variable.equalsIgnoreCase(dataType) ) {
                // Have a matching variable (data type)
                // Get the provisional time series for the station and data type (variable).
                // Specify the aggregation interval if specified (otherwise get the raw data).
                Holder<SmsDisclaimerHeader> disclaimer = new Holder<SmsDisclaimerHeader>();
                Holder<SmsStatusHeader> status3 = new Holder<SmsStatusHeader>();
                ArrayOfStreamflowTransmission dataRecords =
                    service.getColoradoWaterSMSSoap12().getSMSProvisionalData(abbrev, variable,
                    readStartString, readEndString, aggregation, disclaimer, status3 );
                // Check for error
                if ( status3.value.getError() != null ) {
                    throw new RuntimeException ( "Error getting provisional data (" +
                        status3.value.getError().getErrorCode() + ": " + status3.value.getError().getExceptionDescription() + ")." );
                }
                int dataRecordsSize = 0;
                if ( dataRecords != null ) {
                    dataRecordsSize = dataRecords.getStreamflowTransmission().size();
                }
                // Define the time series and add to the list
                TS ts = null;
                // FIXME SAM 2009-11-20 What are the data units?
                if ( timestep == null ) {
                    ts = new IrregularTS();
                }
                else if ( timestep.equalsIgnoreCase("Day") ) {
                    ts = new DayTS();
                }
                else if ( timestep.equalsIgnoreCase("Hour") ) {
                    ts = new HourTS();
                }
                tslist.add ( ts );
                // Set the identifier information
                TSIdent tsident = new TSIdent ( abbrev + "." + dataProvider + "." + variable + "." + timestep + "~ColoradoWaterSMS");
                ts.setIdentifier(tsident);
                // Set the period from the first and last data records
                if ( dataRecordsSize > 0 ) {
                    // Have some data records to process...
                    StreamflowTransmission dataRecord1 = dataRecords.getStreamflowTransmission().get(0);
                    ts.setDate1(parseTransmissionDateTime(dataRecord1.getTransDateTime(),0));
                    ts.setDate1Original(ts.getDate1());
                    StreamflowTransmission dataRecord2 = dataRecords.getStreamflowTransmission().get(
                        dataRecords.getStreamflowTransmission().size());
                    ts.setDate2(parseTransmissionDateTime(dataRecord2.getTransDateTime(),0));
                    ts.setDate2Original(ts.getDate2());
                    // Transfer the data from the data records to the time series.
                    if ( readData ) {
                        ts.allocateDataSpace();
                        String transDateTime;
                        double amount;
                        String transFlag;
                        int resultCount; // Number of values aggregated (use if limiting missing?)
                        DateTime date;
                        int datePrecision = ts.getDate1().getPrecision();
                        for ( StreamflowTransmission dataRecord : dataRecords.getStreamflowTransmission() ) {
                            // Date format is m/d/yyyy hh:mm:ss am/pm
                            transDateTime = dataRecord.getTransDateTime();
                            amount = dataRecord.getAmount();
                            transFlag = dataRecord.getTransFlag();
                            resultCount = dataRecord.getResultCount();
                            date = parseTransmissionDateTime ( transDateTime, datePrecision );
                            ts.setDataValue(date, amount, transFlag, 0);
                        }
                    }
                }
            }
        }
    }
    return tslist;
}
*/

/**
Set the cache of distinct station variables.
*/
private static void setDistinctStationVariableListCache ( List<String> variables )
{
    __distinctStationVariableList = variables;
}

}