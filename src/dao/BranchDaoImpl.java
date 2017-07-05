package dao;

import model.Branch;
import util.StringUtils;
import util.Util;

import java.sql.*;

/**
 * Created by Administrator on 7/5/2017.
 */
public class BranchDaoImpl implements BranchDao {



    private Connection connection = null;
    private static  BranchDaoImpl branchDao;

    public static BranchDaoImpl getInstance(){
        if (branchDao==null){
            branchDao=new BranchDaoImpl();
        }

        return branchDao;
    }

    private boolean connectedToDataBase() throws SQLException, ClassNotFoundException {


        try {
            if (connection == null) {
                Class.forName(Util.dataBaseDriver);
                connection = DriverManager.getConnection(Util.dataBaseURL, Util.dataBaseUser, Util.dataBasePassword);
                return true;
            } else if (connection.isClosed()) {
                connection = null;
                Class.forName(Util.dataBaseDriver);
                connection = DriverManager.getConnection(Util.dataBaseURL, Util.dataBaseUser, Util.dataBasePassword);
                return true;
            } else {
                connection.close();
                connection = null;
                Class.forName(Util.dataBaseDriver);
                connection = DriverManager.getConnection(Util.dataBaseURL, Util.dataBaseUser, Util.dataBasePassword);
                return true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            connection = null;
            return false;
        }
    }


    @Override
    public void getBranchData (Branch branch) {

        Statement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            if (connectedToDataBase()){
                String queryOfGetBranchTelephone= makeQueryOfGetBranchTelephone (branch);
                preparedStatement = connection.createStatement();
                resultSet= preparedStatement.executeQuery(queryOfGetBranchTelephone);

                while (resultSet.next()) {
                   branch.getBranchTelephoneNumbers().add (resultSet.getString("TelphoneNumber"));
                }

                String queryOfGetBranchFaxNumbers= makeQueryOfGetBranchFaxNumbers (branch);
                preparedStatement = connection.createStatement();
                resultSet= preparedStatement.executeQuery(queryOfGetBranchFaxNumbers);

                while (resultSet.next()) {
                    branch.getBranchFaxNumbers ().add (resultSet.getString("FaxNumber"));
                }

            }
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (resultSet != null) {try {resultSet.close();resultSet = null;} catch (Exception var2) {}}
        }

    }

    private String makeQueryOfGetBranchTelephone (Branch branch) {
        String query="Select TelphoneNumber from tblBranchInformation where BranchID="
                +"\""+branch.getBranchCode ()+"\"";

        return query;
    }
    private String makeQueryOfGetBranchFaxNumbers (Branch branch) {
        String query="Select FaxNumber from tblBranchInformation where BranchID="
                +"\""+branch.getBranchCode ()+"\"";
        return query;
    }
}
