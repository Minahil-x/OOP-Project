package edu.project.gui;

import edu.project.manager.UserManager;
import edu.project.model.user.Admin;
import edu.project.storage.FileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private final Admin admin;
    private final UserManager userManager;

    // Tables kept as fields so refresh() can update them
    private JTable taxpayerTable;
    private JTable analyticsTable;

    // Replace the three JPanel fields with JLabel fields:
    private JLabel totalTPLabel;
    private JLabel taxCollectedLabel;
    private JLabel totalValuationLabel;

    public AdminDashboard(Admin admin, UserManager userManager) {
        this.admin = admin;
        this.userManager = userManager;
        buildUI();
    }

    private void buildUI() {
        setTitle("PakTax — Admin: " + admin.getId());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) { doLogout(); }
        });

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(AppTheme.BG_DARK);
        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildCenterPanel(), BorderLayout.CENTER);

        setContentPane(root);
        refresh();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AppTheme.BG_CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                new EmptyBorder(12, 20, 12, 20)
        ));

        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        info.setBackground(AppTheme.BG_CARD);
        info.add(AppTheme.label("◈ PakTax", AppTheme.FONT_TITLE, AppTheme.ACCENT));
        info.add(AppTheme.label("│", AppTheme.FONT_BODY, AppTheme.BORDER));
        info.add(AppTheme.label(admin.getId(), AppTheme.FONT_HEAD, AppTheme.TEXT_PRIMARY));
        info.add(AppTheme.label("•", AppTheme.FONT_BODY, AppTheme.TEXT_MUTED));
        info.add(AppTheme.label("Admin", AppTheme.FONT_BODY, AppTheme.ACCENT));
        bar.add(info, BorderLayout.WEST);

        JPanel btnArea = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnArea.setBackground(AppTheme.BG_CARD);
        JButton refreshBtn = AppTheme.primaryButton("⟳  Refresh");
        refreshBtn.addActionListener(e -> refresh());
        JButton logoutBtn = AppTheme.dangerButton("Logout");
        logoutBtn.addActionListener(e -> doLogout());
        btnArea.add(refreshBtn);
        btnArea.add(logoutBtn);
        bar.add(btnArea, BorderLayout.EAST);

        return bar;
    }

    private JPanel buildCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.BG_DARK);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel heading = AppTheme.label("Admin Dashboard", AppTheme.FONT_TITLE, AppTheme.TEXT_PRIMARY);
        heading.setBorder(new EmptyBorder(0, 0, 16, 0));
        panel.add(heading, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(AppTheme.BG_CARD);
        tabs.setForeground(AppTheme.TEXT_PRIMARY);
        tabs.setFont(AppTheme.FONT_BODY);
        tabs.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

        tabs.addTab("  TaxPayer Records  ", buildTaxpayerTab());
        tabs.addTab("  Regional Analytics  ", buildAnalyticsTab());

        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    // ── Tab 1: TaxPayer Records ───────────────────────────────────────────────

    private JPanel buildTaxpayerTab() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(AppTheme.BG_DARK);
        p.setBorder(new EmptyBorder(16, 0, 0, 0));

        // Summary row
        JPanel summaryRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        summaryRow.setBackground(AppTheme.BG_DARK);

        totalTPLabel        = new JLabel("0");
        taxCollectedLabel   = new JLabel("0.00");
        totalValuationLabel = new JLabel("0.00");

        summaryRow.add(summaryCard("Total Taxpayers",          totalTPLabel,        AppTheme.ACCENT));
        summaryRow.add(summaryCard("Total Tax Collected (PKR)", taxCollectedLabel,   AppTheme.DANGER));
        summaryRow.add(summaryCard("Total Asset Value (PKR)",   totalValuationLabel, AppTheme.TEXT_PRIMARY));
        p.add(summaryRow, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Region", "Filer", "Asset Value (PKR)", "Tax Due (PKR)"};
        taxpayerTable = AppTheme.styledTable(admin.getTable(), cols);
        taxpayerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = styledScroll(taxpayerTable);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    // ── Tab 2: Regional Analytics ─────────────────────────────────────────────

    private JPanel buildAnalyticsTab() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(AppTheme.BG_DARK);
        p.setBorder(new EmptyBorder(16, 0, 0, 0));

        JLabel sub = AppTheme.label("Total tax collected per region across all taxpayers",
                AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED);
        sub.setBorder(new EmptyBorder(0, 0, 8, 0));
        p.add(sub, BorderLayout.NORTH);

        String[] cols = {"Region", "Total Tax Collected (PKR)"};
        analyticsTable = AppTheme.styledTable(admin.generateAnalytics(), cols);
        analyticsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = styledScroll(analyticsTable);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    // ── Refresh ───────────────────────────────────────────────────────────────

    private void refresh() {
        String[][] tpData = FileManager.getAnalytics();
        updateTable(taxpayerTable, tpData,
                new String[]{"ID", "Region", "Filer", "Asset Value (PKR)", "Tax Due (PKR)"});

        String[][] anData = FileManager.getRegionalAnalytics();
        updateTable(analyticsTable, anData,
                new String[]{"Region", "Total Tax Collected (PKR)"});

        // Update summary cards — just set text on the existing labels
        totalTPLabel.setText(FileManager.getTotalTaxPayers());
        taxCollectedLabel.setText(FileManager.getTotalTaxCollected());
        totalValuationLabel.setText(FileManager.getTotalValuation());
    }

    private void updateTable(JTable table, String[][] data, String[] cols) {
        table.setModel(new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        // Re-apply header styling after model reset
        table.getTableHeader().setBackground(AppTheme.BG_INPUT);
        table.getTableHeader().setForeground(AppTheme.TEXT_MUTED);
        table.getTableHeader().setFont(AppTheme.FONT_HEAD);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JPanel summaryCard(String label, JLabel valueLabel, Color valueColor) {
        JPanel card = AppTheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(350, 72));
        card.add(AppTheme.label(label, AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        card.add(Box.createVerticalStrut(4));
        valueLabel.setFont(AppTheme.FONT_TITLE);
        valueLabel.setForeground(valueColor);
        card.add(valueLabel);
        return card;
    }

    private JScrollPane styledScroll(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(AppTheme.BG_CARD);
        scroll.getViewport().setBackground(AppTheme.BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
        return scroll;
    }

    private void doLogout() {
        userManager.logout(admin);
        FileManager.saveUserManager(userManager);
        dispose();
        new LoginFrame(userManager).setVisible(true);
    }
}