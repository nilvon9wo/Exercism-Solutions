from handshake_action import HandshakeAction

def commands(binary):
    handshake = HandshakeAction(int(binary, 2))
    actions = []

    for action in HandshakeAction:
        if action.is_output and handshake & action:
            actions.append(action.description)

    if handshake & HandshakeAction.REVERSE:
        actions.reverse()

    return actions