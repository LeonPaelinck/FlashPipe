import com.sap.it.api.mapping.*;
import java.text.SimpleDateFormat;

//This function takes a substring if the string succeeds the given length
def String SubstringLength(String i_string, String i_length){
    
    Integer length = Integer.parseInt(i_length);
    String output;
    
	 if(i_string.length() >= length){
       output = i_string.substring(0,length)
    }
    else{
        output = i_string
    }
    
	return output; 
}

//This function takes a substring starting from right side
def String SubstringRight(String i_string, String i_length){
    
	Integer lengthFromRight = Integer.parseInt(i_length);
	
    String output = i_string.drop(i_string.size() - lengthFromRight);
    
	return output;
}

//Add MappingContext as an additional argument to read or set Headers and properties.
//This function allows to access a header
def String getHeaderValue(String headerName, MappingContext context){
	return context.getHeader(headerName);
}

//This function allows to access a property
def String getPropertyValue(String propertyVal, MappingContext context){
	return context.getProperty(propertyVal);
}

//Concats all the string into one long, seperated by ","
def void setAllValuesInOneString(String[] input, Output output, MappingContext context){
	String response = new String();
	for(String s : input){
		if(response.isEmpty()){
			response =  s;
		}else{
			response = response + ", " + s;
		}
	}
	output.addValue(response);
}

//Removes all the surpress of a message
def void removeSuppress(String[] values, Output output, MappingContext context) {
    values.each { v ->
        if (!output.isSuppress(v)) {
            output.addValue(v)
        }
    }
}

//passIfExistsAndHasValue - passes value if the node exists and a value is present
def void passIfExistsAndHasValue(String[] values, Output output, MappingContext context){
    if (values != null && values.length > 0) {
		for (int i = 0; i < values.length; i++) {
			String str = values[i];
			if (str != null && str.trim().length() > 0) {
				output.addValue(str);
			} else {
				output.addSuppress();
			}
		}
	} else {
		output.addSuppress();
	}
}


// function that adds a specified amount of time in SEC MIN DAYS YEAR month
def String addDuration(String date, String duration, String durationUnit) {
    // Parse the date string
    def f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    def d = f.parse(date)
    // Add the specified duration
    def cal = Calendar.getInstance()
    cal.setTime(d)
    switch (durationUnit) {
        case 'MIN':
            cal.add(Calendar.MINUTE, duration as Integer)
		case 'SEC':
            cal.add(Calendar.SECOND, duration as Integer)
		case 'HOUR':
            cal.add(Calendar.HOUR, duration as Integer)
		case 'DAY':
            cal.add(Calendar.DAY, duration as Integer)
		case 'MONTH':
            cal.add(Calendar.MONTH, duration as Integer)
		case 'YEAR':
            cal.add(Calendar.YEAR, duration as Integer)
            break
        default:
            // If the default case is reached, the duration unit is unknown.
            // That's probably an error.
            break
    }
    // Set the timezone and format the output
    f.setTimeZone(TimeZone.getTimeZone('GMT' + date[-6..-1]))
    return f.format(cal.getTime())
}

