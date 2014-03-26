Mutable JTree Model
===================

PROJECT AIM(S):

1. To eventually provide a working example of a Mutable JTree Model so that others can make use of it.
2. This is about 1% of a much larger application with the below bug(s). I'm trying to solve this simpler case first.

BUG(S):

1. Once a node has been expanded in the UI, it won't show any child nodes added from that point in time.
2. Removing nodes doesn't update the UI.
3. In the Main class I'm not sure if I have correctly separated the UI thread from the thread that which 
changes the Model.
  In NodeJTreeModelDemo I have tried to do this differently. Not sure if better or worse.

RULE(S):

1. The UI must be in a different thread to that which changes the Model.
2. The JTree only uses the JTreeModel to understand the structure of the Model.
3. The JTree has no direct interaction with the Model.
4. The JTree only listens to the JTreeModel.
5. The JTreeModel listens for changes in the Model, then notifies the JTree.

DISCUSSION:

1. May the JTreeModel change the Model?

