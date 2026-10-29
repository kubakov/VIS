package presentation.view;

import db.GlobalConn;
import db.SQLConnection;
import db.TextConnection;
import domain.model.Item;
import domain.service.ItemService;
import presentation.controller.ItemController;
import presentation.controller.OrderController;
import presentation.model.CreateItemDto;
import presentation.model.CreateOrderDto;
import presentation.model.ItemDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemFrame extends JFrame {

    private final ItemController itemController = new ItemController();
    private final OrderController orderController = new OrderController();
    private final ItemService itemService = new ItemService();

    // Data pro košík
    private final List<Item> cartItems = new ArrayList<>();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("cs", "CZ"));

    // Komponenty pro záložku Položky
    private final JTextField txtName = new JTextField(15);
    private final JTextField txtDesc = new JTextField(15);
    private final JTextField txtPrice = new JTextField(10);
    private final JComboBox<String> sourceCombo = new JComboBox<>(new String[]{"SQL Databáze (H2)", "Textový soubor"});

    private final DefaultTableModel itemTableModel = new DefaultTableModel(
            new Object[]{"ID", "Název", "Popis", "Cena"}, 0
    ) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable itemTable = new JTable(itemTableModel);

    // Komponenty pro záložku Objednávka / Košík
    private final DefaultComboBoxModel<ItemComboWrapper> availableItemsComboModel = new DefaultComboBoxModel<>();
    private final JComboBox<ItemComboWrapper> cbAvailableItems = new JComboBox<>(availableItemsComboModel);

    private final DefaultTableModel cartTableModel = new DefaultTableModel(
            new Object[]{"ID", "Název", "Cena"}, 0
    ) { @Override public boolean isCellEditable(int r, int c) { return false; } };
    private final JTable cartTable = new JTable(cartTableModel);
    private final JLabel lblCartTotal = new JLabel("Celkem: 0,00 Kč");

    // Formulář zákazníka a adresy
    private final JTextField txtFName = new JTextField(12);
    private final JTextField txtLName = new JTextField(12);
    private final JTextField txtEmail = new JTextField(12);
    private final JTextField txtPhone = new JTextField(12);

    private final JTextField txtStreet = new JTextField(12);
    private final JTextField txtHouseNum = new JTextField(6);
    private final JTextField txtCity = new JTextField(12);
    private final JTextField txtPostalCode = new JTextField(6);

    public ItemFrame() {
        super("E-Shop Správa a Objednávky");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 650);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Katalog a položky", createItemsPanel());
        tabbedPane.addTab("Vytvořit objednávku (Košík)", createOrderPanel());

        add(tabbedPane);
        refreshTable();
        refreshAvailableItems();
    }

    private JPanel createItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.add(new JLabel("Aktivní datový zdroj:"));
        topPanel.add(sourceCombo);

        sourceCombo.addActionListener(e -> {
            if (sourceCombo.getSelectedIndex() == 0) {
                GlobalConn.setConnection(new SQLConnection());
            } else {
                GlobalConn.setConnection(new TextConnection());
            }
            refreshTable();
            refreshAvailableItems();
        });
        panel.add(topPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Položky v aktuálním úložišti"));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Nová položka"));

        JPanel formGrid = new JPanel(new GridLayout(3, 2, 8, 8));
        formGrid.add(new JLabel("Název:"));
        formGrid.add(txtName);
        formGrid.add(new JLabel("Popis:"));
        formGrid.add(txtDesc);
        formGrid.add(new JLabel("Cena (Kč):"));
        formGrid.add(txtPrice);

        JButton btnAdd = new JButton("Přidat položku");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
        btnAdd.addActionListener(e -> handleAddItem());

        bottomPanel.add(formGrid, BorderLayout.CENTER);
        bottomPanel.add(btnAdd, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Horní část: Výběr do košíku
        JPanel cartTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        cartTop.add(new JLabel("Položka:"));
        cartTop.add(cbAvailableItems);
        JButton btnAddToCart = new JButton("Přidat do košíku");
        btnAddToCart.addActionListener(e -> handleAddToCart());
        cartTop.add(btnAddToCart);

        JButton btnRemoveCart = new JButton("Odebrat vybranou z košíku");
        btnRemoveCart.addActionListener(e -> handleRemoveFromCart());
        cartTop.add(btnRemoveCart);

        // Střed: Tabulka košíku a souhrn
        JPanel cartCenter = new JPanel(new BorderLayout(5, 5));
        cartCenter.setBorder(BorderFactory.createTitledBorder("Obsah nákupního košíku"));
        cartCenter.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel cartSummaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblCartTotal.setFont(new Font("Arial", Font.BOLD, 14));
        cartSummaryPanel.add(lblCartTotal);
        cartCenter.add(cartSummaryPanel, BorderLayout.SOUTH);

        JPanel upperHalf = new JPanel(new BorderLayout());
        upperHalf.add(cartTop, BorderLayout.NORTH);
        upperHalf.add(cartCenter, BorderLayout.CENTER);

        panel.add(upperHalf, BorderLayout.CENTER);

        // Dolní část: Formulář zákazníka a doručení
        JPanel customerAndAddressPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel custPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        custPanel.setBorder(BorderFactory.createTitledBorder("Údaje zákazníka"));
        custPanel.add(new JLabel("Jméno:"));
        custPanel.add(txtFName);
        custPanel.add(new JLabel("Příjmení:"));
        custPanel.add(txtLName);
        custPanel.add(new JLabel("Email:"));
        custPanel.add(txtEmail);
        custPanel.add(new JLabel("Telefon:"));
        custPanel.add(txtPhone);

        JPanel addrPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        addrPanel.setBorder(BorderFactory.createTitledBorder("Dodací adresa"));
        addrPanel.add(new JLabel("Ulice:"));
        addrPanel.add(txtStreet);
        addrPanel.add(new JLabel("Číslo popisné:"));
        addrPanel.add(txtHouseNum);
        addrPanel.add(new JLabel("Město:"));
        addrPanel.add(txtCity);
        addrPanel.add(new JLabel("PSČ:"));
        addrPanel.add(txtPostalCode);

        customerAndAddressPanel.add(custPanel);
        customerAndAddressPanel.add(addrPanel);

        JButton btnSubmitOrder = new JButton("Odeslat a vytvořit objednávku najednou");
        btnSubmitOrder.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmitOrder.setBackground(new Color(46, 139, 87));
        btnSubmitOrder.setForeground(Color.WHITE);
        btnSubmitOrder.addActionListener(e -> handleSubmitOrder());

        JPanel bottomWrapper = new JPanel(new BorderLayout(10, 10));
        bottomWrapper.add(customerAndAddressPanel, BorderLayout.CENTER);
        bottomWrapper.add(btnSubmitOrder, BorderLayout.SOUTH);

        panel.add(bottomWrapper, BorderLayout.SOUTH);

        return panel;
    }

    private void handleAddToCart() {
        ItemComboWrapper wrapper = (ItemComboWrapper) cbAvailableItems.getSelectedItem();
        if (wrapper == null) return;
        cartItems.add(wrapper.item);
        refreshCartTable();
    }

    private void handleRemoveFromCart() {
        int row = cartTable.getSelectedRow();
        if (row >= 0 && row < cartItems.size()) {
            cartItems.remove(row);
            refreshCartTable();
        } else {
            JOptionPane.showMessageDialog(this, "Vyberte položku v košíku k odebrání.");
        }
    }

    private void refreshCartTable() {
        cartTableModel.setRowCount(0);
        BigDecimal total = BigDecimal.ZERO;
        for (Item item : cartItems) {
            cartTableModel.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    currencyFormat.format(item.getPrice())
            });
            total = total.add(item.getPrice());
        }
        lblCartTotal.setText("Celkem: " + currencyFormat.format(total));
    }

    private void handleSubmitOrder() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Košík je prázdný! Přidejte alespoň jednu položku.", "Chyba", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fName = txtFName.getText().trim();
        String lName = txtLName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        String street = txtStreet.getText().trim();
        String houseNum = txtHouseNum.getText().trim();
        String city = txtCity.getText().trim();
        String pCode = txtPostalCode.getText().trim();

        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || street.isEmpty() || city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vyplňte prosím všechny povinné údaje zákazníka a adresy.", "Chyba validace", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            CreateOrderDto dto = new CreateOrderDto(
                    fName, lName, email, phone,
                    street, city, houseNum, pCode,
                    new ArrayList<>(cartItems)
            );

            // Volání přes controller -> service -> DAO
            orderController.createOrder(dto);

            JOptionPane.showMessageDialog(this, "Objednávka byla úspěšně uložena jako celek!", "Hotovo", JOptionPane.INFORMATION_MESSAGE);

            // Vyčištění formuláře
            cartItems.clear();
            refreshCartTable();
            txtFName.setText("");
            txtLName.setText("");
            txtEmail.setText("");
            txtPhone.setText("");
            txtStreet.setText("");
            txtHouseNum.setText("");
            txtCity.setText("");
            txtPostalCode.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Chyba při vytváření objednávky: " + ex.getMessage(), "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddItem() {
        String name = txtName.getText().trim();
        String desc = txtDesc.getText().trim();
        String priceStr = txtPrice.getText().trim();

        try {
            BigDecimal price = new BigDecimal(priceStr);
            itemController.createItem(new CreateItemDto(name, desc, price));
            txtName.setText("");
            txtDesc.setText("");
            txtPrice.setText("");
            refreshTable();
            refreshAvailableItems();
            JOptionPane.showMessageDialog(this, "Položka uložena!", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Neplatné číslo ceny!", "Chyba", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Chyba", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshTable() {
        itemTableModel.setRowCount(0);
        List<ItemDto> items = itemController.getAllItems();
        for (ItemDto item : items) {
            itemTableModel.addRow(new Object[]{
                    item.getId(),
                    item.getTitle(),
                    item.getDetails(),
                    item.getFormattedPrice()
            });
        }
    }

    private void refreshAvailableItems() {
        availableItemsComboModel.removeAllElements();
        List<Item> items = itemService.getAllItems();
        for (Item item : items) {
            availableItemsComboModel.addElement(new ItemComboWrapper(item));
        }
    }

    private static class ItemComboWrapper {
        final Item item;
        ItemComboWrapper(Item item) { this.item = item; }
        @Override
        public String toString() {
            return item.getName() + " (" + item.getPrice() + " Kč)";
        }
    }
}