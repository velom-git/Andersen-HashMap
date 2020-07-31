import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        Mapa<String,Integer> map = new Mapa<>(16,0.75f);
        map.put("dva",2);
        map.put("tri",3);
        map.forEach(x-> System.out.print(x+" "));

        System.out.println();
        System.out.println("Map size " + map.size());

        map.clear();
        System.out.println("Empty? "+ map.isEmpty());

        map.put("odin",1);
        map.put("chetire",4);
        map.put("piat",5);
        map.forEach(x-> System.out.print(x+" "));

        System.out.println();
        System.out.println("Map size " + map.size());

        map.remove("odin");
        System.out.println(map.get("odin"));

        System.out.println("Map size " + map.size());

        System.out.println(map.get("piat"));

    }
}
