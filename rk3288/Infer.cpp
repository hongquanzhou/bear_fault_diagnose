#include "head/Infer.h"
    
Infer::Infer(string base,string name)
{
    modelBase = base;
    modelFull = base + "/" + name;
    state = SLEEP;
    updateModel();

}
void Infer::setInput(float *input)
{
    //input
    while(state != SLEEP);
    state = INFER;
    memcpy(inputs,input,3*1024*sizeof(float));
}
void Infer::updateModel()
{
    while(state != SLEEP);
    state = UPDATE;
    model = tflite::FlatBufferModel::BuildFromFile(modelFull.c_str(),&error_reporter);
    if(model == nullptr)
    {
        cout<<"error"<<endl;
        return ;
    }
    if(tflite::InterpreterBuilder(*model,resolver)(&interpreter)!=kTfLiteOk)
    {
        cout<<"build interpreter error"<<endl;
        return ;
    }
    interpreter->SetNumThreads(4);
    interpreter->SetAllowFp16PrecisionForFp32(true);
    interpreter->AllocateTensors();
    if(interpreter->AllocateTensors()!=kTfLiteOk)
    {
        cout<<"AllocateTensors error"<<endl;
        return ;
    }
    in_index = interpreter->inputs()[0];
    inputs = interpreter->typed_tensor<float>(in_index);
    state = SLEEP;
}
int Infer::infer(){
    interpreter->Invoke();
    //output dims
    out_index = interpreter->outputs()[0];
    
    //output result
    auto output = interpreter->typed_tensor<float>(out_index);
    float sum = 0;
    int index;
    float max_value=output[11];
    index = 0;
    for(int i=12;i<22;i++)
    {
        if(output[i]>max_value)
        {
            index = i - 11;
            max_value = output[i];
        }
    }
    state = SLEEP;
    return index;
}
