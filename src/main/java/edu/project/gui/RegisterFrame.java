package edu.project.gui;

import edu.project.exceptions.ValidationError;
import edu.project.manager.UserManager;
import edu.project.model.user.TaxPayer;
import edu.project.storage.FileManager;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final UserManager userManager;
    private JTextField idField;
    private JPasswordField passField;
    private JComboBox<String> regionBox;
    private JCheckBox filerCheck;
    private JLabel statusLabel;

    private static final String[] REGIONS = {"punjab","sindh","kpk","baloch","ict","ajk","gb"};

    public RegisterFrame(UserManager userManager) {
        this.userManager = userManager;
        buildUI();
    }

    private void buildUI() {
        setTitle("PakTax — Register");
        setSize(440, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG_DARK);

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        header.setBackground(AppTheme.BG_CARD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));
        header.add(AppTheme.label("PakTax  —  New Account", AppTheme.FONT_TITLE, AppTheme.ACCENT));
        root.add(header, BorderLayout.NORTH);

        // Card
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setBackground(AppTheme.BG_DARK);

        JPanel card = AppTheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(370, 400));

        addField(card, "User ID (e.g. FA25-BCS-001)");
        idField = AppTheme.styledField(20);
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        card.add(idField);
        card.add(Box.createVerticalStrut(14));

        addField(card, "Password");
        passField = AppTheme.styledPassword(20);
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        card.add(passField);
        card.add(Box.createVerticalStrut(14));

        addField(card, "Region of Residence");
        regionBox = new JComboBox<>(REGIONS);
        regionBox.setBackground(AppTheme.BG_INPUT);
        regionBox.setForeground(AppTheme.TEXT_PRIMARY);
        regionBox.setFont(AppTheme.FONT_BODY);
        regionBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        card.add(regionBox);
        card.add(Box.createVerticalStrut(14));

        filerCheck = new JCheckBox("Registered Tax Filer");
        filerCheck.setBackground(AppTheme.BG_CARD);
        filerCheck.setForeground(AppTheme.TEXT_PRIMARY);
        filerCheck.setFont(AppTheme.FONT_BODY);
        card.add(filerCheck);
        card.add(Box.createVerticalStrut(20));

        JButton regBtn = AppTheme.primaryButton("Create Account");
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        regBtn.addActionListener(e -> doRegister());
        card.add(regBtn);
        card.add(Box.createVerticalStrut(10));

        JButton backBtn = AppTheme.ghostButton("Back to Login");
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        backBtn.addActionListener(e -> dispose());
        card.add(backBtn);
        card.add(Box.createVerticalStrut(12));

        statusLabel = AppTheme.label("", AppTheme.FONT_SMALL, AppTheme.DANGER);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);
        card.add(statusLabel);

        centerWrap.add(card);
        root.add(centerWrap, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void addField(JPanel panel, String labelText) {
        panel.add(AppTheme.label(labelText, AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        panel.add(Box.createVerticalStrut(4));
    }

    private void doRegister() {
        String id = idField.getText().trim();
        String pass = new String(passField.getPassword());
        String region = (String) regionBox.getSelectedItem();
        boolean filer = filerCheck.isSelected();

        if (id.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("ID and password are required.");
            return;
        }

        TaxPayer tp = new TaxPayer(id, pass, filer, region);
        try {
            userManager.register(tp);
            FileManager.saveUserManager(userManager);
            FileManager.saveTaxPayer(tp);

            statusLabel.setForeground(AppTheme.ACCENT);
            statusLabel.setText("Account created! You can now log in.");
            idField.setText("");
            passField.setText("");
        } catch (ValidationError ex) {
            statusLabel.setForeground(AppTheme.DANGER);
            statusLabel.setText(ex.getMessage());
        }
    }
}