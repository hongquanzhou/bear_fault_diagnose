from enum import Enum


class Optimizer(Enum):
    sgd = 'sgd'
    momentum = 'momentum'
    adagrad = 'adagrad'
    adadelta = 'adadelta'
    adam = 'adam'
    adamax = 'adamax'
    rmsprop = 'rmsprop'
