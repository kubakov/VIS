import data.model.CustomerEntity;
import data.model.OrderEntity;
import db.DatabaseConnection;
import db.GlobalConn;
import db.SQLConnection;
import presentation.controller.OrderController;
import presentation.model.OrderDetailDto;
import presentation.view.ItemFrame;
import uow.IUnitOfWork;
import uow.SQLUnitOfWorkFactory;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.startWebConsole();

        GlobalConn.setFactory(new SQLUnitOfWorkFactory());

        try (IUnitOfWork uow = GlobalConn.getFactory().createUnitOfWork()) {
            OrderEntity o1 = uow.getOrderDao().getOrderById(1L).orElseThrow();
            OrderEntity o2 = uow.getOrderDao().getOrderById(1L).orElseThrow();
            CustomerEntity c1 = uow.getCustomerDao().getById(o1.getCustomer().getId()).orElseThrow();

            System.out.println("Stejná instance objednávky: " + (o1 == o2));
            System.out.println("Stejná instance zákazníka:  " + (o1.getCustomer() == c1));
        }


        try (IUnitOfWork uow2 = GlobalConn.getFactory().createUnitOfWork()) {
            OrderEntity o3 = uow2.getOrderDao().getOrderById(1L).orElseThrow();
            System.out.println("Nový UoW vytvořil novou mapu (o3 načteno znovu): " + (o3.getId() == 1L));
        }

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