ChatApp - Multi-Gateway Distributed Messaging System
====================================================

📦 Project Overview
-------------------
This is a simple Java-based distributed chat application where multiple clients can connect to different gateway servers, and all gateways share a centralized session service. Users can send messages to each other regardless of the gateway they are connected to.

🧱 Architecture
--------------
- **Client** → Connects to a gateway, sends and receives messages.
- **Gateway** → Handles connected clients and relays messages.
- **SessionService** → Tracks online users and their associated gateway.

📁 Package Structure
--------------------
com.prajwal.chatapp
│                          
├── client                                  
│   └── Client.java
│
├── gateway
│   └── Gateway.java
│
├── service
│   └── SessionService.java

🧪 How to Run
-------------
1. **Compile All Files**
   ```bash
   javac com/prajwal/chatapp/**/*.java
Start Gateways (in separate terminals)


java com.prajwal.chatapp.gateway.Gateway 9000
java com.prajwal.chatapp.gateway.Gateway 9001

Start Clients


java com.prajwal.chatapp.client.Client 127.0.0.1 9000
java com.prajwal.chatapp.client.Client 127.0.0.1 9001


Send Messages
After login, send messages using format:

to:<recipient_userId>:<message>

Example:

to:user2:Hi! How are you?
