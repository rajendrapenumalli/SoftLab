package getway;

import dal.Customer;
import database.DBConnection;
import database.SQL;
import javafx.scene.control.Alert;
import list.ListCustomer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author HikmatD
 */
public class CustomerGetway {

    SQL sql = new SQL();
    DBConnection dbCon = new DBConnection();
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    public void save(Customer customer) {
        con = dbCon.geConnection();
        try {
            pst = con.prepareStatement("insert into Customer values(?,?,?,?,?,?,?)");
            pst.setString(1, null);
            pst.setString(2, customer.customerName);
            pst.setString(3, customer.customerConNo);
            pst.setString(4, customer.customerAddress);
            pst.setString(5, customer.totalBuy);
            pst.setString(6, customer.userId);
            pst.setString(7, LocalDateTime.now().toString());
            pst.executeUpdate();
            pst.close();
            con.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Customer" + customer.customerName + "Added sucessfuly");

            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void view(Customer customer) {
        con = dbCon.geConnection();
        try {
            pst = con.prepareStatement("select * from Customer");
            rs = pst.executeQuery();
            while (rs.next()) {
                customer.id = rs.getString(1);
                customer.customerName = rs.getString(2);
                customer.customerConNo = rs.getString(3);
                customer.customerAddress = rs.getString(4);
                customer.totalBuy = rs.getString(5);
                customer.userId = rs.getString(6);
                customer.date = rs.getString(7);
                customer.userName = sql.getName(customer.userId, customer.userName, "User");
                customer.customerList.addAll(new ListCustomer(customer.id, customer.customerName, customer.customerConNo, customer.customerAddress, customer.totalBuy, customer.userName, customer.date));
            }
            rs.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectedView(Customer customer) {
        con = dbCon.geConnection();
        try {
            pst = con.prepareStatement("select * from Customer where Id=?");
            pst.setString(1, customer.id);
            rs = pst.executeQuery();
            while (rs.next()) {
                customer.customerName = rs.getString(2);
                customer.customerConNo = rs.getString(3);
                customer.customerAddress = rs.getString(4);
                customer.totalBuy = rs.getString(5);
            }
            rs.close();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchView(Customer customer) {
        con = dbCon.geConnection();
        customer.customerList.clear();
        try {
            pst = con.prepareStatement("select * from Customer where CustomerName like ? or CustomerContNo like ?");
            pst.setString(1, "%" + customer.customerName + "%");
            pst.setString(2, "%" + customer.customerName + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                customer.id = rs.getString(1);
                customer.customerName = rs.getString(2);
                customer.customerConNo = rs.getString(3);
                customer.customerAddress = rs.getString(4);
                customer.totalBuy = rs.getString(5);
                customer.userId = rs.getString(6);
                customer.date = rs.getString(7);
                customer.userName = sql.getName(customer.userId, customer.userName, "User");
                customer.customerList.addAll(new ListCustomer(customer.id, customer.customerName, customer.customerConNo, customer.customerAddress, customer.totalBuy, customer.userName, customer.date));
            }
            rs.close();
            pst.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update(Customer customer) {
        con = dbCon.geConnection();
        try {
            pst = con.prepareStatement("UPDATE Customer set CustomerName=?,CustomerContNo=?,CustomerAddress=? where Id=?");
            pst.setString(1, customer.customerName);
            pst.setString(2, customer.customerConNo);
            pst.setString(3, customer.customerAddress);
            pst.setString(4, customer.id);
            pst.executeUpdate();
            pst.close();
            con.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Update Success");

            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Customer customer) {
        con = dbCon.geConnection();
        try {
            pst = con.prepareStatement("delete from Customer where id=?");
            pst.setString(1, customer.id);
            pst.executeUpdate();
            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isNotUsed(Customer customer) {
        con = dbCon.geConnection();
        boolean isNotUsed = false;
        try {
            pst = con.prepareStatement("select * from Sells where CustomerId=?");
            pst.setString(1, customer.id);
            rs = pst.executeQuery();
            while (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("This Customer use in sell'" + rs.getString(2) + "' brand \n delete Customer first");

                alert.showAndWait();

                return isNotUsed;
            }
            rs.close();
            pst.close();
            con.close();
            isNotUsed = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isNotUsed;
    }

}
