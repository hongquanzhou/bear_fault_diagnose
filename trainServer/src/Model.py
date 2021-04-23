import tensorflow as tf
import json


class Model(tf.keras.Model):
    def __init__(self,line):
        super().__init__()
        m = json.loads(line)
        layersSize = m['layersSize']
        ops = m['ops']
        self.OPS = []
        for i in range(len(ops)):
            if (ops[i]['op'] == 'max_pool'):
                pool_size = 2
                strides = 2
                self.OPS.append(tf.keras.layers.MaxPool1D(pool_size=pool_size, strides=strides))
            if (ops[i]['op'] == 'convolution'):
                filters = layersSize[i+1][0]
                kernel_size = layersSize[i][3]
                activation = self.getActivation(ops[i]['func'])
                strides = 2
                self.OPS.append(tf.keras.layers.Conv1D(
                    filters=filters,  # 卷积层神经元（卷积核）数目
                    kernel_size=kernel_size,  # 感受野大小
                    padding='same',  # padding策略（vaild 或 same）
                    activation=activation,  # 激活函数
                    strides=strides
                ))
            if (ops[i]['op'] == 'dense'):
                units = layersSize[i+1][0]
                activation = self.getActivation(ops[i]['func'])
                self.OPS.append(tf.keras.layers.Dense(units=units, activation=activation))
            if (ops[i]['op'] == 'flatten'):
                target_num = layersSize[i+1][1]
                self.OPS.append(tf.keras.layers.Reshape(target_shape=(target_num,)))
            if (ops[i]['op'] == 'softmax'):
                self.OPS.append(tf.nn.softmax)

    def getActivation(self, line):
        if(line=="relu"):
            return tf.nn.relu

    @tf.function
    def call(self,inputs):
        x = inputs
        for i in range(self.OPS.__len__()):
            x = self.OPS[i](x)
        return x