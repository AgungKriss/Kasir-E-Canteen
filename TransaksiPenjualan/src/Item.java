import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Kelas abstrak untuk item penjualan
abstract class Item {
    private String name;
    private double price;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public abstract double calculateTotal();
}

// Kelas turunan untuk makanan
class Food extends Item {
    public Food(String name, double price) {
        super(name, price);
    }

    public double calculateTotal() {
        return getPrice();
    }
}

// Kelas turunan untuk minuman
class Drink extends Item {
    public Drink(String name, double price) {
        super(name, price);
    }

    public double calculateTotal() {
        return getPrice();
    }
}

// Kelas utama GUI
class TransaksiPenjualanGUI extends JFrame {

    private JTextArea txtOutput;
    private JButton btnMenu;
    private JButton btnTambahMenu;
    private JButton btnHapusMenu;
    private JButton btnTransaksi;

    private List<Item> menuList;
    private List<Item> selectedItems;

    public TransaksiPenjualanGUI() {
        super("Kasir Canteen");

        // Membuat komponen tombol Menu
        btnMenu = new JButton("Menu");
        btnTambahMenu = new JButton("Tambah Menu");
        btnHapusMenu = new JButton("Hapus Menu");
        btnTransaksi = new JButton("Transaksi");

        // Membuat area teks untuk output
        txtOutput = new JTextArea(10, 30);
        txtOutput.setEditable(false);
        txtOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Menambahkan action listener pada tombol Menu
        btnMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMenu();
            }
        });

        // Menambahkan action listener pada tombol Tambah Menu
        btnTambahMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tambahMenu();
            }
        });

        // Menambahkan action listener pada tombol Hapus Menu
        btnHapusMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hapusMenu();
            }
        });

        // Menambahkan action listener pada tombol Transaksi
        btnTransaksi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                transaksi();
            }
        });

        // Mengatur layout frame
        setLayout(new BorderLayout());

        // Menambahkan komponen ke frame
        add(new JLabel("Welcome to E-Canteen"), BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnMenu);
        buttonPanel.add(btnTambahMenu);
        buttonPanel.add(btnHapusMenu);
        buttonPanel.add(btnTransaksi);
        add(buttonPanel, BorderLayout.CENTER);

        add(new JScrollPane(txtOutput), BorderLayout.SOUTH);

        // Mengatur properti frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);

        menuList = new ArrayList<>();
        selectedItems = new ArrayList<>();
    }

    // Menampilkan menu makanan dan minuman
    private void showMenu() {
        txtOutput.setText("");

        if (menuList.isEmpty()) {
            txtOutput.append("Tidak ada menu yang tersedia.");
        } else {
            txtOutput.append("Menu Makanan:\n");
            for (Item item : menuList) {
                if (item instanceof Food) {
                    txtOutput.append(item.getName() + " - Rp" + item.getPrice() + "\n");
                }
            }

            txtOutput.append("\nMenu Minuman:\n");
            for (Item item : menuList) {
                if (item instanceof Drink) {
                    txtOutput.append(item.getName() + " - Rp" + item.getPrice() + "\n");
                }
            }
        }
    }

    // Menambahkan menu ke daftar menu
    private void tambahMenu() {
        String[] menuTypes = { "Food", "Drink" };
        String selectedMenuType = (String) JOptionPane.showInputDialog(null, "Pilih jenis menu:", "Tambah Menu",
                JOptionPane.QUESTION_MESSAGE, null, menuTypes, menuTypes[0]);

        if (selectedMenuType != null) {
            String name = JOptionPane.showInputDialog("Masukkan nama menu:");
            if (name != null && !name.isEmpty()) {
                String priceString = JOptionPane.showInputDialog("Masukkan harga menu:");
                if (priceString != null && !priceString.isEmpty()) {
                    try {
                        double price = Double.parseDouble(priceString);
                        Item newItem;
                        if (selectedMenuType.equals("Food")) {
                            newItem = new Food(name, price);
                        } else {
                            newItem = new Drink(name, price);
                        }
                        menuList.add(newItem);
                        JOptionPane.showMessageDialog(null, "Menu berhasil ditambahkan.");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Harga yang dimasukkan tidak valid.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    // Menghapus menu dari daftar menu
    private void hapusMenu() {
        if (menuList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada menu yang tersedia.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String[] menuNames = new String[menuList.size()];
            for (int i = 0; i < menuList.size(); i++) {
                menuNames[i] = menuList.get(i).getName();
            }

            String selectedMenu = (String) JOptionPane.showInputDialog(null, "Pilih menu yang akan dihapus:",
                    "Hapus Menu", JOptionPane.QUESTION_MESSAGE, null, menuNames, menuNames[0]);

            if (selectedMenu != null) {
                for (Item item : menuList) {
                    if (item.getName().equals(selectedMenu)) {
                        menuList.remove(item);
                        JOptionPane.showMessageDialog(null, "Menu berhasil dihapus.");
                        return;
                    }
                }
            }
        }
    }

    // Melakukan transaksi pembelian
    private void transaksi() {
        if (menuList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada menu yang tersedia.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedItems.clear();
        txtOutput.setText("");
        txtOutput.append("Menu Tersedia:\n");
        for (Item item : menuList) {
            txtOutput.append(item.getName() + " - Rp" + item.getPrice() + "\n");
        }

        boolean isTransactionCanceled = false;
        boolean isTransactionFinished = false;
        do {
            String selectedMenu = JOptionPane.showInputDialog("Pilih menu yang akan dibeli (Ketik 'selesai' untuk mengakhiri, 'cancel' untuk membatalkan):");
            if (selectedMenu == null || selectedMenu.isEmpty()) {
                continue;
            }

            if (selectedMenu.equalsIgnoreCase("selesai")) {
                isTransactionFinished = true;
                break;
            } else if (selectedMenu.equalsIgnoreCase("cancel")) {
                isTransactionCanceled = true;
                break;
            }

            Item selectedItem = findItem(selectedMenu);
            if (selectedItem != null) {
                selectedItems.add(selectedItem);
                txtOutput.append("Menu " + selectedItem.getName() + " ditambahkan.\n");
            } else {
                JOptionPane.showMessageDialog(null, "Menu tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (true);

        if (isTransactionCanceled) {
            txtOutput.append("Transaksi dibatalkan.");
            return;
        }

        if (isTransactionFinished) {
            double totalHarga = calculateTotalHarga();
            if (selectedItems.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Tidak ada menu yang dibeli.", "Transaksi Dibatalkan", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            txtOutput.append("\nTotal Harga: Rp" + totalHarga);

            while (true) {
                String input = JOptionPane.showInputDialog("Masukkan nominal pembayaran (Ketik 'cancel' untuk membatalkan):");
                if (input == null || input.isEmpty()) {
                    continue;
                }

                if (input.equalsIgnoreCase("cancel")) {
                    txtOutput.append("\nTransaksi dibatalkan.");
                    return;
                }

                try {
                    double pembayaran = Double.parseDouble(input);
                    if (pembayaran < totalHarga) {
                        JOptionPane.showMessageDialog(null, "Nominal pembayaran kurang dari total harga.", "Pembayaran Tidak Valid", JOptionPane.ERROR_MESSAGE);
                    } else {
                        double kembalian = pembayaran - totalHarga;
                        txtOutput.append("\nTransaksi berhasil.\nKembalian: Rp" + kembalian);
                        break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Nominal pembayaran tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    // Mencari item berdasarkan nama
    private Item findItem(String name) {
        for (Item item : menuList) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    // Menghitung total harga dari item yang dipilih
    private double calculateTotalHarga() {
        double total = 0;
        for (Item item : selectedItems) {
            total += item.calculateTotal();
        }
        return total;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TransaksiPenjualanGUI();
            }
        });
    }
}