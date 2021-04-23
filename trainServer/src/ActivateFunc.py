from enum import Enum


class ActivateFunc(Enum):
    elu = 'elu'
    exponential = 'exponential'
    hard_sigmoid = 'hard_sigmoid'
    liner = 'liner'
    relu = 'relu'
    selu = 'selu'
    sigmoid = 'sigmoid'
    softmax = 'softmax'
    softplus = 'softplus'
    softsign = 'softsign'
    tanh = 'tanh'

