package ru.otus.atm;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ATMImpl implements ATM {
    private Integer balance = 0;
    private Map<Denomination, Integer> money = new EnumMap<>(Denomination.class);

    @Override
    public void deposit(Denomination denomination, Integer amount) {
        money.merge(denomination, amount, Integer::sum);
        balance += denomination.getValue() * amount;
    }

    @Override
    public Map<Denomination, Integer> withdraw(Integer sum) {
        int sumCopy = sum;

        Map<Denomination, Integer> res = new EnumMap<>(Denomination.class);
        Map<Denomination, Integer> copy = new EnumMap<>(Denomination.class);
        copy.putAll(money);

        for (int i = Denomination.values().length - 1; i >= 0; i--) {
            Denomination denominationKey = Denomination.values()[i];
            int denomination = denominationKey.getValue();
            int amount = sum / denomination;
            Integer amountAvailable = copy.get(denominationKey);
            if (amount > 0 && amountAvailable != null) {
                if (amountAvailable < amount) {
                    amount = amountAvailable;
                }
                sum -= amount * denomination;
                res.put(denominationKey, amount);
                int finalAmount = amount;
                copy.computeIfPresent(denominationKey, (k, v) -> v - finalAmount);
            }
        }

        if (sum > 0) {
            throw new NotEnoughMoneyException();
        }

        money.clear();
        money.putAll(copy);
        balance -= sumCopy;

        return res;
    }

    @Override
    public Map<Denomination, Integer> getBalanceByDenomination() {
        return money.entrySet().stream().filter(e -> e.getValue() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Integer getBalance() {
        return balance;
    }
}
