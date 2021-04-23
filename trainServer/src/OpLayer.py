from enum import Enum


class OpLayer(Enum):
    max_pool = 'max_pool'
    min_pool = 'min_pool'
    avg_pool = 'avg_pool'
    flatten = 'flatten'
    dense = 'dense'
    normalize = 'normalize'
    dropout = 'dropout'
