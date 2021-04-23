package util;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FormdataParse {
    BufferedReader bin = null;
    public Map<String,String> names_values = new HashMap<>();

    public FormdataParse(InputStream in)
    {
        bin = new BufferedReader(new InputStreamReader(in));
    }
    public void Parse() throws IOException {
        String name = null;
        String value = null;
        int i = 0;
        for(String line;(line = bin.readLine())!=null;)
        {
            if(i%4 ==1)
            {
                name = line.substring(38,line.length()-1);
            }
            if(i%4 == 3)
            {
                value = line;
                names_values.put(name,value);
            }
            i++;

        }

    }
    public void print()
    {
        Iterator<Map.Entry<String, String>> it = names_values.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<String,String> entry = it.next();
            System.out.println(entry.getKey()+":"+entry.getValue());
        }

    }
    public String getByKey(String key)
    {
        String returnvalue = null;
        Iterator<Map.Entry<String, String>> it = names_values.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<String,String> entry = it.next();
            if(entry.getKey().equals(key))
            {
                returnvalue = entry.getValue();
                break;
            }
            else
            {
                returnvalue = null;
            }
        }
        return returnvalue;
    }
}
