TITLE: C195 Performance Assessment

PURPOSE: The purpose of this application is to allow a user to access, create, update and delete Customers and Customer appointments. This application also allow for the filtering of appointments by certain criteria, adjusts appointment times based on location, and adjusts language of Login page based on the language setting of the computer.

AUTHOR: Blake Ramsey

CONTACT INFO:
Email - bramse6@wgu.edu
Phone - 817-308-8047

APPLICATION VERSION: 1.1

DATE: 02-11-2023

ADDITIONAL REPORT I CHOSE TO RUN: When clicking the Scheduler button, it will show you all the scheduled appointments.
The additional report I chose to run was filtering all appointments by customer. If a customer is selected in the CustomerPage,
and then the Scheduler button is clicked, the Scheduler window will only show the appointments for that customer. This includes
filtering for week, month, contact ID, etc. The only area that is not affected by this filter is the month and type count. I also chose
to include filtering by Type OR Month, on top of the already required Type AND Month.

INTELLIJ / JDK / JAVAFX VERSIONS: IntelliJ IDEA 2022.2 (Community Edition), Oracle OpenJDK version 17.0.4, Javafx-sdk-17.0.2

MYSQL CONNECTOR DRIVER: mysql-connector-j-8.0.32

HOW TO RUN: Run using Java 17 SDK build with VM options: "--module-path ${PATH_TO_FX} --add-modules javafx.fxml,javafx.controls,javafx.graphics".



