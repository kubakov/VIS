package uow;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLUnitOfWorkFactory implements IUnitOfWorkFactory {

    @Override
    public IUnitOfWork createUnitOfWork() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            return new SQLUnitOfWork(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Nepodařilo se vytvořit SQL UnitOfWork", e);
        }
    }
}