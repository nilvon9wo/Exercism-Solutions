import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Camicia {
    static CamiciaResult simulateGame(List<String> playerA, List<String> playerB) {
        return new Camicia()
                .simulate(playerA, playerB);
    }

    public CamiciaResult simulate(List<String> playerA, List<String> playerB) {
        GameState state = new GameState(playerA, playerB);
        GameStatus status;
        do {
            status = this.playTrick(state);
        } while (status == GameStatus.Playing);

        return new CamiciaResult(
                status.toString(),
                state.cardsPlayed,
                state.tricks
        );
    }

    private GameStatus playTrick(GameState state) {
        if (this.isLoop(state)) {
            return GameStatus.Loop;
        }

        state.tricks++;
        int paymentDue = 0;

        do {
            Card card = this.drawCard(state);

            if (card != null) {
                paymentDue = this.resolveInteraction(state, card, paymentDue);
            }
            else {
                this.assignWinToOtherPlayerOnIncompletePayment(state);
            }
        } while (!state.pile.isEmpty());

        return this.determineGameStatus(state);
    }

    private boolean isLoop(GameState state) {
        return !state.history.add(hashDecks(state));
    }

    private int hashDecks(GameState state) {
        int result = 1;
        result = 31 * result + state.deckA.encodePayments().hashCode();
        result = 31 * result + state.deckB.encodePayments().hashCode();
        return result;
    }

    private Card drawCard(GameState state) {
        Optional<Card> optionalCard = state.decks()[state.turn]
                .tryDraw();
        if (optionalCard.isEmpty()) {
            return null;
        }

        state.cardsPlayed++;
        final Card card = optionalCard.get();
        state.pile.add(card);
        return card;
    }

    private int resolveInteraction(GameState state, Card card, int paymentDue) {
        if (paymentDue == 0) {
            this.switchTurn(state);
            paymentDue = card.payment();
        }
        else if (card.payment() == 0) {
            paymentDue--;

            if (paymentDue == 0) {
                this.switchTurn(state);
                this.claimPile(state);
            }
        }
        else {
            paymentDue = card.payment();
            this.switchTurn(state);
        }

        return paymentDue;
    }

    private void assignWinToOtherPlayerOnIncompletePayment(GameState state) {
        this.switchTurn(state);
        this.claimPile(state);
    }

    private GameStatus determineGameStatus(GameState state) {
        return Arrays.stream(state.decks())
                     .anyMatch(Deck::isEmpty)
               ? GameStatus.Finished
               : GameStatus.Playing;
    }

    private void switchTurn(GameState state) {
        state.turn = 1 - state.turn;
    }

    private void claimPile(GameState state) {
        List<Card> temp = StreamSupport
                .stream(state.pile.asEnumerable().spliterator(), false)
                .collect(Collectors.toList());

        state.decks()[state.turn]
                .addAllCards(temp);
        state.pile.clear();
    }
}
