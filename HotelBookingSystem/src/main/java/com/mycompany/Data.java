/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DeanK
 */
public class Data { 

//collective for different data collections?
    //A data structure for bookings e.g. arraylists
    boolean loginFlag = false;
    
    private boolean bookingFlag = false;
    private boolean bookingSuccess;
    
//    boolean modelFetched = false;
    MyTableModel tableModelBookings = new MyTableModel();
    MyTableModel tableModelGuests = new MyTableModel();
    MyTableModel tableModelRooms = new MyTableModel();
    
    int userMode = -1; //-1 neither, 0 for guest, 1 for staff
    String currentloggeduser = null; //used for functions
    
    ArrayList<Booking> AllBookings = new ArrayList<Booking>();
    //TODO:
    HashMap <Integer, Guest> guestList;
    ArrayList<Room> roomsList;  
    
    
    
    
    
    
    
    
        /**
     * @return the bookingFlag
     */
    public boolean isBookingFlag() {
        return bookingFlag;
    }

    /**
     * @param bookingFlag the bookingFlag to set
     */
    public void setBookingFlag(boolean bookingFlag) {
        this.bookingFlag = bookingFlag;
    }

    /**
     * @return the bookingSuccess
     */
    public boolean isBookingSuccess() {
        return bookingSuccess;
    }

    /**
     * @param bookingSuccess the bookingSuccess to set
     */
    public void setBookingSuccess(boolean bookingSuccess) {
        this.bookingSuccess = bookingSuccess;
    }
    
}
