package edu.project.gui;

import java.awt.*;

public class AppTheme {

    // ── Palette ──────────────────────────────────────────────────────────────
    public static final Color BG_DARK      = new Color(13, 17, 23);
    public static final Color BG_CARD      = new Color(22, 27, 34);
    public static final Color BG_INPUT     = new Color(33, 38, 45);
    public static final Color ACCENT       = new Color(0, 196, 154);   // teal-green
    public static final Color ACCENT_HOVER = new Color(0, 230, 180);
    public static final Color DANGER       = new Color(220, 80, 80);
    public static final Color TEXT_PRIMARY = new Color(230, 237, 243);
    public static final Color TEXT_MUTED   = new Color(110, 118, 129);
    public static final Color BORDER       = new Color(48, 54, 61);

    // ── Typography ───────────────────────────────────────────────────────────
    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_HEAD   = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO   = new Font("Consolas",  Font.PLAIN, 12);

    // ── Shared component builders ─────────────────────────────────────────────

    public static javax.swing.JButton primaryButton(String text) {
        javax.swing.JButton btn = new javax.swing.JButton(text);
        btn.setBackground(ACCENT);
        btn.setForeground(BG_DARK);
        btn.setFont(FONT_HEAD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    public static javax.swing.JButton dangerButton(String text) {
        javax.swing.JButton btn = primaryButton(text);
        btn.setBackground(DANGER);
        btn.setForeground(Color.WHITE);
        return btn;
    }

    public static javax.swing.JButton ghostButton(String text) {
        javax.swing.JButton btn = new javax.swing.JButton(text);
        btn.setBackground(BG_INPUT);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(FONT_BODY);
        btn.setFocusPainted(false);
        btn.setBorder(javax.swing.BorderFactory.createLineBorder(BORDER));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    public static javax.swing.JTextField styledField(int cols) {
        javax.swing.JTextField f = new javax.swing.JTextField(cols);
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT);
        f.setFont(FONT_BODY);
        f.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(BORDER),
                javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return f;
    }

    public static javax.swing.JPasswordField styledPassword(int cols) {
        javax.swing.JPasswordField f = new javax.swing.JPasswordField(cols);
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT);
        f.setFont(FONT_BODY);
        f.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(BORDER),
                javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return f;
    }

    public static javax.swing.JLabel label(String text, Font font, Color color) {
        javax.swing.JLabel l = new javax.swing.JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static javax.swing.JPanel card() {
        javax.swing.JPanel p = new javax.swing.JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(BORDER),
                javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));
        return p;
    }

    public static javax.swing.JTable styledTable(Object[][] data, Object[] cols) {
        javax.swing.JTable t = new javax.swing.JTable(data, cols);
        t.setBackground(BG_CARD);
        t.setForeground(TEXT_PRIMARY);
        t.setFont(FONT_BODY);
        t.setRowHeight(28);
        t.setGridColor(BORDER);
        t.setSelectionBackground(ACCENT);
        t.setSelectionForeground(BG_DARK);
        t.getTableHeader().setBackground(BG_INPUT);
        t.getTableHeader().setForeground(TEXT_MUTED);
        t.getTableHeader().setFont(FONT_HEAD);
        t.setFillsViewportHeight(true);
        return t;
    }
}