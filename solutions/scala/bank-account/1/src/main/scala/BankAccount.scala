trait BankAccount:
    def closeAccount(): Unit
    def getBalance: Option[Int]
    def incrementBalance(increment: Int): Option[Int]


object Bank:
    def openAccount(): BankAccount =
        new BankAccount:
            private val lock = new Object
            private var balance: Int = 0
            private var closed: Boolean = false

            def closeAccount(): Unit =
                lock.synchronized:
                    closed = true
                    balance = 0

            def getBalance: Option[Int] =
                lock.synchronized:
                    if closed
                    then None
                    else Some(balance)

            def incrementBalance(increment: Int): Option[Int] =
                lock.synchronized:
                    if closed
                    then None
                    else
                        balance += increment
                        Some(balance)