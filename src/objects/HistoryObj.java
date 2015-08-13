package objects;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by rober on 13.08.2015.
 */
public class HistoryObj {
    private final SimpleStringProperty id;
    private final SimpleStringProperty dateStart;
    private final SimpleStringProperty dateEnd;
    private final SimpleStringProperty timeElapsed;
    private final SimpleStringProperty annotations;

    public HistoryObj(String id, String dateStart, String dateEnd, String timeElapsed, String annotations) {
        this.id = new SimpleStringProperty(id);
        this.dateStart = new SimpleStringProperty(dateStart);
        this.dateEnd = new SimpleStringProperty(dateEnd);
        this.timeElapsed = new SimpleStringProperty(timeElapsed);
        this.annotations = new SimpleStringProperty(annotations);
    }

    public String getAnnotations() {
        return annotations.get();
    }

    public String getId() {
        return id.get();
    }

    public String getDateEnd() {
        return dateEnd.get();
    }

    public String getDateStart() {
        return dateStart.get();
    }

    public String getTimeElapsed() {
        return timeElapsed.get();
    }
}
