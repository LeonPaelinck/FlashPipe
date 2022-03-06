import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

def Message processData(Message message) {
    Logger log = LoggerFactory.getLogger(this.getClass());

     try {
        processData("IFLOW_log_010", message);
    } catch (Exception ex) {
       log.error("processData error",ex);
    }
}


def Message processData_010(Message message) {
    Logger log = LoggerFactory.getLogger(this.getClass());

     try {
        processData("IFLOW_log_010", message);
    } catch (Exception ex) {
       log.error("processData error",ex);
    }
}

def Message processData_020(Message message) {
    Logger log = LoggerFactory.getLogger(this.getClass());

     try {
        processData("IFLOW_log_020", message);
    } catch (Exception ex) {
       log.error("processData error",ex);
    }
}

def Message processData(String prefix, Message message) {
    Logger log = LoggerFactory.getLogger(this.getClass());
    try {
        processBody(prefix, message);
        processHeadersAndProperties(prefix, message);
    } catch (Exception ex00) {
        log.error("processData error",ex00)
        StringWriter sw = new StringWriter();
        ex00.printStackTrace(new PrintWriter(sw));
        log.error(sw.toString());
    }
    return message;
}



def Map removeSapEntries(Map map) {
    def newMap = new HashMap();
    newMap.putAll(map);
    newMap.remove("MplMarkers");
    return newMap;

}



def void processBody(String prefix, Message message) {
    Logger log = LoggerFactory.getLogger(this.getClass());
    ExecutorService pool = Executors.newSingleThreadExecutor();
    def task = {c -> pool.submit( c as Callable)}
    def byte[] body_bytes = null;
    try {
        if (message==null) {
            body_bytes = new byte[0];
        } else if (message.getBody() == null) {
           body_bytes = new byte[0];
        } else {
            body_bytes = message.getBody(byte[].class);
        }

        def props = message.getProperties();
        def property_ENABLE_MPL_LOGGING = props.get("ENABLE_MPL_LOGGING");
        def property_ENABLE_FILE_LOGGING = props.get("ENABLE_FILE_LOGGING");

    property_ENABLE_FILE_LOGGING = "TRUE";
	property_ENABLE_MPL_LOGGING = "TRUE";


        if ("TRUE".equalsIgnoreCase(property_ENABLE_MPL_LOGGING)) {
            def messageLog = messageLogFactory.getMessageLog(message);
            messageLog.addAttachmentAsString(prefix+"_payload", new String(body_bytes), "text/plain");
        }

        if ("TRUE".equalsIgnoreCase(property_ENABLE_FILE_LOGGING)) {
            task{saveFile(""+prefix+"_body.xml", body_bytes)}
        }


    } catch (Exception ex01) {
        log.error("cannot save body",ex01);
        StringWriter sw = new StringWriter();
        ex01.printStackTrace(new PrintWriter(sw));
        log.info(sw.toString());
    }
}

def void processHeadersAndProperties(String prefix, Message message) {
    Logger log = LoggerFactory.getLogger(this.getClass());
    ExecutorService pool = Executors.newSingleThreadExecutor();
    def task = {c -> pool.submit( c as Callable)}
    try {
        def StringBuffer sb_html = new StringBuffer();
        def StringBuffer sb_text = new StringBuffer();
        def map = message.getHeaders();

        dumpProperties_HTML("Headers", map, sb_html);
        dumpProperties_TEXT("Headers", map, sb_text);

        map = removeSapEntries(message.getProperties());
        dumpProperties_HTML("Properties", map, sb_html);
        dumpProperties_TEXT("Properties", map, sb_text);


        def ex = map.get("CamelExceptionCaught");
        if (ex!=null) {

            def exmap = new HashMap();
            exmap.put("exception",ex);
            exmap.put("getCanonicalName",ex.getClass().getCanonicalName());
            exmap.put("getMessage",ex.getMessage());

            StringWriter swe = new StringWriter();
            ex.printStackTrace(new PrintWriter(swe));
            exmap.put("stacktrace",swe.toString());

            if (ex.getClass().getCanonicalName().equals("org.apache.camel.component.ahc.AhcOperationFailedException")) {
                exmap.put("responseBody",org.apache.commons.lang.StringEscapeUtils.escapeXml(ex.getResponseBody()));
                exmap.put("getStatusText",ex.getStatusText());
                exmap.put("getStatusCode",ex.getStatusCode());
            }

            dumpProperties_HTML("property.CamelExceptionCaught", exmap, sb_html);
            dumpProperties_TEXT("property.CamelExceptionCaught", exmap, sb_text);
        }

        def body_test = message.getBody();
        def bodymap = new HashMap();

        bodymap.put("Body",body_test);

        if (body_test!=null) {
            bodymap.put("CanonicalClassName",body_test.getClass().getCanonicalName());
        }


        dumpProperties_HTML("Body", bodymap, sb_html);
        dumpProperties_TEXT("Body", bodymap, sb_text);

        def props = message.getProperties();
        def property_ENABLE_MPL_LOGGING = props.get("ENABLE_MPL_LOGGING");
        def property_ENABLE_FILE_LOGGING = props.get("ENABLE_FILE_LOGGING");

    property_ENABLE_FILE_LOGGING = "TRUE";
	property_ENABLE_MPL_LOGGING = "TRUE";

        if ("TRUE".equalsIgnoreCase(property_ENABLE_MPL_LOGGING)) {
            def messageLog = messageLogFactory.getMessageLog(message);
            messageLog.addAttachmentAsString(prefix, sb_text.toString(), "text/plain");
        }

        if ("TRUE".equalsIgnoreCase(property_ENABLE_FILE_LOGGING)) {
            task{saveFile(""+prefix+".html", sb_html.toString().getBytes())};
        }

    } catch (Exception ex01) {
        log.error("cannot save headers and properties",ex01)
        StringWriter sw = new StringWriter();
        ex01.printStackTrace(new PrintWriter(sw));
        log.info(sw.toString());
    }

}


public void saveFile(String fileName, byte[] bytes) {
    try {
        def String METVIEWER_FOLDER = "metviewer";
        java.nio.file.Path path = Paths.get(METVIEWER_FOLDER+"/"+fileName);
        path.toFile().delete();
        path.getParent().toFile().mkdir();
        if (bytes!=null) {
            Files.write(path, bytes, StandardOpenOption.CREATE);
        } else {
            Files.write(path, "".getBytes(), StandardOpenOption.CREATE);
        }
    } catch (Exception ex) {
        System.out.println("saveFile.exception: filename:"+fileName+" ex:"+ex);
        throw new RuntimeException(ex);
    }
}




public void dumpProperties(String title, Map<String, Object> map, StringBuffer sb) {
    sb.append(title+"\n");
    for (String key : map.keySet()) {
        sb.append(key+"\t"+map.get(key)+"\n");
    }
}

public void dumpProperties_HTML(String title, Map<String, Object> map, StringBuffer sb) {
    sb.append("<h1>"+title+"</h1><br>\n");
    sb.append("<table>\n");
    for (String key : map.keySet()) {
        sb.append("<tr>\n");
        sb.append("<td>"+key+"</td><td>"+map.get(key)+"</td>\n");
        sb.append("</tr>\n");
    }
    sb.append("</table>\n");
}


public void dumpProperties_TEXT(String title, Map<String, Object> map, StringBuffer sb) {
    sb.append(title+"\n");
    for (String key : map.keySet()) {
        sb.append(key+": "+map.get(key)+"\n");
    }
    sb.append("\n");
}
