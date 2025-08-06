package xyz.sadiulhakim.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.sadiulhakim.db.DB;
import xyz.sadiulhakim.exception.PersistenceException;
import xyz.sadiulhakim.util.DateUtil;

import java.sql.SQLException;

public class CustomerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepository.class);

    private static final String INSERT_QUERY = """
            insert into customers(name,email,nid,phone,date_of_birth,address_id) values (?,?,?,?,?,?)
            """;

    public void save(Customer customer) {

        try (var connection = DB.getInstance().getConnection(); var statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getNid());
            statement.setString(4, customer.getPhone());
            statement.setString(5, DateUtil.format(customer.getDateOfBirth()));
            statement.setInt(6, customer.getAddressId());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to save customer name {} email {}", customer.getName(), customer.getEmail());
            throw new PersistenceException(e.getMessage());
        }
    }
}
