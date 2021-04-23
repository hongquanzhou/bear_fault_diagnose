package bean;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class TaskServer {
    String IP;
    int port;
    Socket s;
    BufferedReader in = null;
    BufferedWriter out = null;
    public TaskServer(String IP, int port) throws IOException {
        this.IP = IP;
        this.port = port;
        s = new Socket(IP,port);
        out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }
    public void send(String msg) throws IOException {
        out.write(msg+"\n");
        out.flush();
    }
    public String getData(String msg) throws IOException {
        out.write(msg+"\n");
        out.flush();
        String line = null;
        try{
            line = in.readLine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return line;
    }
    public Status query(String msg) throws IOException {
        out.write(msg+"\n");
        out.flush();
        String line = null;
        try{
            line = in.readLine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("recv:"+line);
        return new Gson().fromJson(line,Status.class);
    }
}
