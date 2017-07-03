package factory;

import model.Account;

/**
 * Created by Hamid on 6/2/2017.
 */
public interface AccountFacade {

    public void getBalance(Account account);

    public void getTransactions(Account account) ;

    public void fundTransfer(Account account) ;

    public void changePin(Account account);

    public void getLoanStatus (Account account);

    public void smsAlarmSelect(Account account);

    public void smsAlarmRegister(Account account);

    public void smsAlarmDelete(Account account);

    public void getTransactionOfPOS(Account account);

    public void getChequeStatus (Account account);

    public void getIBAN(Account account);



}
