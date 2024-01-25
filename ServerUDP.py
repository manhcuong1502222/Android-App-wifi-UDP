import socket
import threading
from time import sleep


clientlst = [] #listener
ServerIP  = "192.168.43.38"
ServerPort = 4840
bufferSize = 1024
msgFromServer = "Hello Client UDP"
bytesToSend = str.encode(msgFromServer)

#Create a datagram socket (UDP)
UDPServerSocket = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM) #1

# Bind to address and ip
UDPServerSocket.bind((ServerIP, ServerPort)) #2
print("UDP server up and listening")

def ReceiveThread():
    while True:
        bytesAddressPair = UDPServerSocket.recvfrom(bufferSize) #waiting here until receving #3
        message = bytesAddressPair[0].decode('utf-8')
        address = bytesAddressPair[1]

        #in ra man hinh data da nhan dc
        print("Received data from:", address)
        print("Data:", message)        

        if address not in clientlst:
            clientlst.append(address)

        #Send to that client
        # UDPServerSocket.sendto(bytesToSend, address) #4

        #Send to all Clients
        for addr in clientlst:
            bytesToSend = str.encode(message)
            UDPServerSocket.sendto(bytesToSend,addr)

#Listen for incoming datagram
thread = threading.Thread(target=ReceiveThread, args=())
thread.start()

while True:
    #do the main task here
    sleep(1)


