package tcss450.uw.edu.phishapp.SetLists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample location for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<SetList> SET_LISTS = new ArrayList<SetList>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SetList> SET_LIST_MAP = new HashMap<String, SetList>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(SetList item) {
        SET_LISTS.add(item);
        SET_LIST_MAP.put(item.longDate, item);
    }

    private static SetList createDummyItem(int position) {
        return new SetList(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore venue information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of location.
     */
    public static class SetList {
        public final String longDate;
        public final String location;
        public final String venue;

        public SetList(String longDate, String location, String venue) {
            this.longDate = longDate;
            this.location = location;
            this.venue = venue;
        }

        @Override
        public String toString() {
            return location;
        }
    }
}
