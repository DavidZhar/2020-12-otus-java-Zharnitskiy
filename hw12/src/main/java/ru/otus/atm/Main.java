package ru.otus.atm;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATMImpl();

        try {
            System.out.println(atm.getBalance());
            atm.deposit(Denomination.m10, 10);
            atm.deposit(Denomination.m100, 10);
            atm.deposit(Denomination.m500, 10);
            atm.deposit(Denomination.m1000, 10);
            System.out.println(atm.getBalance());
            System.out.println(atm.withdraw(1000));
            System.out.println(atm.getBalance());
            System.out.println(atm.withdraw(13750));
            System.out.println(atm.getBalance());
            System.out.println(atm.withdraw(2000));
            System.out.println(atm.getBalance());
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(atm.getBalanceByDenomination());
    }
}
