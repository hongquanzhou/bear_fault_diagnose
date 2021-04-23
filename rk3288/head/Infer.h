#ifndef INFER_H
    #define INFER_H
    #include <iostream>
    #include <fstream>
    #include <map>
    #include <vector>
    #include <time.h>
    #include "tensorflow/lite/interpreter.h"
    #include "tensorflow/lite/model_builder.h"
    #include "tensorflow/lite/interpreter.h"
    #include "tensorflow/lite/kernels/register.h"
    #include "tensorflow/lite/model.h"
    #include "tensorflow/lite/tools/gen_op_registration.h"
    using namespace std;
    enum Infer_state
    {
        INFER,SLEEP,UPDATE
    };
    class Infer
    {
        private:
            enum Infer_state state;
            string modelBase;
            string modelFull;
            int in_index = -1;
            int out_index= -1;
            float *inputs;
            unique_ptr<tflite::Interpreter> interpreter;
            std::unique_ptr<tflite::FlatBufferModel> model;
            tflite::ops::builtin::BuiltinOpResolver resolver;
            tflite::StderrReporter error_reporter;
        public:
            Infer(string Base,string name); 
            void setInput(float *input);
            void updateModel();
            int infer();
    };
#endif