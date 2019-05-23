## About
Command Query Responsibility Segregation (CQRS) implemented in Java


Classes and interfaces
* `interface Command<T>` - send commands which change the state of `T`
* `class CommandHandler<ID, T>` - link between repository and command. It invokes command validation and then persists it if ok
* `interface Repository<ID, T>` - persists commands and snapshots of `T`, and can fetch last state of `T`

For example check UT folder


#### Prerequisites
* Java 12
