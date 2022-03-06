import com.sap.it.api.mapping.*;
import java.text.SimpleDateFormat;

/*Add MappingContext parameter to read or set headers and properties
def String customFunc1(String P1,String P2,MappingContext context) {
         String value1 = context.getHeader(P1);
         String value2 = context.getProperty(P2);
         return value1+value2;
}

Add Output parameter to assign the output value.
def void custFunc2(String[] is,String[] ps, Output output, MappingContext context) {
        String value1 = context.getHeader(is[0]);
        String value2 = context.getProperty(ps[0]);
        output.addValue(value1);
        output.addValue(value2);
}*/

def String customFunc(String FTE,String s_customDate10,String s_reference,String typeDienst){
    
        Date d_customDate10 = formatter.parse(s_customDate10);
        Date d_reference = formatter.parse(s_reference);

        Calendar cal_customDate10 = Calendar.getInstance();
        cal_customDate10.setTime(d_customDate10);
        Calendar cal_reference = Calendar.getInstance();
        cal_reference.setTime(d_reference);


//Calculate b_receiveParticipations and b_receiveTopper based on traineemonths
        //If buitendienst -- 1 month of greater -- true
        //If binnendienst -- 3 month of greater -- true
        int i_additionalmonth = 0;        
        if(cal_customDate10.get(Calendar.DAY_OF_MONTH) < 10){
            i_additionalmonth ++;
            //println i_additionalmonth;
        }
        
        
        if(s_custTypeDienst.equalsIgnoreCase("BINNEN")){
            //participation
            if(( cal_reference.get(Calendar.MONTH) + (12*cal_reference.get(Calendar.YEAR)) ) - ( cal_customDate10.get(Calendar.MONTH) + (12*cal_customDate10.get(Calendar.YEAR))) + i_additionalmonth > 3){
                FTE = 0;
            }
        }else if(s_custTypeDienst.equalsIgnoreCase("BUITEN")){
            if(( cal_reference.get(Calendar.MONTH) + (12*cal_reference.get(Calendar.YEAR)) ) - ( cal_customDate10.get(Calendar.MONTH) + (12*cal_customDate10.get(Calendar.YEAR))) + i_additionalmonth > 1){
                FTE = 0;
            }
        else{
            FTE=FTE
        }
	return FTE 
}