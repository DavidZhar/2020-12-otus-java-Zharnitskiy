package homework;


import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private TreeMap<Customer, String> map = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        return new Map.Entry<>() { // Не уверен, что решение красивое, но так работает
            @Override
            public Customer getKey() {
                Customer firstKeyCustomer = map.firstKey();
                return new Customer(firstKeyCustomer.getId(), firstKeyCustomer.getName(), firstKeyCustomer.getScores());
            }

            @Override
            public String getValue() {
                return map.firstEntry().getValue();
            }

            @Override
            public String setValue(String value) {
                return null;
            }
        };
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return map.higherEntry(customer);
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);

    }
}
