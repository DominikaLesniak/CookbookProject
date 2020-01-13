package com.project.cookbook.constants;

import lombok.Getter;

@Getter
public enum PointedActions {
    ADD_RECIPE(100),
    ADD_RATING(10),
    GET_RATING(5),
    ADD_COMMENT(20),
    GET_COMMENT(10);

    private long points;

    PointedActions(long points) {
        this.points = points;
    }
}
