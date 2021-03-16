package ru.otus.atm;

import java.util.*;
import java.util.stream.Collectors;

public class ATMImpl implements ATM {
    private final Map<Denomination, Integer> money = new TreeMap<>(Comparator.reverseOrder());

    @Override
    public void deposit(Denomination denomination, int amount) {
        money.merge(denomination, amount, Integer::sum);
    }

    @Override
    public Map<Denomination, Integer> withdraw(int sum) {

        Map<Denomination, Integer> res = new EnumMap<>(Denomination.class);
        Map<Denomination, Integer> copy = new EnumMap<>(Denomination.class);
        copy.putAll(money);

        for (Denomination denominationKey : money.keySet()) {
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
            if (sum == 0) break;
        }

        if (sum > 0) {
            throw new NotEnoughMoneyException();
        }

        money.clear();
        money.putAll(copy.entrySet().stream().filter(e -> e.getValue() != 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return res;
    }

    @Override
    public Map<Denomination, Integer> getBalanceByDenomination() {
        return Map.copyOf(money);
    }

    @Override
    public int getBalance() {
        return money.entrySet().stream().reduce(0, (s, e) -> s + (e.getKey().getValue() * e.getValue()), Integer::sum);
    }
}
