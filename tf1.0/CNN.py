import numpy as np
import matplotlib.pyplot as plt
import scipy.io as scio
import os
import pickle
import signal
import tensorflow.compat.v1 as tf
tf.disable_eager_execution()
def label(filename):
    '''
    根据文件名热键编码
    :param filename:
    :return:
    '''
    if "Drive_End" in filename:
        if "B0" in filename:
            return [1,0,0,0,0,0,0,0,0,0,0]
        if "IR0" in filename:
            return [0,1,0,0,0,0,0,0,0,0,0]
        if "OR0" in filename:
            if "@3" in filename:
                return [0,0,1,0,0,0,0,0,0,0,0]
            if "@6" in filename:
                return [0,0,0,1,0,0,0,0,0,0,0]
            if "@12" in filename:
                return [0,0,0,0,1,0,0,0,0,0,0]
    if "Fan_End" in filename:
        if "B0" in filename:
            return [0,0,0,0,0,1,0,0,0,0,0]
        if "IR0" in filename:
            return [0,0,0,0,0,0,1,0,0,0,0]
        if "OR0" in filename:
            if "@3" in filename:
                return [0,0,0,0,0,0,0,1,0,0,0]
            if "@6" in filename:
                return [0,0,0,0,0,0,0,0,1,0,0]
            if "@12" in filename:
                return [0,0,0,0,0,0,0,0,0,1,0]
    if "normal" in filename:
        return [0,0,0,0,0,0,0,0,0,0,1]

def shuffle_dataset(datamatrix,labelmatrix):
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
    return shuffled_dataset,shuffled_labels
def MaxMinNormalize(x):
    Max = max(x)
    Min = min(x)
    x = (x - Min)/(Max - Min)
    return x
def loaddata(n = 1024):
    '''
    加载数据
    :param n:
    :return:
    '''
    #n = 1000 #振动数据长度
    dir = "E:\毕业设计\case_west\CaseWesternReserveUniversityData-master\\"#数据目录，自由调整

    for _, _, files in os.walk(dir):
        pass
    labelmatrix = []
    datamatrix = []
    for file in files:
        i = 0
        labelvector = np.array(label(file))
        path = dir + file
        data = scio.loadmat(path)
        for key in data.keys():
            if "DE" in key:
                length1=data[key].shape[0]
            if "FE" in key:
                length2=data[key].shape[0]
            if "BA" in key:
                length3=data[key].shape[0]
        length=min([length1,length2,length3])

        for i in range(0,int(length/n)):
            datavector={}
            for key in data.keys():

                if "DE" in key:
                    a = data[key][i*n:(i+1)*n]
                    datavector["DE"] = MaxMinNormalize(a.reshape(1,-1)[0])
                if "FE" in key:
                    a = (data[key][i*n:(i+1)*n])
                    datavector["FE"] = MaxMinNormalize(a.reshape(1,-1)[0])
                if "BA" in key:
                    a = (data[key][i*n:(i+1)*n])
                    datavector["BA"] = MaxMinNormalize(a.reshape(1,-1)[0])
            if ("DE" in datavector) and ("FE" in datavector) and ("BA" in datavector):
                datamatrix.append(datavector)
                labelmatrix.append(labelvector)
    return datamatrix,labelmatrix


def splitdata(datamatrix,labelmatrix):
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
    for i in range(0,len(datamatrix)):
        data = {}
        data['X'] = datamatrix[i]
        data['Y'] = labelmatrix[i]
        if i % 10 < 7:
            traindata.append(data)
        if i % 10 > 8:
            validation.append(data)
        if(i % 10 > 6 ) and (i % 10 < 9):
            testdata.append(data)

    return traindata,validation,testdata




def conv2d(x,W,b,strides):  #[batch滑动步长，水平滑动步长，垂直滑动步长，通道滑动步长]
    x = tf.nn.conv2d(x,W,strides= strides,padding = 'SAME')
    x = tf.nn.bias_add(x,b)
    return tf.nn.relu(x)

def maxpool(x,k=2):
    return tf.nn.max_pool(x,ksize=[1,1,k,1],strides=[1,k,k,1],padding='SAME')

def conv_net(x,weights,biases,dropout):
    x = tf.reshape(x,shape=[-1,1,1024,3])  #[batch, in_height, in_width, in_channels]
    conv1 = conv2d(x,weights['wc1'],biases['bc1'],strides=[1,1,2,1])  #[filter_height, filter_width, in_channels, out_channels]
    conv1 = maxpool(conv1)
    conv2 = conv2d(conv1,weights['wc2'],biases['bc2'],strides=[1,1,2,1])
    conv2 = maxpool(conv2)
    conv3 = conv2d(conv2,weights['wc3'],biases['bc3'],strides=[1,1,2,1])
    conv3 = maxpool(conv3)
    fc1 = tf.reshape(conv3,[-1,weights['wd1'].get_shape().as_list()[0]])
    fc1 = tf.add(tf.matmul(fc1,weights['wd1']),biases['bd1'])
    fc1 = tf.nn.relu(fc1)
    fc2=tf.add(tf.matmul(fc1,weights['wd2']),biases['bd2'])
    fc2=tf.nn.relu(fc2)

    #fc2 = tf.nn.dropout(fc2,dropout)
    out = tf.add(tf.matmul(fc2,weights['out']),biases['out'])
    return out



class case_data:
    '''
    提供next_batch 数据
    '''
    def __init__(self,data):
        self._data = data
        self.i = 0
        self.length = len(data)
        print(self.length)

    def next_batch(self,batch_size):
        self.i += 1

        return_x = np.empty(shape=[0,3])
        return_y = []
        if (self.i + 1) * batch_size > self.length:
            self.i = 1
        batch_data = self._data[int((self.i-1)*batch_size):int(self.i*batch_size)]
        for i in range(len(batch_data)):
            x = batch_data[i]['X']
            y = batch_data[i]['Y']
            x_onesample = []

            for key in x.keys():
                x_onesample.append(x[key])
            # print(return_x.shape)
            # print(np.mat(x_onesample).T.shape)
            return_x = np.vstack((return_x,np.mat(x_onesample).T))
            return_y.append(y)
        return return_x,np.mat(return_y)

def pickle_acc(file,vars):
    '''
    序列化数据
    :param file:
    :param vars:
    :return:
    '''
    for var in vars:
        pickle.dump(var,file)
    return

def keyboardhandler(signum,frame):
    global stop
    print("keyboardInteruput_handler")
    stop = True


stop = False
signal.signal(signal.SIGINT,keyboardhandler)
if __name__ == "__main__":
    print('begin')
    datamatrix , labelmatrix = loaddata()
    datamatrix,labelmatrix = shuffle_dataset(datamatrix,labelmatrix)
    traindata, validation, testdata = splitdata(datamatrix,labelmatrix)
    
    print('loaddata over!')
    learning_rate=0.0005
    training_iters=50000
    batch_size=int(50)
    n_input = 1024*3
    display_step=10
    n_classes=11
    dropout=0.5

    weights={'wc1':tf.Variable(tf.random_normal([1,32,3,64])),
             'wc2':tf.Variable(tf.random_normal([1,16,64,32])),
             'wc3':tf.Variable(tf.random_normal([1,8,32,32])),
             'wd1':tf.Variable(tf.random_normal([32*1*16,300])),
             'wd2':tf.Variable(tf.random_normal([300,128])),
             'out':tf.Variable(tf.random_normal([128,n_classes]))}

    biases={'bc1':tf.Variable(tf.random_normal([64])),
            'bc2':tf.Variable(tf.random_normal([32])),
            'bc3':tf.Variable(tf.random_normal([32])),
            'bd1':tf.Variable(tf.random_normal([300])),
            'bd2':tf.Variable(tf.random_normal([128])),
            'out':tf.Variable(tf.random_normal([n_classes]))
            }

    x=tf.placeholder(tf.float32,[None,3])
    y=tf.placeholder(tf.float32,[None,n_classes])
    keep_prob=tf.placeholder(tf.float32)

    y_hat = conv_net(x,weights,biases,dropout)
    cost=tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=y_hat,labels=y))
    optimizer=tf.train.AdamOptimizer(learning_rate=learning_rate).minimize(cost)
    correct_prediction=tf.equal(tf.argmax(y_hat,1),tf.argmax(y,1))
    accuarcy=tf.reduce_mean(tf.cast(correct_prediction,tf.float32))
    init=tf.global_variables_initializer()
    saver=tf.train.Saver()
    train_loss=[]
    train_acc=[]
    valida_acc = []
    test_acc=[]
    data = case_data(traindata)
    validata = case_data(validation)
    testdata = case_data(testdata)
    file=open('./save/train_loss4.txt','wb')


    with tf.Session() as sess:
        sess.run(init)
        if os.path.exists("model/model/checkpoint"):
            saver.restore(sess,"model/model/model")
        step=1

        while not stop:

            batch_x,batch_y = data.next_batch(128)

            _, y_pre = sess.run([optimizer,y_hat],feed_dict={x:batch_x,y:batch_y,keep_prob:dropout})
            if step == 0:
                print(y_pre)
            if step % 50 == 0:
                saver.save(sess,"model/model/model")
            if step%5==0:
                loss_train,acc_train ,yhat_argmax,y_argmax=sess.run([cost,accuarcy,tf.argmax(y_hat,1),tf.argmax(y,1)],feed_dict={x:batch_x,y:batch_y,keep_prob:dropout})
                print(yhat_argmax[0:15])
                print(y_argmax[0:15])
                batch_x,batch_y = validata.next_batch(128)
                loss_valida,acc_valida = sess.run([cost,accuarcy],feed_dict={x:batch_x,y:batch_y,keep_prob:dropout})
                print("Iter :{}  , train_Loss: {:.2f} , tain_acc: {:.4f} ,vali_Loss: {:.2f}  ,vali_acc: {:.4f}".format(step,loss_train,acc_train,loss_valida,acc_valida))
                train_loss.append(loss_train)
                train_acc.append(acc_train)
                valida_acc.append(acc_valida)
                if (train_acc.__len__()>10)  and   (min(train_acc[-10:-1]) > 0.999):
                    break

            step+=1
            if step == training_iters:
                break
        pickle_acc(file,(train_loss,train_acc,valida_acc))

        plt.figure(1)
        plt.plot(train_loss ,c = 'red',label = 'train_loss')
        plt.figure(2)
        plt.plot(train_acc,c = 'blue',label='train_acc')
        plt.plot(valida_acc,c ='yellow',label='valida_acc')
        plt.legend()
        plt.show()
