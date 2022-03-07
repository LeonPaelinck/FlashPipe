import com.sap.it.api.mapping.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

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

//This function will return a boolean with a check if the string is present
def String ContainString(String input, String check){
    
    String output = input.contains(check);
    
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

//Split string at a predifined char
def String splitStringAtChar(String input, String charSplit, int indexReturn){
    String output = "";
	try {
	    String[] parts = input.split(charSplit);
	    output = parts[indexReturn];
	} catch (Exception e) {
		// Do nothing 
	}
	
	return output;
    
}

//Split each value in a seperate context
def void SplitStringAtCharInDifferentContexts(String[] input, Output output, MappingContext context){
	
	String[] iArray = [];
	
	for (int i = 0; i < input.size(); i++) {
		iArray = input[i].split(',');
		for (int j = 0; j < iArray.size(); j++) {
			output.addValue(iArray[j]);
		}
	}
}

//Split string at a match
def String SubStringTillMatch(String input, String match){

    def output = input.substring(0,input.indexOf(match));
	
	return output;
    
}

//reserve the sequence of the string
def String ReverseString(String input){
	def output = input.reverse();
	return output;
}

//Substract X amount of days
def String subtractDays(String inDate, int days) {
	String fmt = "yyyy-MM-dd";
	String dt = inDate;
	java.text.DateFormat df = new java.text.SimpleDateFormat(fmt);
	java.util.Calendar cal = java.util.Calendar.getInstance();
	Date previousDay = null;
	String output = "";
	try {
		cal.setTime(df.parse(dt));
		cal.add(java.util.Calendar.DAY_OF_MONTH, - days);
		previousDay = cal.getTime();
		DateFormat dateFormat = new SimpleDateFormat(fmt);  
        output = dateFormat.format(previousDay);
	} catch (Exception e) {
		output = "";
	}
	
	return output;
		
}

// function determine the day of the week of a date. format returned wil be for example "Monday"
def String DayOfTheWeek(String date) {
	
    // Parse the date string
    def f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
	SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEE");
    def d = f.parse(date)
    // Add the specified duration
    def c = Calendar.getInstance()
	c.setTime(d)
	String dayOfWeek = dateFormat.format(c.getTime());
		
    // Set the timezone and format the output
    return dayOfWeek
}

def String DateSubstring(String input){
	if(input.length()>10)
	    return input.substring(0,10);
	else
	    return input;
}

//Removes everything what is not a numeric value
def String RemoveNonNumeric(String input){
    return input.replaceAll("[^0-9]", "");
}


/*Add Output parameter to assign the output value.*/
def void RepeatFirstQForAll(String[] inDate, Output output, MappingContext context) {
	
	for (int i = 0; i < inDate.length; i++) {
		output.addValue(inDate[0]);
	}
}

//get first value of the context
def void getFirstValueFromContext(String[] inValue, Output output, MappingContext context){
	for (int i = 0; i < inValue.length; i++) {
	    output.addValue(inValue[i]);
	    break;
	}
}

//get last value of the context
def void checkLastValueFromContext(String[] inValue, Output output, MappingContext context){
    for (int i = 0; i < inValue.length; i++) {
	    if ((i == (inValue.length - 1))&&(!inValue[i].equals(""))){
    		output.addValue("true");
    	} else {
    		output.addValue("false");
    	}
	}
}

//remove blank values from the context
def void removeBlanksfromQ(String[] inValue, Output output, MappingContext context){
	for (int i = 0; i < inValue.length; i++) {
	    if(!(inValue[i].equals(""))){
	        output.addValue(inValue[i]);
	    }
	}
}

//url decode content
def String UrlDecode(String input){
	def output = java.net.URLDecoder.decode(input, java.nio.charset.StandardCharsets.UTF_8.toString());
	return output;
}

//url encode content
def String UrlEncode(String input){
	def output = java.net.URLEncoder.encode(input, java.nio.charset.StandardCharsets.UTF_8.toString());
	return output;
}

//base64 encode content
def String base64Encode(String input){
	def output = input.bytes.encodeBase64().toString();
	return output;
}

//base64 decode content
def String base64Decode(String input){
	def output = new String(input.decodeBase64());
	return output;
}

//Generate X amounts of contexts
def void GenerateContexts(String[] input, Output output, MappingContext context){
    Integer k = input[0] as int;
    
	for (int i = 0; i < k; i++) {
	        output.addValue("queueContext " + i);
	}
}