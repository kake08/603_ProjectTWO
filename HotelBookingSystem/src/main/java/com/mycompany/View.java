/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
    JPanel leftPanel1btnCENTER; //Manage bookings forms
    JPanel leftPanel2btnCENTER; //Manage guest forms
    CardLayout bookingFormCards;
    CardLayout bookingMSGCards;
    Integer bookingMode; //1:MANAGE BOOKING 2:MANAGE GUEST 3:MANAGE ROOMS
//    String strLoginMode;
    
    
    //JPanels
    JPanel tabPanel1; //Staff bookings panel
    JPanel leftPanel1MessageBOTTOM;
    JPanel leftPanel3MessageBOTTOM;
    
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
    
    //JCombobox
    JComboBox<String> bookingsFilterOptions;
    JComboBox<String> guestsFilterOptions;
    JComboBox<String> roomsFilterOptions;
    
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
    
    
    //Textbox or Typing fields
    JTextField username;
    JPasswordField password ;
    JTextField guestNametxf; //Guest name - create booking
    JTextField numTxf; //Guest Phone number = createbooking
    JTextField bookingIDtxf1; //CheckingIn guest
    JTextField bookingIDtxf2; //CheckingOut guest
    JTextField bookingIDtxf3;
    
    //Tables and strlist
    MyTableModel tableModelBookings; // bookings
    MyTableModel tableModelGuests;
    MyTableModel tableModelRooms;
    JTable bookingsTable;
    JTable guestsTable;
    JTable roomsTable;
    
    //Data
    Data data = new Data();
    
    
    //Booking Message components
    JLabel guestNameLabel;
    JLabel roomLabel;
    JLabel bookingNoLabel;

    String cardTypeCancelAction = "";
    
    //Constructor
    public View() {

        init();
        loadLogin();
        loadStaffMenuPanel();        
        loadGuestMenuPanel();
        loadBookingForms();
        
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
        this.guestsFilterOptions.addActionListener(listener);
        this.roomsFilterOptions.addActionListener(listener);
        this.submitbtn2.addActionListener(listener);
        this.submitbtn3.addActionListener(listener);
    }
     
    //initializes frame 
    private void init() {
        setSize(900,500);
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
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JLabel labelOptions = new JLabel("Select User: ");
        userOptionComponents.add(labelOptions, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        String[] comboOptions = new String[] {"Login as Guest", "Login as Staff"};
        userComboBox = new JComboBox<String>(comboOptions);
        userOptionComponents.add(userComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
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
        
        //TabbedPanes
        JTabbedPane menuComponents = new JTabbedPane();
        //My bookings overview and My requests
        JPanel myBookingsPanel = new JPanel();
//        JPanel myRequestsPanel = new JPanel();
        
        menuComponents.add("My Bookings", myBookingsPanel);
//        menuComponents.add("My Requests", myRequestsPanel);
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
        
        JLabel title = new JLabel("Booking Confirmed!");
        guestNameLabel = new JLabel("GuestName: "+ data.getRecentBooking().getGuest().guestName);
        roomLabel = new JLabel("Room: " + data.getRecentBooking().getRoom().roomNumber + "Roomtype: " + data.getRecentBooking().getRoom().roomType);
        bookingNoLabel = new JLabel("Room: " + data.getRecentBooking().getBookingID());
    
        
        //Adding message components to panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 1; gbc.gridwidth = 1;
        successMSGCard.add(title, gbc);
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
    
//    -----------------------------LOAD GUEST MENU PANELS--------------------------------------------------------------------------------------
    
    private void loadMyBookingsPanel(JPanel myBookingsPanel) {
        myBookingsPanel.setLayout(new BorderLayout());
        
        //Panel for Booking selection on the left
        JPanel leftPanel1 = new JPanel();
        leftPanel1.setLayout(new BorderLayout());
        
        //Initialize and add subcontainers for the leftPanel
        JPanel leftPanelTopList = new JPanel();
        leftPanelTopList.setLayout(new BorderLayout());
        
        //Adding the subcontainers into leftPanel
        leftPanel1.add(leftPanelTopList, BorderLayout.CENTER);
        
        //Adding components into the subcontainers (LIST)
        String[] strMyBookingsFilter = new String[]{"All Bookings", "Active Bookings", "Pending Requests"};
        JComboBox myBookingsFilter = new JComboBox(strMyBookingsFilter);
        leftPanelTopList.add(myBookingsFilter, BorderLayout.NORTH);
        String[] listItems = new String[] {"Booking ID: asdawaae", "Booking ID: sdkncinwe", "BookingID: sdvwfe", "Booking ID: asdawaae", "Booking ID: sdkncinwe", "BookingID: sdvwfe"
        ,"Booking ID: asdawaae", "Booking ID: sdkncinwe", "BookingID: sdvwfe", "Booking ID: asdawaae", "Booking ID: sdkncinwe", "BookingID: sdvwfe", 
        "Booking ID: asdawaae", "Booking ID: sdkncinwe", "BookingID: sdvwfe", "Booking ID: asdawaae", "Booking ID: sdkncinwe", "BookingID: sdvwfe"};
        
        JList<String> myList = new JList(listItems);
        leftPanelTopList.add(myList, BorderLayout.CENTER);
        
        //Panel for Booking information on the right
        JPanel rightPanel1 = new JPanel();
        rightPanel1.setLayout(new BorderLayout());
        
        //Initialize and add subcontainers for the rightpanel
        JPanel rightPanelTitleTOP = new JPanel();
        JPanel rightPanelContentCENTER = new JPanel();
        JPanel rightPanelBtnsBOTTOM = new JPanel();
        JPanel rightPanelBtnsBOTTOMHELPER = new JPanel();
        rightPanelTitleTOP.setLayout(new BoxLayout(rightPanelTitleTOP, BoxLayout.Y_AXIS));
        rightPanelContentCENTER.setLayout(new BorderLayout());
        rightPanelBtnsBOTTOM.setLayout(new BoxLayout(rightPanelBtnsBOTTOM, BoxLayout.X_AXIS));
        rightPanelBtnsBOTTOMHELPER.setLayout(new FlowLayout());
        
        //Adding the subcontainers into the RIGHT panel
        rightPanel1.add(rightPanelTitleTOP, BorderLayout.NORTH);
        rightPanel1.add(rightPanelContentCENTER, BorderLayout.CENTER);
        rightPanel1.add(rightPanelBtnsBOTTOMHELPER, BorderLayout.SOUTH);
        rightPanelBtnsBOTTOMHELPER.add(rightPanelBtnsBOTTOM);
        
        
//        -------------------------------------------------------------------------
        //Adding components into the subcontainers (LABELS)
        JLabel bookingTitle = new JLabel("<html>Booking 234908</html>", SwingConstants.CENTER); //https://stackoverflow.com/questions/6810581/how-to-center-the-text-in-a-jlabel
        bookingTitle.setFont(new Font("Arial", Font.BOLD, 22));//https://docs.oracle.com/javase/tutorial/uiswing/components/html.html
        bookingTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        rightPanelTitleTOP.add(bookingTitle);

        JLabel bookingContents = new JLabel("<html>Guest ID:  sdfkiijbwef<br/>Room Number: sdjnwefoub</html>", SwingConstants.CENTER);
        bookingContents.setFont(new Font("Arial", Font.PLAIN, 16));
        rightPanelTitleTOP.add(bookingContents);
        
        JButton makeRequestbtn = new JButton("Make Request");
        makeRequestbtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Make a request form...");
            }
        });
        JButton sendMessagebtn = new JButton("Send Message");
        sendMessagebtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Send a Message form....");
            }
        });
        JButton seeMenubtn = new JButton("See Breakfast Menu");
        seeMenubtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("See Breakfast menu....");
            }
        });
        JButton seeRoomDetailsbtn = new JButton("See Room Details");
        seeRoomDetailsbtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("See Room details....");
            }
        });
        
        rightPanelBtnsBOTTOM.add(makeRequestbtn);
        rightPanelBtnsBOTTOM.add(sendMessagebtn);
        rightPanelBtnsBOTTOM.add(seeMenubtn);
        rightPanelBtnsBOTTOM.add(seeRoomDetailsbtn);
        
//        ------------------------------------------------------------------------------
        
        //Adding To Scroll PAne
        JScrollPane sp1 = new JScrollPane(leftPanel1);
        JScrollPane sp2 = new JScrollPane(rightPanel1);
        JSplitPane tabPanel4Inner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp1, sp2);
        tabPanel4Inner.setResizeWeight(.3d);
        myBookingsPanel.add(tabPanel4Inner, BorderLayout.CENTER);
                
    }
    
    
//    -----------------------------END OF GUEST MENU PANELS------------------------------------------------------------------------------------
    
    
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
               
              
        //Adding Top, CENTER and Bottom Subcontainer Panels into left split pane
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
        tabPanel2.setLayout(new BorderLayout());
        
        //Left side of Manage Guests for menu
        JPanel leftPanel2 = new JPanel();
        leftPanel2.setLayout(new BorderLayout());
        
        JPanel leftPanel2btnTOP = new JPanel();
        leftPanel2btnTOP.setLayout(new GridLayout(3,2)); //3 by 2 menu components
        leftPanel2btnCENTER = new JPanel();
//        leftPanel2btnCENTER.setLayout(bookingFormCards); //Setting as cards layout
        
        //Adding Top and Bottom Panels into left split Panel
        leftPanel2.add(leftPanel2btnTOP, BorderLayout.NORTH);
        leftPanel2.add(leftPanel2btnCENTER, BorderLayout.CENTER);
        loadBookingForms();
        
        //Adding buttons to left splitpane, North Border
        JLabel northBtnTitle2 = new JLabel("Filter Guest: ");
        String[] strFilterOptions = new String[]{"All Guests", "Active Guests", "Inactive Guests", "Guests with Request"};
        guestsFilterOptions = new JComboBox<String>(strFilterOptions);
        
        leftPanel2btnTOP.add(northBtnTitle2);
        leftPanel2btnTOP.add(guestsFilterOptions);
        
        //Adding buttons to left splitpane, CENTRAL border for options
        JButton completeBtn = new JButton("Complete Request");
            //Would be ideal to link functionality between left and right split panels.
            
        leftPanel2btnTOP.add(completeBtn);
        
        //Initializing Table model which reflects Guest records
//        String[][] myStrList= new String[][] { {"Data1a", "Data1b", "Data1c"}, {"Data2a", "Data2b", "Data2c"}, {"Data3a","Data3b","Data3c"}};
//        String[] tableHeadings = new String[] {"Guest", "Room Number", "Request"};
        tableModelGuests = new MyTableModel();
        guestsTable = new JTable(tableModelGuests){ 
            public boolean editCellAt(int row, int column, java.util.EventObject e) { //Prevents editing of cells in table = https://rb.gy/1qflxh
            return false;
         }
        };
        
        //Adding Table to scrollPane
        JScrollPane sp1 = new JScrollPane(leftPanel2);
        JScrollPane sp2 = new JScrollPane(guestsTable);        
        JSplitPane tabPanel2Inner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp1, sp2);
        //Where the splitpane is located along the middle of two panes
        tabPanel2Inner.setResizeWeight(.5d); 
        tabPanel2.add(tabPanel2Inner, BorderLayout.CENTER);
    }
    
    //Loads the Manage Rooms tabPanel contents
    private void loadManageRooms(JPanel tabPanel3) {
        
        tabPanel3.setLayout(new BorderLayout());
        
        //Panel for Left side of Manage Rooms for menu
        JPanel leftPanel3 = new JPanel();
        leftPanel3.setLayout(new BorderLayout());
        
        JPanel leftPanel3btnTOP = new JPanel();
        leftPanel3btnTOP.setLayout(new GridLayout(3,2));
        JPanel leftPanel3btnCENTER = new JPanel();
//        leftPanel3btnCENTER.setLayout(manageRoomCards); //TODO
        leftPanel3MessageBOTTOM = new JPanel();
        leftPanel3MessageBOTTOM.setLayout(new FlowLayout());
        
        
        //Adding Top, Center, bottom panels to left split pane
        leftPanel3.add(leftPanel3btnTOP, BorderLayout.NORTH);
        leftPanel3.add(leftPanel3btnCENTER, BorderLayout.CENTER);
        leftPanel3.add(leftPanel3MessageBOTTOM, BorderLayout.SOUTH);
//        loadRoomForms(); //todo

//        Adding label for user feedback to left split pane, bottom border
        JLabel manageRoomMSG = new JLabel("manageRoomMSG to leftPanel3BOTTOM");
        leftPanel3MessageBOTTOM.add(manageRoomMSG);
        
        //Adding buttons to left splitpane, North border
        JLabel northBtnTitle3 = new JLabel("Filter Rooms List: ");
        String[] strFilterOptions = new String[]{"All Rooms", "Available Rooms", "N/A Rooms", 
            "N/A Rooms - Occupied", "N/A Rooms - Booked", "N/A Rooms - Need Cleaning"};
        roomsFilterOptions = new JComboBox<String>(strFilterOptions);
        
        leftPanel3btnTOP.add(northBtnTitle3);
        leftPanel3btnTOP.add(roomsFilterOptions);
               
//        Initializing table model which reflects booking records
            tableModelRooms = new MyTableModel();
            roomsTable = new JTable(tableModelRooms){
            public boolean editCellAt(int row, int column, java.util.EventObject e) { //Prevents editing of cells in table = https://rb.gy/1qflxh
            return false;
         }
        };
        
        
        //Adding Table to scrollPane
        JScrollPane sp1 = new JScrollPane(leftPanel3);
        JScrollPane sp2 = new JScrollPane(roomsTable);        
        JSplitPane tabPanel3Inner = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp1, sp2);
        //Where the splitpane is located along the middle of two panes
        tabPanel3Inner.setResizeWeight(.5d); 
        tabPanel3.add(tabPanel3Inner, BorderLayout.CENTER);
                
    }
    
//    -----------------------------END OF STAFF MANAGEMENT PANELS------------------------------------------------------------------------------
    @Override
    public void createBookingFeedbackMSG(int output) {
        if (output == -1) {
            cancelBookingMSG.setText("Please enter valid booking details");
            bookingMSGCards.show(leftPanel1MessageBOTTOM, "CANCELBOOKMSG");
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
        bookingNoLabel.setText("Booking ID: " + data.getRecentBooking().getBookingID());
        roomLabel.setText("Room Number:" + data.getRecentRoom().roomNumber + "; " + data.getRecentRoom().roomType);
        guestNameLabel.setText("Guest ID: " + data.getRecentGuest().getGuestID() + " Guest Name: " + data.getRecentGuest().guestName);
        
        this.bookingMSGCards.show(leftPanel1MessageBOTTOM, "SUCCESSMSG");
        } else if (!data.isBookingSuccess()) {
            bookingNoLabel.setText("No Room Available for that RoomType. Try Again.");
            roomLabel.setText("");
            guestNameLabel.setText("");
        }
        data.setBookingFlag(false);
        
        return output;
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


