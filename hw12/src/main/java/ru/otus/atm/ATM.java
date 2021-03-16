package ru.otus.atm;

import java.util.Map;

public interface ATM {
    void deposit(Denomination denomination, int amount);

    Map<Denomination, Integer> withdraw(int sum);

    Map<Denomination, Integer> getBalanceByDenomination();

    int getBalance();

}
