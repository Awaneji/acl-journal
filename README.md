# acl-journal
ACL Android Developer Track, 7 Days of Code Challenge

# About
The Android app is developed using Java native following (in a learning process) the MVVM architecture.
The app's core functionality is for users to create, and manage personal journals/memos.
The app is backed by a reliable persistence layer and an approved OAUTH signin/signup framework.

# Persistence
I used the ROOM Orm

# Signin or Signup
I used Firebase Authentication's Google provider with the help of Firebase-ui open source libraries.

# Cloud sync
Due to some issues (incompatibility), as I added Firestore dependencies (all/most of the -auth-ui classes threw a runtime Exception, NoClassDefFoundError and ClassNotFoundException), I commented out parts for using Firestore and used ROOM only. You can find the code for using Firestore in the classes.

# Instrumentation test
Used Espresso

# Unit tests
Used Junit and Mockito
