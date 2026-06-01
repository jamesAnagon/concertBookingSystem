/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import javax.swing.*;
import java.awt.*;
public class BookingSummaryFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(BookingSummaryFrame.class.getName());

    /**
     * Creates new form BookingSummaryFrame
     */
    public BookingSummaryFrame() {
        setTitle("Booking Summary");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        // =========================
        // MAIN PANEL
        // =========================

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBounds(0, 0, 800, 600);

        // =========================
        // TITLE
        // =========================

        JLabel lblTitle = new JLabel("BOOKING SUMMARY");
        lblTitle.setFont(new Font("Serif", Font.BOLD, 32));
        lblTitle.setForeground(new Color(20, 20, 180));
        lblTitle.setBounds(220, 20, 400, 40);
        mainPanel.add(lblTitle);

        // =========================
        // CARD PANEL
        // =========================

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBackground(new Color(190, 240, 240));
        cardPanel.setBounds(240, 80, 320, 450);

        // =========================
        // CONCERT SECTION
        // =========================

        JLabel lblConcert = new JLabel("Concert:");
        lblConcert.setFont(new Font("Serif", Font.BOLD, 20));
        lblConcert.setForeground(new Color(20, 20, 180));
        lblConcert.setBounds(20, 20, 120, 30);
        cardPanel.add(lblConcert);

        JLabel lblConcertName = new JLabel("KYLE - SEOUL");
        lblConcertName.setFont(new Font("Arial", Font.BOLD, 18));
        lblConcertName.setBounds(160, 18, 140, 25);
        cardPanel.add(lblConcertName);

        JLabel lblDate = new JLabel("May 30, 2026");
        lblDate.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDate.setBounds(190, 42, 100, 15);
        cardPanel.add(lblDate);

        // =========================
        // HEADERS
        // =========================

        JLabel lblSeatHeader = new JLabel("SELECTED SEATS:");
        lblSeatHeader.setFont(new Font("Arial", Font.BOLD, 14));
        lblSeatHeader.setBounds(20, 85, 140, 20);
        cardPanel.add(lblSeatHeader);

        JLabel lblPriceHeader = new JLabel("UNIT PRICE:");
        lblPriceHeader.setFont(new Font("Arial", Font.BOLD, 14));
        lblPriceHeader.setBounds(210, 85, 100, 20);
        cardPanel.add(lblPriceHeader);

        // =========================
        // SEATS
        // =========================

        String[] seats = {
                "REG-C3",
                "REG-C4",
                "REG-C5",
                "VIP-A3",
                "VIP-A4"
        };

        String[] prices = {
                "$5",
                "$5",
                "$5",
                "$10",
                "$10"
        };

        int startY = 120;
        int gap = 28;

        for (int i = 0; i < seats.length; i++) {

            JLabel seatLabel = new JLabel(seats[i]);
            seatLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            seatLabel.setBounds(40, startY + (i * gap), 120, 20);
            cardPanel.add(seatLabel);

            JLabel priceLabel = new JLabel(prices[i]);
            priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            priceLabel.setBounds(230, startY + (i * gap), 60, 20);
            cardPanel.add(priceLabel);
        }

        // =========================
        // SEPARATOR
        // =========================

        JSeparator separator = new JSeparator();
        separator.setBounds(20, 270, 280, 10);
        cardPanel.add(separator);

        // =========================
        // TOTAL PAYMENT
        // =========================

        JLabel lblTotalText = new JLabel("TOTAL PAYMENT");
        lblTotalText.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalText.setBounds(20, 285, 180, 25);
        cardPanel.add(lblTotalText);

        JLabel lblTotal = new JLabel("$35");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 30));
        lblTotal.setForeground(new Color(20, 20, 180));
        lblTotal.setBounds(220, 278, 80, 35);
        cardPanel.add(lblTotal);

        // =========================
        // PAYMENT METHOD
        // =========================

        JLabel lblPayment = new JLabel("PAY WITH");
        lblPayment.setFont(new Font("Arial", Font.BOLD, 16));
        lblPayment.setBounds(20, 335, 100, 25);
        cardPanel.add(lblPayment);

        String[] methods = {
                "GCash",
                "PayMaya",
                "Credit Card",
                "Debit Card"
        };

        JComboBox<String> paymentBox =
                new JComboBox<>(methods);

        paymentBox.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentBox.setBounds(160, 333, 120, 30);
        cardPanel.add(paymentBox);

        // =========================
        // PAY BUTTON
        // =========================

        JButton btnPay = new JButton("PAY NOW");
        btnPay.setFont(new Font("Arial", Font.BOLD, 18));
        btnPay.setForeground(Color.WHITE);
        btnPay.setBackground(new Color(20, 20, 180));
        btnPay.setFocusPainted(false);
        btnPay.setBounds(80, 385, 160, 45);

        cardPanel.add(btnPay);

        // =========================
        // ADD PANELS
        // =========================

        mainPanel.add(cardPanel);
        add(mainPanel);

        setVisible(true);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new BookingSummaryFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
