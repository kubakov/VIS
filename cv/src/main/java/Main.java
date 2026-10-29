import db.DatabaseConnection;
import db.GlobalConn;
import db.SQLConnection;
import presentation.controller.OrderController;
import presentation.model.OrderDetailDto;
import presentation.view.ItemFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.startWebConsole();

        GlobalConn.setConnection(new SQLConnection());

        OrderController orderController = new OrderController();

        OrderDetailDto dto = orderController.getOrderDetails(1L)
                .orElseThrow(() -> new RuntimeException("Objednávka nenalezena!"));

        System.out.println("Objednávka č.: " + dto.getOrderId());
        System.out.println("Zákazník:      " + dto.getCustomerName() + " (" + dto.getCustomerContact() + ")");
        System.out.println("Adresa:        " + dto.getAddress());
        System.out.println("Položky:");
        dto.getItemSummaries().forEach(i -> System.out.println("  • " + i));
        System.out.println("Cena celkem:   " + dto.getTotalPrice());

        SwingUtilities.invokeLater(() -> {
            ItemFrame frame = new ItemFrame();
            frame.setVisible(true);
        });
    }
}