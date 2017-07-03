package model;

import java.util.List;

/**
 * Created by Hamid on 6/4/2017.
 */
public class Account {

   private String actionCode="";
   private String accountNumber="";
   private String IBAN="";
   private String messageSequence="";
   private String accountType="";
   private String balance="";
   private String pin="";
   private String newPin="";
   private String callerID="";
   private String requestToSwitch="";
   private String responseFromSwitch="";
   private String startDateOfFax="";
   private String endDateOfFax="";
   private String destinationAccount="";
   private String amountOfTransfer="";
   private String transferType="";
   private String referenceCode="";
   private String balanceOfLoanDebt ="";
   private String loanAmount ="";
   private String loanDate ="";
   private String finalResult="";
   private String mobileNumber="";
   private String kindOfPOSTransaction="";
   private String chequeSerialNumber ="";
   private String chequeStatus ="";
   private String chequeDate ="";
   private String chequeAmount ="";
   private String chequeBenifit ="";
   private String chequeStatusCode ="";
   private char   chequeStatusCodeChar ;

   private String nameAndFamily ="";
   private String ShetabNumber="";
   private List<Transaction> transactions;
   private List<TransactionPOS> transactionsPOS;
   private int kindOfFax;
   private int faxCount;



    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMessageSequence() {
        return messageSequence;
    }

    public void setMessageSequence(String messageSequence) {
        this.messageSequence = messageSequence;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getKindOfFax() {
        return kindOfFax;
    }

    public void setKindOfFax(int kindOfFax) {
        this.kindOfFax = kindOfFax;
    }

    public int getFaxCount() {
        return faxCount;
    }

    public void setFaxCount(int faxCount) {
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

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getAmountOfTransfer() {
        return amountOfTransfer;
    }

    public void setAmountOfTransfer(String amountOfTransfer) {
        this.amountOfTransfer = amountOfTransfer;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }

    public String getBalanceOfLoanDebt () {
        return balanceOfLoanDebt;
    }

    public void setBalanceOfLoanDebt (String balanceOfLoanDebt) {
        this.balanceOfLoanDebt = balanceOfLoanDebt;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<TransactionPOS> getTransactionsPOS() {
        return transactionsPOS;
    }

    public void setTransactionsPOS(List<TransactionPOS> transactionsPOS) {
        this.transactionsPOS = transactionsPOS;
    }

    public String getKindOfPOSTransaction() {
        return kindOfPOSTransaction;
    }

    public void setKindOfPOSTransaction(String kindOfPOSTransaction) {
        this.kindOfPOSTransaction = kindOfPOSTransaction;
    }

    public String getChequeSerialNumber () {
        return chequeSerialNumber;
    }

    public void setChequeSerialNumber (String chequeSerialNumber) {
        this.chequeSerialNumber = chequeSerialNumber;
    }

    public String getChequeStatus () {
        return chequeStatus;
    }

    public void setChequeStatus (String chequeStatus) {
        this.chequeStatus = chequeStatus;
    }

    public String getChequeDate () {
        return chequeDate;
    }

    public void setChequeDate (String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getChequeAmount () {
        return chequeAmount;
    }

    public void setChequeAmount (String chequeAmount) {
        this.chequeAmount = chequeAmount;
    }

    public String getChequeBenifit () {
        return chequeBenifit;
    }

    public void setChequeBenifit (String chequeBenifit) {
        this.chequeBenifit = chequeBenifit;
    }

    public String getChequeStatusCode () {
        return chequeStatusCode;
    }

    public void setChequeStatusCode (String chequeStatusCode) {
        this.chequeStatusCode = chequeStatusCode;
    }

    public char getChequeStatusCodeChar () {
        return chequeStatusCodeChar;
    }

    public void setChequeStatusCodeChar (char chequeStatusCodeChar) {
        this.chequeStatusCodeChar = chequeStatusCodeChar;
    }

    public String getNameAndFamily () {
        return nameAndFamily;
    }

    public void setNameAndFamily (String nameAndFamily) {
        this.nameAndFamily = nameAndFamily;
    }

    public String getShetabNumber() {
        return ShetabNumber;
    }

    public void setShetabNumber(String shetabNumber) {
        ShetabNumber = shetabNumber;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getLoanAmount () {
        return loanAmount;
    }

    public void setLoanAmount (String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanDate () {
        return loanDate;
    }

    public void setLoanDate (String loanDate) {
        this.loanDate = loanDate;
    }
}
