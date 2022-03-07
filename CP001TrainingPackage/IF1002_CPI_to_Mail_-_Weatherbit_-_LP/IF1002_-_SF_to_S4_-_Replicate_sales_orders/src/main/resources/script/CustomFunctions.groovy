import com.sap.it.api.mapping.*;


def String setProperty(String proName,String proValue,MappingContext context){
    context.setProperty(proName, proValue);
	return proValue;
}
def String getProperty(String proName,MappingContext context){
	return context.getProperty(proName);
}
def String appendProperty(String proName,String proValue,MappingContext context){
    def String value = context.getProperty(proName);
    if (value == null || value.equals('')) {
	    context.setProperty(proName, proValue); 
    } else {
        proValue = value + "," + proValue;
        context.setProperty(proName, proValue); 
    }
    return proValue;
}

def String getValueByIndex(String textValue,int idx){
    def String[] values = textValue.split(",");
	return (values.length > idx? values[idx]: null);
}

def void setPropertyWithMultipleValues(String[] proValues,Output output,MappingContext context){
    def String values = null;
    if (proValues != null && proValues.length > 0) {
	    proValues.each{ 
	        v -> 
	        if (values == null)
	            values = v;
	        else
	            values = values + "," + v
	    }
	    output.addValue(values);
    } else
        output.addSuppress();
    
}

def String formatOrderId(String arg, MappingContext context){
	return arg.padLeft(10,'0'); 
}

def String formatOrderItemId(String arg){
	return arg.padLeft(6,'0');
}

