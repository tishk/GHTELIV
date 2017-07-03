package model;

import java.util.List;

/**
 * Created by Hamid on 6/4/2017.
 */
public class Pan {

    private String actionCode;
    private String PanNumber;
    private String messageSequence;
    private String PanType;
    private String balance;
    private String pin;
    private String billID;
    private String PaymentID;
    private String amountOfBill;
    private String referenceCode;
    private String callerID;
    private String requestToSwitch;
    private String responseFromSwitch;
    private String faxCount;
    private String startDateOfFax;
    private String endDateOfFax;
    private String nameAndFamily;
    private String ShebaNumber;
    private String mobileNo;
    private String mobileOperator;
    private String mobileChargeValue;


    private int    kindOfFax;
    private List<Transaction> transactions;



    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getPanNumber() {
        return PanNumber;
    }

    public void setPanNumber(String panNumber) {
        PanNumber = panNumber;
    }

    public String getPanType() {
        return PanType;
    }

    public void setPanType(String panType) {
        PanType = panType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCallerID() {
        return callerID;
    }

    public void setCallerID(String callerID) {
        this.callerID = callerID;
    }

    public String getRequestToSwitch() {
        return requestToSwitch;
    }

    public void setRequestToSwitch(String requestToSwitch) {
        this.requestToSwitch = requestToSwitch;
    }

    public String getResponseFromSwitch() {
        return responseFromSwitch;
    }

    public void setResponseFromSwitch(String responseFromSwitch) {
        this.responseFromSwitch = responseFromSwitch.trim();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getMessageSequence() {
        return messageSequence;
    }

    public void setMessageSequence(String messageSequence) {
        this.messageSequence = messageSequence;
    }

    public String getFaxCount() {
        return faxCount;
    }

    public void setFaxCount(String faxCount) {
        this.faxCount = faxCount;
    }

    public String getStartDateOfFax() {
        return startDateOfFax;
    }

    public void setStartDateOfFax(String startDateOfFax) {
        this.startDateOfFax = startDateOfFax;
    }

    public String getEndDateOfFax() {
        return endDateOfFax;
    }

    public void setEndDateOfFax(String endDateOfFax) {
        this.endDateOfFax = endDateOfFax;
    }


    public int getKindOfFax() {
        return kindOfFax;
    }

    public void setKindOfFax(int kindOfFax) {
        this.kindOfFax = kindOfFax;
    }

    public String getNameAndFamily() {
        return nameAndFamily;
    }

    public void setNameAndFamily(String nameAndFamily) {
        this.nameAndFamily = nameAndFamily;
    }

    public String getShebaNumber() {
        return ShebaNumber;
    }

    public void setShebaNumber(String shebaNumber) {
        ShebaNumber = shebaNumber;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(String paymentID) {
        PaymentID = paymentID;
    }

    public String getAmountOfBill() {
        return amountOfBill;
    }

    public void setAmountOfBill(String amountOfBill) {
        this.amountOfBill = amountOfBill;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getMobileOperator() {
        return mobileOperator;
    }

    public void setMobileOperator(String mobileOperator) {
        this.mobileOperator = mobileOperator;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMobileChargeValue() {
        return mobileChargeValue;
    }

    public void setMobileChargeValue(String mobileChargeValue) {
        this.mobileChargeValue = mobileChargeValue;
    }
}
