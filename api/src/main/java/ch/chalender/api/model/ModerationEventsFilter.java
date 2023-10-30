package ch.chalender.api.model;

import lombok.Data;

@Data
public class ModerationEventsFilter {
    String searchTerm;
    DatesDisplay dates;

    boolean includeStateInReview;
    boolean includeStateNewModification;
    boolean includeStatePublished;
    boolean includeStateRejected;
    boolean includeStateInvalid;

    SortBy sortBy;
    SortOrder sortOrder;

    public enum DatesDisplay {
        ALL("all"),
        FUTURE("future"),
        PAST("past");

        private DatesDisplay(String value) {
        }
    }

    public enum SortBy {
        DATE("date"),
        USER("user"),
        TITLE("title"),
        STATE("state");

        private SortBy(String value) {
        }
    }

    public enum SortOrder {
        ASC("asc"),
        DESC("desc");

        private SortOrder(String value) {
        }
    }
}
