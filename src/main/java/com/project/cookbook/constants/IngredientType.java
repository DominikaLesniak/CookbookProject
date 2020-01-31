package com.project.cookbook.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IngredientType {
    MEAT(true, false, false),
    VEGETABLE(true, true, true),
    FRUIT(true, true, true),
    DAIRY(true, true, false),
    GRAIN(true, true, true),
    VEGETABLE_OIL(true, true, true),
    OTHER_ANIMAL_PRODUCT(true, true, false),
    OTHER(true, true, true);

    public boolean isRegular;
    public boolean isVegetarian;
    public boolean isVegan;
}
