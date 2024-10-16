/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DeanK
 */
public class Database {
    
    private static final String dbusername = "pdc";
    private static final String dbpassword = "pdc";
//    private static final String url = "jdbc:derby://localhost:1527/HotelDB;create=true";
    private static final String url = "jdbc:derby:HotelDB;create=true";
    Connection conn = null;
    
    public Database() {
        
    }
    
    
    //Bookings table: bookingID; GuestName; guestID; isCurrent; roomNumber
    //Guests table: guestID;guestName
    //Rooms table: roomNumber; roomType; roomStatus (0 vacant, 1 occupied, 2 needcleaning
    //Guest request: bookingID; requestType; description; - single booking ID can have multiple requests
    public void establishConnection() {
        if (this.conn == null) {
            try {
                conn = DriverManager.getConnection(url, dbusername, dbpassword);
                Statement statement = conn.createStatement();
                String listName1 = "Bookings";
                String listName2 = "Guests";
                String listName3 = "Rooms";
                String listName4 = "GuestRequest";
                String listName5 = "Staff";
                
                //Check if BOOKINGS table exists yet, if not create it
                if(!checkTableExisting(listName1)) {
                    statement.executeUpdate("CREATE TABLE " + listName1 + " (bookingID int not null, "
                    + "guestName varchar(20), guestID int not null, "
                    + "bookingstatus int not null, roomNumber int, PRIMARY KEY(bookingID))");
                }              
                
                //Check if GUESTS table exists yet, if not create it
                if(!checkTableExisting(listName2)) {
                    statement.executeUpdate("CREATE TABLE " + listName2 + " (guestID int not null, "
                    + "guestName varchar(20), guestPhone varchar(20), guestStatus int)");
                    statement.executeUpdate("INSERT INTO GUESTS (GUESTID, GUESTNAME, GUESTPHONE) VALUES (0, 'qwe', '98989')");
                }
                //Check if ROOMS table exists yet
                if(!checkTableExisting(listName3)) {
                    statement.executeUpdate("CREATE TABLE " + listName3 + " (roomNumber int not null, "
                    + "roomType varchar(20), roomStatus int not null)"); 
                    for (int i = 1; i < 11; i++) {
                        statement.executeUpdate("INSERT INTO ROOMS (ROOMNUMBER, ROOMTYPE, ROOMSTATUS) VALUES (" + i + ", 'STANDARD', 0)");
//                        INSERT INTO pdc.ROOMS (ROOMNUMBER, ROOMTYPE, ROOMSTATUS) VALUES (0, 'Standard', 0);
                    }
                    for (int i = 11; i < 21; i++) {
                        statement.executeUpdate("INSERT INTO ROOMS (ROOMNUMBER, ROOMTYPE, ROOMSTATUS) VALUES (" + i + ", 'DELUXE', 0)");
                    }
                    for (int i = 21; i < 31; i++) {
                        statement.executeUpdate("INSERT INTO ROOMS (ROOMNUMBER, ROOMTYPE, ROOMSTATUS) VALUES (" + i + ", 'SUITE', 0)");
                    }
                    
                }
               
                //Check if guest request table exists yet
                if(!checkTableExisting(listName4)) { 
                    statement.executeUpdate("CREATE TABLE " + listName4 + " (bookingID int not null, guestID int not null,"
                    + "requestType int, description varchar(100))");
                }
                
                if(!checkTableExisting(listName5)) {
                    statement.executeUpdate("CREATE TABLE " + listName5 + " (staffId int not null, "
                    + "staffName varchar(20), staffpw varchar(20))");
                    statement.executeUpdate("INSERT INTO STAFF (STAFFID, STAFFNAME, STAFFPW) VALUES (0, 'kake', 123)");
                }
                                       
                statement.close();
            } catch (SQLException e) {
                System.out.println("SQLException in establishConnection() in database.java: " + e.getMessage());
            }
        }
        
    }
    
    private boolean checkTableExisting(String newTableName) {
        boolean flag = false;
        
        try {
            System.out.println("Checking for existing tables.... ");
//            String[] types = {"TABLE"};
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rsDBMeta = dbmd.getTables(null, null, null, null);
            
            while(rsDBMeta.next()) {
                String tableName = rsDBMeta.getString("TABLE_NAME");
                if (tableName.compareToIgnoreCase(newTableName) == 0) {
                    System.out.println(tableName + " table exists!");
                    flag = true;
                }
            }
//            if (rsDBMeta != null) {
                rsDBMeta.close();
//            }
            
        } catch (SQLException ex) {
            System.out.println("Database: checkTableExisting() SQLException: " + ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println("Database: checkTableExisting() NullPointerException: " + ex.getMessage());
        }
        return flag;
    } 
    
    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch(SQLException ex) {
                System.out.println("Database: loseConnections() SQLException: " + ex.getMessage());
            }
        }
    }
    
    public Data checkStaffLogin(String username, String password, Data data){        
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT staffname, staffpw FROM staff"
            + " WHERE staffname = '" + username + "'");
            
            if (rs.next()) { //if result is present for above query
                String pw = rs.getString("staffpw");
                System.out.println("User found\nChecking valid login.....");
                if (password.compareTo(pw) == 0) {
                    System.out.println("Successfully logged In! Loading staff menu....");
                    data.loginFlag = true;
                    data.currentloggeduser = rs.getString("staffname");
                    return data;
                }
                else {
                    System.out.println("Invalid login - check your password");
                }
            }
            else {
                System.out.println("Invalid Staff Login - username does not exist");
            }
            
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException in Database checkLogin(): " + e.getMessage());
        }
        data.loginFlag = false;
        return data;
    }
    
    //test guest: qwe, 123
    //Returns to model checkGuestLogin
    public Data checkGuestLogin(String username, String password, Data data) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT guestName, GUESTPHONE FROM GUESTS"
            + " WHERE guestName = '" + username + "'");
            if (rs.next()) {
                String pw = rs.getString("GUESTPHONE");
                System.out.println("User found\n Checking valid login......");
                if (password.compareTo(pw) == 0) {
                    System.out.println("Successfully logged In! Loading guest Menu.....");
                    data.currentloggeduser = rs.getString("GUESTNAME");
                    data.loginFlag = true;
                    return data;
                }
                else {
                    System.out.println("Invalid login - check your password");
                }
            } else {
                System.out.println("Invalid Guest Login - Username does not exist");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException in Database checkGuestLogin(): " + e.getMessage());
        }
        data.loginFlag = false;
        return data;
    }
    
    private int getRecordCount(String tableName) {
        
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS recordCount FROM " + tableName); //reference: https://stackoverflow.com/questions/7886462/how-to-get-row-count-using-resultset-in-java
            rs.next();
            int rowCount = rs.getInt("recordCount");
            
            statement.close();
            rs.close();
            return rowCount;
        }catch(SQLException e) {
            System.out.println("SQLException in getRecordCount() in Database.java: " + e.getMessage());
        }

        return -1; //error
    }

    
//  Find a room that matches roomstatus and roomtype, and select one by random. rs.next() returns false when no room matches it and 
//  returns a negative 1 indicating no room found, otherwise the roomNumber is returned which will be assigned to booking
    public int fetchAvailableRoom(String roomType) {
        int roomNumber = -1;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT roomNumber FROM rooms WHERE roomstatus = 0"
                    + " and roomtype = '" + roomType + "' ORDER BY RANDOM() fetch first 1 rows only");
//            SELECT * FROM PDC.ROOMS where roomstatus = 0 and roomtype = 'SUITE' ORDER BY RANDOM() FETCH FIRST 1 ROWS ONLY;
            if (!rs.next()) { //is this right??? if no results
                System.out.println("No rooms available for this Roomtype. Select another roomtype");
            } else {
                roomNumber = rs.getInt("roomNumber");
            }
            
            rs.close();
            statement.close();
        } catch(SQLException e) {
            System.out.println("SQLException in fetchAvailableRooms() in database.java: " + e.getMessage());
        }
        
        return roomNumber; 
    }
    
    
    public Data createBooking(String guestName, String guestPhone, String roomType, int guestID, Data data) {
        Room newRoom;
        Booking newBooking;
        Guest newGuest;
        
        
        
        int assignedRoomNumber = fetchAvailableRoom(roomType);
        if (assignedRoomNumber == -1) { //no room available of this room type
            data.setBookingSuccess(false);
            return data;
//            return false;
        }
        newRoom = new Room(assignedRoomNumber, roomType);

        
        try {
            PreparedStatement pstmt;
            int bookingRowCount = getRecordCount("BOOKINGS");
            int guestRowCount;
            if (guestID == -1) { //No Matching guest, so generate a new guestID and insert
                guestRowCount = getRecordCount("GUESTS");
                pstmt = conn.prepareStatement("INSERT INTO GUESTS (GUESTID, GUESTNAME, GUESTPHONE, GUESTSTATUS)"
                    + "VALUES (?,?,?,?)");
                pstmt.setInt(1, guestRowCount);
                pstmt.setString(2, guestName);
                pstmt.setString(3, guestPhone);
                pstmt.setInt(4, 20); //gueststatus for new guest w/ new booking is 20 = Active
                pstmt.executeUpdate();
                System.out.println("Added new Guest");
            } else { //Guest exists already
//                UPDATE guests SET gueststatus = 1 WHERE guestid = 3
                Statement statement = conn.createStatement();
                statement.executeUpdate("UPDATE guests SET gueststatus = 20 WHERE guestID = " + guestID + " AND gueststatus = 1");
                guestRowCount = guestID;
            }
            
            newGuest = new Guest(guestName, guestPhone);
            newGuest.setGuestID(guestRowCount);
            

            pstmt = conn.prepareStatement("INSERT INTO BOOKINGS (Bookingid, guestname, guestId, bookingstatus, roomnumber)"
                    + "VALUES (?,?,?,?,?)");
            pstmt.setInt (1, bookingRowCount); //99 as test bookingid
            pstmt.setString (2, guestName); //guestname
            pstmt.setInt(3, guestRowCount); //guestid
            pstmt.setInt(4, 1); //set as pending booking
            pstmt.setInt(5, assignedRoomNumber); //test roomnumber
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("UPDATE rooms set roomstatus = 1 where roomnumber = ?");
            pstmt.setInt(1, assignedRoomNumber);               
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("Added New Booking");
            
            data.setBookingSuccess(true);
            newBooking = new Booking(bookingRowCount, newGuest.guestName, newGuest);
            newBooking.setRoom(newRoom);
            
            data = setRecentBookingRoomGuest(data, newBooking, newGuest, newRoom);
            return data;
//            return true;
        } catch(SQLException e) {
            System.out.println("SQLException in Database(createbooking): " +e.getMessage());
        }
        data.setBookingSuccess(false);
        return data;
//        return false;
    }
    
    private Data setRecentBookingRoomGuest(Data data, Booking Booking, Guest guest, Room room) {
        data.setRecentBooking(Booking);
        data.setRecentGuest(guest);
        data.setRecentRoom(room);
        
        return data;
    }

    
    //Called in model before making booking for finding an identical guest (same matching details)
    public Guest matchingGuestExist(String guestName, String guestPhone){
        Guest guest = null;
//        SELECT * FROM guests WHERE guestname = 'qwe' AND guestphone = '12345';
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT guestID, guestName, guestPhone from GUESTS "
                    + "WHERE guestName = ? AND guestphone = ?");
            pstmt.setString(1, guestName);
            pstmt.setString(2, guestPhone);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                guest = new Guest(rs.getString("guestname"), rs.getString("guestphone"));
                guest.setGuestID(rs.getInt("guestid"));                     
            }
            
            rs.close();
            pstmt.close();
            return guest;
        } catch(SQLException e){
            System.out.println("SQLException in matchingGuestExist() in database.java: " + e.getMessage());
        } catch(NullPointerException e) {
            System.out.println("NullPointerException in matchingGuestExist() in database.java" + e.getMessage());
        }
        return guest; //no matches will return a null
    }
    
    
    
    public void  fetchRooms(MyTableModel tableModel, String filter) {
        Vector<String> columnNames = new Vector<>();
        Vector<Vector<Object>> rowData = new Vector<>();
        
        try {
            Statement statement = conn.createStatement();
            ResultSet rs;
            switch(filter) {
                case "Available Rooms":
                    rs = statement.executeQuery("SELECT * FROM rooms WHERE roomstatus = 0");
                    break;
                case "N/A Rooms":
                    rs = statement.executeQuery("SELECT * FROM rooms WHERE roomstatus != 0");
                    break;
                case "N/A Rooms - Occupied":
                    rs = statement.executeQuery("SELECT * FROM rooms WHERE roomstatus = 20 OR roomstatus = 21");
                    break;
                case "N/A Rooms - Booked":
                    rs = statement.executeQuery("SELECT * FROM rooms WHERE roomstatus = 1");
                    break;
                case "N/A Rooms - Need Cleaning":
                    rs = statement.executeQuery("SELECT * FROM rooms WHERE roomstatus = 3 OR roomstatus = 21");
                    break;
                default: //All Rooms
                    rs = statement.executeQuery("SELECT * FROM rooms");
                    break;
            }
            
            //Column names
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                columnNames.add(rsmd.getColumnName(i));
            }
                       
            //Row Data
            while(rs.next()) {
                Vector<Object> row = new Vector<Object>();
                for (int i = 1; i <= numColumns; i++) {
                    if (i == 3) {
                        int status = rs.getInt("ROOMSTATUS");
                        switch (status) {
                            case 0:
                                row.add("AVAILABLE");
                                break;
                            case 1:
                                row.add("BOOKED BY GUEST: " + findMatchGuestwithRoom(rs.getInt("ROOMNUMBER")));//insert here guest id
                                break;
                            case 20:
                                row.add("OCCUPIED by GUEST: " + findMatchGuestwithRoom(rs.getInt("ROOMNUMBER")));
                                break;
                            case 21:
                                row.add("OCCUPIED by GUEST: (Cleaning requested)" + findMatchGuestwithRoom(rs.getInt("ROOMNUMBER")));
                            case 3:
                                row.add("CHECKED OUT - Need cleaning"); 
                                break;
                            default:
                                row.add("Error: INVALID CODE - fetchRooms() in Database.java");
                                break;
                        }                          
                    } else {
                        row.add(rs.getObject(i));                        
                    }
                }
                rowData.add(row);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("SQLException in fetchRooms - Database.java: " + e.getMessage());
        }
        
        tableModel.updateTableModelData(rowData, columnNames);
    }
        
    private String findMatchGuestwithRoom(int roomNumber) {
        String guestDetails = "null";
        
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT guestname, guestid FROM bookings WHERE roomnumber = " + roomNumber);
            rs.next();
            guestDetails = rs.getInt("GUESTID") + " " + rs.getString("GUESTNAME");
            
            statement.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("SQLException in findMatchGuestwithRoom: " + e.getMessage());
        }catch (NullPointerException e) {
            System.out.println("NullPointerException in findMatchGuestwithRoom(no match found)" + e.getMessage());
        }
        
        return guestDetails;
    }
    
    public void fetchGuests(MyTableModel tableModel, String filter) {
        Vector<String> columnNames = new Vector<>();        
        Vector<Vector<Object>> rowData = new Vector<>();  
        
        try{
            Statement statement = conn.createStatement();
            
            ResultSet rs;
            switch(filter) {
                case "Active Guests":
                    rs = statement.executeQuery("SELECT * FROM guests WHERE gueststatus = 20 OR gueststatus = 21");
                    break;
                case "Inactive Guests":
                    rs = statement.executeQuery("SELECT * FROM guests WHERE gueststatus = 1");
                    break;
                case "Guests with Request":
                    rs = statement.executeQuery("SELECT * FROM guests WHERE gueststatus = 21");
                    break;
                default:
                    rs = statement.executeQuery("SELECT * FROM guests"); //ALL GUESTS SHOWN
                    break;
            }
            
            
            //Column names
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            for(int i = 1; i <= numColumns; i++) {
                columnNames.add(rsmd.getColumnName(i));
            }
            
            //Row Data
            while(rs.next()){
                Vector<Object> row = new Vector<Object>();
                for (int i = 1; i <= numColumns; i++) {
                    row.add(rs.getObject(i));
                }
                rowData.add(row);
            }
            rs.close();
            statement.close();
            
        } catch(SQLException e) {
            System.out.println("SQLException in fetchGuests - Database.java: " + e.getMessage());
        }
        
        tableModel.updateTableModelData(rowData, columnNames);   
    }
    
    
    public void fetchBookings(MyTableModel tableModel, String filter) {
//        "All Bookings", "2=Active Bookings", "1=Pending Bookings", "3=Historical Bookings"
        
        
        Vector<String> columnNames = new Vector<>();        
        Vector<Vector<Object>> rowData = new Vector<>();  
        
        try{
            Statement statement = conn.createStatement();
            ResultSet rs;
            switch (filter) {
                case "Active Bookings":
                    rs = statement.executeQuery("SELECT * FROM bookings WHERE bookingstatus = 2");
                    System.out.println("Filter Active Bookings");
                    break;
                case "Pending Bookings":
                    rs = statement.executeQuery("SELECT * FROM bookings WHERE bookingstatus = 1");
                    System.out.println("Filter Pending Bookings");
                    break;
                case "Historical Bookings":
                    rs = statement.executeQuery("SELECT * FROM bookings WHERE bookingstatus = 3");
                    System.out.println("Filter Historical Bookings");
                    break;
                default:
                    rs = statement.executeQuery("SELECT * FROM bookings"); //ALL BOOKINGS PRINTED
                    System.out.println("Filter All Bookings");
                    break;
            }

            
            //Column names
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            for(int i = 1; i <= numColumns; i++) {
                columnNames.add(rsmd.getColumnName(i));
            }
//            tableModel.setColumnIdentifiers(columnNames);
            
            //Row Data
            while(rs.next()){
                Vector<Object> row = new Vector<Object>();
                for (int i = 1; i <= numColumns; i++) {
                    row.add(rs.getObject(i));
                }
                rowData.add(row);
            }
            rs.close();
            statement.close();
            
        } catch(SQLException e) {
            System.out.println("SQLException in fetchBookings - Database.java: " + e.getMessage());
        }
        
        tableModel.updateTableModelData(rowData, columnNames);
    }
    
    public boolean cancelBooking(int roomnumber) {
        System.out.println("Cancelling booking in database...");
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE rooms SET roomstatus = 0 WHERE roomnumber = " + roomnumber);
            statement.executeUpdate("DELETE FROM bookings WHERE roomnumber = " + roomnumber);
            statement.close();
            System.out.println("Successfully cancelled booking");
            return true;
        } catch(SQLException e) {
            System.out.println("SQLException in cancelBooking Database.java: " + e.getMessage());
        }
        return false;
    }
    
    public boolean checkOUTGuest(int roomnumber) {
        System.out.println("Checking Out Guest in database.....");
        try {
            int guestID = findGuestIDwithRoomNo(roomnumber);
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE rooms SET roomstatus = 3 WHERE roomnumber = " + roomnumber);
            statement.executeUpdate("UPDATE bookings SET bookingstatus = 3 WHERE roomnumber = " + roomnumber);
            setGuestIDStatus(guestID); 
            
            statement.close();
            System.out.println("Success checking Out Guest");
            return true;
        } catch (SQLException e) {
            System.out.println("SQLException in checkOutGUest Database.java: " + e.getMessage());
        }       
        return false;
    }
    
    private int findGuestIDwithRoomNo(int roomnumber) {
        int guestID = -1;
        try {
            Statement statement = conn.createStatement();
            
            ResultSet rs = statement.executeQuery("SELECT guestid FROM bookings WHERE roomnumber = " + roomnumber);
            rs.next();
            guestID = rs.getInt("GUESTID");
            
            rs.close();
            statement.close();
        }catch (SQLException e) {
            System.out.println("SQLException in findGuestIDwithRoomNo");
        } catch(NullPointerException e) {
            System.out.println("NullPointerExcecption in setGuestIDStatus after checkout - no match");
        }
        return guestID;
    }
    
    //Checks for active bookings - all bookings are historical, then set Guest status to inactive = 1
    private boolean setGuestIDStatus(int guestID) {
        try {
            Statement statement = conn.createStatement();
            //find matching guestid with roomnumber
            ResultSet rs = statement.executeQuery("SELECT bookingID FROM bookings WHERE guestid = " + guestID + " AND bookingstatus != 3");
            if (!rs.next()) {
                System.out.println("No active bookings");
//                UPDATE guests SET gueststatus = 1 WHERE guestid = 3
                statement.executeUpdate("UPDATE guests SET gueststatus = 1 WHERE guestid = " + guestID);
                return false;
            };
        }catch (SQLException e) {
            System.out.println("SQLException in setGuestIDStatus after checkout:" + e.getMessage());
        } catch(NullPointerException e) {
            System.out.println("NullPointerExcecption in setGuestIDStatus after checkout - no match: " + e.getMessage());
        }
        return true;
    }
    
    public boolean checkINGuest(int roomnumber) {
        System.out.println("Checking In Guest....");
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE rooms SET roomstatus = 20 WHERE roomnumber = " + roomnumber);
            statement.executeUpdate("UPDATE bookings SET bookingstatus = 2 WHERE roomnumber = " + roomnumber);
            statement.close();
            System.out.println("Success checking in Guest");
            return true;
        } catch (SQLException e) {
            System.out.println("SQLException in checkInGUest Database.java: " + e.getMessage());
        }       
        return false;
    }

    
    public String[] findBookingIDMatch(int bookingID, int[] targetRoomStatus) {
        System.out.println("Checking for matching bookingID on the system");
        String[] bookingDetails = null;
        
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM bookings WHERE bookingid = " + bookingID);
            if(!rs.next()) {
                System.out.println("No matching Booking ID on system");
            } else {
                String guestname = rs.getString("GUESTNAME");
                String guestid = Integer.toString(rs.getInt("GUESTID")) ;
                String roomnumber = Integer.toString(rs.getInt("ROOMNUMBER"));                
                bookingDetails = new String[] {guestname, guestid, roomnumber};
                
                rs = statement.executeQuery("SELECT roomstatus FROM rooms WHERE roomnumber = " + roomnumber);
                rs.next();
                int roomstatus = rs.getInt("ROOMSTATUS");
                if (roomstatus != targetRoomStatus[0] && roomstatus != targetRoomStatus[1]){ 
                    System.out.println("Room is already occupied/checked in/checkout/not booked");           
                    return null;
                }
            }
            rs.close();
            statement.close();
        } catch(SQLException e) {
            System.out.println("SQLException in findBookingIDMatch: " + e.getMessage());
        }
        return bookingDetails;
    }
    
    
    
}
