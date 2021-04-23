import tensorflow as tf
import MySQLdb
import threading
import tensorflowjs as tfjs
from State import *
from DataLoader import *
from Model import *
from Optimizer import *

from conf import *

class Task:
    def __init__(self, id):
        self.state2status = ("ready", "running", "suspend", "success", "failed", "stoped")
        db = MySQLdb.connect(host, user, password, database)
        cursor = db.cursor()
        sql = "select * from t_task_info where id = " + str(id)
        cursor.execute(sql)
        data = cursor.fetchone()
        self.id = id
        self.modelName = data[1]
        self.dataSource = data[2]
        self.preParameter = data[3]
        self.taskName = data[4]
        self.state = State(data[7])
        self.process = data[8]
        self.lossFunction = data[9]
        self.learnRateUpdate = data[10]
        self.learnRate = data[11]
        self.batchSize = data[12]
        self.iterNum = data[13]
        self.metric = data[15]

        self.Optimizer = self.parseOptimizer(Optimizer(self.learnRateUpdate))
        self.LossFunc = self.parseLossFunc(self.lossFunction)
        self.Metric = self.parseMetric(self.metric)

        self.dataSource_dir = self.parseDataSourceBase(self.dataSource)
        self.line = self.parseModel()
        self.learnRateData = []
        self.trainLoss = []
        self.testLoss = []
        self.trainAcc = []
        self.testAcc = []
        self.con = threading.Condition()
        self.pos = 0

    def parseMetric(self,metric):
        return tf.keras.metrics.get(metric)

    def parseOptimizer(self,op):
        if(op == Optimizer.sgd):
            return tf.keras.optimizers.SGD(learning_rate=self.learnRate,momentum=0.0)
        elif(op == Optimizer.adam):
            return tf.keras.optimizers.Adam(learning_rate=self.learnRate,beta_1=0.9,beta_2=0.999,epsilon=1e-7)
        elif(op == Optimizer.adagrad):
            return tf.keras.optimizers.Adagrad(learning_rate=self.learnRate,initial_accumulator_value=0.1,epsilon=1e-7,)
        elif(op == Optimizer.adadelta):
            return tf.keras.optimizers.Adadelta(learning_rate=self.learnRate,rho=0.95,epsilon=1e-7,)
        elif(op == Optimizer.adamax):
            return tf.keras.optimizers.Adamax(learning_rate=self.learnRate,beta_1=0.9,beta_2=0.999,epsilon=1e-7,)
        elif(op == Optimizer.rmsprop):
            return tf.keras.optimizers.RMSprop(learning_rate=self.learnRate,rho=0.9, momentum=0.0,epsilon=1e-7,)
        elif(op == Optimizer.momentum):
            return tf.keras.optimizers.Nadam(learning_rate=self.learnRate,beta_1=0.9,beta_2=0.999,epsilon=1e-7,)

    def parseLossFunc(self,loss):
        return tf.keras.losses.get(loss)

    def updateAcc(self, acc):
        db = MySQLdb.connect(host, user, password, database)
        cursor = db.cursor()
        sql = "update t_task_info set test_acc = " + str(round(acc, 2)) + " where id=" + str(self.id)
        print(sql)
        cursor.execute(sql)
        db.commit()

    def getRealTimeData(self,pos):
        len = self.trainLoss.__len__()
        uploadData = []
        uploadData.append(self.learnRateData[pos:len])
        uploadData.append(self.trainLoss[pos:len])
        uploadData.append(self.testLoss[pos:len])
        uploadData.append(self.trainAcc[pos:len])
        uploadData.append(self.testAcc[pos:len])
        self.pos = len
        return str(uploadData)

    def parseModel(self):
        file = modelStructureDir + str(self.modelName)
        f = open(file)
        line = f.readline()
        f.close()
        return line

    def parseDataSourceBase(self, d):
        db = MySQLdb.connect(host, user, password, database)
        cursor = db.cursor()
        sql = "select * from t_data_source where name='" + d + "'"
        cursor.execute(sql)
        data = cursor.fetchone()
        return data[1]

    def updateDb(self):
        db = MySQLdb.connect(host, user, password, database)
        cursor = db.cursor()
        sql = "update t_task_info set status = \"" + self.state.name + "\",process=" + str(
            self.process) + " where Id=" + str(self.id)
        cursor.execute(sql)
        db.commit()

    def update(self):
        # print("state:"+str(self.process) + str(self.state))
        self.process += 1
        if (self.process == 100):
            self.state = State.success
        elif (self.state == State.running):
            threading.Timer(1, self.update).start()
        self.updateDb()

    def updateProcess(self):
        self.process += 1
        if (self.process == 100):
            self.state = State.success
        self.updateDb()

    def doWork(self):
        print("start Train")
        num_epochs = self.iterNum
        batch_size = self.batchSize
        learning_rate = self.learnRate
        print("learning rate:" + str(learning_rate))
        model = Model(self.line)
        data_loader = DataLoader("")
        optimizer = self.Optimizer

        num_batches = int(data_loader.num_train_data // batch_size * num_epochs)
        cnt = 0
        percent_num = int(num_batches // 100)
        for batch_index in range(num_batches):
            if (self.state == State.running):
                X, y = data_loader.get_batch(batch_size)
                X2 = data_loader.test_data
                y2 = data_loader.test_label
                cnt += 1
                with tf.GradientTape() as tape:
                    y_pred = model(X)
                    loss = self.LossFunc(y_true=y, y_pred=y_pred)
                    loss = tf.reduce_mean(loss)
                    if (batch_index % 10 == 0):
                        train_loss = loss
                        train_acc = tf.reduce_mean(tf.cast(tf.equal(tf.argmax(y, 1), tf.argmax(y_pred, 1)), tf.float32))
                        y2_pred = model(X2)
                        test_loss = tf.reduce_mean(self.LossFunc(y_true=y2, y_pred=y2_pred))
                        test_acc = tf.reduce_mean(
                            tf.cast(tf.equal(tf.argmax(y2, 1), tf.argmax(y2_pred, 1)), tf.float32))
                        self.trainLoss.append(float(train_loss))
                        self.testLoss.append(float(test_loss))
                        self.trainAcc.append(float(train_acc))
                        self.testAcc.append(float(test_acc))
                        self.learnRateData.append(self.learnRate)
                    if cnt == percent_num:
                        self.updateProcess()
                        cnt = 0
                grads = tape.gradient(loss, model.variables)
                optimizer.apply_gradients(grads_and_vars=zip(grads, model.variables))
            elif (self.state == State.stoped):
                break
            elif (self.state == State.suspend):
                self.con.acquire()
                print("work thread wait")
                self.con.wait()

        if (self.state == State.success):
            print("\ntrain complete")
            metric = self.Metric
            num_batches = int(data_loader.num_test_data // batch_size)
            for batch_index in range(num_batches):
                start_index, end_index = batch_index * batch_size, (batch_index + 1) * batch_size
                y_pred = model.predict(data_loader.test_data[start_index: end_index])
                metric.update_state(y_true=data_loader.test_label[start_index: end_index],y_pred=y_pred)
            print("test accuracy: %f" % metric.result())
            # 改数据库
            self.updateAcc(float(metric.result()) * 100)
            # 持久化
            self.modelPersitence(model)

    # 模型持久化
    def modelPersitence(self,model):
        print("start transform")
        # 下位机模型转换
        dir = overModelDir + self.taskName
        if (not os.path.exists(dir)):
            os.mkdir(dir)
        # 模型数据存储
        tf.saved_model.save(model, dir)
        self.convert2float_tflite(dir, dir + '/CNN_float.tflite')
        self.conver2quanti_tflite(dir, dir + '/CNN_quanti.tflite')
        # js模型转换
        jsFullDir = jsModelSaveDir + self.taskName
        if (not os.path.exists(jsFullDir)):
            os.mkdir(jsFullDir)
        self.convert2js(dir, jsFullDir)


    def convert2js(self,input,output):
        tfjs.converters.convert_tf_saved_model(input, output)

    def convert2float_tflite(self, input, output, mask=0):
        converter = tf.lite.TFLiteConverter.from_saved_model(input)
        tflite_float_model = converter.convert()
        open(output, "wb").write(tflite_float_model)


    def conver2quanti_tflite(self, input, output):
        '''
        :param input:
        :param output:
        :return:
        '''
        converter = tf.lite.TFLiteConverter.from_saved_model(input)
        converter.optimizations = [tf.lite.Optimize.DEFAULT]
        tflite_quant_model = converter.convert()
        open(output, "wb").write(tflite_quant_model)

    def start(self):
        self.learnRateData = []
        self.trainLoss = []
        self.testLoss = []
        self.trainAcc = []
        self.testAcc = []
        # if (self.state == State.ready):
        self.process = 0
        self.state = State.running
        self.updateDb()
        threading.Timer(0, self.doWork).start()

    def suspend(self):
        if (self.state == State.running):
            self.state = State.suspend
            self.updateDb()

    def goon(self):
        if (self.state == State.suspend):
            self.state = State.running
            self.con.acquire()
            self.con.notify()
            self.updateDb()

    def stop(self):
        if (self.state == State.running):
            self.state = State.stoped
            self.updateDb()

    def restart(self):
        if (self.state == State.stoped or self.state == State.failed or self.state == self.state.success):
            self.state = State.running
            self.process = 0
            self.updateDb()
            self.learnRateData = []
            self.trainLoss = []
            self.testLoss = []
            self.trainAcc = []
            self.testAcc = []
            threading.Timer(0, self.doWork).start()