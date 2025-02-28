# Project Phase One

# Silvia-Maria Berescu

The project implements the Singleton pattern for the Bank class, ensuring that only one
instance of the bank exists throughout the whole test. The system receives commands and 
uses handlers to process them and generates output or transactions for each command. 
BankUtils and CommandUtils classes are used to manipulate the commands.
- The Bank class saves the list of users and exchange rates.
- BankUtils contains utility methods for managing exchange rates, users, and handling 
commands. Commands are parsed and handled in transactionFlow(). The BankUtils class 
interprets the input, determines the appropriate Command subclass, and calls the execute
method to perform the action. This design ensures a clean separation of concerns.
- The ExchangeRate class encapsulates information about currency exchange rates, 
holding the conversion ratio between two different currencies.
- Command is an abstract base class for all commands. Each one implements the execute()
method by calling methods from the CommandUtils to perform specific actions, using, 
this way, the Command Pattern. 
- The CommandUtils class provides reusable methods to perform operations required by 
commands and holds the main logic for all commands. These methods ensure the command 
classes remain concise.
- The User class manages user-specific data such as accounts, transactions, and account 
aliases.
- The TransactionFactory class uses a Factory Pattern to create instances of specific 
transaction types based on the provided TransactionType enum. 
- The TransactionData class is used to encapsulate all input data that may be needed to 
create a transaction.
- Transaction is the base class for all transactions. Subclasses of Transaction represent 
specific transaction types, each having specific fields. Each transaction type provides 
a toJsonNode method that converts its details into a JSON format.
- The Card class is an abstract base class that provides the attributes and methods for 
managing cards. Each subclass of Card customizes how the payment works through the 
makePayment method. 
- ClassicCard represents a standard card. 
- OneTimePayCard represents a card designed for single-use payments, that becomes inactive 
after usage using the makePayment.
- The Account class serves as the foundation for the account types. It defines shared 
properties while requiring subclasses to specify their types through an abstract method.
- ClassicAccount represents a standard bank account.
- SavingsAccount represents a specialized account that earns interest over time.

Exception handling is implemented to ensure the system handles unpredicted behaviour 
such as invalid inputs, insufficient balances, providing appropriate error messages.
