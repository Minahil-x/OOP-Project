package edu.project.storage;

import edu.project.manager.UserManager;
import edu.project.model.tax.Taxable;
import edu.project.model.user.Admin;
import edu.project.model.user.TaxPayer;
import edu.project.model.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = DATA_DIR + "users.ser";
    private static final String TAXPAYERS_FILE = DATA_DIR + "taxpayers.ser";

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

    // ─── TaxPayer list ────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public static void saveTaxPayers(List<TaxPayer> taxPayers) {
        serialize(TAXPAYERS_FILE, (Serializable) new ArrayList<>(taxPayers));
    }

    @SuppressWarnings("unchecked")
    public static List<TaxPayer> loadTaxPayers() {
        List<TaxPayer> list = deserialize(TAXPAYERS_FILE, List.class);
        return list != null ? list : new ArrayList<>();
    }

    public static void saveTaxPayer(TaxPayer taxPayer) {
        List<TaxPayer> list = loadTaxPayers();
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(taxPayer.getId())) {
                list.set(i, taxPayer);
                found = true;
                break;
            }
        }
        if (!found) list.add(taxPayer);
        saveTaxPayers(list);
    }

    public static TaxPayer getTaxPayer(String id) {
        for (TaxPayer tp : loadTaxPayers()) {
            if (tp.getId().equals(id)) return tp;
        }
        return null;
    }

    public static void deleteTaxPayer(String id) {
        List<TaxPayer> list = loadTaxPayers();
        list.removeIf(tp -> tp.getId().equals(id));
        saveTaxPayers(list);
    }

    // ─── Analytics ────────────────────────────────────────────────────────────

    /**
     * Returns a 2D String array for use in a JTable.
     * Columns: ID, Region, Filer, Total Value, Total Tax
     */
    public static String[][] getAnalytics() {
        List<TaxPayer> list = loadTaxPayers();
        String[][] data = new String[list.size()][5];
        for (int i = 0; i < list.size(); i++) {
            TaxPayer tp = list.get(i);
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
        String[] regions = {"punjab", "sindh", "kpk", "baloch", "ict", "ajk", "gb"};
        String[][] data = new String[regions.length][2];
        List<TaxPayer> list = loadTaxPayers();

        for (int i = 0; i < regions.length; i++) {
            double total = 0;
            for (TaxPayer tp : list) {
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
}