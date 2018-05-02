Personal Finance System Project
===============================

This was my project for my final year in my BSc in Computer Science. It
consists of a simple Swing application built in Scala which allows a user to
upload their bank or credit card statement and create categories for each
entry. The categories will be recorded with their descriptions (or whatever the
user chooses) as patterns, which should allow further statements to be matched
to those patterns to the same categories. In time, the usual patterns will have
been recorded, which should require very little input from the users in the
future.

The data entered can then be visualised as summaries broken down by periods,
and after enough data has been entered the user can also visualise simple
budgets.

How to build and run the application
------------------------------------

### What you will need
- SBT 1.1.1 (you can get this with [SDKMAN](http://sdkman.io/))
- Scala 2.12.4 (you can get this with SBT or [SDKMAN](http://sdkman.io/))
- MySql
  - also, the [schema.sql](./code/original_db_schemas/mysql/schema.sql) file,
    for the main database schema
  - and the [test\_schema.sql](./code/original_db_schemas/mysql/test_schema.sql)
    for the test database schema
- The [config.properties](./code/config.properties) file for the main application
  - within the file, replace the mysql database address with your own:
  ```
  MySqlUrl=jdbc:mysql://localhost:3306/personal_finance_system
  ```


- The [testprops](./code/src/test/testprops) and
  [testtextfile](./code/src/test/testtextfile) files, if you plan to run the
  tests.
- a `private.properties` file containing the MySQL username and password for
  the user that can access the main database, and the test database, in the
  following format (case sensitive):
  ```
  MySqlUsername=username
  MySqlPassword=password
  ```

  - replace `username` and `password` with your ones


### How to build and run it
#### set up the project
- clone this project into an empty folder, and cd into it, then into the `code`
  subfolder:
  ```
  $ git clone https://github.com/claudiusbr/personal_finance_system 
  $ cd personal_finance_system/code
  ```
- create the `private.properties` file mentioned above, and save it in this
  folder;
- add your username and password to it, if you haven't yet;
 

#### Set up MySQL
- Create a database called `personal_finance_system`;
- Create a database called `test_personal_finance`;
- Give access to it to the username and password which will go in the `private.properties` file;
- load the schemas into mysql:
  ```
  $ mysql -u username -p personal_finance_system < schema.sql
  $ mysql -u username -p test_personal_finance < test_schema.sql
  ```

#### Run SBT from the `code` folder
```
$ cd personal_finance_system/code
$ sbt run
```


### How to use it
- Make sure your bank statements are in a csv format, and that the column names
  match the below exactly (non-case sensitive, though):
  ```
  date,description,amount
  ... , ... , ...
  ...
  ```

- As this tends to be the trend with bank statements (or at least mine), all income should be negative, and all expenditure positive, so if you are uploading either 
