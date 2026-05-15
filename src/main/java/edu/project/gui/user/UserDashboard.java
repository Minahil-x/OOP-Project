package edu.project.gui.user;

import edu.project.gui.AppTheme;
import edu.project.gui.LoginFrame;
import edu.project.manager.UserManager;
import edu.project.model.tax.Taxable;
import edu.project.model.tax.income.*;
import edu.project.model.tax.property.*;
import edu.project.model.user.TaxPayer;
import edu.project.storage.FileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class UserDashboard extends JFrame {

    private final TaxPayer taxPayer;
    private final UserManager userManager;
    private JTextArea summaryArea;
    private JLabel totalTaxLabel;
    private JLabel totalValueLabel;

    public UserDashboard(TaxPayer taxPayer, UserManager userManager) {
        this.taxPayer = taxPayer;
        this.userManager = userManager;

        // Load saved taxables from file
        TaxPayer saved = FileManager.getTaxPayer(taxPayer.getId());
        if (saved != null) {
            for (Taxable t : saved.getTaxables()) taxPayer.addTaxable(t);
        }

        buildUI();
    }

    private void buildUI() {
        setTitle("PakTax — " + taxPayer.getId());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) { doLogout(); }
        });

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(AppTheme.BG_DARK);

        // ── Top bar ──
        root.add(buildTopBar(), BorderLayout.NORTH);

        // ── Left sidebar: summary ──
        JPanel sidebar = buildSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // ── Center: add taxable panel ──
        JPanel center = buildAddPanel();
        root.add(center, BorderLayout.CENTER);

        setContentPane(root);
        refreshSummary();
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AppTheme.BG_CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                new EmptyBorder(12, 20, 12, 20)
        ));

        JLabel title = AppTheme.label("PakTax", AppTheme.FONT_TITLE, AppTheme.ACCENT);
        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        info.setBackground(AppTheme.BG_CARD);
        info.add(title);
        info.add(AppTheme.label("|", AppTheme.FONT_BODY, AppTheme.BORDER));
        info.add(AppTheme.label(taxPayer.getId(), AppTheme.FONT_HEAD, AppTheme.TEXT_PRIMARY));
        info.add(AppTheme.label("•", AppTheme.FONT_BODY, AppTheme.TEXT_MUTED));
        info.add(AppTheme.label(taxPayer.getRegion().toUpperCase(), AppTheme.FONT_BODY, AppTheme.TEXT_MUTED));
        info.add(AppTheme.label("•", AppTheme.FONT_BODY, AppTheme.TEXT_MUTED));
        info.add(AppTheme.label(taxPayer.getFilerStatus() ? "Filer" : "Non-Filer", AppTheme.FONT_BODY,
                taxPayer.getFilerStatus() ? AppTheme.ACCENT : AppTheme.DANGER));
        bar.add(info, BorderLayout.WEST);

        JPanel btnArea = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnArea.setBackground(AppTheme.BG_CARD);

        JButton reportBtn = AppTheme.primaryButton("Raw Data");
        reportBtn.addActionListener(e -> getDATA());

        JButton logoutBtn = AppTheme.dangerButton("Logout");
        logoutBtn.addActionListener(e -> doLogout());

        btnArea.add(reportBtn);
        btnArea.add(logoutBtn);
        bar.add(btnArea, BorderLayout.EAST);

        return bar;
    }

    private JPanel buildSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(AppTheme.BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER),
                new EmptyBorder(20, 16, 20, 16)
        ));
        panel.setPreferredSize(new Dimension(400, 0));

        panel.add(AppTheme.label("TAX SUMMARY", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        panel.add(Box.createVerticalStrut(12));

        // Total tax card
        JPanel taxCard = AppTheme.card();
        taxCard.setLayout(new BoxLayout(taxCard, BoxLayout.Y_AXIS));
        taxCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        taxCard.add(AppTheme.label("Total Tax Due", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        taxCard.add(Box.createVerticalStrut(4));
        totalTaxLabel = AppTheme.label("PKR 0.00", AppTheme.FONT_TITLE, AppTheme.DANGER);
        taxCard.add(totalTaxLabel);
        panel.add(taxCard);
        panel.add(Box.createVerticalStrut(10));

        // Total value card
        JPanel valCard = AppTheme.card();
        valCard.setLayout(new BoxLayout(valCard, BoxLayout.Y_AXIS));
        valCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        valCard.add(AppTheme.label("Total Asset Value", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        valCard.add(Box.createVerticalStrut(4));
        totalValueLabel = AppTheme.label("PKR 0.00", AppTheme.FONT_TITLE, AppTheme.ACCENT);
        valCard.add(totalValueLabel);
        panel.add(valCard);
        panel.add(Box.createVerticalStrut(16));

        panel.add(AppTheme.label("BREAKDOWN", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        panel.add(Box.createVerticalStrut(8));

        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setBackground(AppTheme.BG_INPUT);
        summaryArea.setForeground(AppTheme.TEXT_PRIMARY);
        summaryArea.setFont(AppTheme.FONT_MONO);
        summaryArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(summaryArea);
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
        scroll.setBackground(AppTheme.BG_INPUT);
        panel.add(scroll);

        return panel;
    }

    private JPanel buildAddPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.BG_DARK);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(AppTheme.label("Add Tax Record", AppTheme.FONT_TITLE, AppTheme.TEXT_PRIMARY), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(AppTheme.BG_CARD);
        tabs.setForeground(AppTheme.TEXT_PRIMARY);
        tabs.setFont(AppTheme.FONT_BODY);

        tabs.addTab("Property", buildPropertyTab());
        tabs.addTab("Salaried / Business", buildSalaryTab());
        tabs.addTab("Agricultural", buildAgriTab());
        tabs.addTab("Asset / Investment", buildAssetTab());

        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    // ── Property Tab ──────────────────────────────────────────────────────────
    private JPanel buildPropertyTab() {
        JPanel p = tabPanel();

        JTextField urbanF = field(p, "Urban Area (acres)");
        JTextField agriF  = field(p, "Agricultural Area (acres)");

        String[] regions = {"punjab","sindh","kpk","baloch","ict","ajk","gb"};
        JComboBox<String> regionBox = new JComboBox<>(regions);
        regionBox.setSelectedItem(taxPayer.getRegion());
        styleCombo(regionBox);
        addRow(p, "Region", regionBox);

        JTextField irrigF    = field(p, "Irrigated % (Punjab/Sindh only, 0-1)");
        JTextField coveredF  = field(p, "Covered Area % (Punjab/Sindh only, 0-1)");

        JButton addBtn = AppTheme.primaryButton("Add Property");
        addBtn.addActionListener(e -> {
            try {
                double urban   = parseField(urbanF, "Urban Area");
                double agri    = parseField(agriF,  "Agri Area");
                String region  = (String) regionBox.getSelectedItem();
                double irrig   = irrigF.getText().isEmpty() ? 0 : parseField(irrigF, "Irrigated %");
                double covered = coveredF.getText().isEmpty() ? 0 : parseField(coveredF, "Covered %");

                Property prop = switch (region) {
                    case "punjab"  -> new Punjab(urban, agri, irrig, covered);
                    case "sindh"   -> new Sindh(urban, agri, irrig, covered);
                    case "kpk"     -> new KPK(urban, agri);
                    case "baloch"  -> new Baloch(urban, agri);
                    case "ict"     -> new ICT(urban, agri);
                    case "ajk"     -> new AJK(urban, agri);
                    case "gb"      -> new GB(urban, agri);
                    default -> throw new IllegalArgumentException("Unknown region");
                };

                taxPayer.addTaxable(prop);
                saveTaxPayer();
                refreshSummary();
                clearFields(urbanF, agriF, irrigF, coveredF);
                showSuccess("Property added successfully.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        p.add(Box.createVerticalStrut(16));
        p.add(addBtn);
        return p;
    }

    // ── Salary Tab ────────────────────────────────────────────────────────────
    private JPanel buildSalaryTab() {
        JPanel p = tabPanel();
        JTextField salaryF   = field(p, "Annual Salary (PKR)");
        JTextField businessF = field(p, "Annual Business Income (PKR, 0 if none)");

        JButton addBtn = AppTheme.primaryButton("Add Income");
        addBtn.addActionListener(e -> {
            try {
                double salary   = parseField(salaryF, "Salary");
                double business = parseField(businessF, "Business Income");
                SalariedAndBusiness inc = new SalariedAndBusiness(salary, business);
                inc.save();
                taxPayer.addTaxable(inc);
                saveTaxPayer();
                refreshSummary();
                clearFields(salaryF, businessF);
                showSuccess("Income added successfully.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        p.add(Box.createVerticalStrut(16));
        p.add(addBtn);
        return p;
    }

    // ── Agricultural Tab ──────────────────────────────────────────────────────
    private JPanel buildAgriTab() {
        JPanel p = tabPanel();
        p.add(AppTheme.label("Enter income per region (leave 0 if none):", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        p.add(Box.createVerticalStrut(10));

        String[] regions = {"punjab","sindh","kpk","baloch","ict","ajk","gb"};
        JTextField[] fields = new JTextField[regions.length];
        for (int i = 0; i < regions.length; i++) {
            fields[i] = field(p, regions[i].toUpperCase() + " income (PKR)");
        }

        JButton addBtn = AppTheme.primaryButton("Add Agricultural Income");
        addBtn.addActionListener(e -> {
            try {
                Agricultural agri = new Agricultural();
                for (int i = 0; i < regions.length; i++) {
                    double val = fields[i].getText().isEmpty() ? 0 : parseField(fields[i], regions[i]);
                    agri.addRegionalIncome(regions[i], val);
                }
                agri.save();
                taxPayer.addTaxable(agri);
                saveTaxPayer();
                refreshSummary();
                for (JTextField f : fields) f.setText("");
                showSuccess("Agricultural income added.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        p.add(Box.createVerticalStrut(16));
        p.add(addBtn);
        return p;
    }

    // ── Asset Tab ─────────────────────────────────────────────────────────────
    private JPanel buildAssetTab() {
        JPanel p = tabPanel();
        JTextField bankF   = field(p, "Bank Investment / Deposits (PKR)");
        JTextField equityF = field(p, "Equity Investment (PKR)");

        JButton addBtn = AppTheme.primaryButton("Add Asset Income");
        addBtn.addActionListener(e -> {
            try {
                double bank   = parseField(bankF, "Bank Investment");
                double equity = parseField(equityF, "Equity Investment");
                Asset asset = new Asset(bank, equity);
                asset.calculateTax(taxPayer);
                asset.save();
                taxPayer.addTaxable(asset);
                saveTaxPayer();
                refreshSummary();
                clearFields(bankF, equityF);
                showSuccess("Asset income added.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        p.add(Box.createVerticalStrut(16));
        p.add(addBtn);
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void refreshSummary() {
        totalTaxLabel.setText(String.format("PKR %,.2f", taxPayer.totalTaxAmount()));
        totalValueLabel.setText(String.format("PKR %,.2f", taxPayer.totalValue()));
        summaryArea.setText(taxPayer.totalTax());
    }

    private void saveTaxPayer() {

    }

    private void doLogout() {
        userManager.logout(taxPayer);
        FileManager.saveUserManager(userManager);
        dispose();
        new LoginFrame(userManager).setVisible(true);
    }

    private JPanel tabPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(AppTheme.BG_CARD);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        return p;
    }

    private JTextField field(JPanel parent, String label) {
        parent.add(AppTheme.label(label, AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        parent.add(Box.createVerticalStrut(4));
        JTextField f = AppTheme.styledField(20);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        parent.add(f);
        parent.add(Box.createVerticalStrut(12));
        return f;
    }

    private void addRow(JPanel parent, String label, JComponent comp) {
        parent.add(AppTheme.label(label, AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        parent.add(Box.createVerticalStrut(4));
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        parent.add(comp);
        parent.add(Box.createVerticalStrut(12));
    }

    private void styleCombo(JComboBox<?> box) {
        box.setBackground(AppTheme.BG_INPUT);
        box.setForeground(AppTheme.TEXT_PRIMARY);
        box.setFont(AppTheme.FONT_BODY);
    }

    private double parseField(JTextField f, String name) {
        try {
            return Double.parseDouble(f.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value for " + name);
        }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields) f.setText("");
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    public JFrame getDATA(){
        JFrame frame = new JFrame("User Data");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));


        JScrollPane scroll = new JScrollPane(p);

        JTextArea l = new JTextArea(taxPayer.getData());

        p.add(l);
        frame.add(scroll);
        frame.setSize(400, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }
}