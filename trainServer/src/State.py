from enum import Enum


class State(Enum):
    ready = 'ready'
    running = 'running'
    suspend = 'suspend'
    success = 'success'
    failed = 'failed'
    stoped = 'stoped'