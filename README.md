# Backend for the Shelfmates Project
Shelfmates is a management tool for book clubs.

## How to run this Project

### ENVs
JWT_SECRET - used to sign the jwt tokens
GOOGLE_CLIENT_ID - google client id used for oauth2
GOOGLE_CLIENT_SECRET - google secret used for oauth2
SHELFMATES_DB_URL - Database URL
SHELFMATES_DB_USER - Database user
SHELFMATES_DB_PASSWORD - Database password

## User Stories

### MVP

- users should be able to signup and to login
- users can find other user by name or email
- users can create "book club" groups and join them
- groups can choose dates for meetings
- groups can vote on books to read
- users can exchange messages in the group
- users can search for books and use them for polls in the group

### Extended

- site admins can look up metrics on a dashboard like user numbers, books read etc.
- users can save ("shelf"), rate and comment on books
- user can follow each other and see information about their books("mates")
- group admins can change the profile picture of the group
