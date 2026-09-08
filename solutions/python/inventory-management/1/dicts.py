def create_inventory(items):
    inventory = {}

    for item in items:
        inventory[item] = inventory.get(item, 0) + 1

    return inventory


def add_items(inventory, items):
    for item in items:
        inventory[item] = inventory.get(item, 0) + 1

    return inventory


def decrement_items(inventory, items):
    for item in items:
        if item in inventory:
            inventory[item] = max(inventory[item] - 1, 0)

    return inventory


def remove_item(inventory, item):
    inventory.pop(item, None)

    return inventory


def list_inventory(inventory):
    return [
        (item, quantity)
        for item, quantity in inventory.items()
        if quantity > 0
    ]