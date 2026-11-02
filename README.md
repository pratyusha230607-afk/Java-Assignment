# ☕ Java Programming Lab - Practical Assignments

[![Java Version](https://img.shields.io/badge/Java-SE%208%2B-orange.svg?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/)
[![Database](https://img.shields.io/badge/Database-MySQL-blue.svg?style=for-the-badge&logo=mysql)](https://www.mysql.com/)
[![GUI](https://img.shields.io/badge/GUI-AWT%20%7C%20Swing%20%7C%20Applet-red.svg?style=for-the-badge)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Architecture](https://img.shields.io/badge/Architecture-Client--Server%20%7C%20RMI-brightgreen.svg?style=for-the-badge)]()
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg?style=for-the-badge)]()

Welcome to the **Java Practical Assignments Repository**. This repository contains a curated collection of core and advanced Java practicals covering foundational concepts, multithreading, GUI development (AWT & Swing), network programming, distributed computing (RMI), database connectivity (JDBC), JavaBeans, and secure application development.

---

## 📑 Table of Contents & Practical Index

| Practical # | Title / Objective | Technology / Concept | Source Code Links |
| :---: | :--- | :--- | :--- |
| **01** | [Matrix Multiplication using I/O Streams](#practical-01-matrix-multiplication-using-io-streams) | `BufferedReader`, `InputStreamReader`, 2D Arrays | [MatrixMultiplication.java](./MatrixMultiplication.java) |
| **02** | [Animated Banner String using Applet & Multithreading](#practical-02-animated-banner-string-using-applet--multithreading) | Java Applet, `Runnable`, Double Buffering | [AnimatedString.java](./AnimatedString.java) |
| **03** | [Interactive GUI Calculator](#practical-03-interactive-gui-calculator) | Java AWT (`Frame`, `Panel`, `Button`, `TextField`) | [Calculator.java](./Calculator.java) |
| **04** | [Client-Server TCP Console Chat](#practical-04-client-server-tcp-console-chat) | Networking, `ServerSocket`, `Socket`, TCP/IP | [ChatServer.java](./ChatServer.java) &bull; [ChatClient.java](./ChatClient.java) |
| **05** | [Swing Notepad Text Editor](#practical-05-swing-notepad-text-editor) | Java Swing (`JFrame`, `JTextArea`, `JFileChooser`, `JMenuBar`) | [Notepad.java](./Notepad.java) |
| **06** | [Distributed Math Computing using RMI](#practical-06-distributed-math-computing-using-rmi) | Remote Method Invocation (`RMI`, `Remote`, `Registry`) | [MathService.java](./MathService.java) &bull; [MathServiceImpl.java](./MathServiceImpl.java) &bull; [MathServer.java](./MathServer.java) &bull; [MathClient.java](./MathClient.java) |
| **07** | [User Login Interface](#practical-07-user-login-interface) | Java Applet Container, AWT Components, Event Handling | [LoginForm.java](./LoginForm.java) |
| **08** | [Database-Backed Authentication Form](#practical-08-database-backed-authentication-form) | Applet + JDBC, MySQL Data Persistence | [DatabaseForm.java](./DatabaseForm.java) &bull; [schema.sql](./schema.sql) |
| **09** | [Student Registration & Management Portal](#practical-09-student-registration--management-portal) | Swing GUI, JDBC, `JTable`, SQL Schema | [StudentPortal.java](./StudentPortal.java) &bull; [Student Schema](./mnt/user-data/outputs/JavaLab/Exp09_StudentPortal/schema.sql) |
| **10** | [Reusable Java Bean for Addition](#practical-10-reusable-java-bean-for-addition) | JavaBeans Architecture, `PropertyChangeSupport`, `Serializable` | [AddBean.java](./AddBean.java) &bull; [AddBeanTest.java](./AddBeanTest.java) |
| **11** | [Secure Input Form with SHA-256 Hashing](#practical-11-secure-input-form-with-sha-256-hashing) | Java Security (`MessageDigest`), Password Strength Meter | [SecureForm.java](./SecureForm.java) |
| **12** | [Full CRUD Operations Database Interface](#practical-12-full-crud-operations-database-interface) | JDBC (`PreparedStatement`), CRUD Operations, MySQL | [CrudForm.java](./CrudForm.java) &bull; [schema.sql](./schema.sql) |

---

## 🚀 Practicals Overview & Execution Details

### [Practical 01: Matrix Multiplication using I/O Streams](./MatrixMultiplication.java)
- **Objective:** Perform matrix multiplication of arbitrary valid dimensions ($A_{m \times k} \times B_{k \times n} = C_{m \times n}$) taking input safely through character streams.
- **Key Concepts:** `InputStreamReader`, `BufferedReader`, dimension compatibility validation, standard formatting.
- **How to Compile & Run:**
  ```bash
  javac MatrixMultiplication.java
  java MatrixMultiplication
  ```

---

### [Practical 02: Animated Banner String using Applet & Multithreading](./AnimatedString.java)
- **Objective:** Create a smooth horizontally scrolling text banner inside an Applet container using a background animator thread.
- **Key Concepts:** `Applet`, `Runnable`, `Thread` lifecycle, double buffering with off-screen `Image` buffer to eliminate render flickering.
- **How to Compile & Run:**
  ```bash
  javac AnimatedString.java
  java AnimatedString
  # Or with appletviewer:
  appletviewer AnimatedString.java
  ```

---

### [Practical 03: Interactive GUI Calculator](./Calculator.java)
- **Objective:** Design and implement a desktop calculator using pure Abstract Window Toolkit (AWT) components.
- **Key Concepts:** `Frame`, `Panel`, `GridBagLayout`, `GridLayout`, `ActionListener`, arithmetic handling, decimal precision using `BigDecimal`.
- **How to Compile & Run:**
  ```bash
  javac Calculator.java
  java Calculator
  ```

---

### [Practical 04: Client-Server TCP Console Chat](./ChatServer.java)
- **Objective:** Establish bidirectional, connection-oriented socket communication between a server process and a client process over TCP/IP.
- **Files:**
  - [`ChatServer.java`](./ChatServer.java) - Listens on TCP port `5000` and receives/sends messages.
  - [`ChatClient.java`](./ChatClient.java) - Connects to the chat server via socket.
- **How to Compile & Run:**
  ```bash
  # Step 1: Compile both
  javac ChatServer.java ChatClient.java

  # Step 2: In Terminal 1 (Start Server)
  java ChatServer

  # Step 3: In Terminal 2 (Start Client)
  java ChatClient localhost
  ```
  *(Type `bye` in either console to terminate the session).*

---

### [Practical 05: Swing Notepad Text Editor](./Notepad.java)
- **Objective:** Build a desktop text editor replicating Windows Notepad using modern Java Swing components.
- **Key Concepts:** `JFrame`, `JTextArea`, `JFileChooser`, `JMenuBar`, `JMenu`, `JMenuItem`, Document Listeners, find and replace, font scaling, word wrap, unsaved changes confirmation dialog.
- **How to Compile & Run:**
  ```bash
  javac Notepad.java
  java Notepad
  ```

---

### [Practical 06: Distributed Math Computing using RMI](./MathService.java)
- **Objective:** Implement a distributed system where a client invokes mathematical operations (`add`, `subtract`, `multiply`) executed on a remote server via Remote Method Invocation (RMI).
- **Files:**
  - [`MathService.java`](./MathService.java) - Remote Interface extending `java.rmi.Remote`.
  - [`MathServiceImpl.java`](./MathServiceImpl.java) - Implementation class extending `UnicastRemoteObject`.
  - [`MathServer.java`](./MathServer.java) - Server bootstrapping the registry and binding the service.
  - [`MathClient.java`](./MathClient.java) - Client performing registry lookup and calling remote methods.
- **How to Compile & Run:**
  ```bash
  # Step 1: Compile all RMI files
  javac MathService.java MathServiceImpl.java MathServer.java MathClient.java

  # Step 2: In Terminal 1 (Start Server)
  java MathServer

  # Step 3: In Terminal 2 (Start Client)
  java MathClient localhost
  ```

---

### [Practical 07: User Login Interface](./LoginForm.java)
- **Objective:** Create a standard user authentication input interface inside an Applet container.
- **Key Concepts:** AWT `TextField` with password masking (`setEchoChar('*')`), grid layout management, form clearance, and interactive feedback.
- **How to Compile & Run:**
  ```bash
  javac LoginForm.java
  java LoginForm
  ```

---

### [Practical 08: Database-Backed Authentication Form](./DatabaseForm.java)
- **Objective:** Capture user credentials from an Applet GUI and store them persistently into a MySQL relational database table via JDBC.
- **Files:**
  - [`DatabaseForm.java`](./DatabaseForm.java) - Applet GUI and JDBC persistence logic.
  - [`schema.sql`](./schema.sql) - Database initialization script (`labdb.users`).
- **How to Compile & Run:**
  ```bash
  # Step 1: Set up database
  mysql -u root -p < schema.sql

  # Step 2: Compile & Run (with MySQL Connector/J)
  javac DatabaseForm.java
  # Linux/macOS:
  java -cp .:mysql-connector-j-8.x.x.jar DatabaseForm
  # Windows:
  java -cp .;mysql-connector-j-8.x.x.jar DatabaseForm
  ```

---

### [Practical 09: Student Registration & Management Portal](./StudentPortal.java)
- **Objective:** Build an end-to-end Student Registration Management Portal with Swing front-end, form validation, MySQL database integration, and live record visualization in a `JTable`.
- **Files:**
  - [`StudentPortal.java`](./StudentPortal.java) - Main portal application.
  - [`schema.sql`](./mnt/user-data/outputs/JavaLab/Exp09_StudentPortal/schema.sql) - Student table schema (`labdb.students`).
- **Key Concepts:** Swing form controls (`JTextField`, `JComboBox`, `JRadioButton`, `JTable`), JDBC prepared statements, dynamic table model population.
- **How to Compile & Run:**
  ```bash
  # Step 1: Set up database table
  mysql -u root -p < mnt/user-data/outputs/JavaLab/Exp09_StudentPortal/schema.sql

  # Step 2: Compile and launch
  javac StudentPortal.java
  java -cp .:mysql-connector-j-8.x.x.jar StudentPortal
  ```

---

### [Practical 10: Reusable Java Bean for Addition](./AddBean.java)
- **Objective:** Construct a reusable Software Component following the JavaBean architecture specification to calculate the sum of numbers and notify registered listeners.
- **Files:**
  - [`AddBean.java`](./AddBean.java) - JavaBean with private properties, zero-argument constructor, getter/setter methods, and `PropertyChangeSupport`.
  - [`AddBeanTest.java`](./AddBeanTest.java) - Test client registering property change listeners and testing bean behavior.
- **How to Compile & Run:**
  ```bash
  javac AddBean.java AddBeanTest.java
  java AddBeanTest
  ```

---

### [Practical 11: Secure Input Form with SHA-256 Hashing](./SecureForm.java)
- **Objective:** Implement a defense-in-depth secure login input form enforcing password strength, input validation, and cryptographic hashing.
- **Key Concepts:** `java.security.MessageDigest` for one-way SHA-256 hashing, real-time password strength computation, regex input validation, masked inputs.
- **How to Compile & Run:**
  ```bash
  javac SecureForm.java
  java SecureForm
  ```

---

### [Practical 12: Full CRUD Operations Database Interface](./CrudForm.java)
- **Objective:** Create a comprehensive database management GUI supporting all CRUD operations:
  - **Create (Insert):** Add new user records with SHA-256 hashed passwords.
  - **Read (Search/Fetch):** Retrieve user details by username.
  - **Update:** Modify existing user passwords.
  - **Delete:** Remove user records from the database.
  - **View All:** Live tabular view of all stored records.
- **Files:**
  - [`CrudForm.java`](./CrudForm.java) - Interactive CRUD GUI and JDBC driver integration.
  - [`schema.sql`](./schema.sql) - MySQL schema for the user repository.
- **How to Compile & Run:**
  ```bash
  javac CrudForm.java
  java -cp .:mysql-connector-j-8.x.x.jar CrudForm
  ```

---

## 🛠️ Prerequisites & Setup

### 1. Java Development Kit (JDK)
Ensure you have JDK 8 or higher installed:
```bash
java -version
javac -version
```
> **Note on Applets:** Applet practicals (`AnimatedString`, `LoginForm`, `DatabaseForm`, `SecureForm`, `CrudForm`) include a standalone `main()` method that embeds the applet inside an AWT `Frame`, allowing them to run on modern JDK versions (JDK 11, 17, 21) without needing deprecated browser plugins.

### 2. MySQL Server & Connector/J
For database practicals (Practicals 08, 09, 12):
1. Install and start [MySQL Server](https://dev.mysql.com/downloads/mysql/).
2. Initialize database schemas:
   ```bash
   mysql -u root -p < schema.sql
   mysql -u root -p < mnt/user-data/outputs/JavaLab/Exp09_StudentPortal/schema.sql
   ```
3. Download the [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) JDBC driver and include the `.jar` in your classpath (`-cp`).

---

## 👤 Author

Developed by **[Pratyusha Purohit](https://github.com/pratyusha230607-afk)**  
GitHub: [@pratyusha230607-afk](https://github.com/pratyusha230607-afk)
