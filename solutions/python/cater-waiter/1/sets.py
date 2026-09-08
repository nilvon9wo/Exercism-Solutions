from sets_categories_data import (
    ALCOHOLS,
    KETO,
    PALEO,
    SPECIAL_INGREDIENTS,
    VEGAN,
    VEGETARIAN,
)


def clean_ingredients(dish_name, dish_ingredients):
    return dish_name, set(dish_ingredients)


def check_drinks(drink_name, drink_ingredients):
    if set(drink_ingredients).isdisjoint(ALCOHOLS):
        return f"{drink_name} Mocktail"

    return f"{drink_name} Cocktail"


def categorize_dish(dish_name, dish_ingredients):
    if dish_ingredients <= VEGAN:
        category = "VEGAN"
    elif dish_ingredients <= VEGETARIAN:
        category = "VEGETARIAN"
    elif dish_ingredients <= PALEO:
        category = "PALEO"
    elif dish_ingredients <= KETO:
        category = "KETO"
    else:
        category = "OMNIVORE"

    return f"{dish_name}: {category}"


def tag_special_ingredients(dish):
    dish_name, dish_ingredients = dish

    special_ingredients = set(dish_ingredients).intersection(
        SPECIAL_INGREDIENTS
    )

    return dish_name, special_ingredients


def compile_ingredients(dishes):
    ingredients = set()

    for dish in dishes:
        ingredients.update(dish)

    return ingredients


def separate_appetizers(dishes, appetizers):
    appetizer_set = set(appetizers)

    return list(set(dishes) - appetizer_set)


def singleton_ingredients(dishes, intersections):
    all_ingredients = compile_ingredients(dishes)

    return all_ingredients - intersections