import java.util.HashMap;

/**
 * Created by steve02 on 2017/3/14.
 */
public class LRUCache {
    class Node{
        int key;
        String code;
        Node pre;
        Node next;

        public Node(int key, String code){
            this.key = key;
            this.code = code;
        }
    }
    int capacity;
    HashMap<Integer, Node> map = new HashMap<Integer, Node>();
    Node head=null;
    Node end=null;

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    public String get(int key) {
        if(map.containsKey(key)){
            Node n = map.get(key);
            remove(n);
            setHead(n);
            return head.code;
        }
        return "no such key";
       // return -1;
    }

    public void remove(Node n){
        if(n.pre!=null){
            n.pre.next = n.next;
        }else{
            head = n.next;
        }

        if(n.next!=null){
            n.next.pre = n.pre;
        }else{
            end = n.pre;
        }

    }

    public void setHead(Node n){
        n.next = head;
        n.pre = null;

        if(head!=null)
            head.pre = n;

        head = n;

        if(end ==null)
            end = head;
    }

    public void set(int key, String code) {
        if(map.containsKey(key)){
            Node old = map.get(key);
            old.code = code;
            remove(old);
            setHead(old);
        }else{
            Node created = new Node(key, code);
            if(map.size()>=capacity){
                map.remove(end.key);
                remove(end);
                setHead(created);

            }else{
                setHead(created);
            }

            map.put(key, created);
        }
    }

}
