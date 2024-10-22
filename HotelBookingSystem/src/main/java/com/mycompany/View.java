/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 *
 * @author DeanK
 */
public class View extends JFrame implements ModelListener{
 
    JPanel mainPanel;
    JPanel loginPanel;
    JPanel startPanel;
    JPanel staffMenuPanel;
    JPanel guestMenuPanel;
    CardLayout cards;
    JPanel leftPanel1btnCENTER; //Contains Manage bookings forms
    CardLayout bookingFormCards;
    CardLayout bookingMSGCards;
    
    Integer bookingMode; //1.Create Booking, 2.CheckIn, 3. CheckOut, 4.CancelBooking
    
    //JPanels
    JPanel tabPanel1; //Staff bookings panel
    JPanel leftPanel1MessageBOTTOM;
    
    //Buttons
    JButton continueButton;
    JButton loginButton;
    JButton btnLogout1; //guest
    JButton btnLogout2; //staff
    JButton submitbtn1; //CHECK IN BUTTON
    JButton submitbtn2;// CHECK OUT BUTTON
    JButton submitbtn3; //Cancel Booking BUTTON

    
    JButton createBookingBtn;
    
    JButton confirmActionbtn; //confirm Action
    JButton cancelActionbtn; //cancel Action (confirmation form)
    
    //JCombobox or Radio Buttons
    JComboBox<String> bookingsFilterOptions;
    
    JComboBox<String> userComboBox;
    JComboBox<String> roomOptions;
   

    //Labels
    JLabel loginMessage;
    JLabel labelPW;
    
    JLabel confirmGuestNameLbl;
    JLabel confirmRoomLbl;
    JLabel checkOutMSG;
    JLabel cancelBookingMSG;
    JLabel confirmLbl;
    
    JLabel bookingMSGtitle;
    
    
    //Textbox or Typing fields
    JTextField username;
    JPasswordField password ;
    JTextField guestNametxf; //Guest name - create booking
    JTextField numTxf; //Guest Phone number = createbooking
    JTextField bookingIDtxf1; //CheckingIn guest
    JTextField bookingIDtxf2; //CheckingOut guest
    JTextField bookingIDtxf3;

    
    //Tables and strlist
    MyTableModel tableModelBookings; 
    JTable bookingsTable;
    
    //Data
    Data data = new Data();
    
    
    //Booking Message components
    JLabel guestNameLabel;
    JLabel roomLabel;
    JLabel bookingNoLabel;
    String cardTypeCancelAction = "";
    
    
    MyBookingPanelManager myBookingPanelMgr;
    ManageGuestsPanelManager manageGuestsPanelMgr;
    ManageRoomsPanelManager manageRoomsPanelMgr;
    
    //Constructor
    public View() {

        init();
        loadLogin();

        manageGuestsPanelMgr = new ManageGuestsPanelManager();
        manageRoomsPanelMgr = new ManageRoomsPanelManager();        
        loadStaffMenuPanel();    
    
        myBookingPanelMgr = new MyBookingPanelManager();
        loadGuestMenuPanel();        
        
        cards.show(mainPanel, "STARTPANEL");
        this.add(mainPanel);
        setVisible(true);
        
    }
    
    //For communication with the Controller.java
    public void addActionListener(ActionListener listener) {
        
        
        this.continueButton.addActionListener(listener);
        this.loginButton.addActionListener(listener);
        this.btnLogout1.addActionListener(listener);
        this.btnLogout2.addActionListener(listener);
        this.createBookingBtn.addActionListener(listener);
        this.submitbtn1.addActionListener(listener);
        this.confirmActionbtn.addActionListener(listener);
        this.cancelActionbtn.addActionListener(listener);
        this.bookingsFilterOptions.addActionListener(listener);      
        this.submitbtn2.addActionListener(listener);
        this.submitbtn3.addActionListener(listener);
    
        
//        --------------------------------------------------------
        myBookingPanelMgr.addActionListener(listener);
        manageRoomsPanelMgr.addActionListener(listener);
        manageGuestsPanelMgr.addActionListener(listener);

        
    }
     
    //initializes frame 
    private void init() {
        setSize(1120,500);
        setLocationRelativeTo(null);
        setTitle("Hotel Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //MAIN PANEL
        mainPanel = new JPanel();
        mainPanel.setBorder(new TitledBorder("Hotel Booking System - MAIN PANEL"));
        cards = new CardLayout();
        mainPanel.setLayout(cards);
        bookingFormCards = new CardLayout();
        bookingMSGCards = new CardLayout();
        
        bookingMode = -1; 
        
        //Start Up Panel
        startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        startPanel.setBorder(new TitledBorder("LOGIN OPTIONS"));
        
        //User-OPTIONS COMPONENTS
        JPanel userOptionComponents = new JPanel();
        userOptionComponents.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
        JLabel labelOptions = new JLabel("Select User: ");
        userOptionComponents.add(labelOptions, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 2;
        String[] comboOptions = new String[] {"Login as Guest", "Login as Staff"};
        userComboBox = new JComboBox<String>(comboOptions);
        userOptionComponents.add(userComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridheight = 1;
        continueButton = new JButton("Continue");
        userOptionComponents.add(continueButton, gbc);
        
        startPanel.add(userOptionComponents, BorderLayout.CENTER);
        mainPanel.add(startPanel, "STARTPANEL");
       
    }
    
    //Loads starting frame - user Login menu
    private void loadLogin () {
        //LOGIN PANEL
        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());            
        loginPanel.setBorder(new TitledBorder("LOGIN PANEL"));
        
        //LOGIN COMPONENTS
        JPanel loginComponents = new JPanel();
        loginComponents.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JLabel labelUser = new JLabel("Username:    ");
        loginComponents.add(labelUser, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;

        username = new JTextField(10);
        loginComponents.add(username, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        labelPW = new JLabel("Password:     ");
        loginComponents.add(labelPW, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        password = new JPasswordField(10);
        loginComponents.add(password, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        JButton button2 = new JButton("Return");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.show(mainPanel, "STARTPANEL");
            }
        });
        loginComponents.add(button2, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 1; gbc.gridheight = 1;
        loginButton = new JButton("Log In");       
        loginComponents.add(loginButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginMessage = new JLabel("                 ");
        loginComponents.add(loginMessage, gbc);
        
        
        loginPanel.add(loginComponents, BorderLayout.CENTER);
        mainPanel.add(loginPanel, "LOGINPANEL");
    }
    
    //Loads the container for the guest MenuPanel and its main components
    private void loadGuestMenuPanel() {
        guestMenuPanel = new JPanel();
        guestMenuPanel.setLayout(new BorderLayout());
        
        //Single TabbedPane - stylistic and future proof decision
        JTabbedPane menuComponents = new JTabbedPane();
        JPanel myBookingsPanel = new JPanel();
        
        menuComponents.add("My Bookings", myBookingsPanel);
        loadMyBookingsPanel(myBookingsPanel);
        
        //Bottom Panel 
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        btnLogout1 = new JButton("Logout");
        btnLogout1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.show(mainPanel, "LOGINPANEL");
                
            }
        });
        
        bottomPanel.add(btnLogout1);
        guestMenuPanel.add(bottomPanel, BorderLayout.SOUTH);
        guestMenuPanel.add(menuComponents, BorderLayout.CENTER);
        mainPanel.add(guestMenuPanel, "GUESTMENUPANEL");
    }

    //Loads the container for the staff MenuPanel and its main components
    private void loadStaffMenuPanel() {
        staffMenuPanel = new JPanel();
        staffMenuPanel.setLayout(new BorderLayout());   
               
        //TabbedPane has tabPanel1, tabPanel2, tabPanel3
        JTabbedPane menuComponents = new JTabbedPane();
        //MANAGE BOOKINGS
        tabPanel1 = new JPanel();               
        //MANAGE GUESTS
        JPanel tabPanel2 = new JPanel();
        //MANAGE ROOMS
        JPanel tabPanel3 = new JPanel();
                
        menuComponents.add("Manage Bookings", tabPanel1);
        menuComponents.add("Manage Guests", tabPanel2);
        menuComponents.add("Manage Rooms", tabPanel3);
        loadManageBookingsPanel(tabPanel1);
        loadManageGuests(tabPanel2);
        loadManageRooms(tabPanel3);
                
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        btnLogout2 = new JButton("Logout");
        btnLogout2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.show(mainPanel, "LOGINPANEL");
            }
        });
        bottomPanel.add(btnLogout2);
        
        staffMenuPanel.add(bottomPanel, BorderLayout.SOUTH);
        staffMenuPanel.add(menuComponents, BorderLayout.CENTER);
        mainPanel.add(staffMenuPanel, "STAFFMENUPANEL");
        
    }
      
    
    
    //REFACTOR BELOW ----------------------------------------------------------REFACTOR BELOW-------------
    
    
    //Loads the booking form for user input in order to add, or manipulate the data.
    private void loadBookingForms() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        //1.EMPTY panel
        JPanel clearForm = new JPanel();
        leftPanel1btnCENTER.add(clearForm, "CLEARFORM");
        
        //2.BOOKING panel - initialize components
        JPanel createForm = new JPanel();
        createForm.setLayout(new GridBagLayout());

        JLabel label1 = new JLabel("Enter New Booking Details: ");
        JLabel guestNameLabel = new JLabel("Guest Name: ");
        guestNametxf = new JTextField(10);
        JLabel numLabel = new JLabel("Phone Number: ");
        numTxf = new JTextField(10);
        JLabel roomTypeLabel = new JLabel("Room Type: ");
        String[] strRoomOptions = new String[] {"STANDARD", "DELUXE", "SUITE"};
        roomOptions = new JComboBox<String>(strRoomOptions);                
        createBookingBtn = new JButton("Create Booking");
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                guestNametxf.setText(null);
                numTxf.setText(null);
                bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG");          
                
            }
        });
        //Adding form components to panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 1; gbc.gridwidth = 1; 
        createForm.add(label1, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        createForm.add(guestNameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        createForm.add(guestNametxf,gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        createForm.add(numLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        createForm.add(numTxf, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        createForm.add(roomTypeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        createForm.add(roomOptions, gbc);
        //Adding Submit Button at the end
        gbc.gridx = 1; gbc.gridy = 4;                   
        createForm.add(createBookingBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        createForm.add(clearBtn, gbc);
        //Adding form to the Panel with cards layout
        leftPanel1btnCENTER.add(createForm, "CREATEFORM");
        
        //3.CHECK IN panel - initialize components
        JPanel checkinForm = new JPanel();
        checkinForm.setLayout(new GridBagLayout());
        JLabel label2 = new JLabel("Check In Guest: ");
        JLabel bookingIdLabel1 = new JLabel("Booking ID: ");
        bookingIDtxf1 = new JTextField(10);
        submitbtn1 = new JButton("Check In Guest");
        JButton clearBtn1 = new JButton("Clear");
        clearBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookingIDtxf1.setText("");
                bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG");
            }
            
        });
        
        //Adding form components to Check In Panel
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
        checkinForm.add(label2, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridheight = 1; gbc.gridwidth = 1;
        checkinForm.add(bookingIdLabel1, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridheight = 1; gbc.gridwidth = 1;
        checkinForm.add(bookingIDtxf1, gbc);
        //Add buttons
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridheight = 1; gbc.gridwidth = 1;
        checkinForm.add(submitbtn1, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridheight = 1; gbc.gridwidth = 1;
        checkinForm.add(clearBtn1, gbc);
        //Adding form to the panel with cards layout
        leftPanel1btnCENTER.add(checkinForm, "CHECKINFORM");
        
        
        //4.CHECK OUT panel - initialize components
        JPanel checkoutForm = new JPanel();
        checkoutForm.setLayout(new GridBagLayout());
        
        JLabel label3 = new JLabel("Check Out Guests: ");
        JLabel bookingIDLabel2 = new JLabel("Booking ID: ");
        bookingIDtxf2 = new JTextField(10);
        submitbtn2 = new JButton("Check Out Guest");
        JButton clearBtn2 = new JButton("Clear");
        clearBtn2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                bookingIDtxf2.setText("");
                bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG");
            }
        });
        
        //Adding form components to CHECK OUT panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 1; gbc.gridwidth = 1;
        checkoutForm.add(label3, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
        checkoutForm.add(bookingIDLabel2, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridheight = 1; gbc.gridwidth = 1;
        checkoutForm.add(bookingIDtxf2, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        checkoutForm.add(submitbtn2, gbc);
        gbc.gridx = 1; gbc.gridy =3;
        checkoutForm.add(clearBtn2, gbc);
        //Adding Checkout form panel with the Cards Layout
        leftPanel1btnCENTER.add(checkoutForm, "CHECKOUTFORM");
        

        
        //5.CANCEL panel - initialize components
        JPanel cancelForm = new JPanel();
        cancelForm.setLayout(new GridBagLayout());
        JLabel label4 = new JLabel("Cancel a Booking: ");
        JLabel bookingIDLabel3 = new JLabel("Booking ID: ");
        bookingIDtxf3 = new JTextField(10);
        submitbtn3 = new JButton("Cancel Booking");
        JButton clearBtn3 = new JButton("Clear");
        clearBtn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookingIDtxf3.setText(null);
                bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG");  
            }
        });
        
        //Adding form components to CANCEL FORM
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 1; gbc.gridwidth = 1;
        cancelForm.add(label4, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        cancelForm.add(bookingIDLabel3, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        cancelForm.add(bookingIDtxf3, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        cancelForm.add(submitbtn3, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        cancelForm.add(clearBtn3, gbc);
        //Adding Cancel form to panel with the Cards Layout
        leftPanel1btnCENTER.add(cancelForm, "CANCELFORM");
        
        
        //Create the form that confirms found matching booking details with booking ID from database 
        //and ask to confirm with user before proceeding
        loadConfirmMatchMethod();
        
        
        
        //Finally, show the CLEAR card on start up.
        bookingFormCards.show(leftPanel1btnCENTER, "CLEARFORM");
       
    }
    
    //Create the form that confirms found matching booking details with booking ID from database 
    //and ask to confirm with user before proceeding either Check In, Check Out or Cancelling Booking
    private void loadConfirmMatchMethod() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        
        //3.a Confirm CHECK IN/CHECKOUT/CANCEL FORM before finalizing - shows the matching booking information found
        JPanel confirmActionForm = new JPanel();
        confirmActionForm.setLayout(new GridBagLayout());
        confirmLbl = new JLabel("Confirm ___: ");
        confirmGuestNameLbl = new JLabel("Guest Name: Guest Phone");
        confirmRoomLbl = new JLabel("Room Number: ");
        confirmActionbtn = new JButton("Confirm Check In"); 
        cancelActionbtn = new JButton("Cancel");
        cancelActionbtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                bookingIDtxf1.setText("");
                bookingFormCards.show(leftPanel1btnCENTER, cardTypeCancelAction); 
                data.setCheckInFlag(false);
                data.setCheckInConfirmed(false);
                confirmGuestNameLbl.setText("");
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 1; gbc.gridwidth = 1;
        confirmActionForm.add(confirmLbl, gbc);
        gbc.gridy = 1;
        confirmActionForm.add(confirmGuestNameLbl, gbc);
        gbc.gridy = 2;
        confirmActionForm.add(confirmRoomLbl, gbc);
        gbc.gridy = 3;
        confirmActionForm.add(confirmActionbtn, gbc);
        gbc.gridx = 1;
        confirmActionForm.add(cancelActionbtn, gbc);
        leftPanel1btnCENTER.add(confirmActionForm,"CONFIRMACTIONFORM");
    }
    
    //This loads the feedback cards that tells the user the outcome of their intended action(s)
    public void loadBookingMSGCards() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        
        //1.EMPTY PANEL
        JPanel defaultEmptyCard = new JPanel();
        leftPanel1MessageBOTTOM.add(defaultEmptyCard, "EMPTYMSG");
        
        //2.Successful booking MSG
        JPanel successMSGCard = new JPanel();
        successMSGCard.setLayout(new GridBagLayout());       
        
        bookingMSGtitle = new JLabel("Booking Confirmed!");
        guestNameLabel = new JLabel("GuestName: "+ data.getRecentBooking().getGuest().guestName);
        roomLabel = new JLabel("Room: " + data.getRecentBooking().getRoom().roomNumber + "Roomtype: " + data.getRecentBooking().getRoom().roomType);
        bookingNoLabel = new JLabel("Room: " + data.getRecentBooking().getBookingID());
    
        
        //Adding message components to panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 1; gbc.gridwidth = 1;
        successMSGCard.add(bookingMSGtitle, gbc);
        gbc.gridy = 1;
        successMSGCard.add(guestNameLabel, gbc);
        gbc.gridy = 2;
        successMSGCard.add(roomLabel, gbc);
        gbc.gridy = 3;
        successMSGCard.add(bookingNoLabel, gbc);
        
        //Adding a card to one of cards layout panel
        leftPanel1MessageBOTTOM.add(successMSGCard, "SUCCESSMSG");        

        
        //2. Successful Check Out MSG
        JPanel ChOutMSGCard = new JPanel();
        ChOutMSGCard.setLayout(new GridBagLayout());
        checkOutMSG = new JLabel("Check Out Success!");
        gbc.gridx = 0; gbc.gridy = 0;
        ChOutMSGCard.add(checkOutMSG, gbc);
        
        //Add card to cards layout panel
        leftPanel1MessageBOTTOM.add(ChOutMSGCard, "CHECKOUTMSG");
        
        //2. Successful Cancel Booking MSG
        JPanel CanBookMSGCard = new JPanel();
        CanBookMSGCard.setLayout(new GridBagLayout());
        cancelBookingMSG = new JLabel("Booking has been cancelled");
        gbc.gridx = 0; gbc.gridy = 0;
        CanBookMSGCard.add(cancelBookingMSG, gbc);
        
        //Add card to cards layout panel
        leftPanel1MessageBOTTOM.add(CanBookMSGCard, "CANCELBOOKMSG");
//---------------------------------------------------------
        this.bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG"); //Show blank message on loadup       
    }     
    
//    -----------------------------LOAD STAFF MANAGEMENT PANELS--------------------------------------------------------------------------------
    //Loads the manage booking tabPanel contents
    private void loadManageBookingsPanel(JPanel tabPanel1) {
        
        tabPanel1.setLayout(new BorderLayout());

        //Manage bookings menu on LEFT
        JPanel leftPanel1 = new JPanel();
        leftPanel1.setLayout(new BorderLayout());
        
        //Initialize and add containers for the leftPanel
        JPanel leftPanel1btnTOP = new JPanel();
        leftPanel1btnTOP.setLayout(new GridLayout(3,2));
        leftPanel1btnCENTER = new JPanel();
        leftPanel1btnCENTER.setLayout(bookingFormCards);
        leftPanel1MessageBOTTOM = new JPanel();
        leftPanel1MessageBOTTOM.setLayout(bookingMSGCards);
               
              
        //Adding Top, CENTER and Bottom Subcontainer Panels into left split panel
        leftPanel1.add(leftPanel1btnTOP, BorderLayout.NORTH);
        leftPanel1.add(leftPanel1btnCENTER, BorderLayout.CENTER);
        leftPanel1.add(leftPanel1MessageBOTTOM, BorderLayout.SOUTH);
        loadBookingForms();
        
        
        //Adding buttons to left splitpane, North border
        JLabel northBtnTitle1 = new JLabel("Filter Booking List:");
        String[] strFilterOptions = new String[]{ "All Bookings", "Active Bookings", "Pending Bookings", "Historical Bookings"};
        bookingsFilterOptions = new JComboBox<String>(strFilterOptions);
        
        leftPanel1btnTOP.add(northBtnTitle1);
        leftPanel1btnTOP.add(bookingsFilterOptions);
        
        //Adding buttons to left splitpane, CENTRAL border
        JButton makeBookingbtn = new JButton("Create Booking");
        makeBookingbtn.addActionListener(new ActionListener() {          
            @Override
            public void actionPerformed(ActionEvent e) {  
                if (bookingMode == 1) {
                    bookingFormCards.show(leftPanel1btnCENTER, "CLEARFORM");
                    bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG");  
                    bookingMode = -1;
                    return;
                }
                bookingFormCards.show(leftPanel1btnCENTER, "CREATEFORM");
                bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG"); 
                bookingMode = 1;
            }
        });
        JButton checkInBtn = new JButton("Check In");   
        checkInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bookingMode == 2) {
                    bookingFormCards.show(leftPanel1btnCENTER, "CLEARFORM");
                    bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG");  
                    bookingMode = -1;
                    return;
                }
                bookingFormCards.show(leftPanel1btnCENTER, "CHECKINFORM");
                bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG"); 
                bookingMode = 2;
            }
            
        });
        JButton checkOutBtn = new JButton("Check Out");
        checkOutBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (bookingMode == 3) {
                    bookingFormCards.show(leftPanel1btnCENTER, "CLEARFORM");
                    bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG");  
                    bookingMode = -1;
                    return;
                }
                bookingFormCards.show(leftPanel1btnCENTER,"CHECKOUTFORM");
                bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG"); 
                bookingMode = 3;
            }
        });
        JButton cancelBtn = new JButton("Cancel Booking");
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bookingMode == 4) {
                    bookingFormCards.show(leftPanel1btnCENTER, "CLEARFORM");
                    bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG");  
                    bookingMode = -1;
                    return;
                }
                bookingFormCards.show(leftPanel1btnCENTER, "CANCELFORM");
                bookingMSGCards.show(leftPanel1MessageBOTTOM, "EMPTYMSG");  
                bookingMode = 4;
            }
            
        });
        leftPanel1btnTOP.add(makeBookingbtn);
        leftPanel1btnTOP.add(checkInBtn);
        leftPanel1btnTOP.add(checkOutBtn);
        leftPanel1btnTOP.add(cancelBtn);
               
        //Initializing Table model which reflects Booking records
        tableModelBookings = new MyTableModel();
        bookingsTable = new JTable(tableModelBookings){
            public boolean editCellAt(int row, int column, java.util.EventObject e) { //Prevents editing of cells in table = https://rb.gy/1qflxh
            return false;
         }
        };
        
        
        //Adding Table to scrollPane
        JScrollPane sp1 = new JScrollPane(leftPanel1);
        JScrollPane sp2 = new JScrollPane(bookingsTable);        
        JSplitPane tabPanel1Inner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp1, sp2);
        //Where the splitpane is located along the middle of two panes
        tabPanel1Inner.setResizeWeight(.5d); 
        tabPanel1.add(tabPanel1Inner, BorderLayout.CENTER);
    }

    //Loads the Manage guests tabPanel contents
    private void loadManageGuests(JPanel tabPanel2) {
        manageGuestsPanelMgr.loadManageGuests(tabPanel2);
    }   
    
//    -----------------------------------MANAGE ROOMS-----------------------------------------------------
    
    //Loads the Manage Rooms tabPanel contents
    private void loadManageRooms(JPanel tabPanel3) {
        manageRoomsPanelMgr.loadManageRooms(tabPanel3);       
    }
    
//    -----------------------------END OF STAFF MANAGEMENT PANELS------------------------------------------------------------------------------
 
    private void loadMyBookingsPanel(JPanel myBookingsPanel) {
        myBookingPanelMgr.loadMyBookingsPanel(myBookingsPanel);
    }

    
    @Override
    public void createBookingFeedbackMSG(int output) {
        if (output == -1) {
            cancelBookingMSG.setText("Please enter valid booking details");
            bookingMSGCards.show(leftPanel1MessageBOTTOM, "CANCELBOOKMSG");
        }
        else if (output == 0) {
            guestNametxf.setText("");
            numTxf.setText("");
        }
    }
    
    @Override
    public void cancelBookingFeedbackMSG(int output){
        if (output == -1){
            bookingIDtxf3.setText("");
            cancelBookingMSG.setText("<html>Invalid BookingID/Error Cancelling: <br/>Only Pending Bookings can be Cancelled<html/>");
            bookingMSGCards.show(leftPanel1MessageBOTTOM, "CANCELBOOKMSG");
        } else if (output == 0) { //completed
            bookingFormCards.show(leftPanel1btnCENTER, "CANCELFORM");
            bookingIDtxf3.setText("");
            cancelBookingMSG.setText("Cancellation complete!");
            bookingMSGCards.show(leftPanel1MessageBOTTOM, "CANCELBOOKMSG");

        } else if (output == 1){ //require user confirmation
            confirmCancelBooking(data); 
        }
    }
    
    @Override
    public void checkOutFeedbackMSG(int output) {
        if (output == -1) { //error              
            checkOutMSG.setText("<html>Invalid Booking ID/Error Checking Out: <br/>Only Active Bookings can be Checked Out<html/>");
            bookingIDtxf2.setText("");
            this.bookingMSGCards.show(leftPanel1MessageBOTTOM, "CHECKOUTMSG");    
        } else if (output == 0) {//completed!
            bookingFormCards.show(leftPanel1btnCENTER, "CHECKOUTFORM");
            checkOutMSG.setText("Check Out Successful!");
            bookingMSGCards.show(leftPanel1MessageBOTTOM, "CHECKOUTMSG");
        } else if (output == 1) { //require user confirmation 
            confirmCheckOut(data);
        }
    }
    
    //-1 = error
    @Override
    public void checkInFeedbackMSG(int output) {
        if (output == -1) {
            checkOutMSG.setText("<html>Invalid BookingID/Error Checking In: <br/>Only Pending Bookings can be Checked In<html/>");
            bookingIDtxf1.setText("");
            data.setCheckInFlag(false);
            data.setCheckInConfirmed(false);
            bookingMSGCards.show(leftPanel1MessageBOTTOM, "CHECKOUTMSG");
        } else if (output == 0) { //completed
            bookingIDtxf1.setText("");
            checkOutMSG.setText("Check In Successful!");
            bookingMSGCards.show(leftPanel1MessageBOTTOM, "CHECKOUTMSG");
            bookingFormCards.show(leftPanel1btnCENTER, "CHECKINFORM");
            data.setCheckInConfirmed(false); //end of check in
        }
    }
    
    @Override
    public void cleanRoomFeedbackMSG(int output) {
        manageRoomsPanelMgr.cleanRoomFeedbackMSG(output);
    }
    
    @Override
    public void OOORoomFeedbackMSG(int output) {
        manageRoomsPanelMgr.OOORoomFeedbackMSG(output);
    }
    
    @Override
    public void onModelUpdate(Data data) {
        if(!data.loginFlag) {
            this.username.setText("");
            this.password.setText("");
            this.loginMessage.setText("Invalid username or password. Try Again.");
        } else { //SHOW THE MENU PANEL -'reload' all the tables as data has been updated
            this.username.setText("");
            this.password.setText("");
            this.loginMessage.setText("");
            if (data.userMode == 0) //guest
            {
                cards.show(mainPanel, "GUESTMENUPANEL");
//                mainPanel.setBorder(new TitledBorder("Hotel Booking System - MAIN PANEL"));
                mainPanel.setBorder(new TitledBorder("You are logged in as " + data.currentloggeduser));
                
            }
            else if (data.userMode == 1) { //staff
                cards.show(mainPanel, "STAFFMENUPANEL");
                mainPanel.setBorder(new TitledBorder("You are logged in as " + data.currentloggeduser + " (STAFF)"));
                if (data.isBookingFlag()) {
                    setManageBookingMSG(data);
                }    
                if (data.isCheckInFlag()) {
                    confirmCheckIn(data);
                }
            }
        }
        
        
    }
        
    private void confirmCheckIn(Data data) {
        cardTypeCancelAction = "CHECKINFORM"; //if user hits cancel on confirmation form, it will show the same form prior
        confirmLbl.setText("Confirm Check In: ");
        confirmGuestNameLbl.setText("Guest Name: " + data.getRecentBooking().getGuest().guestName);
        confirmRoomLbl.setText("Room Number: " + data.getRecentRoom().roomNumber);
        confirmActionbtn.setText("Confirm Check In"); 
        
        bookingFormCards.show(leftPanel1btnCENTER, "CONFIRMACTIONFORM");
    }
    
    private void confirmCheckOut(Data data) {
        cardTypeCancelAction = "CHECKOUTFORM";
        confirmLbl.setText("Confirm Check Out: ");
        confirmGuestNameLbl.setText("Guest Name: " + data.getRecentBooking().getGuest().guestName);
        confirmRoomLbl.setText("Room Number: " + data.getRecentRoom().roomNumber);
        confirmActionbtn.setText("Confirm Check Out"); 
        
        bookingFormCards.show(leftPanel1btnCENTER, "CONFIRMACTIONFORM");
    }
    
    private void confirmCancelBooking(Data data) {
        cardTypeCancelAction = "CANCELFORM";
        confirmLbl.setText("Confirm Cancellation: ");
        confirmGuestNameLbl.setText("Guest Name: " + data.getRecentBooking().getGuest().guestName);
        confirmRoomLbl.setText("Room Number: " + data.getRecentRoom().roomNumber);
        confirmActionbtn.setText("Confirm Cancellation");
        
        bookingFormCards.show(leftPanel1btnCENTER, "CONFIRMACTIONFORM");
    }
    
    private String[] setManageBookingMSG(Data data){      
        String[] output = null;
        if (data.isBookingSuccess()){        
        bookingMSGtitle.setText("Booking successful!");
        bookingNoLabel.setText("Booking ID: " + data.getRecentBooking().getBookingID());
        roomLabel.setText("Room Number:" + data.getRecentRoom().roomNumber + "; " + data.getRecentRoom().roomType);
        guestNameLabel.setText("Guest ID: " + data.getRecentGuest().getGuestID() + " Guest Name: " + data.getRecentGuest().guestName);
        } else if (!data.isBookingSuccess()) {
            bookingMSGtitle.setText("");
            bookingNoLabel.setText("No Room Available for that RoomType. Try Again.");
            roomLabel.setText("");
            guestNameLabel.setText("");          
        }
        this.bookingMSGCards.show(leftPanel1MessageBOTTOM, "SUCCESSMSG");
        data.setBookingFlag(false);
        
        return output;
    }
    
    @Override
    public void updateLoggedGuestBookingsList(String[] updatedBookingsList) {
        myBookingPanelMgr.myBookingsList.setListData(updatedBookingsList);
    }
    
    @Override
    public void viewMyBookingDetails(String[] bookingDetails, int bookingId) {
        this.myBookingPanelMgr.viewMyBookingDetails(bookingDetails, bookingId);
    }
    
    
    public void setUserMode(int userMode) {
        if (userMode == 0) { //guest
            guestMenuPanel.setBorder(new TitledBorder("Guest Menu"));
            loginPanel.setBorder(new TitledBorder("Guest Login"));
            mainPanel.setBorder(new TitledBorder("Hotel Booking System - MAIN PANEL"));
            this.loginMessage.setText("");
        }
        else if (userMode == 1) {
             staffMenuPanel.setBorder(new TitledBorder("Staff Menu"));
             loginPanel.setBorder(new TitledBorder("Staff Login"));
             mainPanel.setBorder(new TitledBorder("Hotel Booking System - MAIN PANEL"));
             this.loginMessage.setText("");
        }
        cards.show(mainPanel, "LOGINPANEL");
    }
    
}


