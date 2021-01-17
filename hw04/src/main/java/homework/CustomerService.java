package homework;


import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> map = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        return new MyEntry<>();
    }

    final class MyEntry<K, V> implements Map.Entry<K, V> {

        @Override
        public K getKey() {
            Customer firstKeyCustomer = map.firstKey();
            return (K) new Customer(firstKeyCustomer.getId(), firstKeyCustomer.getName(), firstKeyCustomer.getScores());
        }

        @Override
        public V getValue() {
            return (V) map.firstEntry().getValue();
        }

        @Override
        public Object setValue(Object value) {
            return null;
        }
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return map.higherEntry(customer);
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);

    }
}
