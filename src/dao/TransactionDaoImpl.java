package dao;

import model.Account;
import model.Transaction;
import model.TransactionPOS;
import util.StringUtils;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Created by Hamid on 6/5/2017.
 */
public class TransactionDaoImpl implements TransactionDao {

    private static TransactionDaoImpl transactionDao;
    private StringUtils stringUtils=new StringUtils();
    private Connection connection = null;



    public static TransactionDaoImpl getInstance(){
        if (transactionDao==null){
            transactionDao=new TransactionDaoImpl();
        }

        return transactionDao;
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
    public void getTransactionOfPos(Account account) {
        Statement preparedStatement=null;
                ResultSet resultSet=null;
        try {
            if (connectedToDataBase()){
                String query=makeQueryOfTransactions(account);
                preparedStatement = connection.createStatement();
                resultSet= preparedStatement.executeQuery(query);
                List<TransactionPOS> transactionsPos=new ArrayList<TransactionPOS>();
                TransactionPOS transactionPOS;
                int row=1;
                while (resultSet.next()) {
                     transactionPOS=new TransactionPOS();
                     transactionPOS.setProcessType(resultSet.getString("proccess_type"));
                     transactionPOS.setReferenceCode(resultSet.getString("reference_code"));
                     transactionPOS.setAmountShaparak(resultSet.getString("amount_shaparak"));
                     transactionPOS.setTerminalType(resultSet.getString("terminal_type"));
                     transactionPOS.setDepositeCircle(resultSet.getString("deposite_circle_number"));
                     transactionPOS.setDepositeDate(resultSet.getString("deposite_date"));
                     transactionPOS.setLocalDate(resultSet.getString("local_time"));
                     transactionPOS.setLocalTime(resultSet.getString("local_date"));
                     transactionPOS.setTraceCode(resultSet.getString("trace_code"));
                     transactionPOS.setTerminalCode(resultSet.getString("terminal_code"));
                     transactionPOS.setRow(String.valueOf(row));
                     transactionsPos.add(transactionPOS);
                     row++;
                }
                account.setTransactionsPOS(transactionsPos);
            }
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (resultSet != null) {try {resultSet.close();resultSet = null;} catch (Exception var2) {}}
        }
    }

    private String makeQueryOfTransactions(Account account) {
        String param="";
        if (account.getKindOfPOSTransaction().equals("2")) param=getDataBaeFromatDateTime(account);
        String query="select top 30 proccess_type, reference_code, amount_shaparak, terminal_type, " +
                      "deposite_circle_number, deposite_date, local_time, local_date, trace_code, " +
                      "terminal_code from tblTransactionPOS where right(IBAN, 15) = "+
                      "\""+account.getAccountNumber()+"\""+param+
                      " order by local_date, local_time desc";
        return query;
    }

    private String getDataBaeFromatDateTime(Account account) {

        String startDate=stringUtils.leftString(account.getStartDateOfFax(),4)+"/"+
                stringUtils.midString(account.getStartDateOfFax(),5,2)+"/"+
                stringUtils.rightString(account.getStartDateOfFax(),2);
        String endDate=stringUtils.leftString(account.getEndDateOfFax(),4)+"/"+
                stringUtils.midString(account.getEndDateOfFax(),5,2)+"/"+
                stringUtils.rightString(account.getEndDateOfFax(),2);
        Formatter formatter=new Formatter();
        String result=" and local_date >= %1$s and local_date <= %2$s";
        return formatter.format(result,"\""+startDate+"\"","\""+endDate+"\"").toString();


    }
}
