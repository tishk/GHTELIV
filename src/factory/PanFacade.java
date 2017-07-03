package factory;

import model.Account;
import model.Pan;
import model.Transaction;

/**
 * Created by Hamid on 6/2/2017.
 */
public interface PanFacade {

    public void getBalance(Pan pan);

    public void getTransactions(Pan pan);

    public void hotCard(Pan pan);

    public void smsAlarmSelect(Pan pan);

    public void smsAlarmRegister(Pan pan);

    public void smsAlarmDelete(Pan pan);

    public void billPayment(Pan pan);

    public void followUp(Pan pan);

    public void topUp(Pan pan);

}
