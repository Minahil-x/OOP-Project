package edu.project.storage;

import edu.project.manager.UserManager;
import edu.project.model.user.TaxPayer;
import edu.project.model.user.User;

import java.io.*;
import java.util.List;

public class FileManager {

    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = DATA_DIR + "users.ser";

    // ─── Init ────────────────────────────────────────────────────────────────

    public static void init() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    // ─── UserManager (entire registry) ───────────────────────────────────────

    public static void saveUserManager(UserManager manager) {
        serialize(USERS_FILE, manager);
    }

    public static UserManager loadUserManager() {
        UserManager manager = deserialize(USERS_FILE, UserManager.class);
        return manager != null ? manager : new UserManager();
    }

    // getAnalytics reads from UserManager directly, not a separate file
    public static String[][] getAnalytics() {
        UserManager manager = loadUserManager();
        List<User> users = manager.getUserList();
        List<TaxPayer> taxPayers = users.stream()
                .filter(u -> u instanceof TaxPayer)
                .map(u -> (TaxPayer) u)
                .toList();

        String[][] data = new String[taxPayers.size()][5];
        for (int i = 0; i < taxPayers.size(); i++) {
            TaxPayer tp = taxPayers.get(i);
            data[i][0] = tp.getId();
            data[i][1] = tp.getRegion();
            data[i][2] = tp.getFilerStatus() ? "Yes" : "No";
            data[i][3] = String.format("%.2f", tp.totalValue());
            data[i][4] = String.format("%.2f", tp.totalTaxAmount());
        }
        return data;
    }

    /**
     * Returns per-region tax totals: { region, totalTax }
     */
    public static String[][] getRegionalAnalytics() {
        UserManager manager = loadUserManager();

        String[] regions = {"punjab", "sindh", "kpk", "baloch", "ict", "ajk", "gb"};
        String[][] data = new String[regions.length][2];

        List<TaxPayer> taxPayers = manager.getUserList().stream()
                .filter(u -> u instanceof TaxPayer)
                .map(u -> (TaxPayer) u)
                .toList();

        for (int i = 0; i < regions.length; i++) {
            double total = 0;
            for (TaxPayer tp : taxPayers) {
                if (tp.getRegion().equalsIgnoreCase(regions[i])) {
                    total += tp.totalTaxAmount();
                }
            }
            data[i][0] = regions[i].toUpperCase();
            data[i][1] = String.format("%.2f", total);
        }
        return data;
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static void serialize(String path, Serializable obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("FileManager: failed to save " + path + " — " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T deserialize(String path, Class<T> type) {
        File f = new File(path);
        if (!f.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return type.cast(ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("FileManager: failed to load " + path + " — " + e.getMessage());
            return null;
        }
    }

    public static TaxPayer getTaxPayer(String id, UserManager userManager) {
        List<User> users = userManager.getUserList();
        List<TaxPayer> taxPayers = users.stream()
                .filter(u -> u instanceof TaxPayer)
                .map(u -> (TaxPayer) u)
                .toList();

        for (TaxPayer tp : taxPayers) {
            if (tp.getId().trim().equals(id)) {
                return tp;
            }
        }
        return null;
    }

    public static String getTotalTaxPayers() {
        return String.valueOf(loadUserManager().getUserList().stream()
                .filter(u -> u instanceof TaxPayer).count());
    }
    public static String getTotalTaxCollected() {
        return String.format("%.2f", loadUserManager().getUserList().stream()
                .filter(u -> u instanceof TaxPayer)
                .mapToDouble(u -> ((TaxPayer) u).totalTaxAmount())
                .sum());
    }
    public static String getTotalValuation() {
        return String.format("%.2f", loadUserManager().getUserList().stream()
                .filter(u -> u instanceof TaxPayer)
                .mapToDouble(u -> ((TaxPayer) u).totalValue())
                .sum());
    }
}