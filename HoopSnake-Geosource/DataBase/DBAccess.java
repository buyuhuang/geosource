package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Connor
 */
public class DBAccess {
	   // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://www.okenso.com:3306/dev?allowMultiQueries=true";

	   //  Database credentials
	   static final String USER = "hdev";
	   static final String PASS = "devsnake371";
    
    private final Connection dbconnection;
    
    public DBAccess() throws SQLException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("fail: " + e.getMessage());
        }
        
        dbconnection = DriverManager.getConnection(DB_URL,USER,PASS);
    }

    /**
     * Get the file name where a serialized dynamic form specification can be
     * found within the file system
     * @param channelName the name of the channel whose form's spec should be retrieved
     * @param ownerName the channel's creator, fort uniqueness
     * @return a string representation of specification's name in the file system
     */
    public String getFormSpecLocation(String channelName, String ownerName)
    {
        Integer fileName = null;
        try (Statement statement = dbconnection.createStatement()) {
            ResultSet results = statement.executeQuery(Queries.getFormSpec(channelName, ownerName));
            results.next();
            fileName = results.getInt(1);
        }
        catch (SQLException SQLe)
        {
            System.err.println("formSpec not found!! Possible broken Incident request");
        }
        return ownerName + "." + fileName;
    }
    
    /**
     * saves the filepath to the database for a Field's contents previously saved to the filesystem
     * @param channelName the name of the channel being posted to
     * @param ownerName the creator of the channel, used for uniqueness
     * @param postNum the number of the post we are populating
     * @param fieldName the title of this picture's field
     * @param filePath the filepath to be posted
     */
    public void saveField(String channelName, String ownerName, int postNum, String fieldName, String filePath)
    {
        try (Statement statement = dbconnection.createStatement()) {
            statement.execute(Queries.saveField(channelName, ownerName, postNum, fieldName, filePath));
        }
        catch (SQLException SQLe)
        {
            System.err.println("Saving picture field failed");
        }
    }

    /**
     * make a new post in the specified channel and return its identifier
     * @param channelName the name of the channel to post to
     * @param ownerName the creator of the channel
     * @param posterName the person posting to this channel
     * @return an integer value indexing the new post within the channel
     */
    public int newPost(String channelName, String ownerName, String posterName) {
        
        try (Statement statement = dbconnection.createStatement()) {
            statement.execute(Queries.newRow(channelName, ownerName, posterName));
            ResultSet results = statement.executeQuery(Queries.getNewPostNum(channelName, ownerName));
            results.next();
            return results.getInt(1);
        }
        catch (SQLException SQLe)
        {
            System.err.println("Creating new Post failed");
            return -1;
        }
    }
}