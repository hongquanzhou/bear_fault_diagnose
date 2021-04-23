package service;

import bean.State;
import bean.TrainState;

public class GetStateService {
    private TrainState state = new TrainState(State.ready,0);
    public TrainState GetState()
    {
        return state;
    }
}
