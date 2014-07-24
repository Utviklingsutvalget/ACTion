# Secrets
This folder is used to contain individual secrets for your local application.

## Google
To use login functionality, you must create a file called `googleoauth.conf`.
This is a property file, and must have the keys `googleclient.id` and `googleclient.secret` as obtained from Google's Developer Console.
You must also have the keys `googleclient.hd` for your hosted-doman preference, and `googleclient.redir` for your redirect url to build properly.
