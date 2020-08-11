import os
os.environ["TF_CPP_MIN_LOG_LEVEL"] = '3'
import tensorflow as tf
import numpy as np
import os
import scipy.io as scio

class DataLoader:
    def __init__(self):
        datamatrix, labelmatrix = self.__loaddata()
        datamatrix, labelmatrix = self.__shuffle_dataset(datamatrix, labelmatrix)
        train_data, vali_data, test_data = self.__splitdata(datamatrix, labelmatrix)
        self.train_x, self.train_y = self.__data2xy(train_data)
        self.vali_x, self.vali_y = self.__data2xy(vali_data)
        self.test_x, self.test_y = self.__data2xy(test_data)
        self.num_train, self.num_vali, self.num_test = self.train_x.shape[0], self.vali_x.shape[0],self. test_x.shape[0]


    def get_train_batch(self,batch_size):
        index = np.random.randint(0, self.num_train, batch_size)
        return self.train_x[index], self.train_y[index]


    def __data2xy(self, data):
        num = data.__len__()
        return_x = np.empty(shape=[num, 1024, 3])
        return_y = np.empty(shape=[num, 11])
        for i in range(num):
            x = data[i]['X']
            x_onesample = np.empty(shape=[1024, 3])
            j = 0
            for key in x.keys():
                x_onesample[:, j] = x[key]
                j = j + 1
            return_x[i, :] = x_onesample
            return_y[i, :] = data[i]['Y']
        return return_x, return_y

    def __label(self,filename):
        '''
        根据文件名热键编码
        :param filename:
        :return:
        '''
        if "Drive_End" in filename:
            if "B0" in filename:
                return [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            if "IR0" in filename:
                return [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            if "OR0" in filename:
                if "@3" in filename:
                    return [0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0]
                if "@6" in filename:
                    return [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0]
                if "@12" in filename:
                    return [0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0]
        if "Fan_End" in filename:
            if "B0" in filename:
                return [0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0]
            if "IR0" in filename:
                return [0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0]
            if "OR0" in filename:
                if "@3" in filename:
                    return [0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0]
                if "@6" in filename:
                    return [0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0]
                if "@12" in filename:
                    return [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0]
        if "normal" in filename:
            return [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1]

    def __shuffle_dataset(self,datamatrix, labelmatrix):
        '''
        打散数据，尽量分布均匀
        :param datamatrix:
        :param labelmatrix:
        :return:
        '''
        shuffled_dataset = []
        shuffled_labels = []
        np.random.seed(0)
        index = np.random.permutation(len(datamatrix))
        for i in range(len(datamatrix)):
            shuffled_dataset.append(datamatrix[index[i]])
            shuffled_labels.append(labelmatrix[index[i]])
        return shuffled_dataset, shuffled_labels

    def MaxMinNormalize(self,x):
        Max = max(x)
        Min = min(x)
        x = (x - Min) / (Max - Min)
        return x

    def __loaddata(self,n=1024):
        '''
        加载数据
        :param n:
        :return:
        '''
        # n = 1000 #振动数据长度
        dir = "E:\毕业设计\case_west\CaseWesternReserveUniversityData-master\\"  # 数据目录，自由调整
        for _, _, files in os.walk(dir):
            pass
        labelmatrix = []
        datamatrix = []
        for file in files:
            i = 0
            labelvector = np.array(self.__label(file))
            path = dir + file
            data = scio.loadmat(path)
            for key in data.keys():
                if "DE" in key:
                    length1 = data[key].shape[0]
                if "FE" in key:
                    length2 = data[key].shape[0]
                if "BA" in key:
                    length3 = data[key].shape[0]
            length = min([length1, length2, length3])

            for i in range(0, int(length / n)):
                datavector = {}
                for key in data.keys():

                    if "DE" in key:
                        a = data[key][i * n:(i + 1) * n]
                        datavector["DE"] =self.MaxMinNormalize(a.reshape(1, -1)[0])
                    if "FE" in key:
                        a = (data[key][i * n:(i + 1) * n])
                        datavector["FE"] = self.MaxMinNormalize(a.reshape(1, -1)[0])
                    if "BA" in key:
                        a = (data[key][i * n:(i + 1) * n])
                        datavector["BA"] = self.MaxMinNormalize(a.reshape(1, -1)[0])
                if ("DE" in datavector) and ("FE" in datavector) and ("BA" in datavector):
                    datamatrix.append(datavector)
                    labelmatrix.append(labelvector)
        return datamatrix, labelmatrix

    def __splitdata(self, datamatrix, labelmatrix):
        '''
        切分数据为训练集、验证集、测试集
        :param datamatrix:
        :param labelmatrix:
        :return:
        '''
        traindata = []
        validation = []
        testdata = []
        print(len(datamatrix))
        for i in range(0, len(datamatrix)):
            data = {}
            data['X'] = datamatrix[i]
            data['Y'] = labelmatrix[i]
            if i % 10 < 7:
                traindata.append(data)
            if i % 10 > 8:
                validation.append(data)
            if (i % 10 > 6) and (i % 10 < 9):
                testdata.append(data)

        return traindata, validation, testdata




class Mymodel(tf.keras.Model):
    def __init__(self):
        super().__init__()
        self.conv1 = tf.keras.layers.Conv1D(filters=16, kernel_size=32, padding='same', strides=2, activation=tf.nn.relu)
        self.pool1 = tf.keras.layers.MaxPool1D(pool_size=2, strides=2)
        self.conv2 = tf.keras.layers.Conv1D(filters=8, kernel_size=16, padding='same', strides=2,  activation=tf.nn.relu)
        self.pool2 = tf.keras.layers.MaxPool1D(pool_size=2, strides=2)
        self.conv3 = tf.keras.layers.Conv1D(filters=8, kernel_size=8, padding='same', strides=2,  activation=tf.nn.relu)
        self.pool3 = tf.keras.layers.MaxPool1D(pool_size=2, strides=2)
        self.flatten = tf.keras.layers.Reshape(target_shape=(16*8,))
        self.dense1 = tf.keras.layers.Dense(units=300, activation=tf.nn.relu)
        self.dense2 = tf.keras.layers.Dense(units=11)


    def call(self, inputs, training=None, mask=None):
        x = self.conv1(inputs)
        x = self.pool1(x)
        x = self.conv2(x)
        x = self.pool2(x)
        x = self.conv3(x)
        x = self.pool3(x)
        x = self.flatten(x)
        x = self.dense1(x)
        x = self.dense2(x)
        outputs = tf.nn.softmax(x)
        return outputs


if __name__ == "__main__":
    num_batches = 10000
    batch_size = 100
    learning_rate = 0.001
    dataLoader = DataLoader()
    model = Mymodel()
    optimizer = tf.keras.optimizers.Adam(learning_rate=learning_rate)
    train_accuracy = tf.keras.metrics.CategoricalAccuracy()
    test_accuracy = tf.keras.metrics.CategoricalAccuracy()
    for batch_index in range(num_batches):
        X, y = dataLoader.get_train_batch(batch_size)
        with tf.GradientTape() as tape:
            y_pred = model(X)
            loss = tf.keras.losses.categorical_crossentropy(y_true=y, y_pred=y_pred)
            loss = tf.reduce_mean(loss)
            correct_prediction = tf.equal(tf.argmax(y_pred, 1), tf.argmax(y, 1))
            accuarcy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))  
            print("batch %d : loss: %f    accurcy: %f" %(batch_index, loss.numpy(), accuarcy))
        grads = tape.gradient(loss, model.variables)
        optimizer.apply_gradients(grads_and_vars=zip(grads, model.variables))
    num_batches = int(dataLoader.num_test // batch_size)
    #test_accurcy
    for batch_index in range(num_batches):
        start_index, end_index = batch_index * batch_size, (batch_index + 1) * batch_size
        y_pred = model.predict(dataLoader.test_x[start_index: end_index])
        test_accuracy.update_state(y_true = dataLoader.test_y[start_index:end_index], y_pred=y_pred)
    print("test_accurcy :%f" % test_accuracy.result())
