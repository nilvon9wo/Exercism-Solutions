from typing import Any


type Cart = dict[str, int]
type Recipe = list[str]
type RecipeIdeas = dict[str, Recipe]
type RecipeUpdate = tuple[str, Recipe]
type RecipeUpdates = list[RecipeUpdate]

type AisleMapping = dict[str, tuple[str, bool]]
type FulfillmentDetails = list[Any]
type FulfillmentCart = dict[str, FulfillmentDetails]

type StoreInventoryDetails = list[Any]
type StoreInventory = dict[str, StoreInventoryDetails]

OUT_OF_STOCK_MESSAGE = "Out of Stock"

def add_item(current_cart: Cart, items_to_add: list[str]) -> Cart:
    for item in items_to_add:
        current_cart[item] = current_cart.get(item, 0) + 1

    return current_cart


def read_notes(notes: list[str]) -> Cart:
    return {item: 1 for item in notes}


def update_recipes(ideas: RecipeIdeas, recipe_updates: RecipeUpdates) -> RecipeIdeas:
    for recipe_name, recipe in recipe_updates:
        ideas[recipe_name] = recipe

    return ideas


def sort_entries(cart: Cart) -> Cart:
    return dict(sorted(cart.items()))


def send_to_store(cart: Cart, aisle_mapping: AisleMapping) -> FulfillmentCart:
    fulfillment_cart = {}

    for item in sorted(cart, reverse=True):
        fulfillment_cart[item] = create_fulfillment_details(cart, aisle_mapping, item)

    return fulfillment_cart


def create_fulfillment_details(cart: Cart, aisle_mapping: AisleMapping, item: str,) -> FulfillmentDetails:
    quantity = cart[item]
    aisle, requires_refrigeration = aisle_mapping[item]
    return [quantity, aisle, requires_refrigeration]


def update_store_inventory(fulfillment_cart: FulfillmentCart, store_inventory: StoreInventory) -> StoreInventory:
    for item, fulfillment_details in fulfillment_cart.items():
        store_inventory[item][0] = _get_inventory_status(fulfillment_details, item, store_inventory)

    return store_inventory


def _get_inventory_status(fulfillment_details: list[Any], item: str, store_inventory: StoreInventory) -> str:
    ordered_quantity = fulfillment_details[0]
    available_quantity = store_inventory[item][0]
    remaining_quantity = available_quantity - ordered_quantity

    if remaining_quantity <= 0:
        return OUT_OF_STOCK_MESSAGE

    return remaining_quantity