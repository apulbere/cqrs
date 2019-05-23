## About
Command Query Responsibility Segregation (CQRS) implemented in Java


Classes and interfaces
* `interface Command<T>` - contains logic for changing the state of `T` as well as pre-condition logic
* `class CommandHandler<ID, T>` - link between repository and command. It invokes command validation and then persists it if ok
* `interface Repository<ID, T>` - persists commands and snapshots of `T`, and can fetch last state of `T`

For details checkUT folder


#### Prerequisites
* Java 12
