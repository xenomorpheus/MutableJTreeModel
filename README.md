Mutable JTree Model
===================

PROJECT AIM(S):

1. To provide a working example of a Mutable JTree Model so that others can make use of it.

BUG(S):

Probably but none reported yet.

RULE(S):

1. The UI must be in a different thread to that which changes the Model.
2. The JTree only uses the JTreeModel to understand the structure of the Model.
3. The JTree has no direct interaction with the Model.
4. The JTree only listens to the JTreeModel.
5. The JTreeModel listens for changes in the Model, then notifies the JTree.

