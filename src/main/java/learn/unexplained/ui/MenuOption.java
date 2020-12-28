package learn.unexplained.ui;

public enum MenuOption {
    EXIT("Exit"),
    DISPLAY_ALL("Display All Encounters"),
    ADD("Add An Encounter"),
    DISPLAY_BY_TYPE("Show All of an Encounter Type"),
    UPDATE_ENCOUNTER("Update an encounter"),
    DELETE_BY_ID("Delete an encounter via Id");

    private String message;

    MenuOption(String name) {
        this.message = name;
    }

    public String getMessage() {
        return message;
    }
}
