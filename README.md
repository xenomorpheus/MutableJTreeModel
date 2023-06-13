Mutable JTree Model
===================

PROJECT AIM(S):

1. To provide a working example of a Mutable JTree Model so that others can make use of it.

ACCEPTANCE CRITERIA:

1. When child Node objects are inserted/removed into a parent Node, the JTree should be updated via tree change events and no other update trigger should be required.
2. When the name of a Node object is changed, the JTree should be updated via tree change events and no other update trigger should be required.

RULE(S):

1. The UI must be in a different thread to that which changes the Model.
2. The JTree only uses the JTreeModel to understand the structure of the Model.
3. The JTree has no direct interaction with the Model.
4. The JTree only listens to the JTreeModel.
5. The JTreeModel listens for changes in the Model, then notifies the JTree.

BUG(S):

Probably, but none reported yet.

Build
=====
mvn package

Run
===
java -jar target/MutableJTreeModel-0.0.1-SNAPSHOT-jar-with-dependencies.jar