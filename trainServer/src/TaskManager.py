import MySQLdb
from Task import *
from conf import *


class TaskManager:
    def __init__(self):
        self.tasks = {}
        # 根据数据库初始化
        db = MySQLdb.connect(host, user, password, database)
        cursor = db.cursor()
        sql = "select * from t_task_info"
        cursor.execute(sql)
        data = cursor.fetchall()
        for item in data:
            self.addTask(Task(item[0]))

    def addTask(self,t):
        if(self.tasks.get(t.id)==None):
            self.tasks[t.id] = t

    def removeTask(self,t):
        if (self.tasks.get(t.id) != None):
            self.tasks.pop(t.id)

    def startTask(self,id):
        if (self.tasks.get(id) != None):
            self.tasks.get(id).start()

    def suspendTask(self,id):
        if (self.tasks.get(id) != None):
            self.tasks.get(id).suspend()

    def goonTask(self,id):
        if (self.tasks.get(id) != None):
            self.tasks.get(id).goon()

    def stopTask(self,id):
        if (self.tasks.get(id) != None):
            self.tasks.get(id).stop()

    def restartTask(self,id):
        if (self.tasks.get(id) != None):
            self.tasks.get(id).restart()

    def getCureveData(self,id,pos):
        if (self.tasks.get(id) != None):
            return self.tasks.get(id).getRealTimeData(pos)
        else:
            return "[[],[],[],[],[]]"
