package com.mycompany.concertbookingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class MainDashboardFrame extends JFrame {
    private DatabaseManager dbManager;
    // Brand colors (match LoginFrame)
    private final Color PRIMARY_COLOR = Color.decode("#1800ad");
    private final Color SECONDARY_COLOR = Color.WHITE;
    private final Color MUTED_TEXT = Color.decode("#C7C2EB");
    private final Color OFF_WHITE = new Color(248, 248, 252); // softer white for backgrounds
    private final DateTimeFormatter EVENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.SMART);
    private final String[] eventNames = {
            "KYLE - SEAOIL",
            "ANCIRO - NYC",
            "AMANOLLAH - SEAWALL",
            "STELLA - LANE",
            "ORBIT - DOME",
            "NOVA - RIVERA"
    };
    private final String[] eventDates = {
            "May 30, 2026",
            "Jun 12, 2026",
            "Jul 03, 2026",
            "Aug 21, 2026",
            "Sep 10, 2026",
            "Oct 05, 2026"
    };
    private JPanel centerCards;
    private BookingPanel bookingPanel;

    public MainDashboardFrame() {
        dbManager = new DatabaseManager();
        
        setTitle("Bookist - Concert Booking System");
        setSize(1120, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmQuit();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(0,18));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));
        mainPanel.setBackground(OFF_WHITE);

        // Navigation header stays the same across dashboard pages
        TopNavBar navBar = new TopNavBar(this, "BOOKING", PRIMARY_COLOR, SECONDARY_COLOR);

        // Clean action strip for the dashboard's primary booking actions
        JPanel banner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.setColor(new Color(226, 226, 242));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
                g2.dispose();
            }
        };
        banner.setOpaque(false);
        banner.setLayout(new FlowLayout(FlowLayout.CENTER, 18, 16));
        banner.setPreferredSize(new Dimension(0, 84));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));

        Font btnFont = new Font("SansSerif", Font.BOLD, 15);
        JButton bookNowBtnStyled = new JButton("BOOK TICKET");
        JButton viewBtnStyled = new JButton("VIEW BOOKINGS");
        JButton cancelBtnStyled = new JButton("CANCEL BOOKING");

        for (JButton b : new JButton[]{bookNowBtnStyled, viewBtnStyled, cancelBtnStyled}) {
            b.setPreferredSize(new Dimension(205, 46));
            b.setBackground(PRIMARY_COLOR);
            b.setForeground(SECONDARY_COLOR);
            b.setFont(btnFont);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        banner.add(bookNowBtnStyled);
        banner.add(viewBtnStyled);
        banner.add(cancelBtnStyled);

        // Top container holds the shared nav and banner
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(new Color(0,0,0,0));
        topContainer.setOpaque(false);
        topContainer.add(navBar);
        topContainer.add(Box.createRigidArea(new Dimension(0,14)));
        topContainer.add(banner);

        mainPanel.add(topContainer, BorderLayout.NORTH);

        // Center area uses card layout: HOME and BOOKING_PANEL
        centerCards = new JPanel(new CardLayout());
        JPanel homeCard = buildHomeCard();
        bookingPanel = new BookingPanel();

        centerCards.add(homeCard, "HOME");
        centerCards.add(bookingPanel, "BOOKINGS");
        mainPanel.add(centerCards, BorderLayout.CENTER);

        // (Logout moved into header)

        add(mainPanel);

        // Actions wiring from the styled buttons
        bookNowBtnStyled.addActionListener(e -> showEventDropdown(bookNowBtnStyled));
        viewBtnStyled.addActionListener(e -> {
            bookingPanel.showViewMode(false);
            ((CardLayout)centerCards.getLayout()).show(centerCards, "BOOKINGS");
        });
        cancelBtnStyled.addActionListener(e -> {
            bookingPanel.showViewMode(true);
            ((CardLayout)centerCards.getLayout()).show(centerCards, "BOOKINGS");
        });

    }

    private void openSeatSelectionFrame(String eventName) {
        this.setVisible(false);
        SeatSelectionFrame frame = new SeatSelectionFrame(this, dbManager, eventName, PRIMARY_COLOR, SECONDARY_COLOR);
        frame.setVisible(true);
    }

    private void showEventDropdown(Component invoker) {
        JPopupMenu popup = new JPopupMenu();
        for (int i = 0; i < eventNames.length; i++) {
            String label = String.format("<html><b>%s</b><br><span style='font-size:10px;color:#666;'>%s • %s</span></html>",
                    eventNames[i], eventDates[i], getTimeUntilEvent(eventDates[i]));
            JMenuItem item = new JMenuItem(label);
            item.setFont(new Font("SansSerif", Font.PLAIN, 12));
            final String selectedEvent = eventNames[i];
            item.addActionListener(e -> openSeatSelectionFrame(selectedEvent));
            popup.add(item);
        }
        popup.show(invoker, 0, invoker.getHeight());
    }

    private String getTimeUntilEvent(String eventDate) {
        try {
            LocalDate event = LocalDate.parse(eventDate, EVENT_DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            if (!event.isAfter(today)) {
                return "Starts today";
            }
            long days = ChronoUnit.DAYS.between(today, event);
            if (days == 1) {
                return "Starts in 1 day";
            }
            if (days < 7) {
                return "Starts in " + days + " days";
            }
            long weeks = days / 7;
            return "Starts in " + weeks + " week" + (weeks > 1 ? "s" : "");
        } catch (Exception ex) {
            return "";
        }
    }

    // Build the home card with a simple featured events carousel
    private JPanel buildHomeCard() {
        JPanel p = new JPanel(new BorderLayout(0, 10));

        JLabel title = new JLabel("FEATURED EVENTS", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(PRIMARY_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        p.add(title, BorderLayout.NORTH);
        p.setBackground(OFF_WHITE);

        // Placeholder data for 6 events
        EventCarouselPanel carousel = new EventCarouselPanel(eventNames, eventDates, PRIMARY_COLOR, SECONDARY_COLOR,
            eventName -> openSeatSelectionFrame(eventName)
        );

        p.add(carousel, BorderLayout.CENTER);
        return p;
    }

    public void showHomeCard() {
        if (centerCards != null) {
            CardLayout cl = (CardLayout) centerCards.getLayout();
            cl.show(centerCards, "HOME");
        }
    }

    private void confirmQuit() {
        int reply = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to quit?", "Exit Application", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
