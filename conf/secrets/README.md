# Secrets
This folder is used to contain individual secrets for your local application.

## Google
To use login functionality, you must create a file called `googleoauth`.
This is a property file, and must have the keys `client_id` and `client_secret` as obtained from Google's Developer Console. Failure to implement this file will most likely result in the application crashing due to IOExceptions.
