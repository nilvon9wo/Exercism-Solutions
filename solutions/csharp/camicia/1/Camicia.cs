using System.Diagnostics.CodeAnalysis;

[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "<Pending>")]

public static class Camicia
{
    public static GameResult SimulateGame(string[] playerA, string[] playerB) 
        => new Game(playerA, playerB)
            .Simulate();

    private class Game(string[] PlayerA, string[] PlayerB)
    {
        public GameResult Simulate()
        {
            GameState state = new(PlayerA, PlayerB);
            GameStatus status;
            do
            {
                status = PlayTrick(state);
            } while (status == GameStatus.Playing);
            
            return new GameResult(status, state.Tricks, state.CardsPlayed);
        }

        private GameStatus PlayTrick(GameState state)
        {
            if (IsLoop(state))
            {
                return GameStatus.Loop;
            }

            state.Tricks++;
            int paymentDue = 0;
            do
            {
                if (DrawCard(state, out Card? card))
                {
                    paymentDue = ResolveInteraction(state, card, paymentDue);
                }
                else
                {
                    AssignWinToOtherPlayerOnIncompletePayment(state);
                }
            } while (!state.Pile.IsEmpty());

            return DetermineGameStatus(state);
        }

        private static int ResolveInteraction(GameState state, Card card, int paymentDue)
        {
            if (paymentDue == 0)
            {
                SwitchTurn(state);
                paymentDue = card.Payment;
            }
            else if (card.Payment == 0)
            {
                paymentDue--;
                if (paymentDue == 0)
                {
                    SwitchTurn(state);
                    ClaimPile(state);
                }
            }
            else
            {
                paymentDue = card.Payment;
                SwitchTurn(state);
            }

            return paymentDue;
        }

        private static void AssignWinToOtherPlayerOnIncompletePayment(GameState state)
        {
            SwitchTurn(state);
            ClaimPile(state);
        }

        private static GameStatus DetermineGameStatus(GameState state) 
            => state.Decks.Any(deck => deck.IsEmpty())
                        ? GameStatus.Finished
                        : GameStatus.Playing;

        private static bool IsLoop(GameState state)
            => !state.History.Add(HashDecks(state));

        private static int HashDecks(GameState state)
            => HashCode.Combine(state.DeckA.EncodePayments(), state.DeckB.EncodePayments());

        private static bool DrawCard(GameState state, out Card card)
        {
            if (!state.Decks[state.Turn].TryDraw(out card))
            {
                return false;
            }

            state.CardsPlayed++;
            state.Pile.Add(card);
            return true;
        }

        private static void SwitchTurn(GameState state)
            => state.Turn = 1 - state.Turn;

        private static void ClaimPile(GameState state)
        {
            state.Decks[state.Turn].AddAll(state.Pile);
            state.Pile.Clear();
        }
    }

    public enum GameStatus
    {
        Playing,
        Finished,
        Loop
    }

    public record GameResult(GameStatus Status, int Tricks, int Cards);
}

internal class GameState
{
    internal GameState(string[] PlayerA, string[] PlayerB)
    {
        DeckA.AddAll(PlayerA);
        DeckB.AddAll(PlayerB);
    }

    internal readonly Deck DeckA = new();
    internal readonly Deck DeckB = new();
    internal Deck[] Decks
        => [DeckA, DeckB];

    internal readonly Deck Pile = new();
    internal int Tricks = 0;
    internal int CardsPlayed = 0;
    internal int Turn = 0;
    internal readonly HashSet<int> History = [];
}

internal record class Card
{
    private static readonly Dictionary<string, int> CardValues = new()
    {
        ["J"] = 1,
        ["Q"] = 2,
        ["K"] = 3,
        ["A"] = 4
    };

    public Card(string card) 
        => Payment = CardValues.TryGetValue(card, out int value)
            ? value
            : 0;
    public int Payment { get; init; }
}

internal class Deck
{
    public Deck() { }
    private Queue<Card> Cards { get; init; } = new();

    public bool IsEmpty() 
        => Cards.Count == 0;

    public bool TryDraw(out Card card) 
        => Cards.TryDequeue(out card!);

    public void Clear() 
        => Cards.Clear();

    public void Add(string card) 
        => Add(new Card(card));

    public void Add(Card card) 
        => Cards.Enqueue(card);

    public void AddAll(IEnumerable<string> cards) 
        => AddAll(cards.Select(card => new Card(card)));

    public void AddAll(IEnumerable<Card> cards)
    {
        foreach (var card in cards)
        {
            Add(card);
        }
    }
    
    protected IEnumerable<Card> AsEnumerable() 
        => Cards;
    
    public void AddAll(Deck deck) 
        => AddAll(deck.AsEnumerable());


    public string EncodePayments() 
        => string.Join(',', Cards.Select(c => c.Payment));
}