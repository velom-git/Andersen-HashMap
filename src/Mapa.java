import java.util.*;

public class Mapa<K, V> implements Iterable<V> {
    private Node<K, V>[] hashTable;
    private int size;
    private float threshold;
    private int sizeTable = 16;
    private float loadFactor = 0.75f;

    public Mapa() {
        this.hashTable = new Node[sizeTable];
        this.threshold = hashTable.length * loadFactor;
    }

    public Mapa(int sizeTable, float loadFactor) {
        this.hashTable = new Node[sizeTable];
        this.threshold = hashTable.length * loadFactor;
    }

    public boolean put(K key, V value) {
        if (size + 1 >= threshold) {
            threshold *= 2;
            increaseArray();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = newNode.hash();
        if (hashTable[index] == null) {
            return add(index, newNode);
        }
        List<Node<K, V>> nodeList = hashTable[index].getNodes();

        for (Node<K, V> node : nodeList) {
            if (keyExist(node, newNode, value) || collision(node, newNode, nodeList)) {
                return true;
            }
        }
        return false;
    }

    private boolean add(int index, Node<K, V> newNode) {
        hashTable[index] = new Node<K, V>(null, null);
        hashTable[index].getNodes().add(newNode);
        size++;
        return true;
    }

    private boolean keyExist(Node<K, V> nodeFromList, Node<K, V> newNode, V value) {
        if (newNode.getKey().equals(nodeFromList.getKey()) && !newNode.getValue().equals(nodeFromList.getValue())) {
            nodeFromList.setValue(value);
            return true;
        }
        return false;
    }

    private boolean collision(Node<K, V> nodeFromList, Node<K, V> newNode, List<Node<K, V>> nodes) {
        if (newNode.hashCode() == nodeFromList.hashCode() && !Objects.equals(newNode.key, nodeFromList.key) && !Objects.equals(newNode.value, nodeFromList.value)) {
            nodes.add(newNode);
            size++;
            return true;
        }
        return false;
    }

    private void increaseArray() {
        Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[oldHashTable.length * 2];
        size = 0;
        for (Node<K, V> node : oldHashTable) {
            if (node != null) {
                for (Node<K, V> n : node.getNodes()) {
                    put(n.key, n.value);
                }
            }
        }
    }


    public boolean remove(K key) {
        int index = hash(key);
        if (hashTable[index] == null) {
            return false;
        }
        if (hashTable[index].getNodes().size() == 1) {
            hashTable[index].getNodes().remove(0);
            size--;
            return true;
        }
        List<Node<K, V>> nodeList = hashTable[index].getNodes();
        for (Node<K, V> node : nodeList) {
            if (key.equals(node.getKey())) {
                nodeList.remove(node);
                return true;
            }
        }
        return false;
    }

    public void clear() {
        if (hashTable != null && size > 0) {
            size = 0;
            for (int i = 0; i < hashTable.length; ++i)
                hashTable[i] = null;
        }
    }

    public V get(K key) {
        int index = hash(key);
        if (index < hashTable.length && hashTable[index] != null) {
            if (hashTable[index].getNodes().size() == 1) {
                return hashTable[index].getNodes().get(0).getValue();
            }
            List<Node<K, V>> list = hashTable[index].getNodes();
            for (Node<K, V> node : list) {
                if (key.equals(node.getKey())) {
                    return node.getValue();
                }
            }
        }
        return null;
    }


    public int size() {
        return size;
    }

    public int hash(K key) {
        int hash = 31;
        hash = hash * 17 + key.hashCode();
        return hash % (hashTable.length-1);
    }

    public boolean isEmpty() {
        return 0 == size;
    }


    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            int counterCells = 0;  // кол-во ячеек
            int counterNext = 0;  // кол-во значений
            Iterator<Node<K, V>> subIterator = null;

            @Override
            public boolean hasNext() {
                if (counterNext == size) { // закончить перебор когда все ячейки пройдены
                    return false;
                }

                if (subIterator == null || !subIterator.hasNext()) {
                    if (moveToNextCell()) {
                        subIterator = hashTable[counterCells].getNodes().iterator();
                    } else {
                        return false;
                    }
                }

                return subIterator.hasNext();
            }

            private boolean moveToNextCell() {
                counterCells++;
                while (counterCells < hashTable.length && hashTable[counterCells] == null) { // проходим по всем ячейкам пропуская пустые
                    counterCells++;
                }
                return counterCells < hashTable.length && hashTable[counterCells] != null;
            }

            @Override
            public V next() {
                counterNext++;
                return subIterator.next().getValue();
            }
        };
    }

    public class Node<K, V> {
        private K key;
        private V value;
        private List<Node<K, V>> nodes;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            nodes = new LinkedList<>();
        }

        private List<Node<K, V>> getNodes() {
            return nodes;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        private int hash() {
            return hashCode() % (hashTable.length-1);
        }

        public int hashCode() {
            int hash = 31;
            hash = hash * 17 + key.hashCode();
            return hash;
        }

        public String toString(){
            return key + " "+ value;
        }

        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Node) {
                Node<K, V> e = (Node<K, V>) o;
                return Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue());
            }
            return false;
        }
    }
}
