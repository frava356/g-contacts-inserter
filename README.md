g-contacts-inserter
===================

A small program in Java that allows you to add new contacts to your Gmail account from command line.

Required files
===================

The files guava-XX.X.X.jar and jsrXXX.jar are included in the repository. However, it is necessary to download Google Contacts V3 API and include the folder in the classpath in order to make it work.

Execution
==================

To run the program you just need to open the terminal and type:
  java -Dfile.enconding=UTF-8 Contacts gmail_user gmail_password contact_name contact_family_name contact_email contact_phone
  
