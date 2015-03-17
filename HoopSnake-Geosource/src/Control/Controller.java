package Control;

import Communication.CommSocket;
import DataBase.DBAccess;
import FileSystem.FileAccess;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.Incident;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Connor
 */
public class Controller {
    
    private DBAccess dbAccess; //database abstraction
    private FileAccess fileAccess; //file system abstraction
    
    private static int numConnections = 5; //maximum simultaneous socket inputs
    
    public Controller()
    {
        dbAccess = new DBAccess();
        fileAccess = new FileAccess();
    }
    
    /**
     * runs the server
     * @param args specifies the port number and concurrent thread count
     */
    public static void main(String[] args)
    {
    	numConnections = Integer.parseInt(args[1]);
        Controller Server = new Controller();
        Server.run(Integer.parseInt(args[0]));
        
    }
    
    public void run(int portNum)
    {
    	CommSocket.portNum = portNum;
        ExecutorService exec = Executors.newCachedThreadPool();
        LinkedList<Future<Incident>> list = new LinkedList();
        for (int x = 0; x < numConnections; x ++)
        {
            list.add(exec.submit(new CommSocket(this))); //fill list of future results
        }
        
        while (true)
        {
            ListIterator<Future<Incident>> iter = list.listIterator();
            while (iter.hasNext())
            {
                Future<Incident> incident = iter.next();
                if (incident.isDone()) //filter out completed socket tasks
                {
                    try
                    {
                        dealWith(incident.get()); //deal with demands
                    }
                    catch (InterruptedException Ie)
                    {
                        //swallowing interrupt, should never happen because of isDone check
                        throw new RuntimeException("Should never receive an Interrupt Exception");
                    }
                    catch (ExecutionException Ee)
                    {
                        throw new RuntimeException("Socket Crashed:" + Ee.getCause().getMessage());
                    }
                    iter.remove();
                    iter.add(exec.submit(new CommSocket(this))); //replace new socket
                }
            }
        }
    }
    
    /**
     * delegates the parsing of an incident to te file and database systems
     * @param incident the incident to be handled
     */
    private void dealWith(Incident incident)
    {
        if (null == incident) return; //wasn't a real request
        int postNum = dbAccess.newPost(incident.getChannelName(), incident.getOwnerName());
        for (FieldWithContent field : incident.getFieldList())
        {
            String filePath = fileAccess.saveField(field.getContent());
            dbAccess.saveField(incident.getChannelName(), incident.getOwnerName(), postNum, field.getTitle(), filePath);
        }
    }
    
    public ArrayList<FieldWithoutContent> getForm(String channelName, String ownerName)
    {
        String fileName = dbAccess.getFormSpecLocation(channelName, ownerName); //get spec's file name in filesystem
        return fileAccess.getFormSpec(fileName); //retreive form spec
    }
}