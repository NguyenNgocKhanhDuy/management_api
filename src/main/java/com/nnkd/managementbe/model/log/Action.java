package com.nnkd.managementbe.model.log;

public enum Action {
    MOVE_TASK(" moved Task: "),
    DELETE_TASK(" deleted Task: "),
    ADD_TASK(" added Task: "),
    CHANGE_DEADLINE(" changed the deadline of task: "),
    MARK_SUBTASK(" marked subtask: ");

    private final String description;

    Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
