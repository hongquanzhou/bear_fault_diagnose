import os
import scipy.io as scio
import numpy as np
from pylab import *


class CaseLoader():
    def label(self, filename):
        '''
        根据文件名热键编码
        : param filename:
        : return:
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

    def shuffle_dataset(self, data_matrix, label_matrix):
        '''
        数据打散  分布更加均匀
        : param datamatrix:
        : param labelmatrix:
        : return:
        '''
        np.random.seed(0)
        index = np.random.permutation(len(data_matrix))
        shuffled_dataset = data_matrix[index]
        shuffled_labels = label_matrix[index]
        return shuffled_dataset, shuffled_labels

    def MaxMinNormalize(self, x):
        max_value = max(x)
        min_value = min(x)
        x = (x - min_value) / (max_value - min_value)
        return x

    def load_data(self, n=1024):
        '''
        加载数据
        : param n:
        : return:
        '''
        # n  =  1000 #振动数据长度
        dir = self.data_dir  # 数据目录
        for _, _, files in os.walk(dir):
            pass
        cnt = 0
        length1 = Inf
        length2 = Inf
        length3 = Inf
        for file in files:
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
            cnt += int(length / n)
        print('cnt:', cnt)
        label_matrix = np.zeros(shape=(cnt, 11), dtype=np.int)
        data_matrix = np.zeros((cnt, 1024, 3), dtype=np.float32)
        cnt = 0
        for file in files:
            path = dir+file
            label_vector = np.array(self.label(file))
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
                for key in data.keys():
                    if "DE" in key:
                        a = data[key][i * n: (i + 1) * n]
                        data_matrix[cnt,:,0] = self.MaxMinNormalize(a.reshape(1, -1)[0])
                    if "FE" in key:
                        a = (data[key][i * n: (i + 1) * n])
                        data_matrix[cnt,:,1]= self.MaxMinNormalize(a.reshape(1, -1)[0])
                    if "BA" in key:
                        a = (data[key][i * n: (i + 1) * n])
                        data_matrix[cnt,:,2] = self.MaxMinNormalize(a.reshape(1, -1)[0])
                label_matrix[cnt] = label_vector
                cnt += 1
        return data_matrix, label_matrix

    def split_data(self, data_matrix, label_matrix, n=1000):
        '''
        : param datamatrix:
        : param labelmatrix:
        : param n:
        : return:
        '''
        len = data_matrix.shape[0]
        train_len = int(len*8/10)
        train_data = data_matrix[0:train_len]
        train_label = label_matrix[0:train_len]
        test_data = data_matrix[train_len:len]
        test_label = label_matrix[train_len:len]
        return (train_data, train_label), (test_data, test_label)

    def __init__(self,d):
        self.data_dir = d
        print('start load data...')
        data_matrix, label_matrix = self.load_data()
        data_matrix, label_matrix = self.shuffle_dataset(data_matrix, label_matrix)
        (self.train_data, self.train_label), (self.test_data, self.test_label) = self.split_data(data_matrix, label_matrix)
        print('data load over')
        self.num_test_data = self.test_data.__len__()
        self.num_train_data = self.train_data.__len__()

    def get_batch(self, size):
        index = np.random.randint(0, self.num_train_data, size)
        return self.train_data[index, :, :], self.train_label[index]