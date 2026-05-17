package edu.project.gui;

import edu.project.exceptions.ValidationException;
import edu.project.manager.UserManager;
import edu.project.model.user.Admin;
import edu.project.model.user.TaxPayer;
import edu.project.model.user.User;
import edu.project.storage.FileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final UserManager userManager;
    private JTextField idField;
    private JPasswordField passField;
    private JLabel statusLabel;

    public LoginFrame(UserManager userManager) {
        this.userManager = userManager;
        buildUI();
    }

    private void buildUI() {
        setTitle("PakTax — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(440, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG_DARK);
        root.setBorder(new EmptyBorder(0, 0, 0, 0));

        // ── Header bar ──
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        header.setBackground(AppTheme.BG_CARD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));
        JLabel logo = AppTheme.label("PakTax", AppTheme.FONT_TITLE, AppTheme.ACCENT);
        header.add(logo);
        root.add(header, BorderLayout.NORTH);

        // ── Center card ──
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setBackground(AppTheme.BG_DARK);

        JPanel card = AppTheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 360));

        JLabel title = AppTheme.label("Welcome back", AppTheme.FONT_TITLE, AppTheme.TEXT_PRIMARY);
        title.setAlignmentX(CENTER_ALIGNMENT);
        JLabel sub = AppTheme.label("Sign in to your account", AppTheme.FONT_BODY, AppTheme.TEXT_MUTED);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(8));
        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(24));

        // ID field
        card.add(AppTheme.label("User ID", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        card.add(Box.createVerticalStrut(4));
        idField = AppTheme.styledField(20);
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        card.add(idField);
        card.add(Box.createVerticalStrut(14));

        // Password field
        card.add(AppTheme.label("Password", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        card.add(Box.createVerticalStrut(4));
        passField = AppTheme.styledPassword(20);
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        card.add(passField);
        card.add(Box.createVerticalStrut(20));

        // Login button
        JButton loginBtn = AppTheme.primaryButton("Sign In");
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> doLogin());
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(12));

        // Register link
        JButton regBtn = AppTheme.ghostButton("Create an account");
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        regBtn.addActionListener(e -> openRegister());
        card.add(regBtn);
        card.add(Box.createVerticalStrut(16));

        // Status label
        statusLabel = AppTheme.label("", AppTheme.FONT_SMALL, AppTheme.DANGER);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);
        card.add(statusLabel);

        centerWrap.add(card);
        root.add(centerWrap, BorderLayout.CENTER);

        // ── Footer ──
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(AppTheme.BG_DARK);
        footer.add(AppTheme.label("Pakistan Tax Management System  •  2025", AppTheme.FONT_SMALL, AppTheme.TEXT_MUTED));
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);

        // Enter key triggers login
        passField.addActionListener(e -> doLogin());
        idField.addActionListener(e -> passField.requestFocus());
    }

    private void doLogin() {
        String id = idField.getText().trim();
        String pass = new String(passField.getPassword());

        if (id.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Please enter ID and password.");
            return;
        }

        try {
            User user = userManager.login(id, pass);
            FileManager.saveUserManager(userManager);
            dispose();

            if (user instanceof Admin) {
                new AdminDashboard((Admin) user, userManager).setVisible(true);
            } else if (user instanceof TaxPayer) {
                new UserDashboard((TaxPayer) user, userManager).setVisible(true);
            }
        } catch (ValidationException ex) {
            statusLabel.setText(ex.getMessage());
            passField.setText("");
        }
    }

    private void openRegister() {
        new RegisterFrame(userManager).setVisible(true);
    }
}