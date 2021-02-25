package ru.otus.atm;

import java.util.Map;

public interface ATM {
    void deposit(Denomination denomination, Integer amount);

    Map<Denomination, Integer> withdraw(Integer sum);

    Map<Denomination, Integer> getBalanceByDenomination();

    Integer getBalance();

}
