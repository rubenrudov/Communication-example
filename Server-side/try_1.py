import json
import socket
import threading
from datetime import datetime

SERVER_CONFIG = ("0.0.0.0", 6000)
PACKET_SIZE = 1024


def handling_request(client_socket: socket.socket, json_struct):
    """
    Purpose: handle the request
    :param client_socket: socket
    :param json_struct: dict
    :return:
    """

    if json_struct["request"] == "time":
        time = "Hello"
        dicti = {"response": str(time)}
        client_socket.send(json.dumps(dicti).encode())
        print(f"Sent: {str(time).encode()}, via: {client_socket}")

    else:
        client_socket.send(json.dumps({"response": "Unknown command"}).encode())     # Check the java code


def new_connection_receiver(client_socket: socket.socket, ip_address: str):
    """
    :param client_socket: connection
    :param ip_address: address
    :return:
    """
    print(f"New connection from {ip_address}, via: {client_socket}")
    request = client_socket.recv(PACKET_SIZE)
    decoded = request.decode()

    json_struct = json.loads(decoded)

    handling_request(client_socket, json_struct)
    return json_struct


def main():
    print("Starts server")
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(SERVER_CONFIG)
    server_socket.listen()  # Listens for all the connections

    while True:
        new_client = server_socket.accept()
        new_thread = threading.Thread(target=new_connection_receiver, args=new_client)

        new_thread.start()


if __name__ == '__main__':
    main()
