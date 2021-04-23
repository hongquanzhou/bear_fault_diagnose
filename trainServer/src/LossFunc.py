from enum import Enum


class LossFunc(Enum):
    categorical_crossentropy = 'categorical_crossentropy'
    binary_crossentropy = 'binary_crossentropy'
    categorical_hinge = 'categorical_hinge'
    cosine_similarity = 'cosine_similarity'
    hinge = 'hinge'
    huber = 'huber'
    kl_divergence = 'kl_divergence'
    log_cosh ='log_cosh'
    mean_absolute_percentage_error = 'mean_absolute_percentage_error'
    mean_absolute_error = ' mean_absolute_error'
    mean_squared_error = 'mean_squared_error'
    mean_squared_logarithmic_error = 'mean_squared_logarithmic_error'
    poisson = 'poisson'
    sparse_categorical_crossentropy = 'sparse_categorical_crossentropy'
    squared_hinge = 'squared_hinge'