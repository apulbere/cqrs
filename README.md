## About
Command Query Responsibility Segregation (CQRS) implemented in Java


Classes and interfaces
* `interface Command<T>` - contains logic for changing the state of `T`
* `interface Validatable<T>` - validates the state of `T`
* `class CommandHandler<ID, T>` - link between repository and command
* `interface Repository<ID, T>` - persists commands and snapshots of `T`, and can fetch last state of `T`

For details check [unit tests](src/test/java/com/apulbere/cqrs/CQRSTest.java)


#### Prerequisites
* Java 12
