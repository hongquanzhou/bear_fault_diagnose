#include "head/Properties.h"
using namespace std;
vector<string> Properties::spilt(char* line)
{
    string s1 = "";
    string s2 = "";
    vector<string> ret;
    int status = 0;
    while(*line!='\0')
    {
        if(status == 0)
        {
            if(*line=='=')
            {
                status = 1;
            }
            else
            {
                if(*line!=' ')
                    s1.push_back(*line);
            } 
        }
        else
        {
            if(*line!=' ')
                s2.push_back(*line);
        }
        line++;
    }
    ret.push_back(s1);
    ret.push_back(s2);
    return ret;
}
        
Properties::Properties(const char* filename)
{
    ifstream in;
    in.open(filename,ios::in);
    char buf[128];
    while(!in.eof())
    {
        in.getline(buf,128);
        vector<string> ss = spilt(buf);
        Map.insert(pair<string,string>(ss[0],ss[1]));
    }
    in.close();
}
string Properties::getProperties(string key)
{
    auto iter = Map.find(key);
    if(iter != Map.end())
        return iter->second;
    else
        return NULL;
}