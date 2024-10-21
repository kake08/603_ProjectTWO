/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany;


/**
 *
 * @author DeanK
 */
public class Model {
    public Database db;
    public Data data;
    
    private ModelListener listener; //listener to notify when model's data is updated (to update view)
    
    public Model() {
        this.db = new Database();
        this.db.establishConnection(); //why not just call setup when instantiating Database
        data = new Data();
    }
    
    public void setListener(ModelListener listener) {
        this.listener = listener;
    }
    
    public void checkStaffLogin(String username, String password) {
        this.data = this.db.checkStaffLogin(username, password, this.data); 
        notifyListener(); //listener is view - update view
    }
    
    public void checkGuestLogin(String username, String password) {
        this.data = this.db.checkGuestLogin(username, password, this.data);
        notifyListener();
    }
    
    public void setUserMode(int usermode) {
        this.data.userMode = usermode;
    }
    
    //Removes user and sets loginFlag to flase
    public void setLoggedOut() { 
        data.loginFlag = false;
        data.currentloggeduser = "";        
    }
    
    public int getUserMode() {
        return this.data.userMode;
    }
    
    
    //Notifying the listener(view) when data is updated    
    public void fetchData() { 
        db.fetchBookings(data.tableModelBookings, data.tableBookingsFilter);
        db.fetchGuests(data.tableModelGuests, data.tableGuestsFilter);
        db.fetchRooms(data.tableModelRooms, data.tableRoomsFilter);
        
        notifyListener();           
    }
    
    public void fetchGuestUserData(){
        String[] myBookingsList = db.fetchGuestsUserData(data.currentloggedGuestID, data.listMyBookingsFilter);
//        if (myBookingsList == null && "Pending Requests".equals(data.listMyBookingsFilter)) 
//            data.myBookingsListstr = new String[] {"No Pending Request"};
//        else if (myBookingsList == null && !"Pending Request".equals(data.listMyBookingsFilter))
//            data.myBookingsListstr = new String[] {"No Bookings"};
        if (myBookingsList == null) {
            data.myBookingsListstr = new String[] {"No Bookings"};
        }
        else {    
            data.myBookingsListstr = myBookingsList;     
        }
        
       
        
        
        listener.updateLoggedGuestBookingsList(data.myBookingsListstr);       
    }
    

   
       
    public void createBooking(String bookingDetails[]){
        String guestName = bookingDetails[0];
        String guestPhone = bookingDetails[1];
        String selectedRoomType = bookingDetails[2];
        
        if ("".equals(guestName) || "".equals(guestPhone)){
            listener.createBookingFeedbackMSG(-1);
            return;
        }
        data.setBookingFlag(true); //true means it will go through booking process      
        
        //if (no matches) else(matches -> feed the guestID to method)
        Guest guest = db.matchingGuestExist(guestName, guestPhone); //-1 is null, other number is the matching guest ID
        if (guest == null) {
            data = db.createBooking(guestName, guestPhone, selectedRoomType, -1, data);//no existing guest found in database

        } else {
            //creates a booking for a existing guest
            data = db.createBooking(guestName, guestPhone, selectedRoomType, guest.getGuestID(), data);
        }               
        //if Booking was NOT successful
        if (!data.isBookingSuccess()) {
            notifyListener();
            return;
        }        
        fetchData(); //refreshes table view
        listener.createBookingFeedbackMSG(0);
        notifyListener();
    }
   
    public void cancelBooking(String bookingID) {
        int bookingIDint;
        try {
            bookingIDint = Integer.parseInt(bookingID);
        } catch (NumberFormatException e) {
            System.out.println("Error converting String to integer");
            listener.cancelBookingFeedbackMSG(-1);
            return;
        }
        
        if (data.isCancelBookingFlag()) {
            System.out.println("Confirming cancellation....");
            boolean output = db.cancelBooking(data.getRecentRoom().roomNumber);
            if (output) {
                data.setCancelBookingFlag(false);
                fetchData();
                listener.cancelBookingFeedbackMSG(0);
            } else {
                listener.cancelBookingFeedbackMSG(-1);
            }                  
                    
        } else {
            int [] requiredStatus = new int[] {1,1};
            String[] bookingDetails = db.findBookingIDMatch(bookingIDint, requiredStatus);
            try {
                data.setRecentRoom(new Room(Integer.parseInt(bookingDetails[2]), ""));
            } catch (NumberFormatException e) {
                System.out.println("Error converting roomnumber string to int - cancelGuest() in Model.java");
                listener.cancelBookingFeedbackMSG(-1);
            }catch (NullPointerException e) {
                System.out.println("Error,Booking ID is not valid");
                listener.cancelBookingFeedbackMSG(-1);
                return;
            }
            Guest matchingGuest = new Guest(bookingDetails[0], ""); //guestname, guestphone
            Booking matchingBooking = new Booking(bookingIDint, bookingDetails[0], matchingGuest); //bookingid, guestname, matchingGuest
            data.setRecentBooking(matchingBooking);   

            notifyListener();
            listener.cancelBookingFeedbackMSG(1);             
            
        }
        
        
    }
     
    public void checkOUTGuest (String bookingID) {
        int bookingIDint;
        try {
            bookingIDint = Integer.parseInt(bookingID);
        } catch (NumberFormatException e) {
            System.out.println("Error converting String to integer");
            listener.checkOutFeedbackMSG(-1);
            return;
        }
        
        if (data.isCheckOutFlag()) {
            System.out.println("Confirming check out....");
            boolean output = db.checkOUTGuest(data.getRecentRoom().roomNumber);
            if (output) {
                data.setCheckOutFlag(false);
                fetchData();
                listener.checkOutFeedbackMSG(0);
            } else {
                fetchData();
                listener.checkOutFeedbackMSG(-1);
            }
            
        }
        else {
            int[] requiredStatus = new int[]{20,21};
            String[] bookingDetails = db.findBookingIDMatch(bookingIDint, requiredStatus); //guestname, guestid, roomnumber
            //assign to recentBooking, then return booking
            try {
                data.setRecentRoom(new Room(Integer.parseInt(bookingDetails[2]), ""));
            } catch (NumberFormatException e) {
                System.out.println("Error converting roomnumber string to int - checkINGuest() in Model.java");
                listener.checkOutFeedbackMSG(-1);
            }catch (NullPointerException e) {
                System.out.println("Error,Booking ID is not valid");
                listener.checkOutFeedbackMSG(-1);
                return;
            }
            Guest matchingGuest = new Guest(bookingDetails[0], ""); //guestname, guestphone
            Booking matchingBooking = new Booking(bookingIDint, bookingDetails[0], matchingGuest); //bookingid, guestname, matchingGuest
            data.setRecentBooking(matchingBooking);   

            notifyListener();
            listener.checkOutFeedbackMSG(1);            
        }

    }
      
    public void checkINGuest (String bookingID) {
        int bookingIDint;
        try {
            bookingIDint = Integer.parseInt(bookingID);
        } catch (NumberFormatException e) {
            System.out.println("Error converting String to integer");
            data.setRecentBooking(null);
            listener.checkInFeedbackMSG(-1);
            return;           
        }
            
        
        if (data.isCheckInConfirmed()) { //if true
            db.checkINGuest(data.getRecentRoom().roomNumber);
            data.setCheckInFlag(false);
            data.setCheckInConfirmed(false);
            fetchData();
            listener.checkInFeedbackMSG(0);

        } else {
            int[] targetStatus = new int[] {1,1};
            data.setCheckInFlag(true);
            String[] bookingDetails = db.findBookingIDMatch(bookingIDint, targetStatus); //guestname, guestid, roomnumber
            //assign to recentBooking, then return booking
            try {
                data.setRecentRoom(new Room(Integer.parseInt(bookingDetails[2]), ""));
            } catch (NumberFormatException e) {
                System.out.println("Error converting roomnumber string to int - checkINGuest() in Model.java");
                listener.checkInFeedbackMSG(-1);
            }catch (NullPointerException e) {
                System.out.println("Error,Booking ID is not valid");
                listener.checkInFeedbackMSG(-1);
                return;
            }
            Guest matchingGuest = new Guest(bookingDetails[0], ""); //guestname, guestphone
            Booking matchingBooking = new Booking(bookingIDint, bookingDetails[0], matchingGuest); //bookingid, guestname, matchingGuest
            data.setRecentBooking(matchingBooking);            
        }        
        notifyListener();
    }
 
    public void houseKeepRoom(String roomNumber) {
//        int roomNumberInt; 
//        try {
//            roomNumberInt = Integer.parseInt(roomNumber);
//            
//        }catch(NumberFormatException e) {
//            System.out.println("Error converting String to integer in cleanRoom in Model.java" + e.getMessage());
//            listener.housekeepFeedbackMSG(-1);
//            return;
//        }
//        
//        boolean isCleaned = db.cleanRoom(roomNumberInt);
//        if (isCleaned) { //Success cleaning message
//            listener.housekeepFeedbackMSG(0);
//        } else { //Room does not need cleaning
//            listener.housekeepFeedbackMSG(1);
//        }
//        fetchData();
    }
    
    public void cleanRoom (String roomNumber) {
        int roomNumberInt; 
        try {
            roomNumberInt = Integer.parseInt(roomNumber);
            
        }catch(NumberFormatException e) {
            System.out.println("Error converting String to integer in cleanRoom in Model.java" + e.getMessage());
            listener.cleanRoomFeedbackMSG(-1);
            return;
        }
        
        boolean isCleaned = db.cleanRoom(roomNumberInt);
        if (isCleaned) { //Success cleaning message
            listener.cleanRoomFeedbackMSG(0);
        } else { //Room does not need cleaning
            listener.cleanRoomFeedbackMSG(1);
        }
        fetchData();
    }
    
    public void fetchRoomStatus(String roomNumber) {
        int roomNumberInt; 
        try {
            roomNumberInt = Integer.parseInt(roomNumber);
            
        }catch(NumberFormatException e) {
            System.out.println("Error converting String to integer in cleanRoom in Model.java" + e.getMessage());
            listener.OOORoomFeedbackMSG(-1);
            return;
        }
        
        int roomstatus = db.findRoomStatus(roomNumberInt);
        if (roomstatus != 0 && roomstatus != 4)     
            listener.OOORoomFeedbackMSG(-1); //0 or 4
        else
            listener.OOORoomFeedbackMSG(roomstatus);
    }
    
    public void setRoomStatus(String roomNumber, int status){
        int roomNumberInt; 
        try {
            roomNumberInt = Integer.parseInt(roomNumber);
            
        }catch(NumberFormatException e) {
            System.out.println("Error converting String to integer in cleanRoom in Model.java" + e.getMessage());
            listener.OOORoomFeedbackMSG(-1);
            return;
        }
        
        int output = db.setRoomStatus(roomNumberInt, status);
        listener.OOORoomFeedbackMSG(output);
        
        fetchData();
    }
    
    private String translateBookingStatus(String status) {
        switch(status) {
            case "1":
                return "Pending Booking - Ready for Check In";
            case "2":
                return "Active - Currently Occupied";
            case "3":
                return "Historical - Checked Out";
                
            }
        return null;
    }
    
    public void fetchMyBookingDetails(String selectedBooking) {
        if (selectedBooking == null || "No Bookings".equals(selectedBooking)) {
            System.out.println("No Booking Selected");
            return;
        }
        String[] bookingId = selectedBooking.split(" ");
        int bookingIdInt = -1;
//        System.out.println(bookingId[1]);
        try {
            bookingIdInt = Integer.parseInt(bookingId[1]);
        } catch(NumberFormatException e) {
            System.out.println("NumbferFormatException in fetchMyBookingDetails; " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("No booking selected: " + e.getMessage());
        }
        String[] bookingDetails = db.fetchMyBookingDetails(bookingIdInt);
        bookingDetails[1] = translateBookingStatus(bookingDetails[1]);
        listener.viewMyBookingDetails(bookingDetails, bookingIdInt);             
    }
    
    public void fetchMyRequestDetails(String selectedRequest) {
        if (selectedRequest == null) {
            System.out.println("No Request Selected");
            return;
        }
        
        String[] requestID = selectedRequest.split(" ");
        
    }
    
   
    //Refresh all lists including: Bookings, Guests and Rooms table -> Only updates database -> Data.
    //Data is always an updated reflection of what's on the database
    private void notifyListener() {
        if (listener != null) {
            listener.onModelUpdate(this.data);
        }
    }
    
}
