import _thread
import os
os.environ['TF_CPP_MIN_LOG_LEVEL']='2'
import socket
import json
import traceback
from TaskManager import *
from SocketStreamReader import *
from Task import *
from conf import *


class server:
    def __init__(self):
        self.tm = TaskManager()
        s = socket.socket()
        s.bind((host,port))
        s.listen()
        print("listen")
        while True:
            client,addr = s.accept()
            _thread.start_new_thread(self.workThread,(client,))

    def workThread(self,client):
        sl = SocketStreamReader(client)
        while True:
            msg = sl.readline()
            msg = msg.decode("utf-8")
            try:
                msg = json.loads(msg)
                No = msg["No"]
                id = msg["taskId"]
                if(No=="submit"):
                    t = Task(id)
                    self.tm.addTask(t)
                elif(No=="GetData"):
                    line = self.tm.getCureveData(id,msg["pos"])
                    client.send((line+'\n').encode("utf-8"))

                else:
                    mode = msg["mode"]
                    if(mode=="start"):
                        print("start")
                        self.tm.startTask(id)
                    elif(mode=="suspend"):
                        print("suspend")
                        self.tm.suspendTask(id)
                    elif(mode=="goon"):
                        print("goon")
                        self.tm.goonTask(id)
                    elif(mode=="stop"):
                        print("stop")
                        self.tm.stopTask(id)
                    elif(mode=="restart"):
                        print("restart")
                        self.tm.restartTask(id)
            except Exception:
                val = traceback.format_exc()
                print(val)

if __name__ == "__main__":
    s = server()
