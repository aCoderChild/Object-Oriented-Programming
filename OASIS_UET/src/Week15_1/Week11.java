import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Week11 {
    public static <T extends Comparable<T>> List<T> sortGeneric(List<T> list) {
        Collections.sort(list);
        return list;
    }
}