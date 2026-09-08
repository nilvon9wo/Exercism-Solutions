from enum import IntFlag
from handshake_action_type import HandshakeActionType

# noinspection bad-assignment
class HandshakeAction(IntFlag):
    WINK = 1, "wink", HandshakeActionType.OUTPUT
    DOUBLE_BLINK = 2, "double blink", HandshakeActionType.OUTPUT
    CLOSE_YOUR_EYES = 4, "close your eyes", HandshakeActionType.OUTPUT
    JUMP = 8, "jump", HandshakeActionType.OUTPUT
    REVERSE = 16, "reverse", HandshakeActionType.MODIFIER

    description: str
    is_output: bool

    # noinspection unresolved-references
    def __new__(cls, value, description, output):
        member = int.__new__(cls, value)
        member._value_ = value
        member.description = description
        member.is_output = output == HandshakeActionType.OUTPUT
        return member

