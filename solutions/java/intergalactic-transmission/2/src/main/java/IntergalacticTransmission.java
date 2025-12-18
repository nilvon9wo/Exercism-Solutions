import java.util.*;

public class IntergalacticTransmission {
    private final TransmissionEncoder encoder;
    private final TransmissionDecoder decoder;
    private IntergalacticTransmission(TransmissionEncoder encoder, TransmissionDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    private IntergalacticTransmission() {
        this(new TransmissionEncoder(), new TransmissionDecoder());
    }

    private static IntergalacticTransmission singletonTransmission;
    private static IntergalacticTransmission getSingletonTransmission() {
        if (singletonTransmission == null) {
            singletonTransmission = new IntergalacticTransmission();
        }

        return singletonTransmission;
    }

    public static List<Integer> getTransmitSequence(List<Integer> messageBits) {
        return getSingletonTransmission()
                .encoder
                .getEncodedSequence(new RawMessage(messageBits));
    }

    public static List<Integer> decodeSequence(List<Integer> encodedSequence) {
        return getSingletonTransmission()
                .decoder
                .getDecodedSequence(new EncodedSequence(encodedSequence));
    }
}
